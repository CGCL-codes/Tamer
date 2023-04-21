package prajna.gui;

import javax.swing.JComponent;
import prajna.data.TimeSpan;

/**
 * Interface for GUI components which represent a span of time. Implementations
 * of this class can include temporal rulers or interactive components
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public interface TimeSpanWidget {

    /**
     * Return the Graphical User Interface component used for displaying the
     * TimeSpan or interacting with this component. For JComponents which
     * implement this interface, this method should simply return the component
     * itself.
     * 
     * @return the component used to render the interface for this
     *         TimeSpanWidget
     */
    public abstract JComponent getComponent();

    /**
     * Return the time span this time slider measures. This method returns the
     * actual time span displayedby the widget, which may be modified by the
     * behavior of this widget.
     * 
     * @return The time span of this TimeSpanWidget
     */
    public TimeSpan getDisplayedSpan();

    /**
     * Sets the time this TimeSpanWidget measures. The span may be modified by
     * the widget for particular reasons. is modified by the increment, so the
     * actual span may be different than the span specified. Therefore, when
     * retrieving the span of time displayed, <code>getDisplayedSpan</code>
     * should be used.
     * 
     * @param span the new TimeSpan for this ruler
     */
    public void setTimeSpan(TimeSpan span);
}
