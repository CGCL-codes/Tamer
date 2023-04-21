package xda.gui;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Menu extends JMenuBar {

    private Vector<JMenu> m_menus;

    public Menu() {
        m_menus = new Vector<JMenu>();
        JMenu mFile = new JMenu("File");
        mFile.setMnemonic('F');
        JMenuItem item = new JMenuItem("Exit");
        item.setMnemonic('X');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        item.setActionCommand("exit");
        mFile.add(item);
        add(mFile);
        m_menus.add(mFile);
        JMenu mProcess = new JMenu("Process");
        mProcess.setMnemonic('P');
        item = new JMenuItem("Transform");
        item.setMnemonic('T');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        item.setActionCommand("transform");
        mProcess.add(item);
        add(mProcess);
        m_menus.add(mProcess);
        JMenu mSearch = new JMenu("Help");
        mSearch.setMnemonic('H');
        item = new JMenuItem("About Xml Double Agent");
        item.setMnemonic('a');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        item.setActionCommand("about");
        mSearch.add(item);
        add(mSearch);
        m_menus.add(mSearch);
    }

    public void setActionListener(ActionListener l) {
        for (JMenu menu : m_menus) {
            for (int i = 0; i < menu.getItemCount(); i++) {
                JMenuItem item = menu.getItem(i);
                if (item != null) {
                    item.addActionListener(l);
                }
            }
        }
    }
}
