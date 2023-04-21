package com.ark.fix.model.fixml;

import com.ark.fix.model.*;

public class NoContraBrokers extends FIXMLField {

    public static final String TAG = "382";

    public static final int DATA_TYPE = FIXDataConverter.INT;

    private String _Value;

    public String getValue() {
        return _Value;
    }

    public void setValue(String s) {
        _Value = s;
    }

    public void initValue(Object s) throws ModelException {
        if (_Value != null) throw new ModelException("Value has already been initialized for field NoContraBrokers.");
        setValue((String) s);
    }

    public String getTagValue() {
        return _Value;
    }

    public void setTagValue(String s) {
        setValue(s);
    }

    public String getTag() {
        return TAG;
    }

    public int getTagDataType() {
        return DATA_TYPE;
    }

    public String[] getTagValueEnum() {
        return new String[0];
    }

    public String toFIXMessage() {
        if (_Value == null || _Value.length() == 0) return EMPTY;
        return TAG + ES + _Value + SOH;
    }

    public String toFIXML(String ident) {
        if (_Value == null || _Value.length() == 0) return "";
        StringBuffer sb = new StringBuffer(ident + "<NoContraBrokers FIXTag=\"382\" DataType=\"int\"");
        sb.append(">" + _Value + "</NoContraBrokers>");
        return sb.toString();
    }
}
