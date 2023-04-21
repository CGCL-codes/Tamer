package com.fanfq.NowTime;

import java.util.Calendar;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;

public class MyService extends Service {

    private SoundPool mSoundPool;

    private int[] hitOk = new int[18 + 26];

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        hitOk[0] = mSoundPool.load(this, R.raw.o06, 0);
        hitOk[1] = mSoundPool.load(this, R.raw.o07, 0);
        hitOk[2] = mSoundPool.load(this, R.raw.o08, 0);
        hitOk[3] = mSoundPool.load(this, R.raw.o09, 0);
        hitOk[4] = mSoundPool.load(this, R.raw.o10, 0);
        hitOk[5] = mSoundPool.load(this, R.raw.o11, 0);
        hitOk[6] = mSoundPool.load(this, R.raw.o12, 0);
        hitOk[7] = mSoundPool.load(this, R.raw.o13, 0);
        hitOk[8] = mSoundPool.load(this, R.raw.o14, 0);
        hitOk[9] = mSoundPool.load(this, R.raw.o15, 0);
        hitOk[10] = mSoundPool.load(this, R.raw.o16, 0);
        hitOk[11] = mSoundPool.load(this, R.raw.o17, 0);
        hitOk[12] = mSoundPool.load(this, R.raw.o18, 0);
        hitOk[13] = mSoundPool.load(this, R.raw.o19, 0);
        hitOk[14] = mSoundPool.load(this, R.raw.o20, 0);
        hitOk[15] = mSoundPool.load(this, R.raw.o21, 0);
        hitOk[16] = mSoundPool.load(this, R.raw.o22, 0);
        hitOk[17] = mSoundPool.load(this, R.raw.o23, 0);
        hitOk[18] = mSoundPool.load(this, R.raw.w00, 0);
        hitOk[19] = mSoundPool.load(this, R.raw.w01, 0);
        hitOk[20] = mSoundPool.load(this, R.raw.w02, 0);
        hitOk[21] = mSoundPool.load(this, R.raw.w03, 0);
        hitOk[22] = mSoundPool.load(this, R.raw.w04, 0);
        hitOk[23] = mSoundPool.load(this, R.raw.w05, 0);
        hitOk[24] = mSoundPool.load(this, R.raw.w06, 0);
        hitOk[25] = mSoundPool.load(this, R.raw.w07, 0);
        hitOk[26] = mSoundPool.load(this, R.raw.w08, 0);
        hitOk[27] = mSoundPool.load(this, R.raw.w09, 0);
        hitOk[28] = mSoundPool.load(this, R.raw.w10, 0);
        hitOk[29] = mSoundPool.load(this, R.raw.w11, 0);
        hitOk[30] = mSoundPool.load(this, R.raw.w12, 0);
        hitOk[31] = mSoundPool.load(this, R.raw.w13, 0);
        hitOk[32] = mSoundPool.load(this, R.raw.w14, 0);
        hitOk[33] = mSoundPool.load(this, R.raw.w15, 0);
        hitOk[34] = mSoundPool.load(this, R.raw.w16, 0);
        hitOk[35] = mSoundPool.load(this, R.raw.w17, 0);
        hitOk[36] = mSoundPool.load(this, R.raw.w18, 0);
        hitOk[37] = mSoundPool.load(this, R.raw.w19, 0);
        hitOk[38] = mSoundPool.load(this, R.raw.w20, 0);
        hitOk[39] = mSoundPool.load(this, R.raw.w21, 0);
        hitOk[40] = mSoundPool.load(this, R.raw.w22, 0);
        hitOk[41] = mSoundPool.load(this, R.raw.w23, 0);
        hitOk[42] = mSoundPool.load(this, R.raw.k0, 0);
        hitOk[43] = mSoundPool.load(this, R.raw.k1, 0);
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        this.registerReceiver(mReceiver, filter);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_TIME_TICK.equals(action)) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                System.out.println(mHour + ":" + mMinute);
                DataStore ds = new DataStore();
                if (mMinute == 0 && ds.isOpen) {
                    switch(mHour) {
                        case 0:
                            if (ds.times[0]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[18], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                            break;
                        case 1:
                            if (ds.times[1]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[19], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 1, 1);
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                            break;
                        case 2:
                            if (ds.times[2]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[20], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 2, 1);
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                            break;
                        case 3:
                            if (ds.times[3]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[21], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 3, 1);
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                            break;
                        case 4:
                            if (ds.times[4]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[22], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 4, 1);
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                            break;
                        case 5:
                            if (ds.times[5]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[23], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 5, 1);
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                            break;
                        case 6:
                            if (ds.times[6]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[24], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 6, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[0], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 7:
                            if (ds.times[7]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[25], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 7, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[1], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 8:
                            if (ds.times[8]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[26], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 8, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[2], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 9:
                            if (ds.times[9]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[27], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 9, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[3], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 10:
                            if (ds.times[10]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[28], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 10, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[4], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 11:
                            if (ds.times[11]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[29], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 11, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[5], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 12:
                            if (ds.times[12]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[30], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 12, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[6], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 13:
                            if (ds.times[13]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[31], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 13, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[7], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 14:
                            if (ds.times[14]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[32], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 14, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[8], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 15:
                            if (ds.times[15]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[33], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 15, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[9], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 16:
                            if (ds.times[16]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[34], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 16, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[10], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 17:
                            if (ds.times[17]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[35], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 17, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[11], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 18:
                            if (ds.times[18]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[36], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 18, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[12], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 19:
                            if (ds.times[19]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[37], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 19, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[13], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 20:
                            if (ds.times[20]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[38], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 20, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[14], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 21:
                            if (ds.times[21]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[39], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 21, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[15], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 22:
                            if (ds.times[22]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[40], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 22, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[16], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                        case 23:
                            if (ds.times[23]) {
                                switch(ds.witchAudio) {
                                    case 0:
                                        mSoundPool.play(hitOk[41], 1, 1, 0, 0, 1);
                                        break;
                                    case 1:
                                        mSoundPool.play(hitOk[42], 1, 1, 0, 0, 1);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mSoundPool.play(hitOk[43], 1, 1, 0, 23, 1);
                                        break;
                                    case 2:
                                        mSoundPool.play(hitOk[17], 1, 1, 0, 0, 1);
                                        break;
                                }
                            }
                            break;
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    class DataStore {

        private boolean isOpen;

        private boolean[] times = { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true };

        private int witchAudio;

        public DataStore() {
            getData();
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean isOpen) {
            this.isOpen = isOpen;
        }

        public boolean[] getTimes() {
            return times;
        }

        public void setTimes(boolean[] times) {
            this.times = times;
        }

        public int getWitchAudio() {
            return witchAudio;
        }

        public void setWitchAudio(int witchAudio) {
            this.witchAudio = witchAudio;
        }

        public void getData() {
            SharedPreferences sharedPreferences = getSharedPreferences("data_info", 0);
            isOpen = sharedPreferences.getBoolean("ISOPEN", false);
            witchAudio = sharedPreferences.getInt("WITCHAUDIO", 0);
            times[0] = sharedPreferences.getBoolean("TIMES_0", true);
            times[1] = sharedPreferences.getBoolean("TIMES_1", true);
            times[2] = sharedPreferences.getBoolean("TIMES_2", true);
            times[3] = sharedPreferences.getBoolean("TIMES_3", true);
            times[4] = sharedPreferences.getBoolean("TIMES_4", true);
            times[5] = sharedPreferences.getBoolean("TIMES_5", true);
            times[6] = sharedPreferences.getBoolean("TIMES_6", true);
            times[7] = sharedPreferences.getBoolean("TIMES_7", true);
            times[8] = sharedPreferences.getBoolean("TIMES_8", true);
            times[9] = sharedPreferences.getBoolean("TIMES_9", true);
            times[10] = sharedPreferences.getBoolean("TIMES_10", true);
            times[11] = sharedPreferences.getBoolean("TIMES_11", true);
            times[12] = sharedPreferences.getBoolean("TIMES_12", true);
            times[13] = sharedPreferences.getBoolean("TIMES_13", true);
            times[14] = sharedPreferences.getBoolean("TIMES_14", true);
            times[15] = sharedPreferences.getBoolean("TIMES_15", true);
            times[16] = sharedPreferences.getBoolean("TIMES_16", true);
            times[17] = sharedPreferences.getBoolean("TIMES_17", true);
            times[18] = sharedPreferences.getBoolean("TIMES_18", true);
            times[19] = sharedPreferences.getBoolean("TIMES_19", true);
            times[20] = sharedPreferences.getBoolean("TIMES_20", true);
            times[21] = sharedPreferences.getBoolean("TIMES_21", true);
            times[22] = sharedPreferences.getBoolean("TIMES_22", true);
            times[23] = sharedPreferences.getBoolean("TIMES_23", true);
        }
    }
}
