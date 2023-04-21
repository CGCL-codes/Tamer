package com.loribel.commons.gui.bo.swing;

import javax.swing.text.Document;
import com.loribel.commons.abstraction.GB_Unregisterable;
import com.loribel.commons.business.abstraction.GB_BOProperty;
import com.loribel.commons.business.abstraction.GB_BusinessObject;
import com.loribel.commons.business.abstraction.GB_SimpleBOValue;
import com.loribel.commons.swing.GB_PasswordField;

/**
 * PasswordField with Business Object Property.
 *
 * @author Gregory Borelli
 * @version 2003/11/21 - 16:43:42 - gen 7.12
 */
public class GB_BOPropertyPasswordField extends GB_PasswordField implements GB_Unregisterable {

    /**
     * Business object property associated to this component.
     */
    protected GB_BOProperty bOProperty;

    /**
     * Business object value associated to this component.
     * Value and component are linked and auto update each other.
     */
    protected GB_SimpleBOValue bOValue;

    /**
     * The adapter to register unregister property event to update component..
     */
    protected GB_BOPropertyAdapterAbstract boPropertyAdapter;

    /**
     * Constructor with Business object property.
     *
     * @param a_bOProperty GB_BOProperty -
     */
    public GB_BOPropertyPasswordField(GB_BOProperty a_bOProperty) {
        this(a_bOProperty, null);
    }

    /**
     * Constructor with Business object property and Business object value.
     *
     * @param a_bOProperty GB_BOProperty -
     * @param a_bOValue GB_SimpleBusinessObjectValue -
     */
    public GB_BOPropertyPasswordField(GB_BOProperty a_bOProperty, GB_SimpleBOValue a_bOValue) {
        super();
        setBOProperty(a_bOProperty);
        setDocument(buildMyModel());
        setBOValue(a_bOValue);
    }

    /**
     * Constructor of GB_BOPropertyPasswordField with parameter(s).
     *
     * @param a_businessObject GB_BusinessObject -
     * @param a_propertyName String -
     */
    public GB_BOPropertyPasswordField(GB_BusinessObject a_businessObject, String a_propertyName) {
        this(a_businessObject.getProperty(a_propertyName), a_businessObject.getBOValue(a_propertyName));
    }

    /**
     * M�thode getBOProperty.
     * <p>
     *
     * @return GB_BOProperty - <tt>bOProperty</tt>
     */
    public GB_BOProperty getBOProperty() {
        return bOProperty;
    }

    /**
     * M�thode setBOProperty.
     * <p>
     *
     * @param a_bOProperty GB_BOProperty - <tt>bOProperty</tt>
     */
    public void setBOProperty(GB_BOProperty a_bOProperty) {
        bOProperty = a_bOProperty;
    }

    /**
     * M�thode getBOValue.
     * <p>
     *
     * @return GB_SimpleBusinessObjectValue - <tt>bOValue</tt>
     */
    public GB_SimpleBOValue getBOValue() {
        return bOValue;
    }

    /**
     * M�thode setBOValue.
     * <p>
     *
     * @param a_bOValue GB_SimpleBusinessObjectValue - <tt>bOValue</tt>
     */
    public void setBOValue(GB_SimpleBOValue a_bOValue) {
        bOValue = a_bOValue;
        if (a_bOValue == null) {
            return;
        }
        Object l_value = a_bOValue.getValue();
        this.setTextFromObject(l_value);
        boPropertyAdapter = new GB_BOPropertyAdapterTextComponent(this, a_bOValue);
    }

    /**
     * Unregister property event from BOValue.
     *
     * @return boolean
     */
    public boolean unregister() {
        if (boPropertyAdapter != null) {
            boPropertyAdapter.unregister();
        }
        return true;
    }

    /**
     * Return the model associated to this component.
     *
     * @return Document - The document used to edit.
     */
    protected Document buildMyModel() {
        return new GB_BOPropertyStringDocument(bOProperty);
    }
}
