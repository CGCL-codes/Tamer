package edu.gsbme.gyoza2d.visual.Menu;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;

public class rootMenu {

    public static JPopupMenu create(ActionListener[] actions) {
        final JPopupMenu menu = new JPopupMenu();
        JMenuItem add = new JMenuItem("Add");
        add.addActionListener(actions[0]);
        menu.add(add);
        JMenuItem edit = new JMenuItem("Edit");
        edit.addActionListener(actions[1]);
        menu.add(edit);
        JMenuItem over = new JMenuItem("Overview");
        over.addActionListener(actions[2]);
        menu.add(over);
        JMenuItem del = new JMenuItem("Delete");
        del.addActionListener(actions[3]);
        menu.add(del);
        JMenuItem focus = new JMenuItem("Focus");
        focus.addActionListener(actions[4]);
        menu.add(focus);
        return menu;
    }

    public static Menu create(Shell shell, Element element, Listener[] actions) {
        Menu menu = new Menu(shell, SWT.POP_UP);
        MenuItem add = new MenuItem(menu, SWT.PUSH);
        add.setText("Add");
        MenuItem Overview = new MenuItem(menu, SWT.PUSH);
        Overview.setText("Overview");
        MenuItem edit = new MenuItem(menu, SWT.PUSH);
        edit.setText("Edit");
        MenuItem delete = new MenuItem(menu, SWT.PUSH);
        delete.setText("Delete");
        MenuItem focus = new MenuItem(menu, SWT.PUSH);
        focus.setText("Focus");
        return menu;
    }
}
