package jnocatan;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jnocatan.dialog.*;
import jnocatan.panel.*;
import java.util.logging.*;

/**
 * JnoCatan is the main class for the application
 *
 * @author  Don Seiler <don@NOSPAM.seiler.us>
 * @version $Id: JnoCatan.java,v 1.30 2004/11/06 04:14:58 rizzo Exp $
 * @since   0.1.0
 */
public class JnoCatan implements ActionListener {

    JnoCatanStateMachine sm = new JnoCatanStateMachine();

    java.util.Properties properties;

    private static Logger logger = Logger.getLogger("jnocatan.JnoCatan");

    JFrame frame;

    JPanel desktop;

    JnoCatanToolBar toolBar;

    JnoCatanMenuBar menuBar;

    /**
     * Create the GUI and show it.
     */
    private void createAndShowGUI() {
        frame = new JFrame("jnocatan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        menuBar = new JnoCatanMenuBar();
        frame.setJMenuBar(menuBar);
        menuBar.connectMenuItem.addActionListener(this);
        menuBar.playernameMenuItem.addActionListener(this);
        menuBar.settingsMenuItem.addActionListener(this);
        menuBar.quitMenuItem.addActionListener(this);
        menuBar.legendMenuItem.addActionListener(this);
        menuBar.serversettingsMenuItem.addActionListener(this);
        menuBar.histogramMenuItem.addActionListener(this);
        menuBar.aboutMenuItem.addActionListener(this);
        toolBar = new JnoCatanToolBar();
        toolBar.rollButton.addActionListener(this);
        toolBar.tradeButton.addActionListener(this);
        toolBar.undoButton.addActionListener(this);
        toolBar.finishButton.addActionListener(this);
        toolBar.roadButton.addActionListener(this);
        toolBar.settlementButton.addActionListener(this);
        toolBar.cityButton.addActionListener(this);
        toolBar.developButton.addActionListener(this);
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JnoCatanMaterialsPanel materialsPanel = new JnoCatanMaterialsPanel(sm);
        JnoCatanResourcesPanel resourcesPanel = new JnoCatanResourcesPanel(sm);
        JnoCatanDevCardPanel devcardPanel = new JnoCatanDevCardPanel(sm);
        JnoCatanPlayerPanel playerPanel = new JnoCatanPlayerPanel(sm);
        leftPanel.add(materialsPanel);
        leftPanel.add(resourcesPanel);
        leftPanel.add(devcardPanel);
        leftPanel.add(playerPanel);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JnoCatanMainPanel mainPanel = new JnoCatanMainPanel(sm);
        JnoCatanChatPanel chatPanel = new JnoCatanChatPanel(sm);
        JnoCatanMessagePanel messagePanel = new JnoCatanMessagePanel(sm);
        rightPanel.add(mainPanel);
        rightPanel.add(chatPanel);
        rightPanel.add(messagePanel);
        desktop = new JPanel(new BorderLayout());
        desktop.setOpaque(true);
        desktop.add(toolBar, BorderLayout.NORTH);
        desktop.add(leftPanel, BorderLayout.LINE_START);
        desktop.add(rightPanel, BorderLayout.LINE_END);
        frame.setContentPane(desktop);
        frame.pack();
        frame.setVisible(true);
        new JnoCatanConnectDialog(frame, sm, properties);
    }

    /**
     * Handles change events and takes appropriate action
     *
     * This is the central nervous system of jnocatan.  It will be the listener
     * for virtually every object in the application and take the action it
     * needs to.
     *
     * @params  ActionEvent event
     * @access  public
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String description = null;
        if (JnoCatanToolBar.ROLL.equals(cmd)) {
            System.out.println("Roll button clicked");
        } else if (JnoCatanToolBar.TRADE.equals(cmd)) {
            System.out.println("Trade button clicked");
        } else if (JnoCatanToolBar.UNDO.equals(cmd)) {
            System.out.println("Undo button clicked");
        } else if (JnoCatanToolBar.FINISH.equals(cmd)) {
            System.out.println("Finish button clicked");
        } else if (JnoCatanToolBar.ROAD.equals(cmd)) {
            System.out.println("Road button clicked");
        } else if (JnoCatanToolBar.SETTLEMENT.equals(cmd)) {
            System.out.println("Settlement button clicked");
        } else if (JnoCatanToolBar.CITY.equals(cmd)) {
            System.out.println("City button clicked");
        } else if (JnoCatanToolBar.DEVELOPMENT.equals(cmd)) {
            System.out.println("Development button clicked");
        } else if (JnoCatanMenuBar.CONNECT.equals(cmd)) {
            System.out.println("Connect seleced from menu");
            new JnoCatanConnectDialog(frame, sm, properties);
        } else if (JnoCatanMenuBar.PLAYERNAME.equals(cmd)) {
            System.out.println("Player Name seleced from menu");
        } else if (JnoCatanMenuBar.SETTINGS.equals(cmd)) {
            new JnoCatanSettingsDialog(frame, sm, properties);
            System.out.println("Settings seleced from menu");
        } else if (JnoCatanMenuBar.QUIT.equals(cmd)) {
            System.out.println("Quit seleced from menu");
            System.exit(0);
        } else if (JnoCatanMenuBar.LEGEND.equals(cmd)) {
            System.out.println("Legend seleced from menu");
        } else if (JnoCatanMenuBar.SERVERSETTINGS.equals(cmd)) {
            System.out.println("Game Settings seleced from menu");
        } else if (JnoCatanMenuBar.HISTOGRAM.equals(cmd)) {
            System.out.println("Dice Histogram seleced from menu");
        } else if (JnoCatanMenuBar.ABOUT.equals(cmd)) {
            System.out.println("About seleced from menu");
            new JnoCatanAboutDialog(frame);
        }
    }

    /**
     * Main
     *
     * @access public
     */
    public static void main(String[] args) {
        JnoCatan jnocatan = new JnoCatan();
        jnocatan.go();
    }

    /**
     * Starts the application
     *
     * @access private
     */
    private void go() {
        String userHome = System.getProperty("user.home");
        properties = new java.util.Properties();
        try {
            properties.load(new FileInputStream(userHome + "/.jnocatanrc"));
        } catch (FileNotFoundException e) {
            System.out.println("*** TODO *** Creating " + userHome + "/.jnocatanrc");
        } catch (IOException e) {
            System.err.println("Unable to load properties file:\n\nIOException: " + e.getMessage());
            e.printStackTrace();
        }
        if (properties.getProperty("loglevel") == null) properties.setProperty("loglevel", "WARNING");
        logger.setLevel(Level.parse(properties.getProperty("loglevel")));
        logger.info("Testing");
        logger.severe("Foo");
        if (properties.getProperty("playername") == null) properties.setProperty("playername", "JnoCatan Newbie");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
