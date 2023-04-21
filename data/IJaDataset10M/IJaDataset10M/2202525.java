package org.itsnat.impl.comp.text;

import org.itsnat.impl.comp.ItsNatHTMLComponentManagerImpl;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.NameValue;
import org.w3c.dom.html.HTMLInputElement;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLInputTextDefaultImpl extends ItsNatHTMLInputTextImpl implements ItsNatHTMLInputText {

    /** Creates a new instance of ItsNatHTMLInputTextDefaultImpl */
    public ItsNatHTMLInputTextDefaultImpl(HTMLInputElement element, NameValue[] artifacts, ItsNatHTMLComponentManagerImpl componentMgr) {
        super(element, artifacts, componentMgr);
        init();
    }
}
