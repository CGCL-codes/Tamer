package org.icehockeymanager.ihm.clients.ihmgui.gui.standings;

import java.awt.BorderLayout;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import org.icehockeymanager.ihm.clients.ihmgui.controller.ClientController;
import org.icehockeymanager.ihm.clients.ihmgui.gui.lib.JIhmPanel;
import org.icehockeymanager.ihm.game.user.User;

/**
 * The PanelStandings contains: - A panel that contains all leagueOwners on it.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelStandings extends JIhmPanel {

    /** The Constant serialVersionUID. */
    static final long serialVersionUID = IHM_SWING_COMPONENT_VERSION;

    /** Panel with all leagueOwners. */
    PanelLeagueStandings panelLeagueStandings = null;

    /** The border layout1. */
    BorderLayout borderLayout1 = new BorderLayout();

    /** The standings icon. */
    ImageIcon standingsIcon = new ImageIcon();

    /**
   * Constructs the frame, sets the user, calls ihmInit.
   * 
   * @param user User to show this frame for
   */
    public PanelStandings(User user) {
        super(user);
        try {
            jbInit();
            ihmInit();
        } catch (Exception e) {
            String msg = "GUI Exception!";
            logger.log(Level.SEVERE, msg, e);
            ClientController.getInstance().showError(msg, e);
        }
    }

    /**
   * JBuilder stuff.
   * 
   * @throws Exception the exception
   * 
   * @exception Exception
   * Exception
   */
    private void jbInit() throws Exception {
        this.setSize(500, 400);
        this.setLayout(borderLayout1);
    }

    /**
   * Create the panelLeagueStandings and add it to the frame.
   */
    private void ihmInit() {
        this.setTitleKey("title.standings");
        panelLeagueStandings = new PanelLeagueStandings();
        this.add(panelLeagueStandings, BorderLayout.CENTER);
    }
}
