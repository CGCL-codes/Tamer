package org.makagiga.commons;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Hashtable;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A slider.
 *
 * EXAMPLE:
 * @code
 * MSlider slider = new MSlider() {
 *   @Override
 *   protected void onChange() {
 *     // value change
 *   }
 * };
 * MSlider.Labels labels = new MSlider.Labels();
 * labels.add(Brush.MIN_SIZE, _("Small"));
 * labels.add(Brush.MEDIUM_SIZE, _("Medium"));
 * labels.add(Brush.MAX_SIZE, _("Large"));
 * slider.setLabels(labels);
 * slider.setMinimum(Brush.MIN_SIZE);
 * slider.setMaximum(Brush.MAX_SIZE);
 * slider.setValue(Brush.DEFAULT_SIZE);
 * @endcode
 */
public class MSlider extends JSlider implements MouseWheelListener, UI.EventsControl, UI.MouseWheelEventsControl {

    private boolean eventsEnabled;

    private boolean mouseWheelEventsEnabled = true;

    /**
	 * Constructs a slider.
	 */
    public MSlider() {
        addChangeListener(new ChangeListener() {

            public void stateChanged(final ChangeEvent e) {
                if (MSlider.this.eventsEnabled) MSlider.this.onChange();
            }
        });
        addMouseWheelListener(this);
    }

    /**
	 * @since 2.0
	 */
    public boolean getEventsEnabled() {
        return eventsEnabled;
    }

    public void setEventsEnabled(final boolean value) {
        eventsEnabled = value;
    }

    /**
	 * @since 2.0
	 */
    public boolean getMouseWheelEventsEnabled() {
        return mouseWheelEventsEnabled;
    }

    /**
	 * @since 2.0
	 */
    public void setMouseWheelEventsEnabled(final boolean value) {
        mouseWheelEventsEnabled = value;
    }

    /**
	 * @since 2.0
	 */
    public void setLabels(final Labels labels) {
        setLabelTable(labels);
        setPaintLabels(true);
    }

    public void showSimpleLabels() {
        Labels labels = new Labels();
        labels.add(getMinimum(), "-");
        labels.add(getMaximum(), "+");
        setLabels(labels);
    }

    /**
	 * @since 2.0
	 */
    public void showTicks(final int step, final boolean snapToTicks, final boolean showLabels) {
        int size = getMinimum();
        int max = getMaximum();
        Labels labels = new Labels();
        while (size <= max) {
            if (showLabels) labels.add(size); else labels.add(size, null);
            size += step;
        }
        setLabels(labels);
        setMajorTickSpacing(step);
        setPaintTicks(true);
        setSnapToTicks(snapToTicks);
    }

    /**
	 * Mouse wheel event handler.
	 * Increments/decrements the slider value.
	 * @param e A mouse wheel event
	 */
    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (mouseWheelEventsEnabled && isEnabled()) MAction.fire(e, "positiveUnitIncrement", "negativeUnitIncrement", this);
    }

    /**
	 * Invoked when value was changed by the user.
	 * @see setEventsEnabled(final boolean)
	 */
    protected void onChange() {
    }

    /**
	 * @since 2.0
	 */
    public static class Labels extends Hashtable<Integer, MLabel> {

        public Labels() {
        }

        public MLabel add(final int value, final String text) {
            MLabel l = new MLabel(text);
            put(value, l);
            return l;
        }

        public MLabel add(final int value) {
            return add(value, Integer.toString(value));
        }
    }
}
