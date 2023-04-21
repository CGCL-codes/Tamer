package org.xmlcml.cmlimpl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.CMLElectron;
import org.xmlcml.cml.CMLMolecule;

public class ElectronImpl extends CMLBaseImpl implements org.xmlcml.cml.CMLElectron {

    protected double count;

    protected String getClassTagName() {
        return ELEMENT_NAMES[ELECTRON];
    }

    public ElectronImpl() {
        super();
    }

    public ElectronImpl(Element element) {
        super(element);
    }

    /** new CMLElectron in context */
    public ElectronImpl(Document document) {
        super(ELEMENT_NAMES[ELECTRON], document);
    }

    /** copy constructor */
    public ElectronImpl(CMLElectron electron, CMLMolecule newMol) {
        this(newMol.getOwnerDocument());
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getCount() {
        return count;
    }

    public boolean updateDOMHasCount() {
        return CMLBaseImpl.updateDOMHasCount(this);
    }

    public boolean updateDOM() throws CMLException {
        if (!domNeedsUpdating) return true;
        if (!super.updateDOM()) return false;
        if (!updateDOMHasCount()) return false;
        domNeedsUpdating = false;
        return true;
    }

    public boolean processDOMHasCount() {
        return CMLBaseImpl.processDOMHasCount(this);
    }

    public boolean processDOM() throws CMLException {
        if (!domNeedsProcessing) return true;
        if (!super.processDOM()) return false;
        if (!processDOMHasCount()) return false;
        return true;
    }

    private static boolean trace = false;

    public void setTrace(boolean t) {
        trace = t;
    }

    public boolean isTrace() {
        return trace;
    }
}
