package com.sitescape.team.comparator;

import java.util.Comparator;
import com.sitescape.team.domain.PersistentLongId;

public class LongIdComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {
        PersistentLongId f1, f2;
        f1 = (PersistentLongId) obj1;
        f2 = (PersistentLongId) obj2;
        if (f1 == f2) return 0;
        if (f1 == null) return 1;
        if (f2 == null) return -1;
        return f1.getId().compareTo(f2.getId());
    }
}
