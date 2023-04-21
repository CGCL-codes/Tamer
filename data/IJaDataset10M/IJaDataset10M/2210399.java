package client.tabs.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import client.GUIHandler;
import client.GUIVariables;
import common.Variables;

public class SettingsPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -462305636407811776L;

    SelectPanel beamer_auswahl;

    public void setPanel() {
        beamer_auswahl.setPanel(Variables.getBeamerTyp());
    }

    public SettingsPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        String[] beamertypen = GUIVariables.getBeamertypes();
        beamer_auswahl = new SelectPanel(beamertypen);
        GUIHandler.addElement(beamer_auswahl, GUIVariables.BeamerTyp);
        beamer_auswahl.setName(GUIVariables.BeamerTyp);
        c.gridy = 0;
        c.gridx = 0;
        JPanel connection = new ConnectionPanel();
        add(connection, c);
        c.gridy = 1;
        c.gridx = 0;
        add(beamer_auswahl, c);
    }
}
