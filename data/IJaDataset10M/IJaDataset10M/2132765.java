package net.face2face.plugins.annonces;

import java.util.*;

/**
 *
 * @author Patrice
 */
public class Rubriques {

    private static final String[][] rubriquesArray = { { "Immobilier", "Vente", "Location" }, { "Auto Moto", "Voitures", "Motos, 2 roues", "Collection", "Equipements", "Caravaning", "Nautisme", "Autres v�hicules" }, { "Bonne affaires", "Maison", "Informatique, Image, Son", "T�l�phonie", "Loisirs", "Famille", "Antiquit�s, Collections", "Bricolage, Jardinage" }, { "Emploi", "Emploi", "Stage", "Formation" }, { "Service", "Services de proximit�", "Cours", "Associations", "Sortir", "Rencontres", "Messages", "Perdu, Trouv�", "Troc, Echange" } };

    private static Hashtable<String, Vector<String>> rubriques = new Hashtable();

    static {
        for (int i = 0; i < rubriquesArray.length; i++) {
            String[] rub = rubriquesArray[i];
            String key = rub[0];
            Vector<String> srub = new Vector();
            for (int j = 1; j < rub.length; j++) {
                srub.add(rub[j]);
            }
            rubriques.put(key, srub);
        }
    }

    public static Object[] getRubriques() {
        return rubriques.keySet().toArray();
    }

    public static Object[] getSousRubriques(String key) {
        return rubriques.get(key).toArray();
    }
}
