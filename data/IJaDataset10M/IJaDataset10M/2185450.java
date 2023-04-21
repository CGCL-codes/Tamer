package org.argouml.uml.ui.behavior.activity_graphs;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;

/**
* @since Aug 11, 2004
* @author mvw
*/
public class ActionSetObjectFlowStateClassifier extends UndoableAction {

    /**
     * Constructor for ActionSetObjectFlowStateClassifier.
     */
    public ActionSetObjectFlowStateClassifier() {
        super();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Object oldClassifier = null;
        Object newClassifier = null;
        Object m = null;
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 box = (UMLComboBox2) source;
            Object ofs = box.getTarget();
            if (Model.getFacade().isAObjectFlowState(ofs)) {
                oldClassifier = Model.getFacade().getType(ofs);
                m = ofs;
            }
            Object cl = box.getSelectedItem();
            if (Model.getFacade().isAClassifier(cl)) {
                newClassifier = cl;
            }
        }
        if (newClassifier != oldClassifier && m != null && newClassifier != null) {
            super.actionPerformed(e);
            Model.getCoreHelper().setType(m, newClassifier);
        }
    }
}
