package org.freelords.sound.fake;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Clip;

/** A custom implementation of {@ref javax.sound.sampled.Clip} used for testing.
 *
 * Basically, this clip does nothing except setting some flags when it is
 * opened, closed, stopped etc. The flags can then be queried by the test to
 * check that an AudioPlayer communicates correctly with the clip.
 *
 * Unfortunately, the clip interface is relatively detailed. However, a player
 * should never mess with the gory details, so most functions just throw an
 * exception.
 */
public class FakeClip implements Clip {

    /** The number of frames that the input file is pretended to have. */
    private static int staticFrameLength = 10;

    /** the supplied input stream. */
    private AudioInputStream stream;

    /** true if the line is started. */
    boolean running = false;

    /** true if the line has been closed. */
    boolean closed = false;

    /** The current playing position. */
    private int framePos = 0;

    /** The listener that awaits status changes.
	 * There should always be up to one listener, which is the player. The
	 * player then passes events further up the chain.
	 */
    private LineListener listener;

    /** Set to true to have the clip block the current Thread (via wait()) on opening. */
    private boolean blockOnOpen = false;

    public FakeClip() {
    }

    public FakeClip(boolean blocking) {
        blockOnOpen = blocking;
    }

    /** Get the supplied input stream for the clip. */
    public AudioInputStream getAudioInputStream() {
        return stream;
    }

    /** Tells whether close has been called or not. */
    public boolean isClosed() {
        return closed;
    }

    /** Tells whether the line is running or not. */
    @Override
    public boolean isRunning() {
        return running;
    }

    /** Open the clip for playback. */
    @Override
    public void open(AudioInputStream stream) throws LineUnavailableException {
        if (this.stream == null && stream != null) {
            this.stream = stream;
        } else if (stream == null) {
            throw new IllegalArgumentException("Stream must not be zero!");
        } else {
            throw new LineUnavailableException("A clip should only be opened once!");
        }
        if (blockOnOpen) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Start playback. */
    @Override
    public void start() {
        if (stream == null) {
            throw new IllegalStateException("No stream set!");
        }
        running = true;
    }

    /** Stops playback and sends an event to the listener. */
    @Override
    public void stop() {
        running = false;
        if (listener != null) {
            listener.update(new LineEvent(this, LineEvent.Type.STOP, framePos));
        }
    }

    /** Force the player to pretend it has finished playing. */
    public void quitPlaying() {
        framePos = staticFrameLength;
        stop();
    }

    @Override
    public void close() {
        closed = true;
    }

    /** Return the size of the audio file in frames. */
    @Override
    public int getFrameLength() {
        return staticFrameLength;
    }

    @Override
    public void addLineListener(LineListener listener) {
        if (this.listener != null) {
            throw new IllegalArgumentException("There is already a listener!");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener is empty!");
        }
        this.listener = listener;
    }

    @Override
    public void removeLineListener(LineListener listener) {
        if (listener != this.listener || this.listener == null) {
            throw new IllegalArgumentException("No Listener registered.");
        }
        this.listener = null;
    }

    @Override
    public void setFramePosition(int frames) {
        if (frames < 0 || frames > getFrameLength()) {
            throw new IllegalArgumentException("invalid number of frames set");
        }
        framePos = frames;
    }

    @Override
    public int getFramePosition() {
        return framePos;
    }

    @Override
    public Control getControl(Control.Type control) {
        throw new IllegalArgumentException("No controls available");
    }

    @Override
    public boolean isControlSupported(Control.Type control) {
        return false;
    }

    @Override
    public long getMicrosecondLength() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public void loop(int count) {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public void open(AudioFormat format, byte[] data, int offset, int bufferSize) {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public void setLoopPoints(int start, int end) {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public void setMicrosecondPosition(long microseconds) {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public int available() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public void drain() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public int getBufferSize() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public AudioFormat getFormat() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public float getLevel() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public long getLongFramePosition() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public long getMicrosecondPosition() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public boolean isActive() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public Control[] getControls() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public Line.Info getLineInfo() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException("not implemented.");
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("not implemented.");
    }
}
