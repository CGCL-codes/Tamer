package com.global360.sketchpadbpmn.graphic;

import com.global360.sketchpadbpmn.documents.StringEnumeration;

public class CatchThrow extends StringEnumeration {

    public static final int ID_NULL = 0;

    public static final int ID_CATCH = 1;

    public static final int ID_THROW = 2;

    public static final String S_NULL = "";

    public static final String S_NONE = "None";

    public static final String S_CATCH_UPPER = "CATCH";

    public static final String S_THROW_UPPER = "THROW";

    public static final String S_CATCH = "Catch";

    public static final String S_THROW = "Throw";

    private static final String internalNames[] = { S_NULL, S_CATCH_UPPER, S_THROW_UPPER };

    private static final String externalNames[] = { S_NONE, S_CATCH, S_THROW };

    private static final int types[] = { ID_NULL, ID_CATCH, ID_THROW };

    public static int[] getTypesArray() {
        return types;
    }

    public CatchThrow() {
        super();
        setValue(ID_NULL);
    }

    public CatchThrow(String value) {
        super();
        setValue(value);
    }

    public CatchThrow(int catchThrowValue) {
        super();
        setValue(catchThrowValue);
    }

    public CatchThrow(CatchThrow original) {
        super(original);
    }

    public CatchThrow clone() throws CloneNotSupportedException {
        return new CatchThrow(this);
    }

    @Override
    protected String[] getExternalValues() {
        return CatchThrow.externalNames;
    }

    @Override
    protected int[] getIntegerValues() {
        return CatchThrow.types;
    }

    @Override
    protected String[] getInternalValues() {
        return CatchThrow.internalNames;
    }
}
