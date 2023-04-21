package org.itsnat.impl.comp.list;

import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.impl.core.domutil.ElementListStructureDefaultImpl;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public class ItsNatListStructureDefaultImpl implements ItsNatListStructure {

    protected static final ItsNatListStructureDefaultImpl SINGLETON = new ItsNatListStructureDefaultImpl();

    /** Creates a new instance of ItsNatListStructureDefaultImpl */
    private ItsNatListStructureDefaultImpl() {
    }

    public static ItsNatListStructureDefaultImpl newItsNatListStructureDefault() {
        return SINGLETON;
    }

    public Element getContentElement(ItsNatList list, int index, Element parentElem) {
        if (parentElem == null) parentElem = list.getItsNatListUI().getElementAt(index);
        return ElementListStructureDefaultImpl.getContentElement(index, parentElem);
    }
}
