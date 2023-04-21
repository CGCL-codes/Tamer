package com.dmoving.log.ui;

import com.dmoving.log.ItemTimeStamp;

public class FilterCondition {

    private FilteredField filteredField;

    private Object filterContent;

    public FilteredField getFilteredField() {
        return filteredField;
    }

    public void setFilteredField(FilteredField filteredField) {
        this.filteredField = filteredField;
    }

    public Object getFilterContent() {
        return filterContent;
    }

    public void setFilterContent(Object filterContent) {
        this.filterContent = filterContent;
    }

    public FilterCondition(FilteredField filteredField, ItemTimeStamp filterContent) {
        this.filteredField = filteredField;
        this.filterContent = filterContent;
    }

    public FilterCondition(FilteredField filteredField, String filterContent) {
        this.filteredField = filteredField;
        this.filterContent = filterContent;
    }

    public void addLogLevelCondition(String logLevel) {
        this.filterContent = FilteredField.LOGLEVEL;
        this.filterContent = logLevel;
    }
}
