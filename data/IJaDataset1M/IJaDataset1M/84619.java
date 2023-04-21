package horizons.gui;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public class HorizonsGuiMenu extends JMenuBar {

    private Vector<JMenu> m_menus;

    public HorizonsGuiMenu() {
        m_menus = new Vector<JMenu>();
        JMenu mFile = new JMenu("File");
        mFile.setMnemonic('F');
        JMenuItem item = null;
        item = new JMenuItem("Open target file");
        item.setMnemonic('O');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        item.setActionCommand(HorizonsGuiMediator.OPEN_FILE);
        mFile.add(item);
        add(mFile);
        mFile.addSeparator();
        item = new JMenuItem("Exit");
        item.setMnemonic('E');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        item.setActionCommand(HorizonsGuiMediator.EXIT);
        mFile.add(item);
        add(mFile);
        m_menus.add(mFile);
        JMenu mMode = new JMenu("Mode");
        mMode.setMnemonic('M');
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem testMode = new JRadioButtonMenuItem("PHS files");
        testMode.setSelected(true);
        testMode.setMnemonic('p');
        testMode.setActionCommand(HorizonsGuiMediator.PHS_MODE);
        group.add(testMode);
        mMode.add(testMode);
        JRadioButtonMenuItem commitMode = new JRadioButtonMenuItem("MPS files");
        commitMode.setSelected(false);
        commitMode.setMnemonic('m');
        commitMode.setActionCommand(HorizonsGuiMediator.MPS_MODE);
        group.add(commitMode);
        mMode.add(commitMode);
        add(mMode);
        m_menus.add(mMode);
        JMenu mSearch = new JMenu("Help");
        mSearch.setMnemonic('H');
        item = new JMenuItem("Instructions");
        item.setMnemonic('I');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        item.setActionCommand("instructions");
        mSearch.add(item);
        item = new JMenuItem("About");
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
