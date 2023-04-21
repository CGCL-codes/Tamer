package com.prolix.editor.main.workspace.reload.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import com.prolix.editor.main.workspace.reload.ReloadWorkspace;
import com.prolix.editor.main.workspace.reload.utils.MetadataDialog;
import com.prolix.editor.main.workspace.reload.utils.TitledReloadToProlixComposite;

/**
 * Property Group Editor Panel
 * 
 * @author Phillip Beauvoir
 * @version $Id: PropertyGroupEditorPanel.java,v 1.4 2006/07/10 11:50:37 phillipus Exp $
 */
public class PropertyGroupEditorPanel extends TitledReloadToProlixComposite {

    /**
     * Metadata button
     */
    private Button _mdButton;

    /**
     * Constructor
     */
    public PropertyGroupEditorPanel(ReloadWorkspace ldEditor, Composite parent) {
        super(ldEditor, parent);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, HELPID_LD_PROPERTIES_GROUP_EDITOR);
    }

    /**
     * Create the part
     */
    protected void setupView() {
        super.setupView();
        _mdButton = createMetadataButton(getChildPanel());
        _mdButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                handleMetadataAction();
            }
        });
        setMinSize(getChildPanel().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        getChildPanel().setSize(getChildPanel().computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    /**
     * Handle the Metadata button pressed Action
     */
    protected void handleMetadataAction() {
        if (getDataComponent() != null) {
            MetadataDialog dialog = new MetadataDialog(getShell(), getDataComponent());
            dialog.open();
        }
    }

    /**
     * Dispose of stuff
     */
    public void dispose() {
        super.dispose();
        _mdButton = null;
    }
}
