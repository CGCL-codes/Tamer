package zinger.dizzy.client;

import com.google.gwt.user.client.*;

public class TimedDisplayController {

    protected final ArtDisplay display;

    protected final int step;

    protected final int period;

    private Timer timer;

    private int target;

    public TimedDisplayController(final ArtDisplay display, final int step, final int period) {
        this.display = display;
        this.step = step;
        this.period = period;
    }

    public int getTarget() {
        return this.target;
    }

    public void setTarget(final int target) {
        this.target = target;
        if (this.timer == null) {
            this.timer = new Timer() {

                public void run() {
                    int position = display.getPosition();
                    final int target = getTarget();
                    if (position > target) {
                        position -= step;
                        if (position < target) position = target;
                    } else if (position < target) {
                        position += step;
                        if (position > target) position = target;
                    }
                    display.setPosition(position);
                    if (position == target) {
                        this.cancel();
                        timer = null;
                    }
                }
            };
            this.timer.scheduleRepeating(this.period);
        }
    }
}
