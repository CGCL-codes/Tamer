package kth.csc.inda.sempc.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import kth.csc.inda.sempc.SEMPC;
import kth.csc.inda.sempc.StringConstants;
import kth.csc.inda.sempc.Utils;
import org.bff.javampd.MPDPlayer.PlayerStatus;
import org.bff.javampd.exception.MPDAdminException;
import org.bff.javampd.exception.MPDConnectionException;

/**
 * The menu bar for the GUI.
 * 
 * @author Sebastian Sjögren
 * @version 2009-05-27
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements ActionListener, ItemListener {

    JMenu musicMenu;

    JMenu serverMenu;

    JMenu controlMenu;

    private JMenuItem connectMenuItem;

    private JMenuItem disconnectMenuItem;

    JMenuItem preferencesMenuItem;

    JMenuItem quitMenuItem;

    JMenuItem playPauseItem;

    JMenuItem nextItem;

    JMenuItem prevItem;

    private JMenuItem updateDatabaseMenuItem;

    /**
	 * Constructor.
	 * Create menus, set shortcut commands, register listeners.
	 */
    public MenuBar() {
        musicMenu = new JMenu(StringConstants.GUI_MAIN_MENU_MUSIC);
        musicMenu.setMnemonic(KeyEvent.VK_M);
        serverMenu = new JMenu(StringConstants.GUI_MAIN_MENU_SERVER);
        serverMenu.setMnemonic(KeyEvent.VK_S);
        controlMenu = new JMenu(StringConstants.GUI_MAIN_MENU_CONTROL);
        controlMenu.setMnemonic(KeyEvent.VK_C);
        playPauseItem = new JMenuItem(StringConstants.GUI_MAIN_MENUITEM_PLAY, KeyEvent.VK_SPACE);
        playPauseItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, ActionEvent.CTRL_MASK));
        playPauseItem.addActionListener(this);
        nextItem = new JMenuItem(StringConstants.GUI_MAIN_MENUITEM_NEXT, KeyEvent.VK_RIGHT);
        nextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK));
        nextItem.addActionListener(this);
        prevItem = new JMenuItem(StringConstants.GUI_MAIN_MENUITEM_PREV, KeyEvent.VK_LEFT);
        prevItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK));
        prevItem.addActionListener(this);
        setConnectMenuItem(new JMenuItem(StringConstants.GUI_MAIN_MENUITEM_CONNECT, KeyEvent.VK_C));
        getConnectMenuItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        getConnectMenuItem().addActionListener(this);
        setDisconnectMenuItem(new JMenuItem(StringConstants.GUI_MAIN_MENUITEM_DISCONNECT, KeyEvent.VK_D));
        getDisconnectMenuItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
        getDisconnectMenuItem().addActionListener(this);
        preferencesMenuItem = new JMenuItem(StringConstants.GUI_MAIN_MENUITEM_PREFERENCES, KeyEvent.VK_P);
        preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        preferencesMenuItem.addActionListener(this);
        quitMenuItem = new JMenuItem(StringConstants.GUI_MAIN_MENUITEM_QUIT, KeyEvent.VK_Q);
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        quitMenuItem.addActionListener(this);
        setUpdateDatabaseMenuItem(new JMenuItem(StringConstants.GUI_MAIN_MENUITEM_UPDATE_DATABASE));
        getUpdateDatabaseMenuItem().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
        getUpdateDatabaseMenuItem().addActionListener(this);
        musicMenu.add(getConnectMenuItem());
        musicMenu.add(getDisconnectMenuItem());
        musicMenu.addSeparator();
        musicMenu.add(preferencesMenuItem);
        musicMenu.addSeparator();
        musicMenu.add(quitMenuItem);
        serverMenu.add(getUpdateDatabaseMenuItem());
        controlMenu.add(playPauseItem);
        controlMenu.addSeparator();
        controlMenu.add(prevItem);
        controlMenu.add(nextItem);
        add(musicMenu);
        add(serverMenu);
        add(controlMenu);
        setOpaque(true);
        setPreferredSize(new Dimension(800, 35));
        setFont(new Font("", Font.PLAIN, 14));
    }

    /**
	 * Called when user clicks in menus
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        final JMenuItem src = (JMenuItem) e.getSource();
        if (src.equals(getConnectMenuItem())) {
            SEMPC.get().setWantToConnect(true);
            SEMPC.get().tryToConnect();
        }
        if (src.equals(getDisconnectMenuItem())) {
            SEMPC.get().setWantToConnect(false);
            SEMPC.get().disconnect();
        }
        if (src.equals(preferencesMenuItem)) SEMPC.getUserProfilesGUI().getFrame().setVisible(true);
        if (src.equals(quitMenuItem)) {
            Utils.log(this, StringConstants.LOG_MAIN_QUITTING);
            SEMPC.get().handleTermination();
        }
        if (src.equals(getUpdateDatabaseMenuItem())) {
            try {
                SEMPC.getMPD().getMPDAdmin().updateDatabase();
                Utils.log(this, StringConstants.LOG_MAIN_UPDATING);
            } catch (MPDAdminException e1) {
            } catch (MPDConnectionException e1) {
            }
        }
        if (src.equals(playPauseItem)) {
            String status = SEMPC.getGUI().getPlayerPanel().getStatus();
            if (status.equals(PlayerStatus.STATUS_PLAYING.toString())) {
                SEMPC.getGUI().getPlayerPanel().getPlayerControlsPanel().pause();
            } else if (status.equals(StringConstants.STATUS_PLAYER_PAUSED) || status.equals(PlayerStatus.STATUS_STOPPED)) {
                SEMPC.getGUI().getPlayerPanel().getPlayerControlsPanel().play();
            }
        }
        if (src.equals(prevItem)) {
            String status = SEMPC.getGUI().getPlayerPanel().getStatus();
            if (status.equals(PlayerStatus.STATUS_PLAYING.toString()) || status.equals(StringConstants.STATUS_PLAYER_PAUSED) || status.equals(PlayerStatus.STATUS_STOPPED)) {
                SEMPC.getGUI().getPlayerPanel().getPlayerControlsPanel().playPrev();
            }
        }
        if (src.equals(nextItem)) {
            String status = SEMPC.getGUI().getPlayerPanel().getStatus();
            if (status.equals(PlayerStatus.STATUS_PLAYING.toString()) || status.equals(StringConstants.STATUS_PLAYER_PAUSED) || status.equals(PlayerStatus.STATUS_STOPPED)) {
                SEMPC.getGUI().getPlayerPanel().getPlayerControlsPanel().playNext();
            }
        }
    }

    /**
	 * Disable some menu items. 
	 */
    public void setGrey() {
        disconnectMenuItem.setEnabled(false);
        connectMenuItem.setEnabled(true);
        serverMenu.setEnabled(false);
        controlMenu.setEnabled(false);
    }

    /**
	 * Enable som menu items.
	 */
    public void enable() {
        connectMenuItem.setEnabled(false);
        disconnectMenuItem.setEnabled(true);
        serverMenu.setEnabled(true);
        controlMenu.setEnabled(true);
    }

    @Override
    public void itemStateChanged(ItemEvent arg0) {
    }

    /**
	 * Sets connect menu item.
	 * @param connectMenuItem
	 */
    public void setConnectMenuItem(JMenuItem connectMenuItem) {
        this.connectMenuItem = connectMenuItem;
    }

    /**
	 * Returns the connect menu item. 
	 * @return
	 */
    public JMenuItem getConnectMenuItem() {
        return connectMenuItem;
    }

    /**
	 * Sets disconnect menu item.
	 * @param disconnectMenuItem
	 */
    public void setDisconnectMenuItem(JMenuItem i) {
        disconnectMenuItem = i;
    }

    /**
	 * Returns the disconnect menu item. 
	 * @return
	 */
    public JMenuItem getDisconnectMenuItem() {
        return disconnectMenuItem;
    }

    /**
	 * Sets update database menu item.
	 * @param updateDatabaseMenuItem
	 */
    public void setUpdateDatabaseMenuItem(JMenuItem i) {
        updateDatabaseMenuItem = i;
    }

    /**
	 * Returns the update database menu item. 
	 * @return
	 */
    public JMenuItem getUpdateDatabaseMenuItem() {
        return updateDatabaseMenuItem;
    }
}
