package org.opensourcephysics.media.core;

import java.beans.*;
import javax.swing.SwingUtilities;
import org.opensourcephysics.controls.*;

/**
 * This is a ClipControl that uses the video itself for timing.
 *
 * @author Douglas Brown
 * @version 1.0
 */
public class VideoClipControl extends ClipControl implements PropertyChangeListener {

    /**
   * Constructs a VideoClipControl.
   *
   * @param videoClip the video clip
   */
    protected VideoClipControl(VideoClip videoClip) {
        super(videoClip);
        video.addPropertyChangeListener(this);
    }

    /**
   * Plays the clip.
   */
    public void play() {
        video.play();
    }

    /**
   * Stops at the next step.
   */
    public void stop() {
        video.stop();
    }

    /**
   * Steps forward one step.
   */
    public void step() {
        video.stop();
        setStepNumber(stepNumber + 1);
    }

    /**
   * Steps back one step.
   */
    public void back() {
        video.stop();
        setStepNumber(stepNumber - 1);
    }

    /**
   * Sets the step number.
   *
   * @param n the desired step number
   */
    public void setStepNumber(int n) {
        if (n == stepNumber) return;
        n = Math.max(0, n);
        final int stepNumber = Math.min(clip.getStepCount() - 1, n);
        Runnable runner = new Runnable() {

            public void run() {
                video.setFrameNumber(clip.stepToFrame(stepNumber));
            }
        };
        SwingUtilities.invokeLater(runner);
    }

    /**
   * Gets the step number.
   *
   * @return the current step number
   */
    public int getStepNumber() {
        return clip.frameToStep(video.getFrameNumber());
    }

    /**
   * Sets the play rate.
   *
   * @param newRate the desired rate
   */
    public void setRate(double newRate) {
        if (newRate == 0 || newRate == rate) return;
        rate = Math.abs(newRate);
        video.setRate(rate);
    }

    /**
   * Gets the play rate.
   *
   * @return the current rate
   */
    public double getRate() {
        return video.getRate();
    }

    /**
   * Turns on/off looping.
   *
   * @param loops <code>true</code> to turn looping on
   */
    public void setLooping(boolean loops) {
        if (loops == isLooping()) return;
        video.setLooping(loops);
    }

    /**
   * Gets the looping status.
   *
   * @return <code>true</code> if looping is on
   */
    public boolean isLooping() {
        return video.isLooping();
    }

    /**
   * Gets the current frame number.
   *
   * @return the frame number
   */
    public int getFrameNumber() {
        return video.getFrameNumber();
    }

    /**
   * Gets the playing status.
   *
   * @return <code>true</code> if playing
   */
    public boolean isPlaying() {
        return video.isPlaying();
    }

    /**
   * Gets the current time in milliseconds measured from step 0.
   *
   * @return the current time
   */
    public double getTime() {
        int n = video.getFrameNumber();
        return (video.getFrameTime(n) - video.getStartTime()) * timeStretch;
    }

    /**
   * Gets the start time of the specified step measured from step 0.
   *
   * @param stepNumber the step number
   * @return the step time
   */
    public double getStepTime(int stepNumber) {
        int n = clip.stepToFrame(stepNumber);
        return (video.getFrameTime(n) - video.getStartTime()) * timeStretch;
    }

    /**
   * Sets the frame duration.
   *
   * @param duration the desired frame duration in milliseconds
   */
    public void setFrameDuration(double duration) {
        if (duration == 0) return;
        duration = Math.abs(duration);
        double ti = video.getFrameTime(video.getStartFrameNumber());
        double tf = video.getFrameTime(video.getEndFrameNumber());
        int count = video.getEndFrameNumber() - video.getStartFrameNumber();
        if (count != 0) timeStretch = duration * count / (tf - ti);
        support.firePropertyChange("frameduration", null, new Double(duration));
    }

    /**
   * Gets the mean frame duration in milliseconds.
   *
   * @return the frame duration in milliseconds
   */
    public double getMeanFrameDuration() {
        int count = video.getEndFrameNumber() - video.getStartFrameNumber();
        if (count != 0) {
            double ti = video.getFrameTime(video.getStartFrameNumber());
            double tf = video.getFrameTime(video.getEndFrameNumber());
            return timeStretch * (tf - ti) / count;
        }
        return timeStretch * video.getDuration() / video.getFrameCount();
    }

    /**
   * Responds to property change events. VideoClipControl listens for the
   * following events: "playing", "looping", "rate" and "framenumber" from Video.
   *
   * @param e the property change event
   */
    public void propertyChange(PropertyChangeEvent e) {
        String name = e.getPropertyName();
        if (name.equals("framenumber")) {
            int n = ((Integer) e.getNewValue()).intValue();
            n = clip.frameToStep(n);
            if (n == stepNumber) return;
            stepNumber = n;
            Integer nInt = new Integer(n);
            support.firePropertyChange("stepnumber", null, nInt);
        } else if (name.equals("playing")) {
            boolean playing = ((Boolean) e.getNewValue()).booleanValue();
            if (!playing) setStepNumber(stepNumber + 1);
            support.firePropertyChange(e);
        } else if (name.equals("rate") || name.equals("looping")) support.firePropertyChange(e);
    }

    /**
   * Removes this listener from the video so it can be garbage collected.
   */
    public void dispose() {
        video.removePropertyChangeListener(this);
    }

    /**
   * Returns an XML.ObjectLoader to save and load data for this class.
   *
   * @return the object loader
   */
    public static XML.ObjectLoader getLoader() {
        return new Loader();
    }
}
