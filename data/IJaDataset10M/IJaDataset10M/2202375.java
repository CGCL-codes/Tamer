package siouxsie.desktop.tutorial.ui.beta;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import siouxsie.desktop.core.ActionRegistry;
import siouxsie.desktop.core.Application;
import siouxsie.desktop.core.IActionManagerFactory;
import siouxsie.desktop.core.IMenuManager;
import siouxsie.desktop.core.IToolBarManager;
import siouxsie.desktop.core.impl.action.AbstractDesktopAction;
import siouxsie.desktop.core.impl.action.MenuManager;
import siouxsie.desktop.core.impl.action.ToolBarManager;

public class BetaApplication extends Application {

    private JPanel panel;

    @Override
    public IActionManagerFactory getActionManagerFactory() {
        return null;
    }

    public BetaApplication() {
        this.panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Beta application area"));
    }

    @Override
    public JComponent getApplicationAreaComponent() {
        return panel;
    }

    @Override
    public String getName() {
        return "Beta";
    }

    @Override
    public String getCategoryId() {
        return "application.category.systeme";
    }

    @Override
    public void selected() {
    }

    @Override
    public void createActions(ActionRegistry actionRegistry) {
    }

    @Override
    public void fillMenuBar(IMenuManager menuManager) {
        MenuManager currentMenuManager = (MenuManager) menuManager;
        String[] menus = new String[] { "Style", "Aide" };
        for (String menu : menus) {
            MenuManager innerMenu = currentMenuManager.addMenu(menu);
            for (int i = 0; i < 5; i++) {
                innerMenu.addAction(new DummyAction(menu + " #" + i));
            }
        }
    }

    @Override
    public void fillToolBar(IToolBarManager toolbarManager) {
        for (int i = 0; i < 2; i++) {
            ((ToolBarManager) toolbarManager).addAction(new DummyAction("Beta #" + i));
        }
    }

    class DummyAction extends AbstractDesktopAction {

        public DummyAction(String name) {
            putValue(Action.NAME, name);
        }

        public void actionPerformed(ActionEvent arg0) {
        }

        public String getId() {
            return null;
        }
    }

    @Override
    public String[] getRoles() {
        return null;
    }
}
