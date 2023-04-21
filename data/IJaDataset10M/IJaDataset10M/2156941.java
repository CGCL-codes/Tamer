package org.itsnat.impl.comp.text;

import org.itsnat.impl.comp.ItsNatHTMLElementComponentUIImpl;
import org.itsnat.comp.text.ItsNatTextComponent;
import org.itsnat.comp.ItsNatHTMLFormComponent;
import org.itsnat.comp.text.ItsNatTextComponentUI;
import org.itsnat.impl.comp.ItsNatHTMLFormCompValueBasedImpl;
import org.itsnat.impl.comp.ItsNatHTMLFormComponentImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ItsNatHTMLTextBasedUIImpl extends ItsNatHTMLElementComponentUIImpl implements ItsNatTextComponentUI {

    /**
     * Creates a new instance of ItsNatHTMLTextBasedUIImpl
     */
    public ItsNatHTMLTextBasedUIImpl(ItsNatHTMLFormCompValueBasedImpl parentComp) {
        super(parentComp);
        setEditable(true);
    }

    public ItsNatHTMLFormComponent getItsNatHTMLFormComponent() {
        return (ItsNatHTMLFormComponent) parentComp;
    }

    public ItsNatHTMLFormComponentImpl getItsNatHTMLFormComponentImpl() {
        return (ItsNatHTMLFormComponentImpl) parentComp;
    }

    public ItsNatTextComponent getItsNatTextComponent() {
        return (ItsNatTextComponent) parentComp;
    }

    public String getText() {
        return getDOMValueProperty();
    }

    public void setText(String str) {
        setDOMValueProperty(str);
    }

    public void insertString(int where, String str) {
        String text = getText();
        text = text.substring(0, where) + str + text.substring(where);
        setText(text);
    }

    public void removeString(int where, int len) {
        String text = getText();
        text = text.substring(0, where) + text.substring(where + len);
        setText(text);
    }

    protected abstract String getDOMValueProperty();

    protected abstract void setDOMValueProperty(String str);
}
