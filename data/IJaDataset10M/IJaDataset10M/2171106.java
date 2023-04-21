package org.jdbf.engine.mapping;

/**
 * <code>GeneratorMap </code> represents a mapping of 
 * key generator
 *
 * @author Giovanni Martone
 * @version $Revision
 * last changed by $Author: gmartone $
 */
public class GeneratorMap {

    /** name of class */
    protected String className;

    /** type of keyGenerator */
    protected String type;

    /** 
     * Creates the GeneratorMap object.	 	 	
     */
    public GeneratorMap() {
        className = getClass().getName();
    }

    /** 
     * Creates the GeneratorMap object.
     *
     * @param type type of generator.
     */
    public GeneratorMap(String type) {
        className = getClass().getName();
        this.type = type;
    }

    /**
     * Return type of generator
     * @return String type of generator
     */
    public String getType() {
        return type;
    }

    /**
     * Set type of generator
     * @param type of generator
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Return the object as String
     * @return String
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append(className).append("[").append("\n").append("type generator ").append(type).append("\n");
        return buff.toString();
    }
}
