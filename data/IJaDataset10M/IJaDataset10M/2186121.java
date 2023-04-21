package uk.ac.reload.straker.editors.learningdesign.shared;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;
import uk.ac.reload.straker.ui.dialogs.NewComponentDialog;
import uk.ac.reload.straker.ui.dialogs.RenameDialog;
import uk.ac.reload.straker.ui.viewers.StrakerTreeViewer;

/**
 * Learning Design Editor Tree Viewer
 * 
 * @author Phillip Beauvoir
 * @version $Id: LDTreeViewer.java,v 1.4 2006/07/10 11:50:26 phillipus Exp $
 */
public abstract class LDTreeViewer extends StrakerTreeViewer {

    /**
     * Constructor
     */
    public LDTreeViewer(LearningDesignDataModel dataModel, Composite parent) {
        super(dataModel, parent);
    }

    protected void handleRenameAction() {
        LD_DataComponent dc = (LD_DataComponent) ((IStructuredSelection) getSelection()).getFirstElement();
        if (dc != null) {
            String newName = askRenameComponentName(dc);
            if (newName != null) {
                dc.setTitle(newName);
                getDataModel().fireDataComponentChanged(dc);
                getDataModel().setDirty(true);
            }
        }
    }

    /**
     * Add new child component and notify
     * @param parent
     * @param child
     */
    protected void addNewComponent(DataComponent parent, DataComponent child) {
        parent.addChild(child);
        child.setDefaults();
        getDataModel().fireDataComponentAdded(child);
        getDataModel().setDirty(true);
        expandToLevel(parent, 1);
    }

    /**
     * Ask the user for a component name
     * @param componentName
     * @param initialValue The initial value
     * @return The name or null if user cancelled
     */
    protected String askNewComponentName(String componentName, String initialValue) {
        NewComponentDialog dialog = new NewComponentDialog(getControl().getShell(), componentName, initialValue);
        dialog.open();
        return dialog.getName();
    }

    /**
     * Ask the user to rename a component
     * @param dc The Component to rename
     * @return The name or null if user cancelled
     */
    protected String askRenameComponentName(LD_DataComponent dc) {
        if (dc == null) {
            return null;
        }
        RenameDialog dialog = new RenameDialog(getControl().getShell(), dc.getTitle());
        dialog.open();
        return dialog.getNewName();
    }
}
