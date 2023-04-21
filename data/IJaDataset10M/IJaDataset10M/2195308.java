package editor.view.menu;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import base.gui.MenuHelper;
import editor.util.Resources;
import editor.view.action.ActionFactory;

/**
 * Class shows a menu for the different views to choice of
 */
public final class ViewMenu extends JMenu {

    /**
     * Constructor
     *
     * @param factory the ActionFactory
     */
    public ViewMenu(final ActionFactory factory) {
        super(Resources.getText("menu.view"));
        setMnemonic(MenuHelper.getMnemonicFromString(Resources.getText("menu.view.mnemonic")));
        final ButtonGroup group = new ButtonGroup();
        addEntry(group, factory.getLoadTabViewAction());
        addEntry(group, factory.getLoadTextViewAction());
        addEntry(group, factory.getLoadTabView2Action());
    }

    private final void addEntry(final ButtonGroup group, final AbstractAction action) {
        final JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
        group.add(item);
        add(item);
    }
}
