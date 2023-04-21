package com.peusoft.widget.calendar;

import java.beans.*;
import java.util.StringTokenizer;

/**
 * @author zhenja
 */
public class YearPeriodPropertyEditor extends PropertyEditorSupport {

    public YearPeriodPropertyEditor() {
    }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        StringTokenizer st = new StringTokenizer(text, "-");
        int begin = new Integer(st.nextToken()).intValue();
        int end = new Integer(st.nextToken()).intValue();
        YearPeriod yp = new YearPeriod(begin, end);
        setValue(yp);
    }
}
