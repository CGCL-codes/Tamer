package org.fudaa.ctulu.gis;

import java.util.Comparator;

public class GISSegmentComparator implements Comparator<GISSegment> {

    private double eps;

    public GISSegmentComparator() {
        this(0.1d);
    }

    public GISSegmentComparator(double eps) {
        this.eps = eps;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public int compare(GISSegment segment1, GISSegment segment2) {
        if (segment1 == null) {
            if (segment2 == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (segment2 == null) {
            return 1;
        }
        boolean isEquals = true;
        if (segment1.getPt1().distance(segment2.getPt1()) > this.eps) {
            if (segment1.getPt1().distance(segment2.getPt2()) > this.eps) {
                isEquals = false;
            } else if (segment1.getPt2().distance(segment2.getPt1()) > this.eps) {
                isEquals = false;
            }
        } else if (segment1.getPt2().distance(segment2.getPt2()) > this.eps) {
            isEquals = false;
        }
        if (!isEquals) {
            if (segment1.getPt1().distance(segment1.getPt2()) < segment2.getPt1().distance(segment2.getPt2())) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }
}
