package org.jcvi.glk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jcvi.auth.DefaultJCVIAuthorizer;
import org.jcvi.auth.JCVIAuthorizer;

public final class GLKUtil {

    private GLKUtil() {
    }

    public static final JCVIAuthorizer DEFAULT_GLK_USER = DefaultJCVIAuthorizer.DEFAULT_TIGR_USER;

    public static void appendFASTA(Appendable appendable, String header, String data) throws IOException {
        appendable.append(">");
        appendable.append(header);
        appendable.append(String.format("%n"));
        appendable.append(String.format(data.replaceAll("(.{60})", "$1%n")));
        if (data.length() % 60 != 0) {
            appendable.append(String.format("%n"));
        }
    }

    public static <K, V> Set<V> getValues(Map<K, V> map, Comparator<V> comparator) {
        return getValues(map.values(), comparator);
    }

    public static <V> Set<V> getValues(Iterable<V> collection, Comparator<V> comparator) {
        Set<V> set = null;
        if (comparator == null) {
            set = new HashSet<V>();
        } else {
            set = new TreeSet<V>(comparator);
        }
        for (V value : collection) {
            set.add(value);
        }
        return set;
    }
}
