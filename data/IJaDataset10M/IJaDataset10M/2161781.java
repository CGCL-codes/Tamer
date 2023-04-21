package org.icehockeymanager.ihm.clients.devgui.gui.lib;

import java.awt.*;
import javax.swing.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * JIhmPanel is a simple JPanel class with a msgKey string for automatic
 * translations update (recursive). Implements JIhmComponens.
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class JIhmPanel extends JPanel implements JIhmComponent {

    static final long serialVersionUID = 6488285293885539367L;

    private User owner = null;

    private String titleKey = null;

    /** Constructor for the JIhmPanel object */
    public JIhmPanel() {
    }

    public JIhmPanel(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public void updateTranslation() {
        Component[] allComp = this.getComponents();
        for (int i = 0; i < allComp.length; i++) {
            if (allComp[i] instanceof JIhmComponent) {
                JIhmComponent tmp = (JIhmComponent) allComp[i];
                tmp.updateTranslation();
            }
        }
    }

    public void updateData() {
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }
}
