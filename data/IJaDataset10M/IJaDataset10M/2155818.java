package poker;

import javax.swing.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MenuGUI implements ActionListener {

    private HoofdSchermGUI hs;

    private ConnectGUI cn;

    private JMenuBar mb = new JMenuBar();

    private JMenu game = new JMenu("Game");

    private JMenuItem host = new JMenuItem("Host Game");

    private JMenuItem join = new JMenuItem("Join Game");

    private JMenuItem disc = new JMenuItem("Disconnect");

    private JMenuItem exit = new JMenuItem("Exit");

    private JMenu tools = new JMenu("Tools");

    private JMenuItem sett = new JMenuItem("Settings");

    public MenuGUI(HoofdSchermGUI hs) {
        this.hs = hs;
        game.add(host);
        host.addActionListener(this);
        game.add(join);
        join.addActionListener(this);
        game.addSeparator();
        game.add(disc);
        disc.addActionListener(this);
        game.add(exit);
        exit.addActionListener(this);
        tools.add(sett);
        sett.addActionListener(this);
        mb.add(game);
        mb.add(tools);
    }

    public JMenuBar show() {
        return mb;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == host) {
            int c = JOptionPane.showConfirmDialog(null, "Are you sure that you want to disconnect and host a game?", "Host", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (c == JOptionPane.NO_OPTION) return;
            try {
                Properties p = new Properties();
                p.load(new FileInputStream("settings.ini"));
                cn = new ConnectGUI(new Speler(p.getProperty("Nick")));
                cn.showHostGameMenu();
                cn.dispose();
            } catch (IOException ioe) {
            }
        }
        if (e.getSource() == join) {
            int c = JOptionPane.showConfirmDialog(null, "Are you sure that you want to join an other game?", "Join", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (c == JOptionPane.NO_OPTION) return;
            try {
                Properties p = new Properties();
                p.load(new FileInputStream("settings.ini"));
                cn = new ConnectGUI(new Speler(p.getProperty("Nick")));
                cn.showJoinGameMenu();
                cn.dispose();
            } catch (IOException ioe) {
            }
        }
        if (e.getSource() == disc) {
            int c = JOptionPane.showConfirmDialog(null, "Are you sure that you want to disconnect?", "Disconnect", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (c == JOptionPane.YES_OPTION) hs.stop(); else return;
        }
        if (e.getSource() == exit) {
            int c = JOptionPane.showConfirmDialog(null, "Are you sure that you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (c == JOptionPane.YES_OPTION) System.exit(0); else return;
        }
        if (e.getSource() == sett) {
            hs.showSettingsMenu();
        }
    }
}
