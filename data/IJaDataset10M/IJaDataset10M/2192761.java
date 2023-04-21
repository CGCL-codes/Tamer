package org.inqle.ui.rap.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.inqle.data.rdf.jena.Connection;
import org.inqle.data.rdf.jena.NamedModel;
import org.inqle.data.rdf.jenabean.Persister;
import org.inqle.ui.rap.IPartType;
import org.inqle.ui.rap.tree.parts.ModelPart;

/**
 * @author David Donohue
 * Feb 8, 2008
 */
public class DeleteModelAction extends Action {

    private String menuText;

    private IWorkbenchWindow window;

    private Persister persister;

    private NamedModel namedModelToDelete = null;

    private ModelPart modelPart = null;

    private static final Logger log = Logger.getLogger(DeleteModelAction.class);

    public DeleteModelAction(String menuText, ModelPart modelPart, IWorkbenchWindow window, Persister persister) {
        this.window = window;
        this.menuText = menuText;
        this.modelPart = modelPart;
        this.namedModelToDelete = modelPart.getRdbModel();
        this.persister = persister;
    }

    public String getText() {
        return menuText;
    }

    @Override
    public void runWithEvent(Event event) {
        boolean deleteObject = false;
        if (this.modelPart instanceof IPartType) {
            IPartType thisPartType = (IPartType) modelPart;
            if (thisPartType.hasChildren()) {
                MessageDialog.openInformation(window.getShell(), "Unable to delete", "Please remove all child objects before deleting this dataset");
                return;
            }
        }
        if (namedModelToDelete != null) {
            deleteObject = MessageDialog.openConfirm(window.getShell(), "Delete this database", "Are you sure you want to delete dataset\n'" + modelPart.getName() + "'?\nTHIS CANNOT BE UNDONE!");
        }
        if (deleteObject) {
            persister.deleteModel(namedModelToDelete);
            IPartType parentPart = modelPart.getParent();
            parentPart.fireUpdate(parentPart);
        }
    }
}
