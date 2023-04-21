package org.j2eebuilder.util;

import java.sql.Timestamp;
import java.beans.PropertyEditorSupport;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)TimestampEditor.java	1.3.1 10/15/2002
 * @version 1.3.1
 */
public class TimestampEditor extends PropertyEditorSupport {

    private static transient LogManager log = new LogManager(TimestampEditor.class);

    public TimestampEditor() {
        super();
    }

    public String getAsText() {
        return String.valueOf(getValue());
    }

    public void setAsText(String s) {
        setValue(java.sql.Timestamp.valueOf(s));
    }
}
