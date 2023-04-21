package org.mobicents.eclipslee.servicecreation.wizards.sbb;

import java.util.HashMap;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.mobicents.eclipslee.servicecreation.ui.SbbEnvEntryPanel;

/**
 * @author cath
 */
public class SbbEnvEntryPage extends WizardPage {

    private static final String PAGE_DESCRIPTION = "Specify the SBB's Environment Entries.";

    /**
	 * @param pageName
	 */
    public SbbEnvEntryPage(String title) {
        super("wizardPage");
        setTitle(title);
        setDescription(PAGE_DESCRIPTION);
    }

    public void createControl(Composite parent) {
        SbbEnvEntryPanel panel = new SbbEnvEntryPanel(parent, SWT.NONE);
        setControl(panel);
        initialize();
        dialogChanged();
    }

    private void initialize() {
    }

    private void dialogChanged() {
        SbbEnvEntryPanel panel = (SbbEnvEntryPanel) getControl();
        if (panel.isCellEditorActive()) {
            updateStatus("You must finish editing the current table cell first.");
            return;
        }
        updateStatus(null);
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public HashMap[] getEnvEntries() {
        SbbEnvEntryPanel panel = (SbbEnvEntryPanel) getControl();
        return panel.getEnvEntries();
    }
}
