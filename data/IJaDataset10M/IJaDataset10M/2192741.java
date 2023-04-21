package fr.cpbrennestt.metier.entite.comparateurs;

import java.util.Comparator;
import fr.cpbrennestt.metier.entite.Joueur;

public abstract class ComparateurJoueur implements Comparator<Joueur> {

    public abstract int compare(Joueur j1, Joueur j2);
}
