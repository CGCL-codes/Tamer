package eu.more.alarmservice.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 
 * @author schmutzler
 */
public class PanelLogo extends java.awt.Panel {

    /**
   * 
   */
    private static final long serialVersionUID = 1520554652038119727L;

    Image moreLogo;

    /** Creates a new instance of PanelLogo */
    public PanelLogo() {
        moreLogo = Toolkit.getDefaultToolkit().getImage(PanelLogo.class.getResource("logo.png"));
        this.setSize(101, 59);
    }

    public void paint(Graphics g) {
        super.paint(g);
        int imagePosH = (this.getWidth() / 2) - (moreLogo.getWidth(this) / 2);
        int imagePosV = (this.getHeight() / 2) - (moreLogo.getHeight(this) / 2);
        g.drawImage(moreLogo, imagePosH, imagePosV, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}
