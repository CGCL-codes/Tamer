package org.itsnat.impl.core.domutil;

import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.domutil.ElementGroup;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.impl.core.doc.ItsNatDocumentImpl;
import org.itsnat.impl.core.ItsNatUserDataImpl;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 *
 * @author jmarranz
 */
public abstract class ElementGroupImpl extends ItsNatUserDataImpl implements ElementGroup {

    protected ItsNatDocumentImpl itsNatDoc;

    /**
     * Creates a new instance of ElementGroupImpl
     */
    public ElementGroupImpl(ItsNatDocumentImpl itsNatDoc) {
        super(false);
        this.itsNatDoc = itsNatDoc;
    }

    public ItsNatDocument getItsNatDocument() {
        return itsNatDoc;
    }

    public ItsNatDocumentImpl getItsNatDocumentImpl() {
        return itsNatDoc;
    }

    public static void restorePatternMarkupWhenRendering(Element parent, DocumentFragment pattern) {
        if (pattern == null) return;
        ItsNatDOMUtil.removeAllChildren(parent);
        parent.appendChild(pattern.cloneNode(true));
    }
}
