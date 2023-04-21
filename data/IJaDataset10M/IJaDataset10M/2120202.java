package models.attaques;

import java.awt.*;
import models.animations.Explosion;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

/**
 * Attaque d'une boule de feu.
 * 
 * Cette classe est une animation qui dessine une boule de feu partant d'une
 * tour vers une creature.
 * 
 * @author Aurelien Da Campo
 * @version 1.1 | 4 mai 2010
 * @since jdk1.6.0_16
 */
public class BouleDeTerre extends Attaque {

    private static final long serialVersionUID = 1L;

    private static final int DIAMETRE_BOULE = 10;

    private static final int DIAMETRE_BOULE_MAX = 20;

    private static final Image IMAGE_BOULE;

    /**
     * Vitesse
     */
    private double vitesse = 0.1;

    /**
     * distance entre la tete de la fleche et la tour
     */
    private double distanceCentreBoule = 0;

    /**
     * position de la tete de la fleche
     */
    private double xCentreBoule, yCentreBoule;

    private double distanceMax;

    private double distanceMaxInitiale;

    static {
        IMAGE_BOULE = Toolkit.getDefaultToolkit().getImage("img/animations/attaques/bouleDeTerre.png");
    }

    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public BouleDeTerre(Jeu jeu, Tour attaquant, Creature cible, long degats, double rayonImpact) {
        super((int) attaquant.getCenterX(), (int) attaquant.getCenterY(), jeu, attaquant, cible);
        this.degats = degats;
        this.rayonImpact = rayonImpact;
        this.distanceMaxInitiale = calculerDistance();
    }

    @Override
    public void dessiner(Graphics2D g2) {
        double xAttaquant = attaquant.getCenterX();
        double yAttaquant = attaquant.getCenterY();
        double angle = Math.atan2(cible.getCenterY() - yAttaquant, cible.getCenterX() - xAttaquant);
        xCentreBoule = Math.cos(angle) * distanceCentreBoule + xAttaquant;
        yCentreBoule = Math.sin(angle) * distanceCentreBoule + yAttaquant;
        int diametre = 0;
        if (distanceMaxInitiale > 100.0) {
            double p = distanceCentreBoule / (distanceMax / 2.0);
            if (distanceCentreBoule > distanceMax / 2.0) p = 1 - (p - 1);
            diametre = (int) (p * DIAMETRE_BOULE_MAX + DIAMETRE_BOULE);
        } else {
            diametre = DIAMETRE_BOULE;
        }
        g2.drawImage(IMAGE_BOULE, (int) xCentreBoule - diametre / 2, (int) yCentreBoule - diametre / 2, diametre, diametre, null);
    }

    @Override
    public void animer(long tempsPasse) {
        if (!estTerminee) {
            distanceCentreBoule += tempsPasse * vitesse;
            distanceMax = calculerDistance();
            if (distanceCentreBoule >= distanceMax) {
                informerEcouteurAttaqueTerminee();
                estTerminee = true;
                jeu.ajouterAnimation((new Explosion((int) xCentreBoule - DIAMETRE_BOULE, (int) yCentreBoule - DIAMETRE_BOULE)));
                attaquerCibles();
                estTerminee = true;
            }
        }
    }

    private double calculerDistance() {
        double diffX = cible.getCenterX() - attaquant.getCenterX();
        double diffY = cible.getCenterY() - attaquant.getCenterY();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
}
