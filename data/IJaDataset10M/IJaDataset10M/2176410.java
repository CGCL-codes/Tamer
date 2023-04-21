package fit;

public class PrimitiveFixture extends Fixture {

    public static long parseLong(Parse cell) {
        return Long.parseLong(cell.text());
    }

    public static double parseDouble(Parse cell) {
        return Double.parseDouble(cell.text());
    }

    public static boolean parseBoolean(Parse cell) {
        return Boolean.valueOf(cell.text()).booleanValue();
    }

    public void check(Parse cell, String value) {
        if (cell.text().equals(value)) {
            right(cell);
        } else {
            wrong(cell, value);
        }
    }

    public void check(Parse cell, long value) {
        if (parseLong(cell) == value) {
            right(cell);
        } else {
            wrong(cell, Long.toString(value));
        }
    }

    public void check(Parse cell, double value) {
        if (parseDouble(cell) == value) {
            right(cell);
        } else {
            wrong(cell, Double.toString(value));
        }
    }

    public void check(Parse cell, boolean value) {
        if (parseBoolean(cell) == value) {
            right(cell);
        } else {
            wrong(cell, "" + value);
        }
    }
}
