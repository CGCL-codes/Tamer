package org.kumenya.ui.chart.axis;

import java.text.NumberFormat;

/**
 * This class determines the spacing of the tick marks on an axis.
 */
public class TickUnit implements Comparable {

    /** The size of the tick unit. */
    private final double size;

    /** A formatter for the tick unit. */
    private final NumberFormat formatter;

    /** The number of minor ticks. */
    private final int minorTickCount;

    /**
     * Creates a new number tick unit.
     *
     * @param size  the size of the tick unit.
     */
    public TickUnit(double size) {
        this(size, NumberFormat.getNumberInstance());
    }

    /**
     * Creates a new number tick unit.
     *
     * @param size  the size of the tick unit.
     * @param formatter  a number formatter for the tick unit (<code>null</code>
     *                   not permitted).
     */
    public TickUnit(double size, NumberFormat formatter) {
        this(size, formatter, 0);
    }

    /**
     * Creates a new number tick unit.
     *
     * @param size  the size of the tick unit.
     * @param formatter  a number formatter for the tick unit (<code>null</code>
     *                   not permitted).
     */
    public TickUnit(double size, NumberFormat formatter, int minorTickCount) {
        this.size = size;
        if (formatter == null) {
            throw new IllegalArgumentException("Null 'formatter' argument.");
        }
        this.formatter = formatter;
        this.minorTickCount = minorTickCount;
    }

    public int getMinorTickCount() {
        return this.minorTickCount;
    }

    /**
     * Compares this tick unit to an arbitrary object.
     *
     * @param object  the object to compare against.
     *
     * @return <code>1</code> if the size of the other object is less than this,
     *      <code>0</code> if both have the same size and <code>-1</code> this
     *      size is less than the others.
     */
    public int compareTo(Object object) {
        if (object instanceof TickUnit) {
            TickUnit other = (TickUnit) object;
            if (this.size > other.getSize()) {
                return 1;
            } else if (this.size < other.getSize()) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    /**
     * Returns the size of the tick unit.
     *
     * @return The size of the tick unit.
     */
    public double getSize() {
        return this.size;
    }

    /**
     * Converts a value to a string.
     *
     * @param value  the value.
     *
     * @return The formatted string.
     */
    public String valueToString(double value) {
        return this.formatter.format(value);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TickUnit)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        TickUnit that = (TickUnit) obj;
        if (!this.formatter.equals(that.formatter)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "[size=" + this.valueToString(this.getSize()) + "]";
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (this.formatter != null ? this.formatter.hashCode() : 0);
        return result;
    }
}
