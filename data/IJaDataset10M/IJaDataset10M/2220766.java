package com.alveole.studio.web.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import com.alveole.studio.web.data.Project;

/**
 * This is the struts2 feature wizard main page.
 * 
 * @author sylvain
 *
 */
public class Struts2Page extends WizardPage {

    /**
	 * Project page.
	 */
    protected Project project;

    /**
	 * Constructor.
	 */
    public Struts2Page() {
        super("Enable struts2 framework");
        setDescription("Select Struts 2 feature status");
        setTitle("Struts 2");
        setPageComplete(true);
    }

    /**
	 * Enable struts2 button.
	 */
    protected Button enableStruts2Button;

    /**
	 * Add struts 2 dependencies.
	 */
    protected Button addStruts2Deps;

    /**
	 * The group
	 */
    private Group grp;

    /**
	 * The target dir.
	 */
    protected Text targetDir;

    /**
	 * Directory browse button.
	 */
    protected Button browse;

    /**
	 * Creates target panel, where features are editable.
	 */
    public void createControl(Composite parent) {
        Composite ret = new Composite(parent, SWT.NONE);
        GridData grid2 = new GridData();
        grid2.grabExcessHorizontalSpace = true;
        grid2.widthHint = GridData.FILL_HORIZONTAL;
        ret.setLayoutData(grid2);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        ret.setLayout(layout);
        enableStruts2Button = new Button(ret, SWT.CHECK);
        enableStruts2Button.setSelection(true);
        enableStruts2Button.setText("Enable struts 2");
        grp = new Group(ret, SWT.NONE);
        GridData grid3 = new GridData();
        grid3.grabExcessHorizontalSpace = true;
        grid3.widthHint = GridData.FILL_HORIZONTAL;
        grp.setLayoutData(grid3);
        grp.setText("Struts 2 feature settings");
        GridLayout grpLayout = new GridLayout();
        grpLayout.numColumns = 3;
        grp.setLayout(grpLayout);
        addStruts2Deps = new Button(grp, SWT.CHECK);
        addStruts2Deps.setText("Copy Struts 2 libraries");
        addStruts2Deps.setSelection(true);
        addStruts2Deps.setEnabled(true);
        GridData grid1 = new GridData();
        grid1.horizontalSpan = 3;
        addStruts2Deps.setLayoutData(grid1);
        grp.setEnabled(true);
        Label label = new Label(grp, SWT.NONE);
        label.setText("Target dir");
        targetDir = new Text(grp, SWT.BORDER);
        targetDir.setEnabled(true);
        GridData grid4 = new GridData();
        grid4.grabExcessHorizontalSpace = true;
        grid4.widthHint = GridData.FILL_HORIZONTAL;
        targetDir.setLayoutData(grid4);
        browse = new Button(grp, SWT.NONE);
        browse.setText("...");
        browse.setEnabled(true);
        browse.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                IContainer selected = project.getProjectContext().getJavaProject().getWorkspace().getRoot().getFolder(new Path(targetDir.getText()));
                if (selected == null) selected = project.getProjectContext().getJavaProject();
                ContainerSelectionDialog dlg = new ContainerSelectionDialog(getShell(), selected, false, "Choose target directory");
                dlg.setInitialSelections(new Object[] { new Path(targetDir.getText()) });
                dlg.open();
                Object[] ret = dlg.getResult();
                if (ret != null && ret.length >= 1) {
                    IPath resource = (IPath) ret[0];
                    targetDir.setText(resource.toString());
                }
            }
        });
        enableStruts2Button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                grp.setEnabled(enableStruts2Button.getSelection());
                addStruts2Deps.setEnabled(enableStruts2Button.getSelection());
                targetDir.setEnabled(enableStruts2Button.getSelection());
                browse.setEnabled(enableStruts2Button.getSelection());
            }
        });
        setControl(ret);
    }

    /**
	 * Get the project.
	 * @return
	 */
    public Project getProject() {
        return project;
    }

    /**
	 * Set the project.
	 * @param project
	 */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
	 * Refresh fields before setting visible.
	 */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            IVirtualComponent component = ComponentCore.createComponent(project.getProjectContext().getJavaProject());
            IPath webcontents = project.getProjectContext().getJavaProject().getFullPath();
            if (component != null && component.exists()) {
                webcontents = component.getRootFolder().getWorkspaceRelativePath().append("WEB-INF/lib/");
            }
            targetDir.setText(webcontents.toString());
        }
    }
}
