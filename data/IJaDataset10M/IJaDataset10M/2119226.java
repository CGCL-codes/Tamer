package org.spantus.android.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.spantus.android.HandlerUtils;
import org.spantus.android.R;
import org.spantus.android.SpntAppletEventListener;
import org.spantus.android.SpntConstant;
import org.spantus.android.audio.PlayServiceImpl;
import org.spantus.android.audio.RecordServiceReader;
import org.spantus.android.audio.RecrordTask;
import org.spantus.android.dto.ExtractorReaderCtx;
import org.spantus.android.dto.SpantusAudioCtx;
import org.spantus.android.dto.SpntAppletState;
import org.spantus.android.handler.PingHandler;
import org.spantus.android.handler.PlayPollHandler;
import org.spantus.android.service.AndroidExtractorsFactory;
import org.spantus.android.service.RecognizeService;
import org.spantus.android.service.SpantusAudioCtxService;
import org.spantus.android.visualization.BarGraphView;
import org.spantus.core.beans.RecognitionResultDetails;
import org.spantus.core.beans.SignalSegment;
import org.spantus.core.io.BaseWraperExtractorReader;
import org.spantus.exception.ProcessingException;
import org.spantus.extractor.impl.ExtractorEnum;
import org.spantus.logger.Logger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SpantusMainActivity extends Activity implements SpntAppletEventListener {

    private static final Logger LOG = Logger.getLogger(SpantusMainActivity.class);

    private SpantusAudioCtx ctx = new SpantusAudioCtx();

    private volatile boolean turnedOnRecordingEnv = false;

    private Button recordBtn;

    private PingHandler pingHandler;

    private PlayPollHandler playPollHandler;

    private PlayServiceImpl playService;

    private SpantusAudioCtxService audioCtxService;

    private TextView logTxt;

    private BarGraphView barGraphView;

    private static Handler mHandler;

    private static final int CMD_ADMIN = 1;

    private static final int CMD_CONFIG = 2;

    private static final int CMD_VIEW_LAST = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ctx = getAudioCtxService().createSpantusAudioParams(sharedPrefs);
        recordBtn = (Button) findViewById(R.id.recordBtn);
        logTxt = (TextView) findViewById(R.id.logTxt);
        pingHandler = new PingHandler(this);
        playPollHandler = new PlayPollHandler(ctx, this);
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                showStatus(retrieveMessageStr(msg));
            }
        };
        recordBtn.setVisibility(View.GONE);
        barGraphView = (BarGraphView) findViewById(R.id.barGraphView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item;
        item = menu.add(0, CMD_ADMIN, 0, R.string.ADMIN);
        item.setIcon(android.R.drawable.ic_menu_edit);
        item = menu.add(0, CMD_VIEW_LAST, 0, R.string.VIEW_AUDIO);
        item.setIcon(android.R.drawable.ic_media_play);
        item = menu.add(0, CMD_CONFIG, 0, R.string.MENU_PREFERENCES);
        item.setIcon(android.R.drawable.ic_menu_manage);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(CMD_ADMIN).setVisible(ctx.getIsOnline());
        menu.findItem(CMD_VIEW_LAST).setVisible(!ctx.getIsOnline());
        menu.findItem(CMD_CONFIG).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case CMD_ADMIN:
                Intent adminActivityIntent = new Intent(this, AdminActivity.class);
                startActivityForResult(adminActivityIntent, 0);
                return true;
            case CMD_VIEW_LAST:
                File file = getAudioCtxService().findFile(ctx);
                Uri fileName = getAudioCtxService().fileToUri(file);
                if (fileName == null) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_EDIT, fileName);
                intent.setClassName("org.spantus.android", ViewAudioFormActivity.class.getCanonicalName());
                final int REQUEST_CODE_EDIT = 1;
                startActivityForResult(intent, REQUEST_CODE_EDIT);
                return true;
            case CMD_CONFIG:
                Intent showSettingsActivityIntent = new Intent(this, ShowSettingsActivity.class);
                startActivityForResult(showSettingsActivityIntent, 0);
                return true;
            default:
                return false;
        }
    }

    public void onRecordBtn(View view) {
        LOG.debug("[onRecordBtn]+++");
        if (turnedOnRecordingEnv == true) {
            RecrordTask recrordTask = new RecrordTask(ctx, mHandler);
            recrordTask.execute();
        }
        LOG.debug("[onRecordBtn]---");
    }

    boolean bTest = true;

    public void onTest(View view) {
        RecordServiceReader recordService = new RecordServiceReader();
        RecognizeService recognizeService = new RecognizeService(ctx);
        ExtractorReaderCtx readerCtx = AndroidExtractorsFactory.createDefaultReader();
        BaseWraperExtractorReader wrappedReader = new BaseWraperExtractorReader(readerCtx.getReader(), 1);
        wrappedReader.setSampleSizeInBits(16);
        File file = getAudioCtxService().findFile(ctx, "du1.wav");
        URL fileNameURL = getAudioCtxService().fileToUrl(file);
        List<RecognitionResultDetails> lastResult = null;
        try {
            showStatus("Start Recognition");
            recordService.readFile(fileNameURL, recordService, wrappedReader);
            List<SignalSegment> list = recordService.extractSegments(readerCtx);
            for (SignalSegment signalSegment : list) {
                lastResult = recognizeService.recognize(signalSegment);
                Map<String, Double> values = new LinkedHashMap<String, Double>();
                for (RecognitionResultDetails recognitionResultDetails : lastResult) {
                    values.put(recognitionResultDetails.getInfo().getMarker().getLabel(), recognitionResultDetails.getScores().get(ExtractorEnum.MFCC_EXTRACTOR.name()));
                    barGraphView.setValues(values);
                    barGraphView.invalidate();
                }
                break;
            }
            showStatus("Stop Recognition");
        } catch (IOException e) {
            throw new ProcessingException(e);
        } catch (URISyntaxException e) {
            throw new ProcessingException(e);
        } catch (JSONException e) {
            throw new ProcessingException(e);
        }
    }

    /**
	 * 
	 * @param view
	 */
    public void onSetupRecordingEnvBtn(View view) {
        LOG.debug("[onSetupRecordingEnvBtn]+++");
        ToggleButton button = (ToggleButton) view;
        boolean turnedOnRecordingEnv = button.isChecked();
        this.turnedOnRecordingEnv = turnedOnRecordingEnv;
        if (ctx.getIsOnline()) {
            pingHandler.pingURL(ctx.getRecordUrl());
        }
        showStatus("UI Started");
        LOG.debug("[onStartRecordingBtn]Playable: {0}", ctx.getIsPlayabe());
        if (Boolean.TRUE.equals(ctx.getIsPlayabe())) {
            playPollHandler.startPollingForAudio();
        }
        LOG.debug("[onSetupRecordingEnvBtn]---");
    }

    public void showStatus(String msg) {
        logTxt.setText(msg);
        LOG.debug("[showStatus] msg: {4}; Conn: {0}, playing: {1}, listening {2}, recording {3}", turnedOnRecordingEnv, ctx.getIsPlaying(), ctx.getIsListening(), ctx.getIsRecording(), msg);
        recordBtn.setVisibility(View.VISIBLE);
        if (!turnedOnRecordingEnv) {
            recordBtn.setVisibility(View.GONE);
            setListeningStatus(SpntAppletState.ErrorConnectionFailure);
        } else if (ctx.getAudioFailure()) {
            setListeningStatus(SpntAppletState.ErrorAudioFailure);
            recordBtn.setEnabled(false);
        } else if (ctx.getIsPlaying()) {
            if (ctx.getAllowStopPlaying()) {
                setListeningStatus(SpntAppletState.stopPlaying);
            } else {
                setListeningStatus(SpntAppletState.Playing);
            }
        } else if (ctx.getIsRecording()) {
            setListeningStatus(SpntAppletState.RecordingClickToStop);
        } else {
            setListeningStatus(SpntAppletState.ClickToTalk);
        }
        recordBtn.invalidate();
    }

    private void setListeningStatus(SpntAppletState aState) {
        Log.d(SpntConstant.SPNT_ANDROID_LOG_TAG, "[setListeningStatus] status " + aState);
        recordBtn.setBackgroundColor(mapColor(aState));
        recordBtn.setText(mapName(aState));
        recordBtn.setEnabled(mapEnable(aState));
        recordBtn.setCompoundDrawablesWithIntrinsicBounds(null, mapImage(aState), null, null);
    }

    private String mapName(SpntAppletState aState) {
        int msg = R.string.STATE_CLICK_TO_TALK;
        switch(aState) {
            case ClickToTalk:
                msg = R.string.STATE_CLICK_TO_TALK;
                break;
            case RecordingClickToStop:
                msg = R.string.STATE_RECORDING_CLICK_TO_STOP;
                break;
            case Playing:
                msg = R.string.STATE_PLAYING;
                break;
            case Initializing:
                msg = R.string.STATE_INITIALISING;
                break;
            case ListeningClickToStop:
                msg = R.string.STATE_LISTENING_CLICK_TO_STOP;
                break;
            case HoldToTalk:
                msg = R.string.STATE_HOLD_TO_STOP;
                break;
            case Recording:
                msg = R.string.STATE_RECORDING;
                break;
            case stopPlaying:
                msg = R.string.STATE_PLAYING_CLICK_TO_STOP;
                break;
            case ErrorAudioFailure:
                msg = R.string.STATE_ERR_AUDIO;
                break;
            case ErrorConnectionFailure:
                msg = R.string.STATE_ERR_NETWORK;
                break;
            default:
                break;
        }
        return getResources().getString(msg);
    }

    /**
	 * 
	 * @param aState
	 * @return
	 */
    private Drawable mapImage(SpntAppletState aState) {
        int image = R.drawable.record;
        switch(aState) {
            case Playing:
                image = R.drawable.play;
                break;
            case stopPlaying:
                image = R.drawable.play_stop;
                break;
            case ClickToTalk:
            case ListeningClickToStop:
            case Recording:
                image = R.drawable.record;
                break;
            case RecordingClickToStop:
                image = R.drawable.record_stop;
                break;
            case ErrorAudioFailure:
            case ErrorConnectionFailure:
                image = R.drawable.error;
                break;
            default:
        }
        return getResources().getDrawable(image);
    }

    /**
	 * 
	 * @param aState
	 * @return
	 */
    private Boolean mapEnable(SpntAppletState aState) {
        Boolean enable = false;
        switch(aState.getStopable()) {
            case canStop:
                enable = Boolean.TRUE;
                break;
            case canNotStop:
                enable = Boolean.FALSE;
                break;
            default:
                throw new IllegalArgumentException("Not implemented");
        }
        return enable;
    }

    /**
	 * 
	 * @param state
	 * @return
	 */
    public int mapColor(SpntAppletState aState) {
        int mapped = Color.BLACK;
        switch(aState.getSeverity()) {
            case Bloked:
                mapped = Color.RED;
                break;
            case Playing:
                mapped = Color.GREEN;
                break;
            case Waiting:
                mapped = Color.GREEN;
                break;
            case PlayingDoNotStop:
                mapped = Color.GREEN;
                break;
            case Recording:
                mapped = Color.CYAN;
                break;
            default:
                throw new IllegalArgumentException("Not implemented");
        }
        return mapped;
    }

    public void setConnectionStatus(boolean status) {
        turnedOnRecordingEnv = status;
        addMessage("Connection Status changed " + turnedOnRecordingEnv);
    }

    public void updateMeterChangedValue() {
    }

    public void resetMeterChangedValue() {
    }

    public void play(InputStream inputStream) {
        ctx.setIsPlaying(true);
        addMessage("Start Play");
        try {
            getPlayService().play(inputStream);
        } catch (MalformedURLException e) {
            LOG.error(e);
        }
        ctx.setIsPlaying(false);
        addMessage("Stop Play");
    }

    public PlayServiceImpl getPlayService() {
        if (playService == null) {
            playService = new PlayServiceImpl();
            playService.setAudioManager((AudioManager) getSystemService(Context.AUDIO_SERVICE));
        }
        return playService;
    }

    public static void addMessage(String msgStr) {
        LOG.debug("[addMessage] Message: {1}", msgStr);
        HandlerUtils.addMessage(mHandler, msgStr);
    }

    public static String retrieveMessageStr(Message msg) {
        return HandlerUtils.retrieveMessageStr(msg);
    }

    public void setPlayService(PlayServiceImpl playService) {
        this.playService = playService;
    }

    public SpantusAudioCtxService getAudioCtxService() {
        if (audioCtxService == null) {
            audioCtxService = new SpantusAudioCtxService();
        }
        return audioCtxService;
    }

    public void setAudioCtxService(SpantusAudioCtxService audioCtxService) {
        this.audioCtxService = audioCtxService;
    }
}
