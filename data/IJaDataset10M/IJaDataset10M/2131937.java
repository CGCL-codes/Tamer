package com.safi.workshop.sqlexplorer.sessiontree.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import com.safi.workshop.sqlexplorer.Messages;
import com.safi.workshop.sqlexplorer.dbproduct.Alias;
import com.safi.workshop.sqlexplorer.dbproduct.User;
import com.safi.workshop.sqlexplorer.plugin.SQLExplorerPlugin;
import com.safi.workshop.sqlexplorer.util.ImageUtil;

/**
 * @author Aadi
 * 
 */
public class NewConnectionDropDownAction extends Action implements IMenuCreator, IViewActionDelegate {

    private Menu menu;

    public NewConnectionDropDownAction() {
        setText(Messages.getString("ConnectionsView.Actions.NewConnection"));
        setToolTipText(Messages.getString("ConnectionsView.Actions.NewConnectionToolTip"));
        setImageDescriptor(ImageUtil.getDescriptor("Images.NewConnectionIcon"));
        setMenuCreator(this);
    }

    public void dispose() {
        if (menu != null) {
            menu.dispose();
        }
    }

    public Menu getMenu(Control parent) {
        if (menu != null) {
            menu.dispose();
            menu = null;
        }
        for (Alias alias : SQLExplorerPlugin.getDefault().getAliasManager().getAliases()) for (User user : alias.getUsers()) {
            if (menu == null) menu = new Menu(parent);
            NewConnection action = new NewConnection(user);
            addActionToMenu(menu, action);
        }
        return menu;
    }

    public Menu getMenu(Menu parent) {
        return null;
    }

    protected void addActionToMenu(Menu parent, Action action) {
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(parent, -1);
    }

    public void init(IViewPart view) {
    }

    public void run(IAction action) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
