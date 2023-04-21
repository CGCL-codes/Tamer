package org.speech.asr.media.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.media.stream.MemoryDataSource;
import org.speech.asr.media.util.PlayerUtils;
import org.speech.asr.media.vo.AudioSource;
import org.speech.asr.media.vo.Time;
import javax.media.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 19, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class AudioPlayer implements PlayerController {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(AudioPlayer.class.getName());

    private static final long TIMER_INTERVAL = 200;

    private List<PlayerListener> listeners;

    private Player jmfPlayer;

    private Timer timer;

    private AudioSource audioSource;

    private PlayerState state;

    private javax.media.Time startTime;

    public AudioPlayer() {
        listeners = new CopyOnWriteArrayList();
        timer = new Timer(true);
        state = PlayerState.UNINITIALIZED;
        startTime = new javax.media.Time(0l);
    }

    public void addListener(PlayerListener l) {
        listeners.add(l);
    }

    public void removeListener(PlayerListener l) {
        listeners.remove(l);
    }

    private Player init() {
        log.debug("Initializing player...");
        try {
            MemoryDataSource dataSource = new MemoryDataSource(audioSource);
            jmfPlayer = Manager.createRealizedPlayer(dataSource);
            state = PlayerState.STOPPED;
            jmfPlayer.addControllerListener(new InnerPlayerListener());
            jmfPlayer.prefetch();
            PlayerUtils.waitForState(jmfPlayer, Player.Prefetched);
        } catch (Exception e) {
            log.error("Exception occurred during initializing player", e);
            state = PlayerState.ERROR;
        }
        fireInitializedEvent();
        return jmfPlayer;
    }

    public void start() {
        if (state == PlayerState.PLAYING) {
            log.warn("Player is in playing state");
            return;
        }
        log.debug("Starting player from {} second ...", startTime.getSeconds());
        jmfPlayer.setMediaTime(startTime);
        jmfPlayer.setStopTime(jmfPlayer.getDuration());
        state = PlayerState.PLAYING;
        jmfPlayer.syncStart(startTime);
    }

    public void pause() {
        log.debug("Pausing player...");
        state = PlayerState.PAUSED;
        jmfPlayer.stop();
    }

    public void stop() {
        log.debug("Stopping player...");
        state = PlayerState.STOPPED;
        jmfPlayer.stop();
    }

    protected void startTimer() {
        timer.cancel();
        timer.purge();
        timer = new Timer();
        timer.scheduleAtFixedRate(new InnerTimerTask(), 0, TIMER_INTERVAL);
    }

    protected void stopTimer() {
        timer.cancel();
        timer.purge();
    }

    public void seek(Time time) {
        firePositionEvent();
    }

    protected PlayerEvent createPlayerEvent() {
        PlayerEvent event = new PlayerEvent();
        event.setEventTimestamp(System.nanoTime());
        javax.media.Time duration = jmfPlayer.getDuration();
        long mediaTime = jmfPlayer.getMediaNanoseconds();
        event.setAudioDuration(new Time(duration.getNanoseconds()));
        event.setAudioTime(new Time(mediaTime));
        event.setAudioSource(audioSource);
        event.setPlayerState(state);
        return event;
    }

    protected void firePositionEvent() {
        PlayerEvent event = createPlayerEvent();
        for (PlayerListener listener : listeners) {
            listener.positionChanged(event);
        }
    }

    protected void fireInitializedEvent() {
        PlayerEvent event = createPlayerEvent();
        for (PlayerListener listener : listeners) {
            listener.playerInitialized(event);
        }
    }

    protected void fireStoppedEvent() {
        PlayerEvent event = createPlayerEvent();
        for (PlayerListener listener : listeners) {
            listener.playerStopped(event);
        }
    }

    protected void fireStartedEvent() {
        PlayerEvent event = createPlayerEvent();
        for (PlayerListener listener : listeners) {
            listener.playerStarted(event);
        }
    }

    protected void firePausedEvent() {
        PlayerEvent event = createPlayerEvent();
        for (PlayerListener listener : listeners) {
            listener.playerPaused(event);
        }
    }

    /**
   * {@inheritDoc}
   */
    public AudioSource getAudioSource() {
        return audioSource;
    }

    /**
   * {@inheritDoc}
   */
    public void setAudioSource(AudioSource audioSource) {
        this.audioSource = audioSource;
        init();
    }

    private class InnerTimerTask extends TimerTask {

        public void run() {
            firePositionEvent();
        }
    }

    private class InnerPlayerListener implements ControllerListener {

        public void controllerUpdate(ControllerEvent event) {
            log.trace("Controller event {}", event);
            if (event instanceof EndOfMediaEvent) {
                handleStop();
            } else if (event instanceof StartEvent) {
                log.trace("Started from {}", ((StartEvent) event).getMediaTime().getSeconds());
                startTimer();
                fireStartedEvent();
            } else if (event instanceof StopByRequestEvent) {
                if (state == PlayerState.PAUSED) {
                    handlePause();
                } else {
                    handleStop();
                }
            } else if (event instanceof StopAtTimeEvent) {
                handleStop();
            } else if (event instanceof MediaTimeSetEvent) {
                firePositionEvent();
            }
        }
    }

    private void handlePause() {
        startTime = jmfPlayer.getMediaTime();
        jmfPlayer.setMediaTime(startTime);
        firePausedEvent();
    }

    private void handleStop() {
        state = PlayerState.STOPPED;
        startTime = new javax.media.Time(0);
        jmfPlayer.setMediaTime(startTime);
        fireStoppedEvent();
        stopTimer();
    }
}
