package net.community.chest.util.map.entries;

import java.util.Comparator;
import java.util.Map;
import net.community.chest.lang.StringUtil;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * <P>Compares 2 {@link java.util.Map.Entry}-es by comparing their keys' <code>toString</code>
 * values (uses case <U>sensitive</U> by default).
 * @author Lyor G.
 * @since Oct 9, 2007 8:23:21 AM
 */
public class MapEntryKeyStringComparator implements Comparator<Map.Entry<?, ?>> {

    private boolean _caseSensitive;

    public boolean isCaseSensitive() {
        return _caseSensitive;
    }

    public void setCaseSensitive(boolean enabled) {
        _caseSensitive = enabled;
    }

    public MapEntryKeyStringComparator(boolean caseSensitive) {
        _caseSensitive = caseSensitive;
    }

    public MapEntryKeyStringComparator() {
        this(true);
    }

    @Override
    public int compare(Map.Entry<?, ?> o1, Map.Entry<?, ?> o2) {
        final Object k1 = (null == o1) ? null : o1.getKey(), k2 = (null == o2) ? null : o2.getKey();
        final String s1 = (null == k1) ? null : k1.toString(), s2 = (null == k2) ? null : k2.toString();
        return StringUtil.compareDataStrings(s1, s2, isCaseSensitive());
    }

    public static final MapEntryKeyStringComparator CASE_SENSITIVE = new MapEntryKeyStringComparator(true), CASE_INSENSITIVE = new MapEntryKeyStringComparator(false);
}
