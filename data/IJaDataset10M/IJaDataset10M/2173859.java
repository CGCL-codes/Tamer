package src.projects.findPeaks.objects;

import src.projects.findPeaks.FPConstants;

public class PeakPairIdx implements Comparable<PeakPairIdx> {

    private final int pk_idx_1;

    private final int pk_idx_2;

    private float height_1;

    private float height_2;

    private double p_value;

    public PeakPairIdx(int a, int b, float h_a, float h_b) {
        pk_idx_1 = a;
        pk_idx_2 = b;
        height_1 = h_a;
        height_2 = h_b;
        p_value = 0;
    }

    public PeakPairIdx(int a, int b, double h_a, double h_b) {
        pk_idx_1 = a;
        pk_idx_2 = b;
        height_1 = (float) h_a;
        height_2 = (float) h_b;
        p_value = 0;
    }

    public final int get_pk_idx_1() {
        return this.pk_idx_1;
    }

    public final int get_location() {
        return this.pk_idx_1;
    }

    public final int get_pk_idx_2() {
        return this.pk_idx_2;
    }

    public final float get_height_1() {
        return this.height_1;
    }

    public final float get_height_2() {
        return this.height_2;
    }

    public final double get_p_value() {
        return this.p_value;
    }

    public final void set_p_value(double p) {
        this.p_value = p;
    }

    public final String toString() {
        return this.pk_idx_1 + "\t" + this.pk_idx_2 + "\t" + this.height_1 + "\t" + this.height_2;
    }

    public int compareTo(PeakPairIdx a) {
        if (a.height_1 < height_1) {
            return 1;
        }
        if (a.height_1 > height_1) {
            return -1;
        }
        if (a.height_2 < height_2) {
            return 1;
        }
        if (a.height_2 > height_2) {
            return -1;
        }
        return 0;
    }

    public void log_transform() {
        if (this.height_1 == 0) {
            this.height_1 = Float.MIN_VALUE;
        } else {
            this.height_1 = (float) Math.log10(this.height_1);
        }
        if (this.height_2 == 0) {
            this.height_2 = Float.MIN_VALUE;
        } else {
            this.height_2 = (float) Math.log10(this.height_2);
        }
    }

    public void log_revert() {
        if (this.height_1 == Float.MIN_VALUE) {
            this.height_1 = 0;
        } else {
            this.height_1 = (float) Math.pow(FPConstants.BASE_TEN, this.height_1);
        }
        if (this.height_2 == Float.MIN_VALUE) {
            this.height_2 = 0;
        } else {
            this.height_2 = (float) Math.pow(FPConstants.BASE_TEN, this.height_2);
        }
    }
}
