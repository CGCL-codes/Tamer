package com.wizzer.m3g.viewer.ui;

import java.util.Vector;
import java.util.Observer;
import java.util.Observable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import com.wizzer.m3g.viewer.Activator;
import com.wizzer.m3g.viewer.domain.SceneGraphManager;
import com.wizzer.m3g.viewer.ui.wizards.M3gObjectExportWizard;
import com.wizzer.m3g.viewer.ui.actions.DisplayM3gAction;
import com.wizzer.m3g.viewer.ui.actions.RefreshDisplayAction;

public class M3gFileView extends ViewPart implements Observer {

    public static final String ID = "com.wizzer.m3g.viewer.ui.m3gfileview";

    private TreeViewer m_fileViewer;

    private Action m_exportAction;

    private Action m_displayAction;

    private Action m_refreshDisplayAction;

    static Vector<M3gNode> g_fileroot = new Vector<M3gNode>();

    /**
     * The default constructor.
     * <p>
     * Adds itself as an Observer to the <code>SceneGraphManager</code>.
     * </p>
     */
    public M3gFileView() {
        SceneGraphManager manager = SceneGraphManager.getInstance();
        manager.addObserver(this);
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 * 
	 * @param parent The control's parent widget.
	 */
    public void createPartControl(Composite parent) {
        m_fileViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        m_fileViewer.setContentProvider(new M3gContentProvider());
        m_fileViewer.setLabelProvider(new M3gLabelProvider());
        m_fileViewer.setInput(g_fileroot);
        getSite().setSelectionProvider(m_fileViewer);
        createActions();
        createToolbar();
        createContextMenu();
    }

    private void createActions() {
        m_exportAction = new Action("Export...") {

            public void run() {
                System.out.println("Exporting M3gNode!");
                IStructuredSelection selection = (IStructuredSelection) m_fileViewer.getSelection();
                M3gObjectExportWizard wizard = new M3gObjectExportWizard();
                wizard.init(getSite().getWorkbenchWindow().getWorkbench(), (IStructuredSelection) selection);
                Shell shell = getSite().getShell();
                WizardDialog dialog = new WizardDialog(shell, wizard);
                dialog.create();
                dialog.setTitle("M3G Object Export Wizard");
                dialog.setMessage("Create a new .m3g file from the selected object.");
                dialog.open();
            }
        };
        m_exportAction.setImageDescriptor(Activator.getImageDescriptor("icons/cube_16.png"));
        m_displayAction = new DisplayM3gAction();
        m_refreshDisplayAction = new RefreshDisplayAction();
        m_fileViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                updateActionEnablement();
            }
        });
    }

    private void createToolbar() {
        IActionBars actionBars = getViewSite().getActionBars();
        IMenuManager dropDownMenu = actionBars.getMenuManager();
        m_displayAction.setEnabled(false);
        m_refreshDisplayAction.setEnabled(false);
        dropDownMenu.add(m_displayAction);
        dropDownMenu.add(m_refreshDisplayAction);
    }

    private void updateActionEnablement() {
        IStructuredSelection sel = (IStructuredSelection) m_fileViewer.getSelection();
    }

    private void createContextMenu() {
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(mgr);
            }
        });
        Menu menu = menuMgr.createContextMenu(m_fileViewer.getControl());
        m_fileViewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, m_fileViewer);
    }

    private void fillContextMenu(IMenuManager mgr) {
        mgr.add(m_exportAction);
        mgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        m_fileViewer.getControl().setFocus();
    }

    /**
	 * Enable the display button.
	 * 
	 * @param enabled <b>true</b> if the display button should be
	 * displayed. Otherwise set to <b>false</b>.
	 */
    public void enableDisplay(boolean enabled) {
        m_displayAction.setEnabled(enabled);
    }

    /**
	 * Enable the refresh display button.
	 * 
	 * @param enabled <b>true</b> if the refresh display button should be
	 * displayed. Otherwise set to <b>false</b>.
	 */
    public void enableRefreshDisplay(boolean enabled) {
        m_refreshDisplayAction.setEnabled(enabled);
    }

    /**
	 * This method is called whenever the observed object is changed.
	 * An application calls an Observable object's notifyObservers method to have
	 * all the object's observers notified of the change.
	 * 
	 * @param observable The observable object.
	 * @param obj An argument passed to the notifyObservers method.
	 */
    public void update(Observable observable, Object obj) {
        if (observable instanceof SceneGraphManager) {
            Vector<M3gNode> fileRoot = ((SceneGraphManager) observable).m_fileroot;
            g_fileroot.removeAllElements();
            for (int i = 0; i < fileRoot.size(); i++) g_fileroot.add(fileRoot.elementAt(i));
            m_fileViewer.refresh();
            m_fileViewer.expandAll();
        }
    }
}
