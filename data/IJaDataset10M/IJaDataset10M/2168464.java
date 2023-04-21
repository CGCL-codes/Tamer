package com.lolcode.types;

public class YarnType extends AbstractLOLVariable {

    /**
	 * The value of this YARN.
	 */
    private String value;

    public YarnType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public NumbarType asNumbar() {
        return new NumbarType(convertToDouble());
    }

    public NumbrType asNumbr() {
        return new NumbrType(convertToLong());
    }

    /**
	 * Takes the first part of this string and attempts to make a double out of it.
	 * @return
	 */
    private double convertToDouble() {
        String[] strings = value.split("[^0-9\\.]+");
        if (strings.length > 0) {
            try {
                return Double.parseDouble(strings[0]);
            } catch (Throwable t) {
            }
        }
        return 0.0;
    }

    /**
	 * Takes the first part of this string and attempts to make a long out of it.
	 * @return
	 */
    private long convertToLong() {
        String[] strings = value.split("[^0-9]+");
        if (strings.length > 0) {
            try {
                return Long.parseLong(strings[0]);
            } catch (Throwable t) {
            }
        }
        return 0;
    }

    public YarnType asYarn() {
        return this;
    }

    public boolean equals(LOLVariable other) {
        return equals((Object) other);
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final YarnType other = (YarnType) obj;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    public String toString() {
        return "Yarn [" + value + "]";
    }
}
