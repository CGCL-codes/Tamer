package com.dcivision.framework.taglib.newCalendar;

public class CascadingStyleSheet {

    public static final String REVISION = "$Revision: 1.1 $";

    private String style;

    private String className;

    private String linkStyle;

    private String linkClass;

    private String link;

    private String field;

    private String target;

    public CascadingStyleSheet() {
        style = null;
        className = null;
        link = null;
        field = null;
        target = null;
        linkStyle = null;
        linkClass = null;
    }

    public String getClassName() {
        return className;
    }

    public String getField() {
        return field;
    }

    public String getLink() {
        return link;
    }

    public String getLinkClass() {
        return linkClass;
    }

    public String getLinkStyle() {
        return linkStyle;
    }

    public String getStyle() {
        return style;
    }

    public String getTarget() {
        return target;
    }

    public void setClassName(String s) {
        className = s;
    }

    public void setField(String s) {
        field = s;
    }

    public void setLink(String s) {
        link = s;
    }

    public void setLinkClass(String s) {
        linkClass = s;
    }

    public void setLinkStyle(String s) {
        linkStyle = s;
    }

    public void setStyle(String s) {
        style = s;
    }

    public void setTarget(String s) {
        target = s;
    }
}
