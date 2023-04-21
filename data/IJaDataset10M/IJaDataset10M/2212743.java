package universe.client.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import universe.common.*;
import universe.common.gui.*;
import universe.client.*;
import universe.server.UniverseServerRequestTurnResults;
import universe.server.UniverseServerReplyTurnResults;

/**
 *  This is the main interface to the client's functionality.
 *
 * @author Sean Starkey
 * @version $Id: PlayerMain.java,v 1.7 2002/05/13 23:50:28 brianrater Exp $
 */
public class PlayerMain extends JFrame {

    private CommandBar commandbar;

    private MainMenu menu;

    private JPanel mainPanel;

    private JPanel infoPanel;

    private JLabel turnLabel;

    private JLabel viewerLabel;

    PlayerMain(String login, String password) {
        UniverseServerRequestTurnResults usrtr = new UniverseServerRequestTurnResults(login, password);
        UniverseServerReplyTurnResults reply = (UniverseServerReplyTurnResults) UniverseClient.request("localhost", 7777, usrtr);
        UniverseClient.database.store(reply);
        Log.debug("server reply status = " + reply.status);
        Log.debug("size of turn results vector = " + reply.results.size());
        String lookAndFeel = Config.getString("gui.plaf", null);
        try {
            javax.swing.UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
        }
        addWindowListener(new MainWindowListener());
        setIconImage(GUIUtils.getDisplayIcon());
        Container content_pane = getContentPane();
        content_pane.setLayout(new BorderLayout());
        menu = new MainMenu(this);
        setJMenuBar(menu);
        commandbar = new CommandBar();
        content_pane.add(commandbar, BorderLayout.NORTH);
        infoPanel = new JPanel();
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        TitledBorder titledBorder = BorderFactory.createTitledBorder(raisedbevel, "Game Information");
        titledBorder.setTitleColor(Color.black);
        infoPanel.setBorder(titledBorder);
        content_pane.add(infoPanel, BorderLayout.CENTER);
        GridBag gridbag = new GridBag(infoPanel);
        viewerLabel = new JLabel("Viewer");
        viewerLabel.setForeground(Color.black);
        gridbag.add(viewerLabel).inset(5).remainderx().left().fill(1.0, 0.0);
        turnLabel = new JLabel("Turn: 0");
        turnLabel.setForeground(Color.black);
        gridbag.add(turnLabel).inset(5).remainderx().left();
        mainPanel = new JPanel();
        GridBag mainGridbag = new GridBag(mainPanel);
        content_pane.add(mainPanel, BorderLayout.CENTER);
        mainGridbag.add(infoPanel).fill(1.0, 0.0).remainderx();
        setTitle("Universe");
        showMainWindow();
    }

    public Object getToolBar() {
        return commandbar;
    }

    public JMenuBar getMainMenu() {
        return menu;
    }

    public void showStartupOptions() {
    }

    public void errorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public synchronized void statusLine(String stringToAdd, boolean refresh) {
    }

    public synchronized void clearStatusArea() {
    }

    public void showMainWindow() {
        pack();
        java.awt.Dimension d = commandbar.getSize();
        setSize(d.width + 20, 320);
        show();
    }

    public void launchResultsScreen() {
    }

    public void gameOptionsProcessed() {
    }

    public void messageListWindow(Vector messages) {
    }

    private class MouseClicked extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
            }
        }
    }

    final class MainWindowListener extends WindowAdapter {

        public void windowClosed(WindowEvent e) {
            System.exit(0);
        }
    }
}
