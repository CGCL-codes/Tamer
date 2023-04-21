package model;

import java.util.ArrayList;

public class OperationDef {

    private int opNo;

    private String opName;

    public OperationDef(int opNo, String opName) {
        this.opNo = opNo;
        this.opName = opName;
    }

    public String getOpName() {
        return opName;
    }

    public int getOpNo() {
        return opNo;
    }
}
