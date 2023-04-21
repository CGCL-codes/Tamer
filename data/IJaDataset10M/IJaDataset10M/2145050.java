package vues.solo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;
import models.outils.Score;

/**
 * Panel d'affichage des étoiles
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juin 2010
 * @since jdk1.6.0_16
 */
public class Panel_Etoiles extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int LARGEUR = 100;

    private static final Image ETOILE;

    private Score score;

    static {
        ETOILE = Toolkit.getDefaultToolkit().getImage("img/icones/star.png");
    }

    /**
     * Contructeur 
     * 
     * @param score le score
     */
    public Panel_Etoiles(Score score) {
        this.score = score;
        setPreferredSize(new Dimension(LARGEUR, ETOILE.getHeight(null) + 4));
    }

    @Override
    public void paint(Graphics g) {
        for (int i = score.getNbEtoiles(); i > 0; i--) g.drawImage(ETOILE, i * ETOILE.getWidth(null) + i * 6, 0, null);
    }
}
