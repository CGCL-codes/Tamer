package org.tigr.seq.seqdata;

import java.util.*;
import org.tigr.seq.log.*;

/**
 * Class to compare base assembly sequences by offset.  Ties are
 * resolved by sequence names, so no element will ever be equal to
 * another.  Very useful for sorting in TreeSets.  */
public class OffsetComparator implements Comparator {

    public boolean equals(Object that) {
        return this == that;
    }

    public int compare(Object pThiz, Object pThat) {
        IBaseAssemblySequence thiz = (IBaseAssemblySequence) pThiz;
        IBaseAssemblySequence that = (IBaseAssemblySequence) pThat;
        int ret = 0;
        try {
            if (thiz.getStartOffset() != that.getStartOffset()) {
                ret = thiz.getStartOffset() - that.getStartOffset();
            } else {
                ret = thiz.getDelegateSequence().getSequenceName().compareTo(that.getDelegateSequence().getSequenceName());
            }
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(OffsetComparator.class, "caught_seqdata_exception"));
        }
        return ret;
    }
}
