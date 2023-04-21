package org.scribble.conversation.model;

import org.scribble.model.*;

/**
 * This interface represents the identity locators associated
 * with a particular type.
 */
public class IdentityLocator extends ModelObject {

    private static final long serialVersionUID = 5279517129760939317L;

    /**
	 * The default constructor for the identity locator.
	 */
    public IdentityLocator() {
    }

    /**
	 * This method returns the type associated with the
	 * identity locator.
	 * 
	 * @return The type
	 */
    public TypeReference getType() {
        return (m_type);
    }

    /**
	 * This method sets the type associated with the
	 * identity locator.
	 * 
	 * @param type The type
	 */
    public void setType(TypeReference type) {
        m_type = type;
    }

    /** 
	 * This method sets the locator associated with the identity
	 * token.
	 * 
	 * @param identity The identity
	 * @param locator The locator
	 */
    public void setLocator(String identity, String locator) {
        m_identities.add(identity);
        m_locators.add(locator);
    }

    /**
	 * This method returns the number of identity locators.
	 * 
	 * @return The number of identity locators
	 */
    public int getNumberOfLocators() {
        return (m_identities.size());
    }

    /**
	 * This method returns the identity at the supplied
	 * index.
	 * 
	 * @param index The index
	 * @return The identity at the index, or null if out of
	 * 						range
	 */
    public String getIdentity(int index) {
        String ret = null;
        if (index < m_identities.size()) {
            ret = m_identities.get(index);
        }
        return (ret);
    }

    /**
	 * This method returns the locator at the supplied
	 * index.
	 * 
	 * @param index The index
	 * @return The locator at the index, or null if out of
	 * 						range
	 */
    public String getLocator(int index) {
        String ret = null;
        if (index < m_locators.size()) {
            ret = m_locators.get(index);
        }
        return (ret);
    }

    /**
	 * This method returns the locator associated with
	 * the supplied identity name.
	 * 
	 * @param identity The identiy name
	 * @return The locator for the identity, or null if not
	 * 						found
	 */
    public String getLocator(String identity) {
        String ret = null;
        int index = m_identities.indexOf(identity);
        if (index != -1) {
            ret = m_locators.get(index);
        }
        return (ret);
    }

    private TypeReference m_type = null;

    private java.util.List<String> m_identities = new java.util.Vector<String>();

    private java.util.List<String> m_locators = new java.util.Vector<String>();
}
