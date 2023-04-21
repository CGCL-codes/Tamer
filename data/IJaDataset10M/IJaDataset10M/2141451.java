package edu.washington.assist.audio.wav;

public interface SoundClip {

    public long getDuration();

    public void start();

    public void stop();

    public void seek(long where);

    public void destroy();

    public long getPosition();

    public boolean isRunning();
}
