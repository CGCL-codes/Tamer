package com.google.code.b0rx0r.advancedSamplerEngine.effect.modulation;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class EnvelopeModulation implements Modulation {

    private SortedSet<Point> points = new TreeSet<Point>();

    private EnvelopeInterpolationStrategy eis = new LinearEnvelopeInterpolationStrategy();

    public EnvelopeModulation addPoint(int offset, float value) {
        Point p = new Point(offset, value);
        points.add(p);
        return this;
    }

    @Override
    public float getValue(long offset) {
        if (points.isEmpty()) return 1f;
        assert points.size() > 0;
        Point first = points.first();
        Point last = points.last();
        if (offset <= first.offset) {
            return first.value;
        }
        if (offset >= last.offset) {
            return last.value;
        }
        assert points.size() > 1;
        Iterator<Point> it = points.iterator();
        Point current = it.next();
        Point previous = null;
        while (true) {
            if (current.offset == offset) return current.value;
            if (current.offset > offset) {
                return eis.getValue(previous.value, (int) (offset - previous.offset), current.value, current.offset - previous.offset);
            }
            previous = current;
            current = it.next();
        }
    }

    private static class Point implements Comparable<Point> {

        public int offset;

        public float value;

        public Point(int offset, float value) {
            this.offset = offset;
            this.value = value;
        }

        @Override
        public int compareTo(Point o) {
            return offset - o.offset;
        }
    }

    @Override
    public void setGlobalOffset(long offset) {
    }
}
