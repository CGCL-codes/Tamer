package com.gite.application;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import com.gite.core.LookAndFeelPlugins;
import com.gite.core.MySystemTray;
import com.gite.core.Utils;

public class GITeFrame extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 325371953206067899L;

    private JTabbedPane tabbedPane;

    GITeStart parent;

    public GITeFrame() {
    }

    public GITeFrame(GITeStart parent, String title) {
        this.parent = parent;
        drawOnIt(title);
        new MySystemTray(this);
    }

    private void drawOnIt(String title) {
        this.setTitle(title);
        final JSplitPane jSplitPane = new JSplitPane();
        jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setContinuousLayout(true);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setResizeWeight(1.0);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        addChat("Chat with []");
        tabbedPane.setTabComponentAt(0, new MyTabComponent(tabbedPane, null));
        jSplitPane.setLeftComponent(tabbedPane);
        this.setJMenuBar(drawMenu());
        JTree tree = new JTree();
        JScrollPane sp = new JScrollPane(tree);
        sp.setPreferredSize(new Dimension(200, 550));
        sp.setMaximumSize(new Dimension(35000, 550));
        sp.setMinimumSize(new Dimension(200, 550));
        jSplitPane.setRightComponent(sp);
        this.add(jSplitPane);
        jSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new SplitPaneListener(this, tabbedPane, sp));
    }

    private void addChat(String title) {
        JComponent panel = this.parent.getChatStart().init();
        tabbedPane.addTab(title, panel);
    }

    private JMenuBar drawMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(drawUtilsMenu());
        jMenuBar.add(drawLookAndFeelMenu());
        return jMenuBar;
    }

    private JMenu drawUtilsMenu() {
        JMenu jMenu = new JMenu();
        jMenu.setText("Utils");
        jMenu.setMnemonic(KeyEvent.VK_U);
        JMenuItem menuItem = new JMenuItem();
        menuItem.setText("Transport status");
        menuItem.addActionListener(new TransportStatusAction(tabbedPane));
        jMenu.add(menuItem);
        return jMenu;
    }

    private JMenu drawLookAndFeelMenu() {
        JMenu jMenu = new JMenu();
        jMenu.setText("Look and Feels");
        jMenu.setMnemonic(KeyEvent.VK_L);
        Set<String> looks = Utils.getAllLookAndFeels();
        ButtonGroup looksGroup = new ButtonGroup();
        for (String str : looks) {
            JMenu menu = new JMenu();
            menu.setText(str);
            jMenu.add(menu);
            LookAndFeelPlugins lfp = (LookAndFeelPlugins) Utils.getLookAndFeelClass(str);
            Set<String> values = lfp.getAvailableValues();
            for (String val : values) {
                JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(val);
                menu.add(menuItem);
                looksGroup.add(menuItem);
                menuItem.addActionListener(new LookAndFeelAction(this, str, val));
            }
        }
        return jMenu;
    }
}

class SplitPaneListener implements PropertyChangeListener {

    JFrame frame;

    JTabbedPane tabbedPane;

    JScrollPane sp;

    Dimension tp;

    Dimension tpMin;

    Point sl;

    SplitPaneListener(JFrame frame, JTabbedPane tabbedPane, JScrollPane sp) {
        this.frame = frame;
        this.tabbedPane = tabbedPane;
        this.sp = sp;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        int nv = ((Integer) evt.getNewValue()).intValue();
        int ov = ((Integer) evt.getOldValue()).intValue();
        sl = frame.getLocationOnScreen();
        if (nv <= 1) {
            tpMin = tabbedPane.getMinimumSize();
            tp = tabbedPane.getSize();
            sp.setPreferredSize(new Dimension(sp.getSize().width, tp.height));
            frame.setLocation(sl.x + tp.width, sl.y);
            tabbedPane.setPreferredSize(new Dimension(0, 0));
            tabbedPane.setMinimumSize(new Dimension(1, 1));
            frame.pack();
        } else if (ov == 1) {
            frame.setLocation(sl.x - tp.width, sl.y);
            tabbedPane.setPreferredSize(tp);
            tabbedPane.setMinimumSize(tpMin);
            sp.setPreferredSize(sp.getSize());
            frame.pack();
        }
    }
}

class LookAndFeelAction implements ActionListener {

    JFrame parent;

    String classString;

    String lookString;

    LookAndFeelAction(JFrame parent, String classString, String lookString) {
        this.parent = parent;
        this.classString = classString;
        this.lookString = lookString;
    }

    public void actionPerformed(ActionEvent arg0) {
        setLookAndFeel(classString, lookString);
    }

    private void setLookAndFeel(String classStr, String lookStr) {
        Utils.getLookAndFeelClass(classStr).setLookAndFeel(lookStr);
        SwingUtilities.updateComponentTreeUI(parent);
    }

    public static void setLookAndFeel(String classStr, String lookStr, JFrame par) {
        Utils.getLookAndFeelClass(classStr).setLookAndFeel(lookStr);
        SwingUtilities.updateComponentTreeUI(par);
    }
}
