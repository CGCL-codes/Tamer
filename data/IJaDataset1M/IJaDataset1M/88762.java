package org.quickfix.fix44;

import org.quickfix.FieldNotFound;
import org.quickfix.Group;
import org.quickfix.field.*;

public class TestRequest extends Message {

    public TestRequest() {
        getHeader().setField(new MsgType("1"));
    }

    public TestRequest(org.quickfix.field.TestReqID aTestReqID) {
        getHeader().setField(new MsgType("1"));
        set(aTestReqID);
    }

    public void set(org.quickfix.field.TestReqID value) {
        setField(value);
    }

    public org.quickfix.field.TestReqID get(org.quickfix.field.TestReqID value) throws FieldNotFound {
        getField(value);
        return value;
    }

    public org.quickfix.field.TestReqID getTestReqID() throws FieldNotFound {
        org.quickfix.field.TestReqID value = new org.quickfix.field.TestReqID();
        getField(value);
        return value;
    }

    public boolean isSet(org.quickfix.field.TestReqID field) {
        return isSetField(field);
    }

    public boolean isSetTestReqID() {
        return isSetField(112);
    }
}
