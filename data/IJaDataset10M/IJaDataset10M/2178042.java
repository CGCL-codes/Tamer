package abbot.swt.matcher;

import org.eclipse.swt.widgets.Widget;
import abbot.swt.utilities.ExtendedComparator;

/**
 * A {@link WidgetDataMatcher} that matches {@link String} values. The value to match can be a Java
 * regular expression. See {@link ExtendedComparator}.
 */
public class StringDataMatcher extends WidgetDataMatcher {

    /**
	 * Gets the data matching state of a {@link Widget}. This method exists as a convenient way to
	 * use the {@link StringDataMatcher}'s data matching logic without having to create an instance
	 * of one.
	 * 
	 * @param widget
	 *            a {@link Widget}
	 * @param key
	 *            the key to be used when retrieving the {@link Widget}'s data (or <code>null</code>
	 *            if no key is to be used
	 * @param value
	 *            the data value to be matched
	 * @return <code>true</code> if the {@link Widget}'s data matches the value, <code>false</code>
	 *         otherwise
	 */
    public static boolean isStringDataMatch(Widget widget, String key, String value) {
        return isStringDataMatch(value, getData(widget, key));
    }

    /**
	 * Gets the data matching state of a {@link Widget}'s data. This method exists as a convenient
	 * way to use the {@link WidgetDataMatcher}'s data matching logic without having to create an
	 * instance of one.
	 * 
	 * @param value
	 *            the {@link Object} to match
	 * @param data
	 *            the candidate {@link Object}
	 * @return <code>true</code> if the data matches the value, <code>false</code> otherwise
	 */
    public static boolean isStringDataMatch(String value, Object data) {
        if (value == null) return data == null;
        if (data instanceof String) return ExtendedComparator.stringsMatch(value, (String) data);
        return false;
    }

    /**
	 * Creates a {@link StringDataMatcher}.
	 * 
	 * @param key
	 *            The key to use for retrieving a {@link Widget}'s data (or <code>null</code> if no
	 *            key is to be used).
	 * @param value
	 *            The value to be matched. Can be <code>null</code>.
	 * @param clazz
	 *            The {@link Class} of {@link Widget}s to consider.
	 * @param visible
	 *            If <code>true</code> then only <i>visible</i> {@link Widget}s are considered.
	 */
    public StringDataMatcher(String key, String value, Class<? extends Widget> clazz, boolean visible) {
        super(key, value, clazz, visible);
    }

    /**
	 * Creates a {@link StringDataMatcher}.<br>
	 * Only <i>visible</i> {@link Widget}s are considered.
	 * 
	 * @param key
	 *            The key to use for retrieving a {@link Widget}'s data (or <code>null</code> if no
	 *            key is to be used).
	 * @param value
	 *            The value to be matched. Can be <code>null</code>.
	 * @param clazz
	 *            The {@link Class} of {@link Widget}s to consider.
	 */
    public StringDataMatcher(String key, String value, Class<? extends Widget> clazz) {
        super(key, value, clazz);
    }

    /**
	 * Creates a {@link StringDataMatcher}.<br> {@link Widget}s of all {@link Widget} subclasses are
	 * considered.
	 * 
	 * @param key
	 *            The key to use for retrieving a {@link Widget}'s data (or <code>null</code> if no
	 *            key is to be used).
	 * @param value
	 *            The value to be matched. Can be <code>null</code>.
	 * @param visible
	 *            If <code>true</code> then only <i>visible</i> {@link Widget}s are considered.
	 */
    public StringDataMatcher(String key, String value, boolean visible) {
        super(key, value, visible);
    }

    /**
	 * Creates a {@link StringDataMatcher}.<br>
	 * Only <i>visible</i> {@link Widget}s are considered.<br> {@link Widget}s of all {@link Widget}
	 * subclasses are considered.
	 * 
	 * @param key
	 *            The key to use for retrieving a {@link Widget}'s data (or <code>null</code> if no
	 *            key is to be used).
	 * @param value
	 *            The value to be matched. Can be <code>null</code>.
	 */
    public StringDataMatcher(String key, String value) {
        super(key, value);
    }

    /**
	 * Creates a {@link StringDataMatcher}.<br>
	 * Uses the {@link #DEFAULT_KEY} to retrieve a {@link Widget}'s data.
	 * 
	 * @param value
	 *            The value to be matched. Can be <code>null</code>.
	 * @param clazz
	 *            The {@link Class} of {@link Widget}s to consider.
	 * @param visible
	 *            If <code>true</code> then only <i>visible</i> {@link Widget}s are considered.
	 */
    public StringDataMatcher(String value, Class<? extends Widget> clazz, boolean visible) {
        super((Object) value, clazz, visible);
    }

    /**
	 * Creates a {@link StringDataMatcher}.<br>
	 * Only <i>visible</i> {@link Widget}s are considered.<br>
	 * Uses the {@link #DEFAULT_KEY} to retrieve a {@link Widget}'s data.
	 * 
	 * @param value
	 *            The value to be matched. Can be <code>null</code>.
	 * @param clazz
	 *            The {@link Class} of {@link Widget}s to consider.
	 */
    public StringDataMatcher(String value, Class<? extends Widget> clazz) {
        super((Object) value, clazz);
    }

    /**
	 * Creates a {@link StringDataMatcher}.<br>
	 * Uses the {@link #DEFAULT_KEY} to retrieve a {@link Widget}'s data.<br> {@link Widget}s of all
	 * {@link Widget} subclasses are considered.
	 * 
	 * @param value
	 *            The value to be matched. Can be <code>null</code>.
	 * @param visible
	 *            If <code>true</code> then only <i>visible</i> {@link Widget}s are considered.
	 */
    public StringDataMatcher(String value, boolean visible) {
        super(value, visible);
    }

    /**
	 * Creates a {@link StringDataMatcher}.<br>
	 * Only <i>visible</i> {@link Widget}s are considered.<br>
	 * Uses the {@link #DEFAULT_KEY} to retrieve a {@link Widget}'s data.<br> {@link Widget}s of all
	 * {@link Widget} subclasses are considered.
	 * 
	 * @param value
	 *            The value to be matched. Can be <code>null</code>.
	 */
    public StringDataMatcher(String value) {
        super(value);
    }

    @Override
    public boolean matches(Widget widget) {
        return widgetMatches(widget) && isStringDataMatch(widget, key, (String) value);
    }
}
