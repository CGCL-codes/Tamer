package org.codehaus.groovy.grails.web.taglib.jsp;

/**
 * @author Graeme Rocher
 * @since 28-Feb-2006
 */
public class JspCheckboxTag extends JspInvokeGrailsTagLibTag {

    private static final String TAG_NAME = "checkBox";

    private String name;

    private String value;

    public JspCheckboxTag() {
        setTagName(TAG_NAME);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
