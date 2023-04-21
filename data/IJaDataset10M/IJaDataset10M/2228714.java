package org.itsnat.impl.comp.factory.button.toggle;

import org.itsnat.comp.ItsNatComponent;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.factory.FactoryItsNatComponentImpl;
import org.itsnat.impl.comp.ItsNatComponentManagerImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class FactoryItsNatFreeRadioButtonLabelImpl extends FactoryItsNatComponentImpl {

    /**
     * Creates a new instance of FactoryItsNatFreeRadioButtonImpl
     */
    public FactoryItsNatFreeRadioButtonLabelImpl() {
    }

    public ItsNatComponent createItsNatComponent(Element elem, String compType, NameValue[] artifacts, boolean ignoreIsComponentAttr, ItsNatComponentManagerImpl compMgr) {
        return compMgr.createItsNatFreeRadioButtonLabel(elem, artifacts);
    }

    public String getKey() {
        return "freeRadioButtonLabel";
    }
}
