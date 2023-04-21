package ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy;

import java.util.LinkedList;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.ui.actions.RefreshAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import ca.ucalgary.cpsc.ebe.fitClipse.DeveloperCoordinator;
import ca.ucalgary.cpsc.ebe.fitClipse.ICoordinator;
import ca.ucalgary.cpsc.ebe.fitClipse.connector.BeanConnector;
import ca.ucalgary.cpsc.ebe.fitClipse.runner.FitManager;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.editor.WikiEditor;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.editor.WikiEditorController;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.editor.WikiEditorInput;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions.DeleteAction;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions.GenerateActionFixtureAction;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions.NewFitPageAction;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions.NewWikiPageAction;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions.RefreshViewAction;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions.RunFitSuiteAction;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions.RunLastAction;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions.RunTestAction;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.menu.actions.toggleFitTestAttributeAction;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.model.WikiPageModel;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testResults.TestResultView;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.wizards.GenerateFixtureCodeWizard;

public class TestHierarchyView extends ViewPart {

    private TestHierarchyController pageController = new TestHierarchyController();

    private NewFitPageAction newFitPageAction = null;

    private NewWikiPageAction newWikiPageAction = null;

    private RunTestAction runTestAction = null;

    private RunFitSuiteAction runFitSuiteAction = null;

    private toggleFitTestAttributeAction tagAsFitTestAction = null;

    private GenerateActionFixtureAction generateActionFixtureAction = null;

    private RunLastAction runLastAction = null;

    private RefreshViewAction refreAction = null;

    private DeleteAction deleteAction = null;

    public static final String ID = TestHierarchyView.class.getName();

    private Composite top = null;

    private TreeViewer viewer = null;

    TestHierarchyDocument treeData = null;

    @Override
    public void createPartControl(Composite parent) {
        GridLayout gridLayout = new GridLayout();
        top = new Composite(parent, SWT.NONE);
        top.setLayout(gridLayout);
        createTree();
        DeveloperCoordinator dc = DeveloperCoordinator.getInstance();
        dc.register(this);
        createToolbar();
    }

    public void refreshView() {
        try {
            if (treeData == null) {
                treeData = new TestHierarchyDocument();
            }
            WikiPageModel root = treeData.buildModel();
            viewer.setInput(root);
            viewer.refresh(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * This method initializes tree
	 * 
	 */
    private void createTree() {
        GridData gridData = new org.eclipse.swt.layout.GridData();
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        viewer = new TreeViewer(top);
        viewer.setContentProvider(new TestHierarchyContentProvider());
        viewer.setLabelProvider(new TestHierarchyLabelProvider());
        viewer.setUseHashlookup(true);
        viewer.getControl().setLayoutData(gridData);
        viewer.expandAll();
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                WikiPageModel model = (WikiPageModel) selection.getFirstElement();
                if (model.isFitTest()) WikiEditorController.newFitEditor(model.getName(), model.getQName()); else WikiEditorController.newWikiEditor(model.getName(), model.getQName());
            }
        });
        Tree tree = viewer.getTree();
        MenuManager root_menu = new MenuManager();
        MenuManager newMenu = new MenuManager("New");
        newFitPageAction = new NewFitPageAction(viewer);
        newMenu.add(newFitPageAction);
        newWikiPageAction = new NewWikiPageAction(viewer);
        newMenu.add(newWikiPageAction);
        root_menu.add(newMenu);
        MenuManager fitTestMenu = new MenuManager("FIT Test");
        runTestAction = new RunTestAction(viewer);
        fitTestMenu.add(runTestAction);
        runFitSuiteAction = new RunFitSuiteAction(viewer);
        fitTestMenu.add(runFitSuiteAction);
        runLastAction = new RunLastAction(viewer);
        fitTestMenu.add(runLastAction);
        tagAsFitTestAction = new toggleFitTestAttributeAction(viewer);
        fitTestMenu.add(tagAsFitTestAction);
        root_menu.add(fitTestMenu);
        MenuManager generateFixtureMenu = new MenuManager("Generate Fixture...");
        generateActionFixtureAction = new GenerateActionFixtureAction(viewer);
        generateFixtureMenu.add(generateActionFixtureAction);
        root_menu.add(generateFixtureMenu);
        deleteAction = new DeleteAction(viewer);
        root_menu.add(deleteAction);
        refreAction = new RefreshViewAction(viewer);
        root_menu.add(refreAction);
        tree.setMenu(root_menu.createContextMenu(tree));
        viewer.addSelectionChangedListener(newFitPageAction);
        viewer.addSelectionChangedListener(newWikiPageAction);
        viewer.addSelectionChangedListener(runTestAction);
        viewer.addSelectionChangedListener(runFitSuiteAction);
        viewer.addSelectionChangedListener(tagAsFitTestAction);
        viewer.addSelectionChangedListener(generateActionFixtureAction);
        viewer.addSelectionChangedListener(deleteAction);
    }

    @Override
    public void setFocus() {
    }

    protected void createToolbar() {
        IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
        toolbarManager.add(runTestAction);
        toolbarManager.add(runLastAction);
        toolbarManager.add(refreAction);
    }
}
