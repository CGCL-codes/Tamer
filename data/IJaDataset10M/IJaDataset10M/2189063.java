package dk.pervasive.jcaf;

import java.io.Serializable;

/**
 * This interface is used to serialize an object to XML.
 * 
 * @author Jakob E. Bardram
 *  
 */
public interface XMLSerializable extends Serializable {

    /**
     * Serializes this object to XML.
     * 
     * @return - a string of valid XML.
     */
    public abstract String toXML();
}
