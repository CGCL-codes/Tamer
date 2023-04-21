package org.tm4j.topicmap.tmdm;

import java.util.Set;
import java.util.Map;
import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import org.tm4j.net.Locator;

/**
	@author <a href="mailto:xuan--2007.05--org.tm4j.topicmap.tmdm--tm4j.org@public.software.baldauf.org">Xuân Baldauf</a>
*/
public class TMDMUtil {

    protected static final boolean enableWrapUnmodifieable = TMDMUtil.class.desiredAssertionStatus();

    public static boolean locatorRepresentsURI(Locator locator, String uri) {
        return (locator.getNotation().equals("URI") && locator.getAddress().equals(uri));
    }

    public static <T> Set<T> maybeWrapUnmodifiable(Set<T> set) {
        if (enableWrapUnmodifieable) {
            if (set != null) {
                return Collections.unmodifiableSet(set);
            } else {
                return Collections.emptySet();
            }
        } else {
            if (set != null) {
                return set;
            } else {
                return Collections.emptySet();
            }
        }
    }

    public static <T> Collection<T> maybeWrapUnmodifiable(Collection<T> collection) {
        if (enableWrapUnmodifieable) {
            if (collection != null) {
                return Collections.unmodifiableCollection(collection);
            } else {
                return Collections.emptyList();
            }
        } else {
            if (collection != null) {
                return collection;
            } else {
                return Collections.emptyList();
            }
        }
    }
}
