package marvin.statistic;

import java.awt.Color;

/**
 * Bar Chart entry.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinBarChartEntry {

    protected String name;

    protected double value;

    protected Color color;

    public MarvinBarChartEntry() {
        this("", 0, Color.black);
    }

    public MarvinBarChartEntry(String name, double value) {
        this(name, value, Color.black);
    }

    public MarvinBarChartEntry(String name, double value, Color color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }
}
