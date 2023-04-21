package com.parfumball.eclipse.plugin.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import com.parfumball.TreeObject;
import com.parfumball.eclipse.plugin.controller.NetworkInterfacesController;
import com.parfumball.pcap.PcapWrapper;

/**
 * The NetworkInterfacesView shows the available network interfaces
 * on the local host.
 * 
 * @author prasanna
 */
public class NetworkInterfacesView extends ViewPart {

    /**
     * The TreeViewer that lists the network interfaces identified in the
     * system.
     */
    private TreeViewer viewer;

    /**
	 * A fancy DrillDownAdapter.
	 */
    private DrillDownAdapter drillDownAdapter;

    /**
	 * The NetworkInterfacesController. The controller controls all
	 * interactions between the view and the model.
	 */
    private NetworkInterfacesController controller;

    /**
	 * The constructor.
	 */
    public NetworkInterfacesView() {
    }

    /**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
    public void createPartControl(Composite parent) {
        if (!checkWPcap()) {
            createDisabledView(parent);
            return;
        }
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        drillDownAdapter = new DrillDownAdapter(viewer);
        viewer.setContentProvider(new NetworkInterfacesViewContentProvider(getViewSite()));
        viewer.setLabelProvider(new NetworkInterfacesViewLabelProvider());
        viewer.setInput(getViewSite());
        controller = new NetworkInterfacesController(this);
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
    }

    /**
	 * Returns true if WPcap is installed properly.
	 * 
	 * @return
	 */
    private boolean checkWPcap() {
        try {
            PcapWrapper wrapper = PcapWrapper.newInstance();
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    /**
	 * Creates a disabled version of this view.
	 * 
	 * @param parent
	 */
    public void createDisabledView(Composite parent) {
        Text disabled = new Text(parent, SWT.MULTI | SWT.READ_ONLY);
        disabled.setText("To see the list of network interfaces, please install ParfumBall properly. " + "ParfumBall depends on WinPcap to work properly.");
    }

    /**
	 * A hook for adding a ISelectionChangedListener to the underlying TreeViewer.
	 *  
	 * @param listener
	 */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        viewer.addSelectionChangedListener(listener);
    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                NetworkInterfacesView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    /**
	 * Fills the views context menu with the menu actions.
	 * 
	 * @param manager
	 */
    private void fillContextMenu(IMenuManager manager) {
        manager.add(controller.getStartCaptureAction());
        manager.add(controller.getStopCaptureAction());
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
	 * Populates the view's local pull down and local tool bars.
	 *
	 */
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    /**
	 * Populates this view's local toolbar.
	 * 
	 * @param manager
	 */
    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(controller.getStartCaptureAction());
        manager.add(new Separator());
        manager.add(controller.getStopCaptureAction());
    }

    /**
	 * Fills the view's local toolbar.
	 * @param manager
	 */
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(controller.getStartCaptureAction());
        manager.add(controller.getStopCaptureAction());
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
    }

    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
            }
        });
    }

    public void showMessage(String message) {
        MessageDialog.openInformation(viewer.getControl().getShell(), "Network Interfaces", message);
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    /**
     * Returns the underlying TreeViewer.
     * 
     * @return
     */
    public TreeViewer getViewer() {
        return viewer;
    }

    /**
     * Returns the current selection in the underlying TreeViewer.
     * 
     * @return
     */
    public TreeObject getCurrentSelection() {
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        return (TreeObject) selection.getFirstElement();
    }
}
