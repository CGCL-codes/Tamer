package com.onetwork.core.ui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

public abstract class ONetworkFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    protected JToolBar cadastrosToolBar;

    protected JMenuBar mainMenuBar;

    protected JTabbedPane mainTabbedPane;

    public ONetworkFrame(String title) {
        super(title);
        this.initComponents();
        this.configure();
        this.configureMenuBar();
        this.configureToolBar();
        this.configureMainWindow();
    }

    protected abstract void configure();

    protected abstract void configureMenuBar();

    protected abstract void configureToolBar();

    protected abstract void configureMainWindow();

    protected void initComponents() {
        cadastrosToolBar = new JToolBar();
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainMenuBar = new JMenuBar();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        cadastrosToolBar.setFloatable(false);
        cadastrosToolBar.setRollover(true);
        getContentPane().add(cadastrosToolBar, BorderLayout.PAGE_START);
        getContentPane().add(mainTabbedPane, BorderLayout.CENTER);
        setJMenuBar(mainMenuBar);
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 1000) / 2, (screenSize.height - 700) / 2, 1000, 700);
    }
}
