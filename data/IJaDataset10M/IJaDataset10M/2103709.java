package com.safi.workshop.audio;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import com.safi.workshop.part.AsteriskDiagramEditorPlugin;
import com.swtdesigner.ResourceManager;

public class ImportPromptStartPage extends WizardPage {

    private ImportMode mode;

    public enum ImportMode {

        IMPORT_EXISTING, RECORD_NEW, KEEP_EXISTING
    }

    ;

    /**
   * Create the wizard
   */
    public ImportPromptStartPage() {
        super("wizardPage");
        setImageDescriptor(ResourceManager.getPluginImageDescriptor(AsteriskDiagramEditorPlugin.getDefault(), "icons/wizban/PromptWizard.gif"));
    }

    /**
   * Create contents of the wizard
   * 
   * @param parent
   */
    public void createControl(Composite parent) {
        ImportAudioFileWizard wiz = (ImportAudioFileWizard) getWizard();
        if (wiz.getMode() == ImportAudioFileWizard.Mode.NEW) {
            setTitle("New Prompt Wizard");
            setDescription("Do you want to import an existing file or create a new recording?");
        } else {
            setTitle("Edit Prompt");
            setDescription("Edit prompt " + wiz.getPrompt().getName());
        }
        Composite container = new Composite(parent, SWT.NULL);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginLeft = 20;
        container.setLayout(gridLayout);
        setControl(container);
        if (wiz.getMode() == ImportAudioFileWizard.Mode.EDIT) {
            final Button keepExistingButton = new Button(container, SWT.RADIO);
            keepExistingButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    mode = ImportMode.KEEP_EXISTING;
                }
            });
            keepExistingButton.setSelection(true);
            final GridData gd_keepExistingAudioButton = new GridData(SWT.LEFT, SWT.BOTTOM, true, true);
            gd_keepExistingAudioButton.horizontalIndent = 50;
            keepExistingButton.setLayoutData(gd_keepExistingAudioButton);
            keepExistingButton.setText("Keep existing audio prompt");
            mode = ImportMode.KEEP_EXISTING;
        } else mode = ImportMode.IMPORT_EXISTING;
        final Button importExistingAudioButton = new Button(container, SWT.RADIO);
        importExistingAudioButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                mode = ImportMode.IMPORT_EXISTING;
            }
        });
        importExistingAudioButton.setSelection(mode == ImportMode.IMPORT_EXISTING);
        final GridData gd_importExistingAudioButton = new GridData(SWT.LEFT, SWT.BOTTOM, true, mode == ImportMode.IMPORT_EXISTING);
        gd_importExistingAudioButton.horizontalIndent = 50;
        importExistingAudioButton.setLayoutData(gd_importExistingAudioButton);
        importExistingAudioButton.setText("Import existing audio file");
        final Button recordANewButton = new Button(container, SWT.RADIO);
        recordANewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                mode = ImportMode.RECORD_NEW;
            }
        });
        final GridData gd_recordANewButton = new GridData(SWT.LEFT, SWT.TOP, true, true);
        gd_recordANewButton.horizontalIndent = 50;
        recordANewButton.setLayoutData(gd_recordANewButton);
        if (((ImportAudioFileWizard) this.getWizard()).getMode() == ImportAudioFileWizard.Mode.NEW) {
            recordANewButton.setText("Record a new prompt");
        } else {
            recordANewButton.setText("Record a new prompt or edit current");
        }
    }

    public ImportMode getImportMode() {
        return mode;
    }
}
