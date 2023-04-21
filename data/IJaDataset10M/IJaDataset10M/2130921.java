package sk.bur.viliam.notilo.view.tool;

import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBuilder {

    ActionListener _listener;

    JFrame _frame;

    JMenuBar _menuBar;

    JMenu _menu;

    public MenuBuilder(JFrame frame) {
        _frame = frame;
    }

    public MenuBuilder(JFrame frame, ActionListener actionListener) {
        _frame = frame;
        _listener = actionListener;
    }

    public JMenuBar addBar() {
        _menuBar = new JMenuBar();
        _frame.setJMenuBar(_menuBar);
        return _menuBar;
    }

    public JMenuItem addItem(AppAction action) {
        JMenuItem item = new JMenuItem(action.getAction());
        _menu.add(item);
        return item;
    }

    public void addItemSeparator() {
        _menu.addSeparator();
    }

    public JMenu addMenu(String title, int key) {
        _menu = new JMenu(title);
        _menu.setMnemonic(key);
        _menuBar.add(_menu);
        return _menu;
    }

    public void addMenuSeparator() {
        _menuBar.add(Box.createHorizontalGlue());
    }
}
