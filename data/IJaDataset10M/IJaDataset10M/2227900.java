package promidi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

/**
 * A sequencer describes a device that records and plays back a sequence
 * of control information for any electronic musical instrument. The proMIDI
 * Sequencer allows you you to record and playback MIDI data.
 * <br><br>
 * The minimal time resolution of a sequencer is a tick. The proMIDI sequencer
 * has a rate of 512 ticks per bar. 
 * @example promidi_sequencer
 * @related Song
 * @related Track
 */
public class Sequencer implements Runnable {

    /**
	 * Holds the song that is played by the sequencer
	 */
    private Song song;

    private int loopCount;

    private long loopStartPoint;

    private long loopEndPoint;

    private float bpm;

    private boolean recording;

    private List songPositionListeners = new ArrayList();

    private boolean running;

    private boolean finished = true;

    /**
	 * Keeps the System time on start of sequencer
	 */
    static long startTimeMillis;

    private long lastTickPosition;

    private long startTickPosition;

    /**
	 * The actual playback position as tick
	 */
    private long tickPosition;

    /**
	 * The number of loops played by the sequencer
	 */
    private int playedLoops;

    Thread playThread;

    /**
     * A value indicating that looping should continue
     * indefinitely rather than complete after a specific
     * number of loops.
     */
    public static final int LOOP_CONTINUOUSLY = -1;

    public Sequencer() {
    }

    /**
	 * Sets the song the sequencer has to play.
	 * @param i_song Song, the song the sequencer has to play
	 * @throws InvalidMidiDataException
	 * @example promidi_sequencer
	 * @related Sequencer
	 * @related Song
	 * @related setSong ( )
	 */
    public void setSong(final Song i_song) {
        this.song = i_song;
    }

    /**
	 * @invisible
	 * @param i_stream
	 * @throws IOException
	 * @throws InvalidMidiDataException
	 */
    public void setSong(final InputStream i_stream) throws IOException {
    }

    /**
	 * Returns the song the sequencer is currently playing
	 * @return Song, the song the sequencer is playing
	 * @related Sequencer
	 * @related Song
	 * @related getSong ( )
	 */
    public Song getSong() {
        return song;
    }

    /**
	 * @invisible
	 * @param track
	 * @param channel
	 */
    public void recordEnable(final Track track, final int channel) {
    }

    /**
	 * @invisible
	 * @param track
	 */
    public void recordDisable(final Track track) {
    }

    /**
	 * Returns the actual tempo of the sequencer in BPM.
	 * @return float, the tempo of the sequencer in BPM
	 * @related Sequencer
	 * @related setTempoInBPM ( )
	 */
    public float getTempoInBPM() {
        return song.getTempo();
    }

    /**
	 * Sets the actual tempo of the sequencer in BPM.
	 * @param bpm float, the new tempo of the sequencer in BPM
	 * @related Sequencer
	 * @related getTempoInBPM ( )
	 */
    public void setTempoInBPM(final float bpm) {
        this.bpm = bpm;
    }

    /**
	 * Returns the actual position of the sequencer in the actual
	 * song in ticks.
	 * @return long, the actual position of the sequencer in ticks.
	 * @related Sequencer
	 * @related setTickPosition ( )
	 */
    public long getTickPosition() {
        return tickPosition;
    }

    /**
	 * Sets the actual position of the sequencer in ticks.
	 * @param tickPosition long, actual position of the sequencer
	 * @related Sequencer
	 * @related getTickPosition ( )
	 */
    public void setTickPosition(final long tickPosition) {
        startTickPosition = tickPosition;
        startTimeMillis = System.currentTimeMillis();
        this.lastTickPosition = tickPosition;
    }

    /**
	 * @invisible
	 * @param track
	 * @param mute
	 */
    public void setTrackMute(final int track, final boolean mute) {
    }

    /**
	 * @invisible
	 * @param track
	 * @return
	 */
    public boolean getTrackMute(final int track) {
        return false;
    }

    /**
	 * @invisible
	 * @param track
	 * @param solo
	 */
    public void setTrackSolo(final int track, final boolean solo) {
    }

    /**
	 * @invisible
	 * @param track
	 * @return 
	 */
    public boolean getTrackSolo(final int track) {
        return false;
    }

    /**
	 * Sets the startpoint of the loop the sequencer should play
	 * @param tick long, the startpoint of the loop
	 * @example promidi_sequencer
	 * @related Sequencer
	 * @related getLoopStartPoint ( )
	 * @related setLoopEndPoint ( )
	 * @related getLoopEndPoint ( )
	 */
    public void setLoopStartPoint(final long tick) {
        this.loopStartPoint = tick;
    }

    /**
	 * Returns the startpoint of the loop the sequencer should play
	 * @return long, the startpoint of the loop
	 * @related Sequencer
	 * @related setLoopStartPoint ( )
	 * @related setLoopEndPoint ( )
	 * @related getLoopEndPoint ( )
	 */
    public long getLoopStartPoint() {
        return loopStartPoint;
    }

    /**
	 * Sets the endpoint of the loop the sequencer should play
	 * @param tick long, the endpoint of the loop
	 * @example promidi_sequencer
	 * @related Sequencer
	 * @related setLoopStartPoint ( )
	 * @related getLoopStartPoint ( )
	 * @related getLoopEndPoint ( )
	 */
    public void setLoopEndPoint(final long tick) {
        this.loopEndPoint = tick;
    }

    /**
	 * Returns  the endpoint of the loop the sequencer should play
	 * @return long, the endpoint of the loop
	 * @related Sequencer
	 * @related setLoopStartPoint ( )
	 * @related getLoopStartPoint ( )
	 * @related setLoopEndPoint ( )
	 */
    public long getLoopEndPoint() {
        return loopEndPoint;
    }

    /**
	 * Tells the sequencer to permanently play the current loop
	 * @related Sequencer
	 * @related noLoop ( )
	 */
    public void loop() {
        setLoopCount(-1);
    }

    /**
	 * Tells the sequencer to stop playing the loop
	 * @related Sequencer
	 * @related loop ( )
	 */
    public void noLoop() {
        setLoopCount(0);
    }

    /**
	 * Sets how often the loop of the sequencer has to be played.
	 * @param count int, number of times the loop has to be played
	 * @example promidi_sequencer
	 * @related Sequencer
	 * @related getLoopCount ( )
	 */
    public void setLoopCount(int count) {
        this.loopCount = count;
    }

    public int getLoopCount() {
        return loopCount;
    }

    /**
	 * Add a song position listener to the sequencer. See the
	 * SongPositionListener javadoc.
	 * @invisible
	 * @param songPositionListener
	 */
    public void addSongPositionListener(SongPositionListener songPositionListener) {
        songPositionListeners.add(songPositionListener);
    }

    final void notifySongPositionListeners(long tick) {
        for (int i = 0; i < songPositionListeners.size(); i++) {
            ((SongPositionListener) songPositionListeners.get(i)).notifyTickPosition(tick);
        }
    }

    /**
	 * Starts the sequencer.
	 * @example promidi_sequencer
	 * @related stop ( )
	 */
    public void start() {
        running = false;
        while (!finished) {
            Thread.yield();
        }
        finished = false;
        running = true;
        playedLoops = 0;
        startTimeMillis = System.currentTimeMillis();
        tickPosition = startTickPosition;
        resetControllers();
        playThread = new Thread(this);
        playThread.setPriority(Thread.MAX_PRIORITY - 1);
        playThread.start();
    }

    /**
	 * Stops the playback of the sequencer.
	 * @example promidi_sequencer
	 * @related start ( )
	 */
    public void stop() {
        recording = false;
        running = false;
        sendMidiPanic(false);
        startTickPosition = tickPosition;
    }

    /**
	 * Use this method to see if the sequencer is running.
	 * @return boolean: true if the sequencer is running otherwise false
	 */
    public boolean isRunning() {
        return running;
    }

    /**
	 * @invisible
	 *
	 */
    public void startRecording() {
        recording = true;
        start();
    }

    /**
	 * @invisible
	 *
	 */
    public void stopRecording() {
        stop();
    }

    /**
	 * @invisible
	 * @return
	 */
    public boolean isRecording() {
        return recording;
    }

    private void timerEvent() {
        long currentTick = startTickPosition + (long) ((System.currentTimeMillis() - startTimeMillis) * (song.resolution * (getTempoInBPM() / 60000)));
        for (long playTick = lastTickPosition; playTick <= currentTick; playTick++) {
            if (getLoopCount() == Sequencer.LOOP_CONTINUOUSLY && startTickPosition < getLoopEndPoint()) {
                tickPosition = ((playTick - getLoopStartPoint()) % (getLoopEndPoint() - getLoopStartPoint())) + getLoopStartPoint();
            } else {
                tickPosition = playTick;
            }
            if (tickPosition == (getLoopEndPoint() - 1)) {
                playedLoops++;
            }
            if (playedLoops > 0 && tickPosition == getLoopStartPoint()) {
                flushNoteOnCache();
                resetControllers();
            }
            for (int i = 0; i < song.getNumberOfTracks(); i++) {
                Track track = song.getTrack(i);
                track.sendEventsForTick(tickPosition);
            }
            notifySongPositionListeners(tickPosition);
        }
        lastTickPosition = currentTick + 1;
    }

    /**
	 * Resets the controllers.
	 */
    private void resetControllers() {
        for (int i = 0; i < song.getNumberOfTracks(); i++) {
            song.getTrack(i).resetControllers(tickPosition);
        }
    }

    /**
	 * Sends a noteOff for all actual notes.
	 */
    private void flushNoteOnCache() {
        for (int i = 0; i < song.getNumberOfTracks(); i++) {
            song.getTrack(i);
        }
    }

    void sendMidiPanic(boolean doControllers) {
        try {
            for (int i = 0; i < song.getNumberOfTracks(); i++) {
                song.getTrack(i).sendMidiPanic(doControllers);
            }
        } catch (Exception e) {
        }
    }

    /**
	 * @invisible
	 */
    public void run() {
        while (running) {
            try {
                Thread.sleep(1);
                timerEvent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finished = true;
    }

    /**
	 * @invisible
	 * @return 
	 */
    public long getRealTimeTickPosition() {
        long currentTick = startTickPosition + (long) ((System.currentTimeMillis() - startTimeMillis) * (song.resolution * (getTempoInBPM() / 60000)));
        if (getLoopCount() == Sequencer.LOOP_CONTINUOUSLY) {
            currentTick = ((currentTick - getLoopStartPoint()) % (getLoopEndPoint() - getLoopStartPoint())) + getLoopStartPoint();
        }
        return (currentTick);
    }
}
