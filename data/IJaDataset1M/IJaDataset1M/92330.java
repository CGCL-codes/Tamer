package com.nokia.ats4.appmodel.perspective.modeldesign.controller;

import com.nokia.ats4.appmodel.event.KendoEvent;
import com.nokia.ats4.appmodel.event.KendoEventListener;
import com.nokia.ats4.appmodel.grapheditor.event.AddOutGateEvent;
import com.nokia.ats4.appmodel.model.KendoApplicationModel;
import com.nokia.ats4.appmodel.model.KendoModel;

/**
 * Adds output gate in the active application model
 * 
 * 
 * @author Hannu-Pekka Hakam&auml;ki
 * @version $Revision: 2 $
 */
public class AddOutputGateCommand implements KendoEventListener {

    /**
     * Creates a new instance of AddOutputGateCommand
     */
    public AddOutputGateCommand() {
    }

    @Override
    public void processEvent(KendoEvent event) {
        AddOutGateEvent evt = (AddOutGateEvent) event;
        if (evt.getSource() instanceof KendoApplicationModel) {
            ((KendoApplicationModel) evt.getSource()).addOutGate((double) evt.getX(), (double) evt.getY());
        } else {
            KendoModel model = evt.getKendoModel();
            if (model instanceof KendoApplicationModel) {
                ((KendoApplicationModel) model).addOutGate(evt.getX(), evt.getY());
            }
        }
    }
}
