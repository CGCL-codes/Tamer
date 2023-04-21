package org.makagiga.commons.transition;

import java.io.Serializable;
import javax.swing.JComponent;
import org.pushingpixels.trident.Timeline;

/**
 * @since 3.8.6
 */
public abstract class Transition implements Serializable {

    public static final Transition NO_TRANSITION = new NoTransition();

    private long duration;

    public long getDuration() {
        return duration;
    }

    public void setDuration(final long value) {
        duration = value;
    }

    public abstract void setup(final JComponent c, final Timeline timeline, final ImageView from, final ImageView to);

    public void start(final JComponent c, final Timeline timeline) {
        timeline.play();
    }

    public void stop(final JComponent c, final Timeline timeline) {
        timeline.end();
    }

    protected Transition(final long duration) {
        this.duration = duration;
    }
}
