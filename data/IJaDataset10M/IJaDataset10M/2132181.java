package org.itsnat.feashow.features.comp.buttons.normal;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.SyncMode;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class InputSubmitFormTreeNode extends FeatureTreeNode implements EventListener {

    protected ItsNatHTMLInputSubmit inputComp;

    public InputSubmitFormTreeNode() {
    }

    public void startExamplePanel() {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.inputComp = (ItsNatHTMLInputSubmit) compMgr.createItsNatComponentById("inputId");
        inputComp.setEventListenerParams("click", false, SyncMode.SYNC, null, null, -1);
        inputComp.addEventListener("click", this);
    }

    public void endExamplePanel() {
        this.inputComp.dispose();
        this.inputComp = null;
    }

    public void handleEvent(Event evt) {
        log(evt.getCurrentTarget() + " " + evt.getType());
        evt.preventDefault();
        log("Canceled");
    }
}
