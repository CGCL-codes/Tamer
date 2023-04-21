package org.opencms.util;

import java.io.Serializable;

/**
 * Base class for all string mode enumeration classes.<p>
 *
 * Like:<br>
 * <ul>
 *   <li>{@link org.opencms.db.CmsUserSettings.CmsSearchResultStyle}
 * </ul>
 *
 * @author Michael Moossen 
 * 
 * @version $Revision: 1.4 $
 * 
 * @since 6.5.5 
 */
public abstract class A_CmsModeStringEnumeration implements Serializable {

    /** The internal mode descriptor. */
    private final String m_mode;

    /**
     * Default constructor.<p>
     * 
     * @param mode the internal mode descriptor
     */
    protected A_CmsModeStringEnumeration(String mode) {
        m_mode = mode;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof A_CmsModeStringEnumeration) {
            if (obj.getClass().equals(this.getClass())) {
                A_CmsModeStringEnumeration eObj = (A_CmsModeStringEnumeration) obj;
                return eObj.getMode().equals(m_mode);
            }
        }
        return false;
    }

    /**
     * Returns the mode.<p>
     *
     * @return the mode
     */
    public String getMode() {
        return m_mode;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return m_mode.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return m_mode;
    }
}
