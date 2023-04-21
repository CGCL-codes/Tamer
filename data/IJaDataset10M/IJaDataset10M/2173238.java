package iicm.vrml.pw;

import java.io.*;
import java.util.Hashtable;

/**
 * SFInt32 - Field that holds one int (32 bit)
 * Copyright (c) 1996 IICM
 *
 * @author Michael Pichler, Karin Roschker
 * @version 0.1, latest change:  30 Aug 96
 */
public class SFInt32 extends Field {

    private int value;

    public String fieldName() {
        return FieldNames.FIELD_SFInt32;
    }

    Field newFieldInstance() {
        return new SFInt32(value);
    }

    SFInt32(int val) {
        value = val;
    }

    public final int getValue() {
        return value;
    }

    public final void setValue(int val) {
        value = val;
    }

    void copyValue(Field source) {
        value = ((SFInt32) source).value;
    }

    void readValue(VRMLparser parser) throws IOException {
        value = (int) readIntValue(parser.istok);
    }

    void writeValue(PrintStream os, Hashtable writtenrefs) {
        os.print(value);
    }

    void writeX3dValue(PrintStream os, Hashtable writtenrefs) {
        os.print(value);
    }
}
