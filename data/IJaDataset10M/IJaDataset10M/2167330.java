package org.mobicents.eclipslee.servicecreation.popup.actions;

import java.io.InputStreamReader;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
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
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DeleteModuleSubmenu implements IObjectActionDelegate, IMenuCreator {

    public DeleteModuleSubmenu() {
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
        createModulesMenus(menu);
    }

    private void createModulesMenus(Menu parent) {
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
        if (obj instanceof IProject) {
            try {
                IProject project = (IProject) obj;
                IFile parentPom = project.getFile("pom.xml");
                MavenXpp3Reader reader = new MavenXpp3Reader();
                Model model = reader.read(new InputStreamReader(parentPom.getContents()));
                for (String module : model.getModules()) {
                    if (!module.equals("du")) {
                        MenuItem item = new MenuItem(parent, SWT.NONE);
                        item.setText(module);
                        item.addSelectionListener(new DeleteSelectionListener());
                    }
                }
            } catch (Exception e) {
                ServiceCreationPlugin.log("Exception caught creating menu: " + e.getMessage());
            }
        }
    }

    private class DeleteSelectionListener extends SelectionAdapter {

        public void widgetSelected(SelectionEvent e) {
            MenuItem item = (MenuItem) e.getSource();
            DeleteModuleAction action = new DeleteModuleAction(item.getText());
            action.selectionChanged(null, selection);
            action.run(null);
        }
    }

    private IAction fDelegateAction;

    private ISelection selection;

    private boolean fFillMenu;
}
