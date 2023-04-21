package org.columba.core.filter;

import org.columba.core.config.DefaultItem;
import org.columba.core.xml.XmlElement;

public class FilterCriteria extends DefaultItem {

    private static final String ELEMENT = "criteria";

    private static final String CRITERIA = "criteria";

    private static final String TYPE = "type";

    private static final String PATTERN = "pattern";

    public static final int CONTAINS = 0;

    public static final int CONTAINS_NOT = 1;

    public static final int IS = 2;

    public static final int IS_NOT = 3;

    public static final int BEGINS_WITH = 4;

    public static final int ENDS_WITH = 5;

    public static final int DATE_BEFORE = 6;

    public static final int DATE_AFTER = 7;

    public static final int SIZE_SMALLER = 8;

    public static final int SIZE_BIGGER = 9;

    private final String[] criteria = { "contains", "contains not", "is", "is not", "begins with", "ends with", "before", "after", "smaller", "bigger" };

    public FilterCriteria() {
        super(new XmlElement(FilterCriteria.ELEMENT));
    }

    public FilterCriteria(XmlElement root) {
        super(root);
    }

    public String getCriteriaString() {
        return getRoot().getAttribute(FilterCriteria.CRITERIA);
    }

    public void setCriteria(int c) {
        setCriteriaString(criteria[c]);
    }

    public int getCriteria() {
        String condition = getCriteriaString();
        int c = -1;
        for (int i = 0; i < criteria.length; i++) {
            if (criteria[i].equals(condition)) c = i;
        }
        return c;
    }

    public void setCriteriaString(String s) {
        getRoot().addAttribute(FilterCriteria.CRITERIA, s);
    }

    public String getTypeString() {
        return getRoot().getAttribute(FilterCriteria.TYPE);
    }

    public void setTypeString(String s) {
        getRoot().addAttribute(FilterCriteria.TYPE, s);
    }

    public String getPatternString() {
        return getRoot().getAttribute(FilterCriteria.PATTERN);
    }

    public void setPatternString(String pattern) {
        getRoot().addAttribute(FilterCriteria.PATTERN, pattern);
    }
}
