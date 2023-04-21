package org.openconcerto.utils.sync;

import java.util.ArrayList;
import java.util.List;

public class RangeList {

    private final List<Range> list = new ArrayList<Range>();

    private final int limit;

    public RangeList(int limit) {
        this.limit = limit;
    }

    public void add(Range range) {
        if (range.getStart() < 0 || range.getStart() >= limit) {
            throw new IllegalArgumentException(range + " start out of limit");
        }
        if (range.getStop() < 0 || range.getStop() > limit) {
            throw new IllegalArgumentException(range + " stop out of limit");
        }
        if (list.size() > 0) {
            Range last = list.get(list.size() - 1);
            if (last.getStop() == range.getStart()) {
                last.setStop(range.getStop());
            } else if (range.getStart() < last.getStart()) {
                throw new IllegalArgumentException("start (" + range.getStart() + ") < lastStart (" + last.getStart() + ")");
            } else {
                list.add(range);
            }
        } else {
            list.add(range);
        }
    }

    public List<Range> getUsedRanges() {
        return this.list;
    }

    public List<Range> getUnusedRanges() {
        List<Range> result = new ArrayList<Range>();
        if (list.size() == 0) {
            result.add(new Range(0, limit));
            return result;
        }
        Range r = new Range(0, list.get(0).getStart());
        if (!r.isEmpty()) {
            result.add(r);
        }
        for (int i = 0; i < list.size() - 1; i++) {
            Range r1 = this.list.get(i);
            Range r2 = this.list.get(i + 1);
            result.add(new Range(r1.getStop(), r2.getStart()));
        }
        Range lastRange = new Range(list.get(list.size() - 1).getStop(), limit);
        if (!lastRange.isEmpty()) {
            result.add(lastRange);
        }
        return result;
    }

    public void dump() {
        System.out.println("RangeList 0 - " + this.limit);
        System.out.println("Used");
        for (Range r : getUsedRanges()) {
            System.out.println(r);
        }
        System.out.println("Unused");
        final List<Range> unusedRanges = getUnusedRanges();
        for (Range r : unusedRanges) {
            System.out.println(r);
        }
        System.out.println("===============");
    }
}
