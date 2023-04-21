package com.chromamorph;

public class TimeSignature implements Comparable<TimeSignature> {

    private Location onsetLocation, offsetLocation;

    private Integer numerator, denominator;

    public TimeSignature(Location onsetLocation, Location offsetLocation, Integer numerator, Integer denominator) {
        super();
        this.onsetLocation = onsetLocation;
        this.offsetLocation = offsetLocation;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int compareTo(TimeSignature ts) {
        int diff = getOnsetLocation().compareTo(ts.getOnsetLocation());
        if (diff != 0) return diff;
        diff = getOffsetLocation().compareTo(ts.getOffsetLocation());
        if (diff != 0) return diff;
        diff = getNumerator().compareTo(ts.getNumerator());
        if (diff != 0) return diff;
        diff = getDenominator().compareTo(ts.getDenominator());
        if (diff != 0) return diff;
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TimeSignature)) return false;
        return compareTo((TimeSignature) obj) == 0;
    }

    public String toString() {
        return "[" + getOnsetLocation() + ", " + getOffsetLocation() + ", " + getNumerator() + "/" + getDenominator() + "]";
    }

    public Location getOnsetLocation() {
        return onsetLocation;
    }

    public void setOnsetLocation(Location onsetLocation) {
        this.onsetLocation = onsetLocation;
    }

    public Location getOffsetLocation() {
        return offsetLocation;
    }

    public void setOffsetLocation(Location offsetLocation) {
        this.offsetLocation = offsetLocation;
    }

    public Integer getNumerator() {
        return numerator;
    }

    public void setNumerator(Integer numerator) {
        this.numerator = numerator;
    }

    public Integer getDenominator() {
        return denominator;
    }

    public void setDenominator(Integer denominator) {
        this.denominator = denominator;
    }

    public Rational getBarLengthInCrotchets() {
        return new Rational(getNumerator(), getDenominator()).times(new Rational(4, 1));
    }

    public Rational getDurationInCrotchets(Location on, Location off) {
        Rational numberOfCompleteBars = new Rational(off.getBarNumber() - on.getBarNumber());
        return getBarLengthInCrotchets().times(numberOfCompleteBars.minus(on.getBarFraction()).plus(off.getBarFraction()));
    }
}
