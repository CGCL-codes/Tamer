package org.smepp.logger;

public final class Level {

    public static final Level ALL = new Level(Integer.MIN_VALUE);

    public static final Level FINEST = new Level(300);

    public static final Level FINER = new Level(400);

    public static final Level FINE = new Level(500);

    public static final Level CONFIG = new Level(700);

    public static final Level INFO = new Level(800);

    public static final Level WARNING = new Level(900);

    public static final Level SEVERE = new Level(1000);

    public static final Level OFF = new Level(Integer.MAX_VALUE);

    private final int value;

    private Level(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public String toString() {
        switch(value) {
            case Integer.MIN_VALUE:
                return "ALL";
            case 300:
                return "FINEST";
            case 400:
                return "FINER";
            case 500:
                return "FINE";
            case 700:
                return "CONFIG";
            case 800:
                return "INFO";
            case 900:
                return "WARNING";
            case 1000:
                return "SEVERE";
            case Integer.MAX_VALUE:
                return "OFF";
            default:
                return String.valueOf(value);
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Level other = (Level) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.value;
        return hash;
    }
}
