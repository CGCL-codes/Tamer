package org.dpes.compare;

import org.dpes.compare.AComparator.CmpType;

public class CmpResult {

    private boolean areIdentical = false;

    private CmpType type;

    CmpResult() {
        setType(CmpType.NoType);
    }

    CmpResult(boolean isIdentical) {
        setType(CmpType.NoType);
        setIdentical(isIdentical);
    }

    void setType(CmpType type) {
        this.type = type;
    }

    public CmpType getType() {
        return type;
    }

    void setIdentical(boolean areIdentical) {
        this.areIdentical = areIdentical;
    }

    public boolean areIdentical() {
        return areIdentical;
    }
}
