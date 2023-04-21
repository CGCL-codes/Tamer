package eu.redseeds.sc.current.ui.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import eu.redseeds.scl.model.rsl.rsldomainelements.actors.ActorDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.notions.NotionDTO;
import eu.redseeds.scl.model.rsl.rsldomainelements.systemelements.SystemElementDTO;

public class RenameWizardPage extends WizardPage {

    protected Composite container;

    protected Text elementName;

    protected ISelection selection;

    protected RenameWizardPage(ISelection selection) {
        super(ResourceMessages.Rename_windowTitle);
        this.selection = selection;
        setTitle(ResourceMessages.Rename_title);
        setDescription(ResourceMessages.Rename_description);
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        Label labelClipboardName = new Label(container, SWT.NULL);
        labelClipboardName.setText(ResourceMessages.Rename_label);
        elementName = new Text(container, SWT.BORDER | SWT.SINGLE);
        elementName.setText("");
        elementName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        elementName.setLayoutData(gd);
        elementName.setFocus();
        setControl(container);
        Object selectedObj = getSelectedObject(selection);
        if (selectedObj instanceof ActorDTO) {
            ActorDTO actor = (ActorDTO) selectedObj;
            elementName.setText(actor.getName());
        } else if (selectedObj instanceof NotionDTO) {
            NotionDTO notion = (NotionDTO) getSelectedObject(selection);
            elementName.setText(notion.getName());
        } else if (selectedObj instanceof SystemElementDTO) {
            SystemElementDTO sysEl = (SystemElementDTO) selectedObj;
            elementName.setText(sysEl.getName());
        }
        setPageComplete(false);
        updateStatus(null);
    }

    public String getNewElementName() {
        return this.elementName.getText();
    }

    @Override
    public Control getControl() {
        return container;
    }

    /**
	 * Ensures that data is properly set.
	 */
    protected void dialogChanged() {
        String elemNameString = getNewElementName();
        ((RenameWizard) getWizard()).setNewElementName(elemNameString);
        if (elemNameString.length() == 0) {
            updateStatus(ResourceMessages.Validation_name);
            return;
        }
        Object selectedObj = getSelectedObject(selection);
        if (selectedObj instanceof ActorDTO) {
            ActorDTO actor = (ActorDTO) selectedObj;
            if (!actor.isNameUnique(elemNameString)) {
                updateStatus(ResourceMessages.Validation_uniqueName);
                return;
            }
        }
        if (selectedObj instanceof NotionDTO) {
            NotionDTO notion = (NotionDTO) selectedObj;
            if (!notion.isNameUnique(elemNameString)) {
                updateStatus(ResourceMessages.Validation_uniqueName);
                return;
            }
        }
        if (selectedObj instanceof SystemElementDTO) {
            SystemElementDTO sysEl = (SystemElementDTO) selectedObj;
            if (!sysEl.isNameUnique(elemNameString)) {
                updateStatus(ResourceMessages.Validation_uniqueName);
                return;
            }
        }
        updateStatus(null);
    }

    protected void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    protected Object getSelectedObject(ISelection selection) {
        if (selection != null) {
            ITreeSelection treeSelection = (ITreeSelection) selection;
            TreePath[] tr = treeSelection.getPaths();
            if (tr[0] != null) {
                Object selectedObj = tr[0].getLastSegment();
                return selectedObj;
            } else return null;
        } else return null;
    }

    protected ISelection getSelection() {
        return selection;
    }
}
