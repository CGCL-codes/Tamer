package org.commerce.mismo.bean;

import org.commerce.mismo.ContactPoint;
import org.commerce.mismo.ContactPointRoleType;
import org.commerce.mismo.ContactPointType;

/**
 * Simple JavaBean implementation of the MISMO 2.1 <code>ContactPoint</code>
 * interface.
 * 
 * @version $Id: ContactPointBean.java,v 1.1.1.1 2007/04/16 05:07:03 clafonta Exp $
 */
public class ContactPointBean extends BaseBean implements ContactPoint {

    /**
	 * hibernate identifier field
	 */
    private Long contactPointId;

    /**
     * @see #getRoleType()
     */
    private ContactPointRoleType role;

    /**
     * @see #getType()
     */
    private ContactPointType type;

    /**
     * @see #getTypeOtherDescription()
     */
    private String typeOtherDescription;

    /**
     * @see #getValue()
     */
    private String value;

    /**
     * @see #getPreferenceIndicator()
     */
    private Boolean preferenceIndicator;

    /**
     * @hibernate.id generator-class="increment" type="long"   column="CONTACT_POINT_ID"
     * @return Long
     */
    public Long getContactPointId() {
        return contactPointId;
    }

    /**
     * @param contactPointId The contactPointId to set.
     */
    public void setContactPointId(Long contactPointId) {
        this.contactPointId = contactPointId;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#getRoleType()
     */
    public ContactPointRoleType getRoleType() {
        return role;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#setRoleType(ContactPointRoleType)
     */
    public void setRoleType(ContactPointRoleType type) {
        this.role = type;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#getType()
     */
    public ContactPointType getType() {
        return type;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#setType(ContactPointType)
     */
    public void setType(ContactPointType type) {
        this.type = type;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#getTypeOtherDescription()
     */
    public String getTypeOtherDescription() {
        return typeOtherDescription;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#setTypeOtherDescription(String)
     */
    public void setTypeOtherDescription(String desc) {
        this.typeOtherDescription = desc;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#getValue()
     */
    public String getValue() {
        return value;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#setValue(String)
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#getPreferenceIndicator()
     */
    public Boolean getPreferenceIndicator() {
        return preferenceIndicator;
    }

    /**
     * @see org.commerce.mismo.ContactPoint#setPreferenceIndicator(Boolean)
     */
    public void setPreferenceIndicator(Boolean b) {
        this.preferenceIndicator = b;
    }
}
