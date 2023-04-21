package org.apache.naming;

import javax.naming.Context;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

/**
 * Represents a reference address to an EJB.
 *
 * @author Remy Maucherat
 * @version $Revision: 467222 $ $Date: 2006-10-24 05:17:11 +0200 (Tue, 24 Oct 2006) $
 */
public class EjbRef extends Reference {

    /**
     * Default factory for this reference.
     */
    public static final String DEFAULT_FACTORY = org.apache.naming.factory.Constants.DEFAULT_EJB_FACTORY;

    /**
     * EJB type address type.
     */
    public static final String TYPE = "type";

    /**
     * Remote interface classname address type.
     */
    public static final String REMOTE = "remote";

    /**
     * Link address type.
     */
    public static final String LINK = "link";

    /**
     * EJB Reference.
     * 
     * @param ejbType EJB type
     * @param home Home interface classname
     * @param remote Remote interface classname
     * @param link EJB link
     */
    public EjbRef(String ejbType, String home, String remote, String link) {
        this(ejbType, home, remote, link, null, null);
    }

    /**
     * EJB Reference.
     * 
     * @param ejbType EJB type
     * @param home Home interface classname
     * @param remote Remote interface classname
     * @param link EJB link
     */
    public EjbRef(String ejbType, String home, String remote, String link, String factory, String factoryLocation) {
        super(home, factory, factoryLocation);
        StringRefAddr refAddr = null;
        if (ejbType != null) {
            refAddr = new StringRefAddr(TYPE, ejbType);
            add(refAddr);
        }
        if (remote != null) {
            refAddr = new StringRefAddr(REMOTE, remote);
            add(refAddr);
        }
        if (link != null) {
            refAddr = new StringRefAddr(LINK, link);
            add(refAddr);
        }
    }

    /**
     * Retrieves the class name of the factory of the object to which this 
     * reference refers.
     */
    public String getFactoryClassName() {
        String factory = super.getFactoryClassName();
        if (factory != null) {
            return factory;
        } else {
            factory = System.getProperty(Context.OBJECT_FACTORIES);
            if (factory != null) {
                return null;
            } else {
                return DEFAULT_FACTORY;
            }
        }
    }
}
