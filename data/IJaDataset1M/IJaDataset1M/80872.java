package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;

/**
 * @since Oct 10, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public class ActionSetModelElementStereotype extends UndoableAction {

    /**
     * The instance.
     */
    private static final ActionSetModelElementStereotype SINGLETON = new ActionSetModelElementStereotype();

    /**
     * Constructor for ActionSetModelElementStereotype.
     */
    protected ActionSetModelElementStereotype() {
        super(Translator.localize("Set"), null);
        putValue(Action.SHORT_DESCRIPTION, Translator.localize("Set"));
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        Collection oldStereo = null;
        Object newStereo = null;
        Object target = null;
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 combo = (UMLComboBox2) source;
            if (Model.getFacade().isAStereotype(combo.getSelectedItem())) {
                newStereo = combo.getSelectedItem();
            }
            if (Model.getFacade().isAModelElement(combo.getTarget())) {
                target = combo.getTarget();
                oldStereo = Model.getFacade().getStereotypes(target);
            }
            if ("".equals(combo.getSelectedItem())) {
                newStereo = null;
            }
        }
        if (oldStereo != null && !oldStereo.contains(newStereo) && target != null) {
            if (newStereo != null) {
                Model.getCoreHelper().addStereotype(target, newStereo);
            }
        }
    }

    /**
     * @return Returns the SINGLETON.
     */
    public static ActionSetModelElementStereotype getInstance() {
        return SINGLETON;
    }
}
