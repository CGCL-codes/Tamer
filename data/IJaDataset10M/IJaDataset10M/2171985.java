package com.nhncorp.cubridqa.cases;

import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import com.nhncorp.cubridqa.Activator;
import com.nhncorp.cubridqa.model.Case;
import com.nhncorp.cubridqa.swtdesigner.ResourceManager;

/**
 * 
 * The view is used to display the cases.
 * @ClassName: CasesView
 * @Description: the view of case .
 * @date 2009-9-7
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class CasesView extends ViewPart {

    private Action viewAction;

    private Action editAction;

    private Action runAction;

    public static final String ID = "com.nhncorp.cubridqa.cases.casesView";

    public CaseComposite list;

    public Composite parent;

    /**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
    @Override
    public void createPartControl(Composite parent) {
        list = new CasesList(parent, SWT.NONE, this);
        this.parent = parent;
        list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        createActions();
        initializeToolBar();
        initializeMenu();
    }

    /**
	 * Create the actions
	 */
    private void createActions() {
        runAction = new Action("Run SQL/Shell") {

            public void run() {
            }
        };
        runAction.setHoverImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/run_tool.gif"));
        editAction = new Action("Edit SQL/Shell") {

            public void run() {
            }
        };
        editAction.setHoverImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/edit.gif"));
        viewAction = new Action("View SQL/Shell") {

            public void run() {
            }
        };
        viewAction.setHoverImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/details_view.gif"));
    }

    /**
	 * Initialize the toolbar
	 */
    private void initializeToolBar() {
        IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
        toolbarManager.add(runAction);
        toolbarManager.add(viewAction);
        toolbarManager.add(editAction);
    }

    /**
	 * Initialize the menu
	 */
    private void initializeMenu() {
        IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
    }

    @Override
    public void setFocus() {
    }

    public void showList(CaseComposite cl) {
        this.list.dispose();
        this.list = cl;
        list.getParent().layout(true, true);
    }

    /**
	 * show Case list
	 * 
	 * @param cases
	 */
    public void showCaseList(List cases) {
        CasesList cl = null;
        if (!(list instanceof CasesList)) {
        } else {
            list.fresh(cases);
        }
    }

    /**
	 * show Case list
	 * 
	 * @param cases
	 * @param checked
	 */
    public void showCaseList(List<Case> cases, boolean checked) {
        CasesList cl = null;
        if (!(list instanceof CasesList)) {
        } else {
            ((CasesList) list).setCheckedCase(cases, checked);
        }
    }
}
