package org.itsnat.impl.comp.list;

import org.itsnat.comp.list.ItsNatComboBox;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.list.ItsNatComboBoxUI;
import org.itsnat.impl.comp.list.ItsNatHTMLSelectComboBoxImpl;
import org.itsnat.impl.core.domutil.ItsNatDOMUtilInternal;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLSelectComboBoxUIImpl extends ItsNatHTMLSelectUIImpl implements ItsNatComboBoxUI {

    /**
     * Creates a new instance of ItsNatHTMLSelectComboBoxUIImpl
     */
    public ItsNatHTMLSelectComboBoxUIImpl(ItsNatHTMLSelectComboBoxImpl parentComp) {
        super(parentComp);
    }

    public ItsNatHTMLSelectComboBox getItsNatHTMLSelectComboBox() {
        return (ItsNatHTMLSelectComboBox) parentComp;
    }

    public ItsNatHTMLSelectComboBoxImpl getItsNatHTMLSelectComboBoxImpl() {
        return (ItsNatHTMLSelectComboBoxImpl) parentComp;
    }

    public void setSelectedIndex(int index) {
        int len = getLength();
        for (int i = 0; i < len; i++) {
            HTMLOptionElement option = getHTMLOptionElementAt(i);
            boolean oldState = option.getSelected();
            boolean newState = (index == i);
            if (oldState != newState) {
                ItsNatDOMUtilInternal.setBooleanAttribute(option, "selected", newState);
            }
        }
    }

    public ItsNatComboBox getItsNatComboBox() {
        return (ItsNatComboBox) parentComp;
    }
}
