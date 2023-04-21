package com.googlecode.mibible.browser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

public class BrowserFrame extends JFrame {

    /** デフォルトのInsets */
    public static final Insets DEFAULT_INSETS = new Insets(2, 5, 2, 5);

    /** Mediator */
    private Mediator mediator = new Mediator();

    /** File Menu */
    private JMenuBar menuBar = new JMenuBar();

    /**
     * Frameの初期表示を行う。
     */
    public void initialize() {
        this.mediator.setBrowserFrame(this);
        Rectangle bounds = new Rectangle();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        bounds.width = (int) (size.width * 0.8);
        bounds.height = (int) (size.height * 0.8);
        bounds.x = (size.width - bounds.width) / 2;
        bounds.y = (size.height - bounds.height) / 2;
        setBounds(bounds);
        setTitle("mibible browser");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                BrowserFrame.this.mediator.exit();
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
        });
        this.menuBar = getMenu();
        setJMenuBar(this.menuBar);
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        JSplitPane leftRightSplitPane = new JSplitPane();
        leftRightSplitPane.setDividerLocation((int) (bounds.width * 0.40));
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0d;
        gbc.weighty = 1.0d;
        gbc.fill = GridBagConstraints.BOTH;
        getContentPane().add(leftRightSplitPane, gbc);
        JSplitPane topBottomSplitPane = new JSplitPane();
        topBottomSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        topBottomSplitPane.setDividerLocation((int) (bounds.height));
        topBottomSplitPane.setOneTouchExpandable(true);
        topBottomSplitPane.setLeftComponent(getDescriptionPanel(mediator));
        topBottomSplitPane.setRightComponent(getCommunicationPanel(mediator));
        leftRightSplitPane.setLeftComponent(getTreePanel(mediator));
        leftRightSplitPane.setRightComponent(topBottomSplitPane);
        JTextField statusField = new JTextField();
        statusField.setEditable(false);
        statusField.setBorder(new EmptyBorder(new Insets(2, 5, 2, 5)));
        statusField.setBackground(getBackground());
        gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 5, 2, 5);
        getContentPane().add(statusField, gbc);
        this.mediator.setStatusField(statusField);
    }

    /**
     * メニューバーを作成する。
     * @return メニューバー
     */
    private JMenuBar getMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getFileMenu());
        return menuBar;
    }

    /**
     * メニューの「File」部分を作成する。
     * @return Fileメニュー
     */
    private JMenu getFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        this.updateFileMenu(menu);
        return menu;
    }

    private void updateFileMenu(JMenu menu) {
        JMenuItem open = new JMenuItem("Open MIB...");
        open.setMnemonic('O');
        open.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Properties prop = BrowserFrame.this.mediator.getProperties();
                String openDirectory = prop.getProperty(Mediator.FILE_CHOOSER_DIRECTORY, ".");
                JFileChooser dialog = new JFileChooser(new File(openDirectory));
                File[] files;
                int result;
                dialog.setMultiSelectionEnabled(true);
                result = dialog.showOpenDialog(BrowserFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    files = dialog.getSelectedFiles();
                    BrowserFrame.this.mediator.openMib(files);
                    if (files.length > 0) {
                        openDirectory = files[0].getParent();
                        prop.setProperty(Mediator.FILE_CHOOSER_DIRECTORY, openDirectory);
                    }
                }
            }
        });
        menu.add(open);
        JMenuItem close = new JMenuItem("Close MIB");
        close.setMnemonic('C');
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BrowserFrame.this.mediator.unloadMib();
            }
        });
        menu.add(close);
        menu.addSeparator();
        Properties prop = BrowserFrame.this.mediator.getProperties();
        String historyStr = prop.getProperty("mibbrowser.history", "0");
        int history = Integer.valueOf(historyStr);
        boolean displayHistory = false;
        for (int index = 1; index <= history; index++) {
            String fileName = prop.getProperty("mibbrowser.history." + index, "");
            if (fileName.equals("")) {
                continue;
            }
            File file = new File(fileName);
            String historyMenuStr;
            try {
                historyMenuStr = index + " " + file.getName() + " [" + file.getCanonicalPath() + "]";
                JMenuItem historyMenu = new JMenuItem(historyMenuStr);
                if (index < 10) {
                    historyMenu.setMnemonic(String.valueOf(index).charAt(0));
                }
                menu.add(historyMenu);
                displayHistory = true;
                historyMenu.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        String fileName = e.getActionCommand();
                        String[] tmp = fileName.split("\\[");
                        if (tmp.length > 0) {
                            String[] tmp2 = tmp[1].split("\\]");
                            BrowserFrame.this.mediator.openMib(new File(tmp2[0]));
                        }
                    }
                });
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (displayHistory) {
            menu.addSeparator();
        }
        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BrowserFrame.this.mediator.exit();
            }
        });
        menu.add(exit);
    }

    public void updateHistoryMenu() {
        JMenu menu = this.menuBar.getMenu(0);
        menu.removeAll();
        this.updateFileMenu(menu);
    }

    /**
     * メニューの「Help」部分を作成する。
     * @return Helpメニュー
     */
    private JMenu getHelpMenu() {
        JMenu menu = new JMenu("Help");
        menu.setMnemonic('H');
        JMenuItem about = new JMenuItem("About mibible");
        about.setMnemonic('A');
        about.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(BrowserFrame.this, "sorry. under construction.");
            }
        });
        menu.add(about);
        return menu;
    }

    /**
     * Mediatorと関連付けを行ったTreePanelを作成する。
     * @param mediator
     * @return　作成したTreePanel
     */
    private JPanel getTreePanel(Mediator mediator) {
        TreePanel panel = new TreePanel(mediator);
        panel.initialize();
        return panel;
    }

    /**
     * Mediatorと関連付けを行ったDescriptionPanelを作成する。
     * @param mediator
     * @return　作成したDescriptionPanel
     */
    private JPanel getDescriptionPanel(Mediator mediator) {
        DescriptionPanel panel = new DescriptionPanel(mediator);
        panel.initialize();
        return panel;
    }

    /**
     * Mediatorと関連付けを行ったCommunicationPanelを作成する。
     * @param mediator
     * @return　作成したCommunicationPanel
     */
    private JPanel getCommunicationPanel(Mediator mediator) {
        CommunicationPanel panel = new CommunicationPanel(mediator);
        panel.initialize();
        return panel;
    }
}
