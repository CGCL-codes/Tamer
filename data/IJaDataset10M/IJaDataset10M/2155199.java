package org.mm.proxycache.util;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Comparator;
import com.sleepycat.persist.model.Persistent;

@Persistent
public class CaseInsensitiveComparator implements Comparator<String> {

    public int compare(String o1, String o2) {
        checkNotNull(o1);
        checkNotNull(o2);
        return o1.compareToIgnoreCase(o2);
    }
}
