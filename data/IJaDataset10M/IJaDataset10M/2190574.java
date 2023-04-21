package prajna.viz.display;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JPanel;
import prajna.data.TimeSpan;
import prajna.gui.TimeBar;
import prajna.gui.TimeSpanWidget;

/**
 * <P>
 * This class provides utilities for drawing timeline displays. The
 * TimeLineCanvas provides a convenient mechanism for ordering and displaying
 * TimeBars in particular, but since it is a JPanel, it can also contain other
 * Swing components.
 * </P>
 * <P>
 * The TimeLineCanvas uses a vertical Grid layout by default. It requires a
 * timespan to be set for any of the TimeLine functions to work. Once the
 * timeline is set, the left side of the canvas corresponds to the earliest
 * time of the timespan, while the right side is the latest time of the time
 * span. A convenience method adds a TimeRuler to the top of the display
 * </P>
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class TimeLineCanvas extends JPanel {

    private static final long serialVersionUID = -686480826139265231L;

    private ArrayList<TimeBar> events = new ArrayList<TimeBar>();

    private TimeSpan span = null;

    private TimeSpanWidget ruler = null;

    /**
     * Creates a new TimeLineCanvas object.
     */
    public TimeLineCanvas() {
        setLayout(new GridLayout(0, 1));
    }

    /**
     * Creates a new TimeLineCanvas object.
     * 
     * @param startDate The start time for this TimeLineCanvas
     * @param stopDate The stop date for this TimeLineCanvas
     */
    public TimeLineCanvas(Date startDate, Date stopDate) {
        setLayout(new GridLayout(0, 1));
        span = new TimeSpan(startDate, stopDate);
    }

    /**
     * Creates a new TimeLineCanvas object.
     * 
     * @param timeSpan The timespan for this TimeLineCanvas
     */
    public TimeLineCanvas(TimeSpan timeSpan) {
        setLayout(new GridLayout(0, 1));
        span = timeSpan;
    }

    /**
     * Adds a TimeBar to the display. Implicitly sets the span of the TimeBar
     * to be equal to the timespan of this TimeCanvas.
     * 
     * @param bar The Timebar to be added.
     */
    public void addTimeBar(TimeBar bar) {
        bar.setTimeSpan(span);
        events.add(bar);
        add(bar);
    }

    /**
     * Adds a TimeSpanWidget. This also implicitly sets the timespan of the
     * TimeLineCanvas to be equal to the measured span of the widget. The
     * TimeSpanWidget is added at the top of the display (as the first
     * JComponent in the container).
     * 
     * @param newRuler The TimeRuler to be added
     */
    public void setTimeRuler(TimeSpanWidget newRuler) {
        if (ruler != null) {
            remove(ruler.getComponent());
        }
        ruler = newRuler;
        add(ruler.getComponent(), 0);
        setTimeSpan(ruler.getDisplayedSpan());
    }

    /**
     * Specifically sets the timespan for this TimeLineCanvas. This will also
     * reset the span of all TimeBars.
     * 
     * @param timeSpan the timespan for this TimeLineCanvas
     */
    public void setTimeSpan(TimeSpan timeSpan) {
        span = timeSpan;
        for (TimeBar bar : events) {
            bar.setTimeSpan(span);
        }
    }
}
