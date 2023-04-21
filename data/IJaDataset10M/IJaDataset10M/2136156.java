package metier.perso;

import controleur.Controleur;

public class PersoMob extends Perso {

    public PersoMob(Controleur cont, String[] images, int imagesEnCours, int posX, int posY, String nom, int pvMax, int pv, int manaMax, int mana, int level) {
        super(cont, images, imagesEnCours, posX, posY, nom, pvMax, pv, manaMax, mana, level);
    }
}
