package org.xulbooster.eclipse.xb.ui.snippets.item.radiogroup;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.xulbooster.eclipse.xb.ui.snippets.utils.LineWizardPageFactory;

/**
 * The "New" wizard page allows setting the container for
 * the new file as well as the file name. The page
 * will only accept file name without the extension OR
 * with the extension that matches the expected one (xul).
 */
public class RadioGroupWizardPage extends WizardPage {

    private Text radioGroupId;

    private ISelection selection;

    /**
	 * Constructor for SampleNewWizardPage.
	 * @param pageName
	 */
    public RadioGroupWizardPage(ISelection selection) {
        super("wizardPage");
        setTitle("Radio Group Editor Element");
        setDescription("This wizard creates a new radio group.");
        this.selection = selection;
    }

    /**
	 * @see IDialogPage#createControl(Composite)
	 */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;
        radioGroupId = LineWizardPageFactory.createTextLine(container, "Radio Group id:");
        initialize();
        dialogChanged();
        setControl(container);
    }

    /**
	 * Tests if the current workbench selection is a suitable
	 * container to use.
	 */
    private void initialize() {
    }

    /**
	 * Uses the standard container selection dialog to
	 * choose the new value for the container field.
	 */
    private void handleBrowse() {
        ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), false, "Select new file container");
        if (dialog.open() == ContainerSelectionDialog.OK) {
            Object[] result = dialog.getResult();
            if (result.length == 1) {
                radioGroupId.setText(((Path) result[0]).toOSString());
            }
        }
    }

    /**
	 * Ensures that both text fields are set.
	 */
    private void dialogChanged() {
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getRadioGroupId() {
        return radioGroupId.getText();
    }
}
