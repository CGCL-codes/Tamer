package com.dynamide;

public interface IValueBeanList extends IValueBean {

    public Object cell(int r);

    public int getCurrentRow();

    public int getDisplayRowCount();

    /** @return the zero-based index of the first visible row.
     */
    public int getDisplayFirstRowNum();
}
