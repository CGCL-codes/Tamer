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
public class FactoryItsNatFreeCheckBoxLabelImpl extends FactoryItsNatComponentImpl {

    /**
     * Creates a new instance of FactoryItsNatFreeCheckBoxDefaultImpl
     */
    public FactoryItsNatFreeCheckBoxLabelImpl() {
    }

    public ItsNatComponent createItsNatComponent(Element elem, String compType, NameValue[] artifacts, boolean ignoreIsComponentAttr, ItsNatComponentManagerImpl compMgr) {
        return compMgr.createItsNatFreeCheckBoxLabel(elem, artifacts);
    }

    public String getKey() {
        return "freeCheckBoxLabel";
    }
}
