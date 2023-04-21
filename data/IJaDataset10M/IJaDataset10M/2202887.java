package xhack.ui.basic;

import xhack.ui.*;
import xhack.object.Map;
import xhack.object.World;
import xhack.menu.HackMenu;
import java.awt.*;
import javax.swing.*;
import xhack.game.Game;
import xhack.game.Preferences;
import java.awt.event.*;

/**
 * Top level UI container for a 2D, bitmap graphic based interface.  The actual
 * painting is performed in {@link xhack.ui.basic.MainPanel}, which delegates to
 * {@link xhack.ui.basic.MapPanel}, {@link xhack.ui.basic.MenuPanel},
 * {@link xhack.ui.basic.StatusPanel} and {@link xhack.ui.basic.MessagePanel}.
 */
public class BasicUI extends JFrame implements WindowListener, UI {

    public Game game;

    public MainPanel ui;

    public InputProcessor ip;

    private boolean locked = false;

    public BasicUI(Game game) throws HeadlessException {
        try {
            this.game = game;
            ui = new MainPanel(this);
            ip = new InputProcessor(this);
            jbInit(ui, ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public KeyMap getKeyMap() {
        return ip.getKeyMap();
    }

    public MainPanel getPanel() {
        return ui;
    }

    public Game getGame() {
        return game;
    }

    public void windowClosed(WindowEvent e) {
        game.exit();
    }

    public void windowClosing(WindowEvent e) {
        game.exit();
    }

    public void dispose() {
        Preferences.setDisplayWidth(getWidth());
        Preferences.setDisplayHeight(getHeight());
        super.dispose();
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    private void jbInit(MainPanel ui, InputProcessor ip) throws Exception {
        getContentPane().add(ui);
        setTitle("xhack alpha9");
        addKeyListener(ip);
        addWindowListener(this);
        pack();
        setVisible(true);
        ip.setSplashContext();
    }

    public void display(Map map) {
        ui.setMap(map);
        ui.mapChanged();
    }

    public void display(World world) {
        ui.setWorld(world);
        ui.statusChanged();
    }

    public void display(String message, Color color) {
        ui.printMessage(message, color);
        ui.messageChanged();
    }

    public void display(HackMenu menu) {
        ui.setMenu(menu);
        ui.menuChanged();
    }

    public void showMenu(boolean b) {
        if (b) ip.setMenuContext(); else {
            if (ui.getMap() == null) ip.setSplashContext(); else ip.setGameContext();
        }
        ui.showMenu(b);
    }

    public void animate(int tileID, int row, int col, int row2, int col2) {
    }

    public void redraw() {
        ui.redraw();
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }
}
