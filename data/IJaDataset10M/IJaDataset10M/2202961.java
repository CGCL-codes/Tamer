package org.dom4j.dtd;

/**
 * <p>
 * <code>AttributeDecl</code> represents an element declaration in a DTD.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.6 $
 */
public class ElementDecl {

    /** Holds value of property name. */
    private String name;

    /** Holds value of property model. */
    private String model;

    public ElementDecl() {
    }

    public ElementDecl(String name, String model) {
        this.name = name;
        this.model = model;
    }

    /**
     * Getter for property name.
     * 
     * @return Value of property name.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property name.
     * 
     * @param name
     *            New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property model.
     * 
     * @return Value of property model.
     */
    public String getModel() {
        return model;
    }

    /**
     * Setter for property model.
     * 
     * @param model
     *            New value of property model.
     */
    public void setModel(String model) {
        this.model = model;
    }

    public String toString() {
        return "<!ELEMENT " + name + " " + model + ">";
    }
}
