package net.sf.escripts.elements;

import net.sf.escripts.EscriptsElement;

/**
* The class {@link PropertyElement} implements a <code>&lt;property&gt;</code> element, which can be
* nested inside an <code>&lt;ant&gt;</code> element to pass a property to an Ant build file.
* {@link PropertyElement}s will be evaluated by the enclosing {@link AntElement} and are not
* executable by themselves.
*
* @author mirko
* @version $Revision: 12 $
**/
public class PropertyElement extends EscriptsElement {

    private String name;

    private String value;

    /**
    * Creates a new {@link PropertyElement}.
    *
    * @author mirko
    **/
    public PropertyElement() {
        super();
    }

    /**
    * Sets the <code>name</code> attribute.
    *
    * @param name the property name
    *
    * @author mirko
    **/
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Sets the <code>value</code> attribute.
    *
    * @param value the property value
    *
    * @author mirko
    **/
    public void setValue(String value) {
        this.value = value;
    }

    /**
    * Gets the <code>name</code> attribute.
    *
    * @return the property name
    *
    * @author mirko
    **/
    public String getName() {
        return name;
    }

    /**
    * Gets the <code>value</code> attribute.
    *
    * @return the property value
    *
    * @author mirko
    **/
    public String getValue() {
        return value;
    }
}
