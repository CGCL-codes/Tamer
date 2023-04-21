package net.sf.jwan.servlet.gui.form;

public class AbstractWanFormInput {

    protected String cssClass;

    public AbstractWanFormInput() {
        cssClass = null;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
}
