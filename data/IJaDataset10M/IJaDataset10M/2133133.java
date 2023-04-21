package umlc.parseTree;

;

public class Multiplicity {

    public static boolean toInfinity = true;

    private int multiplicity_low_range;

    private int multiplicity_hi_range;

    private boolean to_infinity;

    private int state;

    private static int ONE_NUMBER = 1;

    private static int INFINITY = 2;

    private static int RANGE = 3;

    private static int RANGE_TO_INFINITY = 4;

    Multiplicity() {
    }

    Multiplicity(int oneValue) {
        multiplicity_low_range = oneValue;
        multiplicity_hi_range = oneValue;
        to_infinity = false;
        state = ONE_NUMBER;
    }

    Multiplicity(boolean _toInfinity) {
        multiplicity_low_range = 0;
        multiplicity_hi_range = 0;
        to_infinity = true;
        state = INFINITY;
    }

    Multiplicity(int low, int high) {
        multiplicity_low_range = low;
        multiplicity_hi_range = high;
        to_infinity = false;
        state = RANGE;
    }

    Multiplicity(int low, boolean _toInfinity) {
        multiplicity_low_range = low;
        multiplicity_hi_range = 0;
        to_infinity = true;
        state = RANGE_TO_INFINITY;
    }

    public boolean isManyRelationship() {
        if (state == ONE_NUMBER) return false; else return true;
    }

    public String toString() {
        if (state == ONE_NUMBER) {
            return (multiplicity_low_range + "");
        } else if (state == INFINITY) {
            return "*";
        } else if (state == RANGE) {
            return multiplicity_low_range + ".." + multiplicity_hi_range;
        } else if (state == RANGE_TO_INFINITY) {
            return multiplicity_low_range + "..*";
        }
        return "State Not Set";
    }

    void pretty_print() {
        if (state == ONE_NUMBER) {
            System.out.print(" " + multiplicity_low_range + " ");
        } else if (state == INFINITY) {
            System.out.print(" * ");
        } else if (state == RANGE) {
            System.out.print(" " + multiplicity_low_range + ".." + multiplicity_hi_range + " ");
        } else if (state == RANGE_TO_INFINITY) {
            System.out.print(" " + multiplicity_low_range + "..*");
        }
    }
}
