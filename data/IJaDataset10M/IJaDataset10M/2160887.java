package net.sf.poormans.gui;

import net.sf.poormans.gui.workspace.WorkspaceBrowserManager;
import net.sf.poormans.gui.workspace.WorkspaceSiteTreeManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Composite 'workspace', placed at the edit tab.
 * 
 * @version $Id: CompositeWorkspace.java 1419 2008-07-06 16:04:46Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class CompositeWorkspace extends Composite {

    protected CompositeWorkspace(final Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        this.setLayout(gridLayout);
        WorkspaceToolBarManager.getInstance(this, SWT.NONE);
        createSashForm();
        setSize(new Point(700, 500));
        WorkspaceToolBarManager.getInstance().fillComboSiteSelection();
    }

    /**
	 * This method initializes sashForm.
	 */
    private void createSashForm() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        SashForm sashForm = new SashForm(this, SWT.HORIZONTAL);
        sashForm.setLayoutData(gridData);
        createCompositeWorkingArea(sashForm);
        WorkspaceBrowserManager.getInstance(sashForm);
        WorkspaceBrowserManager.getInstance().setFile("help/index.html");
        sashForm.setWeights(new int[] { 1, 3 });
    }

    /**
	 * This method initializes composite.
	 */
    private void createCompositeWorkingArea(Composite parent) {
        GridLayout gridLayoutCompositeWorkspace = new GridLayout();
        gridLayoutCompositeWorkspace.horizontalSpacing = 0;
        gridLayoutCompositeWorkspace.marginWidth = 0;
        gridLayoutCompositeWorkspace.marginHeight = 0;
        gridLayoutCompositeWorkspace.verticalSpacing = 0;
        Composite compositeWorkingspace = new Composite(parent, SWT.NONE);
        compositeWorkingspace.setLayout(gridLayoutCompositeWorkspace);
        WorkspaceSiteTreeManager.getInstance(compositeWorkingspace, SWT.NONE);
    }
}
