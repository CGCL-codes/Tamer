package org.itsnat.feashow.features.comp;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchor;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.SyncMode;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class FormTreeNode extends FeatureTreeNode implements EventListener {

    protected ItsNatHTMLForm formComp;

    protected ItsNatHTMLAnchor linkComp;

    public FormTreeNode() {
    }

    public void startExamplePanel() {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.formComp = (ItsNatHTMLForm) compMgr.createItsNatComponentById("formId");
        formComp.setEventListenerParams("submit", false, SyncMode.SYNC, null, null, -1);
        formComp.setEventListenerParams("reset", false, SyncMode.SYNC, null, null, -1);
        formComp.addEventListener("submit", this);
        formComp.addEventListener("reset", this);
        this.linkComp = (ItsNatHTMLAnchor) compMgr.createItsNatComponentById("linkId");
        linkComp.addEventListener("click", this);
    }

    public void endExamplePanel() {
        this.formComp.dispose();
        this.formComp = null;
        this.linkComp.dispose();
        this.linkComp = null;
    }

    public void handleEvent(Event evt) {
        log(evt.getCurrentTarget() + " " + evt.getType());
        EventTarget currentTarget = evt.getCurrentTarget();
        if (currentTarget == formComp.getHTMLFormElement()) {
            if (evt.getType().equals("submit")) {
                log("Submit canceled");
                evt.preventDefault();
            }
        } else {
            formComp.reset();
        }
    }
}
