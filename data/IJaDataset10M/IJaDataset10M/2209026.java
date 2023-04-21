package abbot.swt.matcher;

import org.eclipse.swt.widgets.Widget;
import abbot.swt.tester.WidgetTester;
import abbot.swt.tester.misc.Visible;

/**
 * A {@link Matcher} that matches {@link Widget}s based on {@link Class} and, optionally,
 * visibility.
 */
public class WidgetMatcher extends ClassMatcher<Widget> {

    /**
	 * Gets the visibility matching state of a {@link Widget}. This method exists as a convenient
	 * way to use the {@link WidgetMatcher}'s visibility matching logic without having to create an
	 * instance of one.
	 * 
	 * @param widget
	 *            a {@link Widget}
	 * @param visible
	 * @return <code>true</code> if <code>visible</code> is <code>false</code> or the {@link Widget}
	 *         is visible, <code>false</code> otherwise
	 */
    public static boolean isVisibleMatch(Widget widget, boolean visible) {
        return !visible || isVisible(widget);
    }

    /**
	 * Gets the visibility state of a {@link Widget}. This method exists as a convenient way to use
	 * the {@link WidgetMatcher}'s visibility matching logic without having to create an instance of
	 * one.
	 * 
	 * @param widget
	 *            A {@link Widget}
	 * @return <code>true</code> if the {@link Widget} is visible, <code>false</code> otherwise
	 */
    public static boolean isVisible(Widget widget) {
        WidgetTester tester = WidgetTester.getTester(widget);
        return (tester instanceof Visible) && !widget.isDisposed() && ((Visible) tester).isVisible(widget);
    }

    /**
	 * If <code>true</code> then only visible {@link Widget}s can match. If <code>false</code> then
	 * visibility is not taken into consideration.
	 */
    protected final boolean visible;

    /**
	 * Creates a {@link WidgetMatcher} that matches {@link Widget}s based on {@link Class} and,
	 * optionally, visibility.
	 * 
	 * @param clazz
	 *            A {@link Class} that extends {@link Widget}.
	 * @param visible
	 *            If <code>true</code> then only visible {@link Widget}s can match. If
	 *            <code>false</code> then {@link Widget}'s visibility is not taken into
	 *            consideration.
	 */
    public WidgetMatcher(Class<? extends Widget> clazz, boolean visible) {
        super(clazz);
        this.visible = visible;
    }

    /**
	 * Creates a {@link WidgetMatcher} that matches visible {@link Widget}s based on {@link Class}.
	 * 
	 * @param clazz
	 *            A {@link Class} that extends {@link Widget}
	 */
    public WidgetMatcher(Class<? extends Widget> clazz) {
        this(clazz, true);
    }

    /**
	 * Creates a {@link WidgetMatcher} that matches {@link Widget}s based on, optionally,
	 * visibility.
	 * 
	 * @param visible
	 *            If <code>true</code> then only visible {@link Widget}s can match. If
	 *            <code>false</code> then {@link Widget}'s visibility is not taken into
	 *            consideration.<br>
	 *            <b>Note:</b> If <code>visible</code> is <code>false</code> then the
	 *            {@link WidgetMatcher} will match <i>all</i> {@link Widget}s.
	 */
    public WidgetMatcher(boolean visible) {
        this(Widget.class, visible);
    }

    @Override
    public boolean matches(Widget widget) {
        return super.matches(widget) && isVisibleMatch(widget, visible);
    }

    /**
	 * @return the labels and {@link String} representations of the receiver's fields
	 */
    protected String toStringFields() {
        return super.toStringFields() + ", visible: " + visible;
    }
}
