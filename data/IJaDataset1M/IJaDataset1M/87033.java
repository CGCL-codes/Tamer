package iia.jeux.modele;

import java.util.ArrayList;
import java.util.Random;

public class JoueurAleatoire extends JoueurActif implements JoueurStratege {

    public JoueurAleatoire(String s, PlateauJeu p) {
        super(s, p);
    }

    public Coup proposeCoup() {
        ArrayList arraylist = getPlateau().coupsPossibles(this);
        int i = (new Random()).nextInt(arraylist.size());
        return (Coup) arraylist.get(i);
    }
}
