package edu.umn.gis.mapscript;

public final class MS_JOIN_TYPE {

    public static final MS_JOIN_TYPE MS_JOIN_ONE_TO_ONE = new MS_JOIN_TYPE("MS_JOIN_ONE_TO_ONE");

    public static final MS_JOIN_TYPE MS_JOIN_ONE_TO_MANY = new MS_JOIN_TYPE("MS_JOIN_ONE_TO_MANY");

    public final int swigValue() {
        return swigValue;
    }

    public String toString() {
        return swigName;
    }

    public static MS_JOIN_TYPE swigToEnum(int swigValue) {
        if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue) return swigValues[swigValue];
        for (int i = 0; i < swigValues.length; i++) if (swigValues[i].swigValue == swigValue) return swigValues[i];
        throw new IllegalArgumentException("No enum " + MS_JOIN_TYPE.class + " with value " + swigValue);
    }

    private MS_JOIN_TYPE(String swigName) {
        this.swigName = swigName;
        this.swigValue = swigNext++;
    }

    private MS_JOIN_TYPE(String swigName, int swigValue) {
        this.swigName = swigName;
        this.swigValue = swigValue;
        swigNext = swigValue + 1;
    }

    private MS_JOIN_TYPE(String swigName, MS_JOIN_TYPE swigEnum) {
        this.swigName = swigName;
        this.swigValue = swigEnum.swigValue;
        swigNext = this.swigValue + 1;
    }

    private static MS_JOIN_TYPE[] swigValues = { MS_JOIN_ONE_TO_ONE, MS_JOIN_ONE_TO_MANY };

    private static int swigNext = 0;

    private final int swigValue;

    private final String swigName;
}
