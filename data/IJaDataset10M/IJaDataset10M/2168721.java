package oop.character;

public class Attribute {

    private final String name;

    private double value;

    private final int min;

    private final int max;

    public Attribute(String name, int min, int max) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.value = (min + max) / 2;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        if (value >= this.min && value <= this.max) {
            this.value = value;
        }
    }

    public int getMax() {
        return this.max;
    }

    public int getMin() {
        return this.min;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return getName() + ": " + getValue();
    }
}
