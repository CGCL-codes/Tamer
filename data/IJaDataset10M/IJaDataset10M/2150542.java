package com.loribel.commons.gui.bo.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.loribel.commons.business.abstraction.GB_SimpleBOValue;
import com.loribel.commons.swing.abstraction.GB_ITextComponentNumber;
import com.loribel.commons.util.GB_NumberTools;
import com.loribel.commons.util.Log;

/**
 * Adapter to manage events between a TextComponentNumber and a {@link
 * GB_BusinessObjectValue} of a BusinessObject.
 * <br />
 * Events are managed in the both directions.<br />
 *   - When user edit TextField, value of BusinessObject are updated.
 *   - When the value of BusinessObject changed, TextField is updated.
 *
 * @author Gregory Borelli
 * @version 2003/11/21 - 16:43:44 - gen 7.12
 */
class GB_BOPropertyAdapterTextComponentNumber extends GB_BOPropertyAdapterAbstract implements DocumentListener, FocusListener {

    /**
     * Component source of the events.
     */
    protected GB_ITextComponentNumber textComponentNumber;

    /**
     * The value before focus Gained.
     */
    protected Number oldValue;

    /**
     * Constructor of GB_BOPropertyAdapterTextComponentNumber with parameter(s).
     *
     * @param a_textComponentNumber GB_ITextComponentNumber -
     * @param a_businessObjectValue GB_SimpleBusinessObjectValue -
     */
    public GB_BOPropertyAdapterTextComponentNumber(GB_ITextComponentNumber a_textComponentNumber, GB_SimpleBOValue a_businessObjectValue) {
        super(a_businessObjectValue);
        textComponentNumber = a_textComponentNumber;
        init();
    }

    /**
     * Method init.
     * <br />
     */
    private void init() {
        JTextField l_textComp = (JTextField) textComponentNumber;
        l_textComp.getDocument().addDocumentListener(this);
        l_textComp.addFocusListener(this);
    }

    /**
     * Returns the value from the component.
     *
     * @return Object
     */
    protected Object getValueFromComponent() {
        Number retour = textComponentNumber.getNumberValue();
        Class l_numberType = businessObjectValue.getType();
        try {
            retour = GB_NumberTools.toNumber(l_numberType, retour);
        } catch (NumberFormatException e) {
            Log.logWarning(this, e);
            return businessObjectValue.getValue();
        }
        return retour;
    }

    /**
     * Met a jour la businessObjectValue par rapport � la valeur du composant
     * TextField associ�. <br />
     * Cette m�thode est appel�e suite � un �v�nement Swing.
     */
    protected void updateFromBOValue2() {
        Number l_value = (Number) businessObjectValue.getValue();
        textComponentNumber.setNumberValue(l_value);
    }

    /**
     * Teste si le composant et la valeur du BOValue sont identiques.
     *
     * @return boolean
     */
    protected boolean checkValues() {
        Number l_value = (Number) businessObjectValue.getValue();
        Number l_value2 = textComponentNumber.getNumberValue();
        return GB_NumberTools.equalsValues(l_value, l_value2);
    }

    /**
     * Invoked when a component gains the keyboard focus.
     *
     * @param e FocusEvent -
     */
    public void focusGained(FocusEvent e) {
        oldValue = textComponentNumber.getNumberValue();
    }

    /**
     * Invoked when a component loses the keyboard focus.
     *
     * @param e FocusEvent -
     */
    public void focusLost(FocusEvent e) {
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     * Method from DocumentListener.
     *
     * @param e DocumentEvent -
     */
    public void changedUpdate(DocumentEvent e) {
        this.updateFromComponent();
    }

    /**
     * Gives notification that there was an insert into the document.
     * Method from DocumentListener.
     *
     * @param e DocumentEvent -
     */
    public void insertUpdate(DocumentEvent e) {
        this.updateFromComponent();
    }

    /**
     * Gives notification that a portion of the document has been removed.
     * M�thode issue de l'interface DocumentListener.
     *
     * @param e DocumentEvent -
     */
    public void removeUpdate(DocumentEvent e) {
        this.updateFromComponent();
    }
}
