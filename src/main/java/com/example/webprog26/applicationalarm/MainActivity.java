package com.example.webprog26.applicationalarm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MainActivity_TAG";

    private static final int FILE_SELECT_CODE = 100;
    private static final int ALARM_RINGTONE_MAXLENGTH = 8;

    private TimePicker mTimePicker;
    private CheckBox mChbAlarmRepeat, mChbAlarmVibration;
    private RelativeLayout mDaysOfWeekLayout;
    private TextView mTvAlarmRingtone;
    private ArrayList<Integer> mDaysAlarmRepeatList;
    private Uri mRingtoneUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        Button btnSetAlarm = (Button) findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(this);
        Button btnShowAlarms = (Button) findViewById(R.id.btnShowAlarms);
        btnShowAlarms.setOnClickListener(this);

        mChbAlarmRepeat = (CheckBox) findViewById(R.id.chbRepeatAlarm);
        mChbAlarmVibration = (CheckBox) findViewById(R.id.chbAlarmVibration);
        mChbAlarmVibration.setOnCheckedChangeListener(this);
        mChbAlarmRepeat.setOnCheckedChangeListener(this);

        mDaysOfWeekLayout = (RelativeLayout) findViewById(R.id.daysOfWeekLayout);

        mDaysAlarmRepeatList = new ArrayList<>();
        initDaysAlarmRepeatList(mDaysAlarmRepeatList);

        TextView tvMo = (TextView) findViewById(R.id.tvMo);
        TextView tvTu = (TextView) findViewById(R.id.tvTu);
        TextView tvWe = (TextView) findViewById(R.id.tvWe);
        TextView tvTh = (TextView) findViewById(R.id.tvTh);
        TextView tvFr = (TextView) findViewById(R.id.tvFr);
        TextView tvSt = (TextView) findViewById(R.id.tvSt);
        TextView tvSu = (TextView) findViewById(R.id.tvSu);

        TextView[] textViews = new TextView[]{tvMo, tvTu, tvWe, tvTh, tvFr, tvSt, tvSu};
        initTextViewsWithOnclickListener(textViews);

        mRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        mTvAlarmRingtone = (TextView) findViewById(R.id.tvRingtone);
        mTvAlarmRingtone.setText(getResources().getString(R.string.alarm_melody_default));
        mTvAlarmRingtone.setOnClickListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetAlarm:
                int hour, minutes;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = mTimePicker.getHour();
                    minutes = mTimePicker.getMinute();
                } else {
                    hour = mTimePicker.getCurrentHour();
                    minutes = mTimePicker.getCurrentMinute();
                }

                Log.i(TAG, "hour: " + hour + ", minutes: " + minutes);
                Log.i(TAG, "mDaysAlarmRepeatList.size() " + mDaysAlarmRepeatList.size());
                Intent alarmClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                alarmClockIntent.putExtra(AlarmClock.EXTRA_VIBRATE, mChbAlarmVibration.isChecked());

                if(mChbAlarmRepeat.isChecked())
                {
                    if(mDaysAlarmRepeatList != null)
                    {
                        alarmClockIntent.putExtra(AlarmClock.EXTRA_DAYS, mDaysAlarmRepeatList);
                    }
                }
                alarmClockIntent.putExtra(AlarmClock.EXTRA_RINGTONE, mRingtoneUri.toString());
                alarmClockIntent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                alarmClockIntent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
                startActivity(alarmClockIntent);
                break;
            case R.id.btnShowAlarms:
                Intent showAlarmsIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                startActivity(showAlarmsIntent);
                break;
            case R.id.tvMo:
                changeTextViewState(((TextView) view), mDaysAlarmRepeatList, Calendar.MONDAY);
                Log.i(TAG, "mDaysAlarmRepeatList.size() " + mDaysAlarmRepeatList.size());
                break;
            case R.id.tvTu:
                changeTextViewState(((TextView) view), mDaysAlarmRepeatList, Calendar.TUESDAY);
                Log.i(TAG, "mDaysAlarmRepeatList.size() " + mDaysAlarmRepeatList.size());
                break;
            case R.id.tvWe:
                changeTextViewState(((TextView) view), mDaysAlarmRepeatList, Calendar.WEDNESDAY);
                Log.i(TAG, "mDaysAlarmRepeatList.size() " + mDaysAlarmRepeatList.size());
                break;
            case R.id.tvTh:
                changeTextViewState(((TextView) view), mDaysAlarmRepeatList, Calendar.THURSDAY);
                Log.i(TAG, "mDaysAlarmRepeatList.size() " + mDaysAlarmRepeatList.size());
                break;
            case R.id.tvFr:
                changeTextViewState(((TextView) view), mDaysAlarmRepeatList, Calendar.FRIDAY);
                Log.i(TAG, "mDaysAlarmRepeatList.size() " + mDaysAlarmRepeatList.size());
                break;
            case R.id.tvSt:
                changeTextViewState(((TextView) view), mDaysAlarmRepeatList, Calendar.SATURDAY);
                Log.i(TAG, "mDaysAlarmRepeatList.size() " + mDaysAlarmRepeatList.size());
                break;
            case R.id.tvSu:
                changeTextViewState(((TextView) view), mDaysAlarmRepeatList, Calendar.SUNDAY);
                Log.i(TAG, "mDaysAlarmRepeatList.size() " + mDaysAlarmRepeatList.size());
                break;
            case R.id.tvRingtone:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mpeg3");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, getResources().getString(R.string.install_file_manager),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.chbRepeatAlarm:
                Log.i(TAG, "repeatAlarm " + b);
                if(mDaysOfWeekLayout.getVisibility() == View.VISIBLE)
                {
                    mDaysOfWeekLayout.setVisibility(View.GONE);
                } else {
                    mDaysOfWeekLayout.setVisibility(View.VISIBLE);
                }

                if(!b && mDaysAlarmRepeatList != null)
                {
                    mDaysAlarmRepeatList = null;
                } else {
                    mDaysAlarmRepeatList = new ArrayList<>();
                    initDaysAlarmRepeatList(mDaysAlarmRepeatList);
                }
                break;
            case R.id.chbAlarmVibration:
                Log.i(TAG, "vibrationAlarm " + b);
                break;
        }
    }

    /**
     * Inits ArrayList<Integer> that contains days, when alarm repeats
     * @param daysAlarmRepeatList
     */
    private void initDaysAlarmRepeatList(ArrayList<Integer> daysAlarmRepeatList) {
        daysAlarmRepeatList.add(Calendar.MONDAY);
        daysAlarmRepeatList.add(Calendar.TUESDAY);
        daysAlarmRepeatList.add(Calendar.WEDNESDAY);
        daysAlarmRepeatList.add(Calendar.THURSDAY);
        daysAlarmRepeatList.add(Calendar.FRIDAY);
        daysAlarmRepeatList.add(Calendar.SATURDAY);
        daysAlarmRepeatList.add(Calendar.SUNDAY);
    }

    /**
     * Inits array of TextViews with OnClickListener
     * @param textViews
     */
    private void initTextViewsWithOnclickListener(TextView[] textViews)
    {
        for(TextView textView: textViews)
        {
            textView.setOnClickListener(this);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    /**
     * Checks, that list already has such value
     * @param daysAlarmRepeatList
     * @param dayValue
     * @return boolean that indicates is current day already exists in the list
     */
    private boolean isListContainsDay(ArrayList<Integer> daysAlarmRepeatList, Integer dayValue)
    {
        return daysAlarmRepeatList.contains(dayValue);
    }

    /**
     * Adds day to the list or removes it from list, if list already has such value
     * @param daysAlarmRepeatList
     * @param dayValue
     * @return true if value has been added, false if value has been removed
     */
    private boolean operateWithDayInList(ArrayList<Integer> daysAlarmRepeatList, Integer dayValue)
    {
        if(isListContainsDay(daysAlarmRepeatList, dayValue))
        {
            daysAlarmRepeatList.remove(dayValue);
            return false;
        }

        daysAlarmRepeatList.add(dayValue);
        return true;
    }

    /**
     * Paints underline on clicked TextView if dayValue was added to the list
     * or removes it, if dayValue was removed from the list
     * @param textView
     * @param daysAlarmRepeatList
     * @param dayValue
     */
    private void changeTextViewState(TextView textView, ArrayList<Integer> daysAlarmRepeatList, Integer dayValue)
    {
        if(!operateWithDayInList(daysAlarmRepeatList, dayValue))
        {
            textView.setPaintFlags(0);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public String getFilename(Uri uri)
    {
        String filePath = FileUtils.getInstance().getPath(this, uri);
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);

        if(fileName.length() > ALARM_RINGTONE_MAXLENGTH)
        {
            fileName = fileName.substring(0, ALARM_RINGTONE_MAXLENGTH) + "...";
        }

        return normalizeFileName(fileName);
    }

    /**
     * Clears FileName from "_" & makes it's first letter an upperCase
     * @param fileName
     * @return String fileName
     */
    private String normalizeFileName(String fileName)
    {
        if(fileName == null || fileName.length() == 0)
        {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        char[] fileNameToCharArray = fileName.toCharArray();

        if(Character.isLowerCase(fileNameToCharArray[0]))
        {
            fileNameToCharArray[0] = Character.toUpperCase(fileNameToCharArray[0]);
        }

        for(char c: fileNameToCharArray)
        {
            if(c == '_')
            {
                c = ' ';
            }
            stringBuilder.append(c);

        }
        return stringBuilder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == FILE_SELECT_CODE)
            {
                mRingtoneUri = data.getData();
                Log.i(TAG, "Chosen file's Uri is " + data.getData().getPath());


                        mTvAlarmRingtone.setText(getResources().getString(R.string.alarm_melody, getFilename(mRingtoneUri)));
            }
        }
    }
}
