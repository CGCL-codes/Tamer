package com.daffodilwoods.rbtreesizesequence;

import java.util.Comparator;

public interface SizeSequenceUser {

    public long traceDistance(Object locationId1, Object locationId2, int max);

    public Comparator getComparator();
}
