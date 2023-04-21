package org.itsnat.feashow.features.comp.iemob;

import org.itsnat.comp.list.ItsNatList;
import org.itsnat.comp.list.ItsNatListStructure;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableRowElement;

public class IEMobileFreeListStructure implements ItsNatListStructure {

    public IEMobileFreeListStructure() {
    }

    public Element getContentElement(ItsNatList list, int index, Element parentElem) {
        HTMLTableRowElement rowElem = (HTMLTableRowElement) parentElem;
        HTMLTableCellElement cellElem = (HTMLTableCellElement) ItsNatTreeWalker.getFirstChildElement(rowElem);
        HTMLAnchorElement link = (HTMLAnchorElement) ItsNatTreeWalker.getFirstChildElement(cellElem);
        return link;
    }
}
