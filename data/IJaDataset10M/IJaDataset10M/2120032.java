package ru.jnano.math.interval;

import java.util.Arrays;
import java.util.Comparator;

public class IntervalNumber implements Comparator<IntervalNumber> {

    private double low;

    private double high;

    public IntervalNumber(double low, double high) {
        super();
        if (low > high) throw new IllegalArgumentException();
        this.low = low;
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public double getHigh() {
        return high;
    }

    public IntervalNumber summ(IntervalNumber num) {
        return new IntervalNumber(this.getLow() + num.getLow(), this.getHigh() + num.getHigh());
    }

    public IntervalNumber pow(double p) {
        return new IntervalNumber(Math.pow(this.getLow(), p), Math.pow(this.getHigh(), p));
    }

    public IntervalNumber mul(IntervalNumber b) {
        double[] t = new double[4];
        t[0] = this.getLow() * b.getLow();
        t[1] = this.getLow() * b.getHigh();
        t[2] = this.getHigh() * b.getLow();
        t[3] = this.getHigh() * b.getHigh();
        Arrays.sort(t);
        return new IntervalNumber(t[0], t[3]);
    }

    public static double compareProbability(IntervalNumber a, IntervalNumber b) {
        if (a.getHigh() < b.getLow()) return 1;
        if (b.getHigh() < a.getLow()) return -1;
        if ((a.getHigh() >= b.getLow()) && (a.getHigh() <= b.getHigh())) {
            if (a.getLow() < b.getLow()) {
                return 1 - ((a.getHigh() - b.getLow()) * (a.getHigh() - b.getLow())) / ((a.getHigh() - a.getLow()) * (b.getHigh() - b.getLow()));
            } else {
                return (b.getHigh() - a.getHigh()) / (b.getHigh() - b.getLow());
            }
        }
        if ((b.getHigh() >= a.getLow()) && (b.getHigh() <= a.getHigh())) {
            if (b.getLow() < a.getLow()) {
                return -(1 - ((b.getHigh() - a.getLow()) * (b.getHigh() - a.getLow())) / ((b.getHigh() - b.getLow()) * (a.getHigh() - a.getLow())));
            } else {
                return -((a.getHigh() - b.getHigh()) / (a.getHigh() - a.getLow()));
            }
        }
        return 0;
    }

    @Override
    public int compare(IntervalNumber a, IntervalNumber b) {
        return (int) IntervalNumber.compareProbability(a, b);
    }
}
