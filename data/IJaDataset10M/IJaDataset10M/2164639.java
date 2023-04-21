package com.bonkey.wizards.group;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import com.bonkey.actions.NewFileSystemAction;
import com.bonkey.config.ConfigManager;
import com.bonkey.filesystem.backup.BackupGroup;

/**
 * Wizard page for specifying the settings of a new @link {@link BackupGroup}
 * 
 * @author marcel
 */
public class NewGroupTargetPage extends WizardPage implements SelectionListener {

    /**
	 * Possible backup targets
	 */
    private List target;

    /**
	 * Create new backup target
	 */
    private Button newLocation;

    private String[] selectedTargets;

    public NewGroupTargetPage(String pageName) {
        super(pageName);
    }

    public NewGroupTargetPage(String pageName, String title, ImageDescriptor titleImage) {
        super(pageName, title, titleImage);
    }

    public void createControl(Composite parent) {
        setTitle(Messages.getString("NewGroupTargetPage.Title"));
        setMessage(Messages.getString("NewGroupTargetPage.SelectTargets"));
        Composite base = new Composite(parent, SWT.NULL);
        setControl(base);
        GridLayout layout = new GridLayout();
        layout.horizontalSpacing = 20;
        layout.verticalSpacing = 10;
        Label label = new Label(base, SWT.LEFT);
        label.setText(Messages.getString("NewGroupTargetPage.Target"));
        target = new List(base, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        for (String name : ConfigManager.getConfigManager().getWritableFilesystemNamesArray()) {
            target.add(name);
        }
        target.addSelectionListener(this);
        target.setLayoutData(new GridData(SWT.FILL, SWT.NULL, true, false));
        newLocation = new Button(base, SWT.PUSH);
        newLocation.setText(Messages.getString("NewGroupTargetPage.CreateNewTarget"));
        newLocation.addSelectionListener(this);
        base.setLayout(layout);
        setPageComplete(false);
    }

    public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == target) {
            selectedTargets = target.getSelection();
            setPageComplete(target.getSelectionCount() != 0);
        } else if (e.getSource() == newLocation) {
            NewFileSystemAction action = new NewFileSystemAction();
            action.run(null);
            target.removeAll();
            for (String name : ConfigManager.getConfigManager().getWritableFilesystemNamesArray()) {
                target.add(name);
            }
        }
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }

    public String[] getSelectedTargets() {
        return selectedTargets;
    }
}
