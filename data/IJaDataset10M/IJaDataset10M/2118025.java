package nzdis.agent.amc;

import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.*;
import nzdis.agent.Constants;
import nzdis.util.config.ConfigurationGUI;

/**
 * Title:        Agent Management Console
 * Description:  GUI for controlling/ monitoring an OPAL agent platform instance.
 * Start/Stop agents etc.
 * Copyright:    Copyright (c) 2000
 * Company:      Universtiy of Otago
 *@author dcarter
 *@version 1.0
 */
public class AMCFrame extends JInternalFrame {

    AMC amcAgent;

    String pluginsToLoad;

    Plugin[] plugins;

    PluginVisuals[] pluginVisuals;

    JPanel contentPane;

    JMenuBar jMenuBar1 = new JMenuBar();

    JMenu jMenuFile = new JMenu();

    JMenuItem jMenuFileExit = new JMenuItem();

    JMenu jMenuHelp = new JMenu();

    JMenuItem jMenuHelpAbout = new JMenuItem();

    JToolBar jToolBar = new JToolBar();

    JButton jButton1 = new JButton();

    JButton jButton2 = new JButton();

    JButton jButton3 = new JButton();

    ImageIcon image1;

    ImageIcon image2;

    ImageIcon image3;

    JLabel statusBar = new JLabel();

    JTabbedPane pluginsTabbedPanel = new JTabbedPane();

    JMenuItem jMenuConfigPlatform = new JMenuItem();

    JMenuItem jMenuSendMessage = new JMenuItem();

    BorderLayout borderLayout1 = new BorderLayout();

    /**Construct the frame*/
    public AMCFrame(AMC a) {
        super("Opal Agent Management Console", true, true, true, true);
        amcAgent = a;
        pluginsInit();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int menuInsertPos = 1;
        for (int i = 0; i < pluginVisuals.length; i++) {
            if (pluginVisuals[i] != null) {
                pluginsTabbedPanel.add(pluginVisuals[i].displayComponent, plugins[i].getShortName());
                if (pluginVisuals[i].menu != null) jMenuBar1.add(pluginVisuals[i].menu, menuInsertPos++);
            }
        }
    }

    private void pluginsInit() {
        pluginsToLoad = this.amcAgent.getConfiguration().getString(Constants.AMC_PLUGINS);
        Vector pluginNames = new Vector();
        System.out.println(pluginsToLoad);
        StringTokenizer st = new StringTokenizer(pluginsToLoad, ",");
        while (st.hasMoreTokens()) {
            pluginNames.add(st.nextToken());
        }
        plugins = new Plugin[pluginNames.size()];
        pluginVisuals = new PluginVisuals[pluginNames.size()];
        for (int i = 0; i < pluginNames.size(); i++) {
            try {
                Class pc = Class.forName(pluginNames.get(i).toString());
                plugins[i] = (Plugin) pc.newInstance();
                pluginVisuals[i] = plugins[i].initPlugin(amcAgent);
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Error loading plugin: " + pluginNames.get(i));
                e.printStackTrace();
            }
        }
    }

    /**Component initialization*/
    private void jbInit() throws Exception {
        image1 = new ImageIcon(AMCFrame.class.getResource("openFile.gif"));
        image2 = new ImageIcon(AMCFrame.class.getResource("closeFile.gif"));
        image3 = new ImageIcon(AMCFrame.class.getResource("help.gif"));
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setSize(new Dimension(600, 500));
        statusBar.setText(" ");
        jMenuFile.setText("AMC");
        jMenuFile.setDelay(0);
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new AMCFrame_jMenuFileExit_ActionAdapter(this));
        jMenuHelp.setText("Help");
        jMenuHelp.setDelay(0);
        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new AMCFrame_jMenuHelpAbout_ActionAdapter(this));
        jButton1.setIcon(image1);
        jButton1.setToolTipText("Open File");
        jButton2.setIcon(image2);
        jButton2.setToolTipText("Close File");
        jButton3.setIcon(image3);
        jButton3.setToolTipText("Help");
        jMenuConfigPlatform.setText("Platform Options");
        jMenuConfigPlatform.addActionListener(new AMCFrame_jMenuConfigPlatform_actionAdapter(this));
        jMenuSendMessage.setText("Send FIPA Message");
        jMenuSendMessage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final MessageForm f = new MessageForm(amcAgent.getPlatform());
                amcAgent.desktop.add(f);
                f.setVisible(true);
            }
        });
        jToolBar.add(jButton1);
        jToolBar.add(jButton2);
        jToolBar.add(jButton3);
        jMenuFile.add(jMenuConfigPlatform);
        jMenuFile.add(jMenuSendMessage);
        jMenuFile.addSeparator();
        jMenuFile.add(jMenuFileExit);
        jMenuHelp.add(jMenuHelpAbout);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenuHelp);
        this.setJMenuBar(jMenuBar1);
        contentPane.add(statusBar, BorderLayout.SOUTH);
        contentPane.add(pluginsTabbedPanel, BorderLayout.CENTER);
        contentPane.add(jToolBar, BorderLayout.NORTH);
    }

    /** File | Exit action performed. */
    public void jMenuFileExit_actionPerformed(ActionEvent e) {
        System.out.println("Closing up the AMC gui. The Platform is still running.");
        amcAgent.deactivate();
        setVisible(false);
    }

    /** Help | About action performed. */
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
        AMC_AboutBox dlg = AMC_AboutBox.getInstance();
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        this.amcAgent.desktop.remove(dlg);
        this.amcAgent.desktop.add(dlg);
        dlg.setVisible(true);
    }

    void jMenuConfigPlatform_actionPerformed(ActionEvent e) {
        ConfigurationGUI c = new ConfigurationGUI(this.amcAgent.getPlatform().getConfiguration(), "platform.xml");
        JInternalFrame f = c.getInternalFrame();
        amcAgent.desktop.remove(f);
        amcAgent.desktop.add(f);
        f.setVisible(true);
    }

    public void showCentre() {
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setVisible(true);
    }
}

class AMCFrame_jMenuFileExit_ActionAdapter implements ActionListener {

    AMCFrame adaptee;

    AMCFrame_jMenuFileExit_ActionAdapter(AMCFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jMenuFileExit_actionPerformed(e);
    }
}

class AMCFrame_jMenuHelpAbout_ActionAdapter implements ActionListener {

    AMCFrame adaptee;

    AMCFrame_jMenuHelpAbout_ActionAdapter(AMCFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jMenuHelpAbout_actionPerformed(e);
    }
}

class AMCFrame_jMenuConfigPlatform_actionAdapter implements java.awt.event.ActionListener {

    AMCFrame adaptee;

    AMCFrame_jMenuConfigPlatform_actionAdapter(AMCFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jMenuConfigPlatform_actionPerformed(e);
    }
}
