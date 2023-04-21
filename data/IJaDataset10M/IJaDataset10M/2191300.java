package org.objectstyle.cayenne.dataview.dvmodeler;

import org.jdom.*;

/**
 *
 * @author Nataliya Kholodna
 * @since 1.1
 */
public class DisplayFormat {

    private String className = "";

    private String pattern = "";

    public DisplayFormat(Element element) {
        className = element.getAttributeValue("class");
        pattern = element.getChild("pattern").getText();
    }

    public DisplayFormat() {
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

    public Element getDisplayFormatElement() {
        Element e = new Element("display-format");
        e.setAttribute(new Attribute("class", className));
        Element ePattern = new Element("pattern");
        ePattern.addContent(pattern);
        e.addContent(ePattern);
        return e;
    }
}
