package de.cabanis.unific.utilities;

/**
 * TODO javadoc
 * @author Nicolas Cabanis
 */
public class SimplePercentage {

    private byte percentage;

    public SimplePercentage(byte percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percentage = percentage;
    }

    public SimplePercentage(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percentage = (byte) percentage;
    }

    public String stringValue() {
        return String.valueOf(percentage);
    }

    public byte value() {
        return percentage;
    }

    public boolean equals(Object obj) {
        if (obj instanceof SimplePercentage) {
            return (((SimplePercentage) obj).percentage == this.percentage);
        }
        return false;
    }

    public int hashCode() {
        return percentage;
    }
}
