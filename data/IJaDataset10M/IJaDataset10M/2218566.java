package org.mobicents.eclipslee.servicecreation.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorXMLSubmenu implements IObjectActionDelegate, IMenuCreator {

    public ResourceAdaptorXMLSubmenu() {
        super();
    }

    public void run(IAction action) {
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public void dispose() {
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            fFillMenu = true;
            if (action != null) {
                if (fDelegateAction != action) {
                    fDelegateAction = action;
                    fDelegateAction.setMenuCreator(this);
                }
                this.selection = selection;
                action.setEnabled(true);
                return;
            }
            return;
        }
        action.setEnabled(false);
    }

    public Menu getMenu(Control control) {
        return null;
    }

    public Menu getMenu(Menu parent) {
        Menu menu = new Menu(parent);
        menu.addMenuListener(new MenuAdapter() {

            public void menuShown(MenuEvent e) {
                if (fFillMenu) {
                    Menu m = (Menu) e.widget;
                    MenuItem items[] = m.getItems();
                    for (int i = 0; i < items.length; i++) items[i].dispose();
                    fillMenu(m);
                    fFillMenu = false;
                }
            }
        });
        return menu;
    }

    private void fillMenu(Menu menu) {
        createResourceAdaptorMenus(menu);
    }

    private void createResourceAdaptorMenus(Menu parent) {
        if (selection == null && selection.isEmpty()) {
            return;
        }
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }
        IStructuredSelection ssel = (IStructuredSelection) selection;
        if (ssel.size() > 1) {
            return;
        }
        Object obj = ssel.getFirstElement();
        if (obj instanceof IFile) {
            try {
                ResourceAdaptorJarXML resourceAdaptorJarXML = new ResourceAdaptorJarXML((IFile) obj);
                ResourceAdaptorXML resourceAdaptors[] = resourceAdaptorJarXML.getResourceAdaptors();
                for (int i = 0; i < resourceAdaptors.length; i++) {
                    Menu child = new Menu(parent);
                    MenuItem item = new MenuItem(parent, SWT.CASCADE);
                    item.setText(resourceAdaptors[i].getName() + ", " + resourceAdaptors[i].getVersion() + ", " + resourceAdaptors[i].getVendor());
                    item.setMenu(child);
                    item = new MenuItem(child, SWT.NONE);
                    item.setText("Identity...");
                    item.addSelectionListener(new IdentitySelectionListener());
                    item = new MenuItem(child, SWT.NONE);
                    item.setText("Libraries...");
                    item.addSelectionListener(new LibrariesSelectionListener());
                    item = new MenuItem(child, SWT.NONE);
                    item.setText("Resource Adaptor Types...");
                    item.addSelectionListener(new RaTypesSelectionListener());
                    item = new MenuItem(child, SWT.NONE);
                    item.setText("Configuration Properties...");
                    item.addSelectionListener(new ActivityTypesSelectionListener());
                    item = new MenuItem(child, SWT.SEPARATOR);
                    item = new MenuItem(child, SWT.NONE);
                    item.setText("Delete");
                    item.addSelectionListener(new DeleteSelectionListener());
                }
            } catch (Exception e) {
                System.err.println("Exception caught creating menu: " + e.getMessage());
            }
        }
    }

    private class RaTypesSelectionListener extends SelectionAdapter {

        public void widgetSelected(SelectionEvent e) {
            MenuItem item = (MenuItem) e.getSource();
            Menu parent = item.getParent();
            MenuItem parentItem = parent.getParentItem();
            EditResourceAdaptorRaTypesAction action = new EditResourceAdaptorRaTypesAction(parentItem.getText());
            action.selectionChanged(null, selection);
            action.run(null);
        }
    }

    private class LibrariesSelectionListener extends SelectionAdapter {

        public void widgetSelected(SelectionEvent e) {
            MenuItem item = (MenuItem) e.getSource();
            Menu parent = item.getParent();
            MenuItem parentItem = parent.getParentItem();
            EditResourceAdaptorLibrariesAction action = new EditResourceAdaptorLibrariesAction(parentItem.getText());
            action.selectionChanged(null, selection);
            action.run(null);
        }
    }

    private class ActivityTypesSelectionListener extends SelectionAdapter {

        public void widgetSelected(SelectionEvent e) {
            MenuItem item = (MenuItem) e.getSource();
            Menu parent = item.getParent();
            MenuItem parentItem = parent.getParentItem();
            EditResourceAdaptorConfigPropertiesAction action = new EditResourceAdaptorConfigPropertiesAction(parentItem.getText());
            action.selectionChanged(null, selection);
            action.run(null);
        }
    }

    private class IdentitySelectionListener extends SelectionAdapter {

        public void widgetSelected(SelectionEvent e) {
            MenuItem item = (MenuItem) e.getSource();
            Menu parent = item.getParent();
            MenuItem parentItem = parent.getParentItem();
            EditResourceAdaptorIdentityAction action = new EditResourceAdaptorIdentityAction(parentItem.getText());
            action.selectionChanged(null, selection);
            action.run(null);
        }
    }

    private class DeleteSelectionListener extends SelectionAdapter {

        public void widgetSelected(SelectionEvent e) {
            MenuItem item = (MenuItem) e.getSource();
            Menu parent = item.getParent();
            MenuItem parentItem = parent.getParentItem();
            DeleteResourceAdaptorAction action = new DeleteResourceAdaptorAction(parentItem.getText());
            action.selectionChanged(null, selection);
            action.run(null);
        }
    }

    private IAction fDelegateAction;

    private ISelection selection;

    private boolean fFillMenu;
}
