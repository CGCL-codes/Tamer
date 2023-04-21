package org.objectstyle.cayenne.dataview.dvmodeler;

import org.jdom.*;

/**
 *
 * @author Nataliya Kholodna
 * @version 1.0
 */
public class EditFormat {

    private String className = "";

    private String pattern = "";

    public EditFormat(Element element) {
        className = element.getAttributeValue("class");
        pattern = element.getChild("pattern").getText();
    }

    public EditFormat() {
    }

    public boolean isEmpty() {
        if (className.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setPattern(String Pattern) {
        this.pattern = Pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public Element getEditFormatElement() {
        Element e = new Element("edit-format");
        e.setAttribute(new Attribute("class", className));
        Element ePattern = new Element("pattern");
        ePattern.addContent(pattern);
        e.addContent(ePattern);
        return e;
    }
}
