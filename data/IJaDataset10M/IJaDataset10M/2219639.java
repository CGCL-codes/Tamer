package jts.ihm.gui.render;

import java.awt.Dimension;
import javax.swing.JInternalFrame;

/**Cette classe repr�sente la position du prochain aiguillage.
 * 
 * @author Yannick BISIAUX
 *
 */
@SuppressWarnings("serial")
public class PanelAiguillage extends JInternalFrame {

    private static final int PANEL_WIDTH = 40;

    private static final int PANEL_HEIGHT = 40;

    public PanelAiguillage() {
        super("Aiguillages", true, true, true, true);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setVisible(true);
        this.setLocation(800, 800);
    }
}
