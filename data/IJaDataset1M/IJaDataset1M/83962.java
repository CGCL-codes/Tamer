package net.sf.xanno.poc.representatives.web;

import org.jdom.Element;
import net.sf.xanno.poc.representatives.AbstractXmlElementRepresentative;

public class ServletRep extends AbstractXmlElementRepresentative {

    private String description;

    private String displayName;

    private String name;

    private String clazz;

    public ServletRep() {
        super("servlet");
    }

    @Override
    public Element renderElement() {
        if (description != null && !description.equals("")) {
            element.addContent(new Element("description").setText(description));
        }
        if (displayName != null && !displayName.equals("")) {
            element.addContent(new Element("display-name").setText(displayName));
        }
        element.addContent(new Element("servlet-name").setText(name));
        element.addContent(new Element("servlet-class").setText(clazz));
        return element;
    }

    public String getServletClass() {
        return clazz;
    }

    public void setServletClass(String servletClass) {
        clazz = servletClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
