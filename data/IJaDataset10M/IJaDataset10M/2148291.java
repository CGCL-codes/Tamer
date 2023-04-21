package org.apache.solr.util;

import java.util.List;
import java.util.Map;

/**
 * This class is scheduled for deletion.  Please update your code to the moved package.
 *
 * @deprecated Use org.apache.solr.common.util.SimpleOrderedMap<T>.
 */
@Deprecated
public class SimpleOrderedMap<T> extends org.apache.solr.common.util.SimpleOrderedMap<T> {

    public SimpleOrderedMap() {
        super();
    }

    @Deprecated
    public SimpleOrderedMap(List nameValuePairs) {
        super(nameValuePairs);
    }

    public SimpleOrderedMap(Map.Entry<String, T>[] nameValuePairs) {
        super(nameValuePairs);
    }
}
