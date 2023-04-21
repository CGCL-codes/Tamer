package org.itsnat.feashow.features.comp.buttons.normal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public class MouseOverOutTreeNode extends FeatureTreeNode implements EventListener, ActionListener, ChangeListener {

    protected ItsNatHTMLInputButton inputComp;

    public MouseOverOutTreeNode() {
    }

    public void startExamplePanel() {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.inputComp = (ItsNatHTMLInputButton) compMgr.createItsNatComponentById("inputId");
        inputComp.addEventListener("mouseover", this);
        inputComp.addEventListener("mouseout", this);
        ButtonModel dataModel = inputComp.getButtonModel();
        dataModel.addActionListener(this);
        dataModel.addChangeListener(this);
    }

    public void endExamplePanel() {
        this.inputComp.dispose();
        this.inputComp = null;
    }

    public void handleEvent(Event evt) {
        log(evt.getCurrentTarget() + " " + evt.getType());
    }

    public void actionPerformed(ActionEvent e) {
        log(e.getClass().toString());
    }

    public void stateChanged(ChangeEvent e) {
        ButtonModel model = (ButtonModel) e.getSource();
        String fact = "";
        if (model.isArmed()) fact += "armed ";
        if (model.isPressed()) fact += "pressed ";
        if (model.isRollover()) fact += "rollover ";
        if (model.isSelected()) fact += "selected ";
        if (!fact.equals("")) {
            log(e.getClass() + " " + fact + " ");
        }
    }
}
