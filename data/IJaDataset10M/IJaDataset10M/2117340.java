package org.kumenya.ui.chart.drawing;

/**
 * Rectangle in the RateChart's coordinate space.
 *
 * @author Jean Morissette
 */
public class Rectangle {

    public final long timestamp1;

    public final Object value1;

    public final long timestamp2;

    public final Object value2;

    public Rectangle(long timestamp1, Object value1, long timestamp2, Object value2) {
        this.timestamp1 = timestamp1;
        this.value1 = value1;
        this.timestamp2 = timestamp2;
        this.value2 = value2;
    }

    public Rectangle union(Rectangle r) {
        throw new UnsupportedOperationException();
    }
}
