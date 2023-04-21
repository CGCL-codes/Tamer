package com.genia.toolbox.web.gwt.form.client.form.impl;

import com.genia.toolbox.web.gwt.basics.client.support.GwtObjectUtils;
import com.genia.toolbox.web.gwt.form.client.form.SubFormItem;
import com.genia.toolbox.web.gwt.form.client.form.SubFormPolicy;

/**
 * Base implementation of {@link SubFormItem}.
 */
public abstract class AbstractSubFormItemImpl extends AbstractSimpleItem implements SubFormItem {

    /**
   * wether this oracle display its suggestions as html strings.
   */
    private boolean displayStringHTML = false;

    /**
   * whether to use a popup window to display the sub form.
   */
    private boolean popup = false;

    /**
   * the form identifier of this sub-form.
   */
    private String subFormIdentifier;

    /**
   * the policy associated with this sub-form.
   */
    private SubFormPolicy subFormPolicy = new SubFormPolicyImpl();

    /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param object
   *          the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the object
   *         argument; <code>false</code> otherwise.
   * @see java.lang.Object#equals(java.lang.Object)
   */
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        if (object instanceof AbstractSubFormItemImpl) {
            SubFormItem otherItem = (SubFormItem) object;
            if (!GwtObjectUtils.nullSafeEquals(getSubFormIdentifier(), otherItem.getSubFormIdentifier())) {
                return false;
            }
            return GwtObjectUtils.nullSafeEquals(getSubFormPolicy(), otherItem.getSubFormPolicy());
        }
        return false;
    }

    /**
   * getter for the subFormIdentifier property.
   * 
   * @return the subFormIdentifier
   */
    public String getSubFormIdentifier() {
        return subFormIdentifier;
    }

    /**
   * getter for the subFormPolicy property.
   * 
   * @return the subFormPolicy
   */
    public SubFormPolicy getSubFormPolicy() {
        return subFormPolicy;
    }

    /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * @see java.lang.Object#hashCode()
   */
    @Override
    public int hashCode() {
        return GwtObjectUtils.nullSafeHashCode(new Object[] { new Integer(super.hashCode()), getSubFormIdentifier(), getSubFormPolicy() });
    }

    /**
   * getter for the displayStringHTML property.
   * 
   * @return the displayStringHTML
   */
    public boolean isDisplayStringHTML() {
        return displayStringHTML;
    }

    /**
   * getter for the popup property.
   * 
   * @return the popup
   */
    public boolean isPopup() {
        return popup;
    }

    /**
   * setter for the displayStringHTML property.
   * 
   * @param displayStringHTML
   *          the displayStringHTML to set
   */
    public void setDisplayStringHTML(boolean displayStringHTML) {
        this.displayStringHTML = displayStringHTML;
    }

    /**
   * setter for the popup property.
   * 
   * @param popup
   *          the popup to set
   */
    public void setPopup(boolean popup) {
        this.popup = popup;
    }

    /**
   * setter for the subFormIdentifier property.
   * 
   * @param subFormIdentifier
   *          the subFormIdentifier to set
   */
    public void setSubFormIdentifier(final String subFormIdentifier) {
        this.subFormIdentifier = subFormIdentifier;
    }

    /**
   * setter for the subFormPolicy property.
   * 
   * @param subFormPolicy
   *          the subFormPolicy to set
   */
    public void setSubFormPolicy(final SubFormPolicy subFormPolicy) {
        this.subFormPolicy = subFormPolicy;
    }
}
