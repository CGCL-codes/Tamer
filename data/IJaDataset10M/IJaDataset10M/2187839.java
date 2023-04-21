package com.oreditions.champollion.model.typesize;

import com.oreditions.champollion.visitor.*;
import java.io.File;
import java.util.TreeMap;

/**
 *
 * @author olivier
 */
public class TSType implements TSVisitable {

    public static final long VERY_SMALL = 100000;

    public static final long SMALL = 500000;

    public static final long MIDDLE = 1000000;

    public static final long BIG = 5000000;

    public static final long VERY_BIG = 10000000;

    public static final long HUGE = 500000000;

    public static final long VERY_HUGE = 100000000;

    public static final long ENORMOUS = 500000000;

    public static final long VERY_ENORMOUS = 1000000000;

    public static final long FROM_BEYOND = -1;

    public static final String DESCR_VERY_SMALL = "Size &lt; 100 kB";

    public static final String DESCR_SMALL = "100 kB &le; Size &lt; 500 kB";

    public static final String DESCR_MIDDLE = "500 kB &le; Size &lt; 1 MB";

    public static final String DESCR_BIG = "1 MB &le; Size &lt; 5 MB";

    public static final String DESCR_VERY_BIG = "5 MB &le; Size &lt; 10 MB";

    public static final String DESCR_HUGE = "10 MB &le; Size &lt; 50 MB";

    public static final String DESCR_VERY_HUGE = "50 MB &le; Size &lt; 100 MB";

    public static final String DESCR_ENORMOUS = "100 MB &le; Size &lt; 500 MB";

    public static final String DESCR_VERY_ENORMOUS = "500 MB &le; Size &lt; 1 GB";

    public static final String DESCR_FROM_BEYOND = "1 GB &le; Size";

    protected String ext = null;

    protected TreeMap<Long, TSInterval> intervals = new TreeMap<Long, TSInterval>();

    public TSType(String ext) {
        this.ext = ext;
        intervals.put(VERY_SMALL, new TSInterval(0, VERY_SMALL, DESCR_VERY_SMALL, ext));
        intervals.put(SMALL, new TSInterval(VERY_SMALL, SMALL, DESCR_SMALL, ext));
        intervals.put(MIDDLE, new TSInterval(SMALL, MIDDLE, DESCR_MIDDLE, ext));
        intervals.put(BIG, new TSInterval(MIDDLE, BIG, DESCR_BIG, ext));
        intervals.put(VERY_BIG, new TSInterval(BIG, VERY_BIG, DESCR_VERY_BIG, ext));
        intervals.put(HUGE, new TSInterval(VERY_BIG, HUGE, DESCR_HUGE, ext));
        intervals.put(VERY_HUGE, new TSInterval(HUGE, VERY_HUGE, DESCR_VERY_HUGE, ext));
        intervals.put(ENORMOUS, new TSInterval(VERY_HUGE, ENORMOUS, DESCR_ENORMOUS, ext));
        intervals.put(VERY_ENORMOUS, new TSInterval(ENORMOUS, VERY_ENORMOUS, DESCR_VERY_ENORMOUS, ext));
        intervals.put(FROM_BEYOND, new TSInterval(VERY_ENORMOUS, FROM_BEYOND, DESCR_FROM_BEYOND, ext));
    }

    public void add(File f) {
        long l = f.length();
        if (l < VERY_SMALL) {
            intervals.get(VERY_SMALL).addFile(f);
            return;
        }
        if (l < SMALL) {
            intervals.get(SMALL).addFile(f);
            return;
        }
        if (l < MIDDLE) {
            intervals.get(MIDDLE).addFile(f);
            return;
        }
        if (l < BIG) {
            intervals.get(BIG).addFile(f);
            return;
        }
        if (l < VERY_BIG) {
            intervals.get(VERY_BIG).addFile(f);
            return;
        }
        if (l < HUGE) {
            intervals.get(HUGE).addFile(f);
            return;
        }
        if (l < VERY_HUGE) {
            intervals.get(VERY_HUGE).addFile(f);
            return;
        }
        if (l < ENORMOUS) {
            intervals.get(ENORMOUS).addFile(f);
            return;
        }
        if (l < VERY_ENORMOUS) {
            intervals.get(VERY_ENORMOUS).addFile(f);
            return;
        }
        if (l >= VERY_ENORMOUS) {
            intervals.get(FROM_BEYOND).addFile(f);
            return;
        }
        throw new RuntimeException("TSType sorting issue. Should not happen...");
    }

    public void acceptVisitor(TSVisitor vis) {
        vis.visit(this);
    }

    public TreeMap<Long, TSInterval> getIntervals() {
        return intervals;
    }

    public String getExtension() {
        return ext;
    }

    public int getNumberOfFiles() {
        int i = 0;
        for (TSInterval inter : intervals.values()) i += inter.getSizes().size();
        return i;
    }
}
