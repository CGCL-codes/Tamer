package com.gwtaf.ext.core.client.record.recordfields;

import com.gwtaf.ext.core.client.record.RecordField;
import com.gwtaf.ext.core.client.record.fielddef.StringFieldDefEx;
import com.gwtext.client.core.TextAlign;

public abstract class StringRecordField<T> extends RecordField<T, String> {

    public StringRecordField(String name) {
        super(new StringFieldDefEx(name));
    }

    public TextAlign getAlign() {
        return TextAlign.LEFT;
    }
}
