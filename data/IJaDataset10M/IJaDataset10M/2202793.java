package models.animations;

import i18n.Langue;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import vues.GestionnaireDesPolices;

/**
 * Classe de gestion de l'animation d'un gain d'une étoile
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class GainEtoile extends Animation {

    private static final long serialVersionUID = 1L;

    private static final float ETAPE_ALPHA = .02f;

    private static final Image ETOILE;

    private float alpha = 1.0f;

    private int largeur;

    private int hauteur;

    static {
        ETOILE = Toolkit.getDefaultToolkit().getImage("img/icones/star.png");
    }

    /**
	 * Constructeur de l'animation
	 * 
	 * @param largeur largeur de l'anim
	 * @param hauteur hauteur de l'anim
	 */
    public GainEtoile(int largeur, int hauteur) {
        super(0, 0);
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    @Override
    public void dessiner(Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        Font ancienne = g2.getFont();
        g2.setFont(GestionnaireDesPolices.POLICE_TITRE);
        g2.setColor(Color.WHITE);
        int xOffset = -100;
        g2.drawString(Langue.getTexte(Langue.ID_TITRE_ETOILE_GAGNEE), largeur / 2 + xOffset, hauteur / 2);
        g2.drawImage(ETOILE, largeur / 2 + xOffset - 50, hauteur / 2 - 50, 50, 50, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
        g2.setFont(ancienne);
    }

    @Override
    public void animer(long tempsPasse) {
        if (alpha >= ETAPE_ALPHA) alpha -= ETAPE_ALPHA;
        if (alpha < ETAPE_ALPHA) estTerminee = true;
    }
}
