package fr.kb.frais.vue.tarif;

import java.util.HashMap;
import java.util.Map;
import fr.kb.frais.modele.date.Mois;
import fr.kb.frais.modele.object.Personne;

public class InternalFichePersonneFactory {

    private static Map<Personne, InternalFichePersonne> fiches = new HashMap<Personne, InternalFichePersonne>();

    public static InternalFichePersonne getInstance(Personne personne, Mois mois) {
        return fiches.get(personne);
    }

    public static void put(Personne personne, InternalFichePersonne fiche) {
        fiches.put(personne, fiche);
    }
}
