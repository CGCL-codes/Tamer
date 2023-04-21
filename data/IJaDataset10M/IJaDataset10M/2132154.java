package org.apache.jetspeed.om.registry;

/**
 * Interface describing a parameter for a registry entry. 
 *
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @version $Id: Parameter.java,v 1.2 2004/02/23 03:11:39 jford Exp $
 */
public interface Parameter extends RegistryEntry {

    /** @return the value for this parameter */
    public String getValue();

    /** Sets the value of this parameter.
     * 
     * @param value the new parameter value
     */
    public void setValue(String value);

    /** @return the parameter's type */
    public String getType();

    /** Sets the type of this parameter.value.
     * 
     * @param type the new parameter type
     */
    public void setType(String type);
}
