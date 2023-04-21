package com.jhlabs.map;

import java.io.Serializable;
import java.text.NumberFormat;

public class Unit implements Serializable {

    static final long serialVersionUID = -6704954923429734628L;

    public static final int ANGLE_UNIT = 0;

    public static final int LENGTH_UNIT = 1;

    public static final int AREA_UNIT = 2;

    public static final int VOLUME_UNIT = 3;

    public String name, plural, abbreviation;

    public double value;

    public static NumberFormat format;

    static {
        format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setGroupingUsed(false);
    }

    public Unit(String name, String plural, String abbreviation, double value) {
        this.name = name;
        this.plural = plural;
        this.abbreviation = abbreviation;
        this.value = value;
    }

    public double toBase(double n) {
        return n * value;
    }

    public double fromBase(double n) {
        return n / value;
    }

    public double parse(String s) throws NumberFormatException {
        try {
            return format.parse(s).doubleValue();
        } catch (java.text.ParseException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }

    public String format(double n) {
        return format.format(n) + " " + abbreviation;
    }

    public String format(double n, boolean abbrev) {
        if (abbrev) return format.format(n) + " " + abbreviation;
        return format.format(n);
    }

    public String format(double x, double y, boolean abbrev) {
        if (abbrev) return format.format(x) + "/" + format.format(y) + " " + abbreviation;
        return format.format(x) + "/" + format.format(y);
    }

    public String format(double x, double y) {
        return format(x, y, true);
    }

    public String toString() {
        return plural;
    }

    public boolean equals(Object o) {
        if (o instanceof Unit) {
            return ((Unit) o).value == value;
        }
        return false;
    }
}
