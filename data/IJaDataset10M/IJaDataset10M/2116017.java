package cx.ath.contribs.klex.forester.actions;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;
import cx.ath.contribs.klex.forester.ForesterPlugin;
import cx.ath.contribs.klex.forester.model.DbConnection;

public class DatabaseChooseMenue implements IWorkbenchWindowPulldownDelegate2 {

    private Menu fMenu;

    protected boolean recreateMenu = false;

    public DatabaseChooseMenue() {
        super();
    }

    public void dispose() {
        setMenu(null);
    }

    private void setMenu(Menu menu) {
        if (fMenu != null) {
            fMenu.dispose();
        }
        fMenu = menu;
    }

    public void init(IWorkbenchWindow window) {
    }

    protected void addToMenu(Menu menu, IAction action, int accelerator) {
        StringBuffer label = new StringBuffer();
        if (accelerator >= 0 && accelerator < 10) {
            label.append('&');
            label.append(accelerator);
            label.append(' ');
        }
        label.append(action.getText());
        action.setText(label.toString());
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(menu, -1);
    }

    private void initMenu() {
        final DatabaseChooseMenue dbChoose = this;
        fMenu.addMenuListener(new MenuAdapter() {

            public void menuShown(MenuEvent e) {
                Menu m = (Menu) e.widget;
                MenuItem[] items = m.getItems();
                for (int i = 0; i < items.length; i++) {
                    items[i].dispose();
                }
                for (DbConnection db : ForesterPlugin.getInstance().getDbConnectionGroup()) {
                    ActionContributionItem item = new ActionContributionItem(new DatabaseConnectAction(db, dbChoose));
                    item.fill(fMenu, -1);
                }
                recreateMenu = false;
            }
        });
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void run(IAction action) {
    }

    public Menu getMenu(Menu parent) {
        setMenu(new Menu(parent));
        initMenu();
        return fMenu;
    }

    public Menu getMenu(Control parent) {
        setMenu(new Menu(parent));
        initMenu();
        return fMenu;
    }
}
