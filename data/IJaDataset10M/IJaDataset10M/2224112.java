package org.itsnat.feashow.features.comp.autobuild.mdforms;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

public class MarkupDrivenInputTextTreeNode extends FeatureTreeNode implements EventListener {

    protected Element parentElem;

    protected HTMLInputElement inputElem;

    protected Element changeElem;

    public MarkupDrivenInputTextTreeNode() {
    }

    public void startExamplePanel() {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        this.parentElem = doc.getElementById("compGroupId");
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.setMarkupDrivenComponents(true);
        compMgr.buildItsNatComponents(parentElem);
        this.inputElem = (HTMLInputElement) doc.getElementById("inputId");
        itsNatDoc.addEventListener((EventTarget) inputElem, "change", this, false);
        this.changeElem = doc.getElementById("changeFromServerId");
        itsNatDoc.addEventListener((EventTarget) changeElem, "click", this, false);
    }

    public void endExamplePanel() {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        itsNatDoc.removeEventListener((EventTarget) inputElem, "click", this, false);
        this.inputElem = null;
        itsNatDoc.removeEventListener((EventTarget) changeElem, "click", this, false);
        this.changeElem = null;
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.removeItsNatComponents(parentElem, true);
        compMgr.setMarkupDrivenComponents(false);
        this.parentElem = null;
    }

    public void handleEvent(Event evt) {
        EventTarget currTarget = evt.getCurrentTarget();
        if (currTarget == changeElem) inputElem.setValue("Text in server");
        log("Current Value: " + inputElem.getValue());
    }
}
