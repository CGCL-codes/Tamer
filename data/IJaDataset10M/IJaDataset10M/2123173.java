package org.awelements.table.filter;

import org.awelements.table.Filter;
import org.awelements.table.TableUtils;

public class BooleanPropertyFilter extends Filter {

    private String mProperty;

    public BooleanPropertyFilter(String filterName, String title, String propertyName) {
        super(filterName, title);
        mProperty = propertyName;
    }

    public String getProperty() {
        return mProperty;
    }

    public void setProperty(String property) {
        mProperty = property;
    }

    @Override
    protected String getDefaultDescription() {
        return getTitle();
    }

    @Override
    public FilterParameter[] getParameterDescriptions() {
        return null;
    }

    @Override
    public boolean isColumnFilter() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public boolean accept(int rowIndex, Object object) {
        try {
            return TableUtils.isTrue(TableUtils.getProperty(object, getProperty()));
        } catch (Exception e) {
            return false;
        }
    }
}
