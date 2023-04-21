package com.newbie.iSee;

import java.io.File;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class SoundUnit {

    private MediaRecorder mRecorder;

    private MediaPlayer mPlayer;

    private boolean isRecording = false;

    private String mBase;

    private String mFilePath;

    private final int MAX_DURATION = 30 * 1000;

    private boolean _isFinishedRecording;

    private boolean _rpRun;

    private CameraPage.UpdateHandler _uh;

    private RecordProgress _rp;

    private CameraPage _context;

    public SoundUnit(CameraPage context) {
        mBase = Environment.getExternalStorageDirectory() + "/Sounds";
        File folder = new File(mBase);
        if (!folder.exists()) folder.mkdir();
        _isFinishedRecording = false;
        _context = context;
        _uh = context.new UpdateHandler();
        _rp = new RecordProgress();
    }

    public SoundUnit() {
        mBase = Environment.getExternalStorageDirectory() + "/Sounds";
        File folder = new File(mBase);
        if (!folder.exists()) folder.mkdir();
        _isFinishedRecording = false;
    }

    public String toggleRecording() {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
        isRecording = !isRecording;
        return isRecording ? "stop" : "finished";
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        if (CameraPage.pid == null) mFilePath = mBase + "/REC" + ((int) (Math.random() * 10000)) + ".mp4"; else mFilePath = mBase + "/REC" + CameraPage.pid + ".mp4";
        Log.d("SoundUnit", mFilePath);
        try {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecorder.setOutputFile(mFilePath);
            mRecorder.prepare();
            mRecorder.start();
        } catch (Throwable t) {
            t.printStackTrace();
            isRecording = !isRecording;
            return;
        }
        Log.d("SoundUnit", "Start recording");
    }

    public String getFilePath() {
        return mFilePath;
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            _isFinishedRecording = true;
        } catch (Throwable t) {
            t.printStackTrace();
            isRecording = !isRecording;
            return;
        }
        Log.d("SoundUnit", "End recording");
        stopRecordProgress();
        Log.d("SoundUnit", mFilePath);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFilePath);
            mPlayer.prepare();
        } catch (Throwable t) {
            t.printStackTrace();
            isRecording = !isRecording;
            return;
        }
        mPlayer.start();
        Log.d("SoundUnit", "Start playing");
    }

    public int getDuration() {
        if (mPlayer == null) return 0;
        return mPlayer.getDuration();
    }

    public int getCurrentPosition() {
        if (mPlayer == null) return 0;
        return mPlayer.getCurrentPosition();
    }

    public int getVolume() {
        if (!isRecording) return 0;
        return mRecorder.getMaxAmplitude();
    }

    class RecordProgress extends Thread {

        public void run() {
            if (mPlayer == null && mRecorder == null) {
                Log.d("error", "RecordProgress error");
                return;
            }
            while (_rpRun) {
                if (!isRecording && mPlayer != null && !mPlayer.isPlaying()) {
                    _uh.sendEmptyMessage(CameraPage.BAR_STOP);
                    return;
                } else if (!isRecording && mPlayer != null) _uh.sendEmptyMessage(CameraPage.BAR_GO); else if (isRecording) _uh.sendEmptyMessage(CameraPage.BAR_VOL);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    Log.d("error", "sleep error");
                }
            }
        }
    }

    public void startRecordProgress() {
        try {
            _rp = new RecordProgress();
            _rpRun = true;
            _rp.start();
            Log.d("record", "start showing time");
        } catch (Exception e) {
            Log.d("error", "thread start error");
        }
    }

    public void stopRecordProgress() {
        _rpRun = false;
        Log.d("record", "stop showing time");
    }

    public boolean isFinishedRecording() {
        return _isFinishedRecording;
    }
}
