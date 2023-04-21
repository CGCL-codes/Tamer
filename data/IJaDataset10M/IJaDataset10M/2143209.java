package biblio;

import java.util.*;

public class CtrRechParAuteur {

    private Bibliotheque biblio;

    public CtrRechParAuteur(Bibliotheque bib) {
        this.lierBibliotheque(bib);
        System.out.println();
        System.out.print("Recherche par auteur");
        System.out.println();
        System.out.print("Saisissez le nom de l'auteur : ");
        String nomAuteur = IO.lireChaine();
        System.out.println();
        System.out.print("Entrez Son prénom : ");
        String prenomAuteur = IO.lireChaine();
        Auteur aut = biblio.unAuteur(nomAuteur, prenomAuteur);
        if (aut != null) {
            VueAuteur vAut = new VueAuteur(aut);
            vAut.elimineObserveur();
            vAut = null;
        } else {
            System.out.println("Auteur inconnu.");
            System.out.println();
        }
    }

    private void lierBibliotheque(Bibliotheque bib) {
        biblio = bib;
    }
}
