package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * The model behind the UMLActivatorComboBox. I don't use the UMLComboBoxModel
 * since this mixes the GUI and the model too much and is much more maintainance 
 * intensive then this implementation.
 */
public class UMLActivatorComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLActivatorComboBoxModel.
     * @param container
     */
    public UMLActivatorComboBoxModel(UMLUserInterfaceContainer container) {
        super(container, "activator");
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Object target = getContainer().getTarget();
        if (target instanceof MMessage) {
            MMessage mes = (MMessage) target;
            removeAllElements();
            setElements(CollaborationsHelper.getHelper().getAllPossibleActivators(mes));
            setSelectedItem(mes.getActivator());
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValid(MModelElement)
     */
    protected boolean isValid(MModelElement m) {
        return ((m instanceof MMessage) && m != getContainer().getTarget() && !((MMessage) (getContainer().getTarget())).getPredecessors().contains(m) && ((MMessage) m).getInteraction() == ((MMessage) (getContainer().getTarget())).getInteraction());
    }
}
