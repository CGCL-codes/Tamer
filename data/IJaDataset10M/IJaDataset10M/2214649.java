package calclipse.lib.math.util.graph.plotting;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Contains properties regadring parameters of a plot.
 * @author T. Sommerland
 */
public class PlotProperties {

    public static final double DEFAULT_MIN = 0;

    public static final double DEFAULT_MAX = Math.PI * 2;

    public static final double DEFAULT_STEP = Math.PI / 24;

    private final EventListenerList listenerList = new EventListenerList();

    private final ChangeEvent changeEvent = new ChangeEvent(this);

    private double min;

    private double max;

    private double step;

    public PlotProperties(final double min, final double max, final double step) {
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public PlotProperties() {
        this(DEFAULT_MIN, DEFAULT_MAX, DEFAULT_STEP);
    }

    public void addChangeListener(final ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(final ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    protected void fireStateChanged() {
        for (final ChangeListener listener : listenerList.getListeners(ChangeListener.class)) {
            listener.stateChanged(changeEvent);
        }
    }

    public double getStep() {
        return step;
    }

    public void setStep(final double step) {
        if (step <= 0) {
            throw new IllegalArgumentException("step <= 0");
        }
        this.step = step;
        fireStateChanged();
    }

    public double getMax() {
        return max;
    }

    public void setMax(final double max) {
        if (max <= min) {
            throw new IllegalArgumentException("max <= min");
        }
        this.max = max;
        fireStateChanged();
    }

    public double getMin() {
        return min;
    }

    public void setMin(final double min) {
        if (min >= max) {
            throw new IllegalArgumentException("min >= max");
        }
        this.min = min;
        fireStateChanged();
    }
}
