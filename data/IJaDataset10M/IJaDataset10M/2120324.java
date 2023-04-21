package net.jetrix.spectator.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import net.jetrix.agent.TSpecAgent;

/**
 * @author Emmanuel Bourg
 * @version $Revision: 751 $, $Date: 2008-08-28 04:20:33 -0400 (Thu, 28 Aug 2008) $
 */
public class SpectatorFrame extends JFrame {

    private ChannelMenu menu;

    private SpectatorPanel panel;

    public SpectatorFrame(TSpecAgent agent) throws HeadlessException {
        setTitle("Jetrix TetriNET Spectator");
        setPreferredSize(new Dimension(640, 520));
        JMenuBar menuBar = new JMenuBar();
        menuBar.setVisible(true);
        setJMenuBar(menuBar);
        menu = new ChannelMenu(agent);
        menuBar.add(menu);
        panel = new SpectatorPanel();
        getContentPane().add(panel);
    }

    public SpectatorPanel getSpectatorPanel() {
        return panel;
    }

    public void dispose() {
        if (menu != null) {
            menu.stop();
        }
        super.dispose();
    }
}
