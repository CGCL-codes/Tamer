package com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.swixml.SwingEngine;
import com.dhcc.gui.BasicDHCCPanel;
import com.dhcc.gui.DHCCSplash;
import com.dhcc.gui.StatusBar;
import com.dhcc.utils.gui.DHCCToolBar;
import com.dhcc.utils.gui.IconManager;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

public class DHCCPanel extends BasicDHCCPanel {

    private static final long serialVersionUID = 2038255280024139693L;

    /** Titel der Applikation */
    private static final String DHCC_NAME = "Dark Heresy Character Creator";

    private static final JFrame mainFrame = new JFrame(DHCC_NAME);

    public static void main(String[] args) {
        SwingEngine.setAppFrame(mainFrame);
        initGUI();
    }

    private static void initGUI() {
        SwingEngine swix = new SwingEngine();
        swix.getTaglib().registerTag("dhcctoolbar", DHCCToolBar.class);
        new DHCCSplash(mainFrame, true);
        DHCCPanel mainPanel = new DHCCPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(208, 208, 192));
        mainPanel.add(mainPanel.getContentPane(), BorderLayout.CENTER);
        mainPanel.add(mainPanel.getMainToolBar(), BorderLayout.NORTH);
        mainPanel.add(mainPanel.getCharTreeViewPane(), BorderLayout.WEST);
        mainPanel.add(new StatusBar(), BorderLayout.SOUTH);
        mainPanel.add(mainPanel.getHelpLabel(), BorderLayout.EAST);
        mainFrame.setJMenuBar(mainPanel.getMenuBar());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(mainPanel);
        mainFrame.setIconImage(IconManager.getInstance(BasicDHCCPanel.class, "/com/dhcc/gui/dhcc_iconset_black_24").getImage("window-icon"));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int mainFrameWidth = screenSize.width < 1024 ? screenSize.width : 1024;
        int mainFrameHeight = screenSize.height < 768 ? screenSize.height : 700;
        mainFrame.setMinimumSize(new Dimension(800, 600));
        mainFrame.setPreferredSize(new Dimension(mainFrameWidth, mainFrameHeight));
        mainFrame.setSize(mainFrameWidth, mainFrameHeight);
        mainFrame.validate();
        Dimension size = mainFrame.getSize();
        mainFrame.setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2);
        mainFrame.pack();
        mainFrame.setVisible(true);
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
        }
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }
}
