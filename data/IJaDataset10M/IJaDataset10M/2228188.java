package org.itsnat.impl.comp.factory.text;

import org.itsnat.impl.comp.ItsNatHTMLComponentManagerImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatHTMLInputImpl;
import org.itsnat.comp.ItsNatHTMLElementComponent;
import org.itsnat.core.NameValue;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatHTMLInputTextImpl extends FactoryItsNatHTMLInputImpl {

    /** Creates a new instance of FactoryItsNatHTMLInputTextImpl */
    public FactoryItsNatHTMLInputTextImpl() {
    }

    public ItsNatHTMLElementComponent createItsNatHTMLComponent(HTMLElement element, String compType, NameValue[] artifacts, ItsNatHTMLComponentManagerImpl compMgr) {
        if (compType == null) return compMgr.createItsNatHTMLInputText((HTMLInputElement) element, artifacts); else if (compType.equals("formattedTextField")) return compMgr.createItsNatHTMLInputTextFormatted((HTMLInputElement) element, artifacts); else return null;
    }

    public String getTypeAttr() {
        return "text";
    }
}
