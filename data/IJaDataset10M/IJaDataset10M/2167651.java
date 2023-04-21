package org.simplexj.ext;

/**
 * @author tedze@users.sourceforge.net 
 *
 * Attribute interface
 */
public class RssAttribute implements Attribute {

    private String name;

    private Class type;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the type.
     */
    public Class getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(Class type) {
        this.type = type;
    }
}
