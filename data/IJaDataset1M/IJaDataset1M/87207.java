package org.argouml.uml.ui.behavior.use_cases;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.argouml.application.api.Argo;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;
import org.argouml.uml.ui.UMLConnectionListModel;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MClassifier;

/**
 * Binary relation list model for associations with usecases
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLUseCaseAssociationListModel extends UMLConnectionListModel {

    /**
	 * Constructor for UMLUseCaseAssociationListModel.
	 * @param container
	 * @param property
	 * @param showNone
	 */
    public UMLUseCaseAssociationListModel(UMLUserInterfaceContainer container, String property, boolean showNone) {
        super(container, property, showNone);
    }

    /**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getChoices()
	 */
    protected Collection getChoices() {
        Vector choices = new Vector();
        choices.addAll(super.getChoices());
        Vector choices2 = new Vector();
        Collection specpath = UseCasesHelper.getHelper().getSpecificationPath((MUseCase) getTarget());
        if (!specpath.isEmpty()) {
            Iterator it = choices.iterator();
            while (it.hasNext()) {
                MClassifier choice = (MClassifier) it.next();
                if (choice instanceof MUseCase) {
                    Collection specpath2 = UseCasesHelper.getHelper().getSpecificationPath((MUseCase) choice);
                    if (!specpath.equals(specpath2)) choices2.add(choice);
                } else choices2.add(choice);
            }
        } else {
            choices2 = choices;
        }
        return choices2;
    }

    /**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
	 */
    protected String getAddDialogTitle() {
        return Argo.localize("UMLMenu", "dialog.title.add-associated-usecases");
    }
}
