package de.keepondreaming.xml;

/**
 *	Helper interface of {@link de.keepondreaming.xml.ClassProxy} to 
 *	set/read attributes.
 * 
 * $Author: wintermond $
 * $Date: 2005/07/10 18:39:01 $
 * $Log: ClassProxyAccess.java,v $
 * Revision 1.5  2005/07/10 18:39:01  wintermond
 * javadoc
 *
 * Revision 1.4  2005/07/09 09:54:39  wintermond
 * Javadoc and implemented ProxyClassCreator
 *
 * Revision 1.3  2005/07/09 08:29:13  wintermond
 * new cvs substitution tag
 *
 */
public interface ClassProxyAccess extends ProxyClassCreator {

    /**
     * Sets the attribute with the given key
     * 
     * @param key
     * @param value
     */
    public void setAttribute(String key, Object value);

    /**
     * Returns the attribute value of the given key.
     * 
     * @param key
     * 
     * @return The value currently assigned to the key <code>key</code>
     */
    public Object getAttribute(String key);
}
