package logique.calculateur;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import logique.reseau.Troncon;
import logique.reseau.Ville;

/**
 * Nouvelle version de l'algorithme, plus simple et potentiellement plus efficace : 
 * 
 * 	Entre deux villes :
 * 		On lance un pas de Dijkstra par ville. 
 * 		A la première collision on évalue le score de l'itinéraire résultant.
 * 		A partir de là, on évalue l'amélioration qu'apporte chaque pas au score 
 * 	de l'itinéraire.
 * 		On arrete quand l'amélioration est inférieure à un SEUIL au bout d'un 
 * 	certain nombre de PAS.
 * 
 * 	Entre deux villes et des étapes intermédiaires :
 * 		Même principe qu'Optimal
 * 
 * @author nancel
 *
 */
public class NDijkstra extends Initialisation {

    private static final float SEUIL = 1;

    private static final int PAS = 10;

    private Ville best;

    private int derniereAmelioration = -1;

    /**
	 * Liste des tronçons déjà traversés et pas encore développés. 
	 * Régulièrement mise à jour (add - remove) lors des parcours.
	 */
    private HashMap<Ville, PriorityQueue<Ville>> villesTri;

    private HashMap<Ville, Integer> poidsVilles;

    /**
	 * Indique pour chaque étape et pour chaque ville, le tronçon à emprunter pour y acceder.
	 */
    private HashMap<Ville, HashMap<Ville, Troncon>> predecesseurs;

    /**
	 * Indique pour chaque étape la liste des villes atteintes par cette étape
	 */
    private HashMap<Ville, HashSet<Ville>> villesAtteintes;

    /**
	 * Indique pour chaque étape la liste des troncons atteints par cette étape
	 */
    private HashMap<Ville, HashSet<Troncon>> tronconsAtteints;

    /**
	 * Solution d'un Dijkstra2
	 */
    private Itineraire solution;

    int moyenneTempsDijkstra = 0;

    Date d1, d2;

    @Override
    protected Itineraire calcule() {
        villesVoisines = new LinkedList<Ville>();
        int nbChemins;
        Ville[][] todo;
        int nbEtapes = prefs.getEtapesObligatoires().getEtapes().size();
        if (nbEtapes == 0) {
            return dijkstra2(prefs.getVilleDepart(), prefs.getVilleArrivee());
        }
        if (nbEtapes == 1) {
            ArrayList<Itineraire> its = new ArrayList<Itineraire>();
            its.add(dijkstra2(prefs.getVilleDepart(), prefs.getEtapesObligatoires().getEtapes().getFirst()));
            its.add(dijkstra2(prefs.getEtapesObligatoires().getEtapes().getFirst(), prefs.getVilleArrivee()));
            return new Itineraire(prefs.getVilleDepart(), its);
        }
        HashMap<Ville, HashMap<Ville, Itineraire>> chemins = new HashMap<Ville, HashMap<Ville, Itineraire>>();
        for (Ville v : prefs.getEtapesObligatoires().getEtapes()) {
            chemins.put(v, new HashMap<Ville, Itineraire>());
        }
        chemins.put(prefs.getVilleDepart(), new HashMap<Ville, Itineraire>());
        nbChemins = nbEtapes * (nbEtapes - 1) / 2;
        todo = new Ville[nbChemins][2];
        int index = 0;
        for (int i = 0; i < nbEtapes - 1; i++) {
            for (int j = i + 1; j < nbEtapes; j++) {
                todo[index][0] = prefs.getEtapesObligatoires().getEtapes().get(i);
                todo[index][1] = prefs.getEtapesObligatoires().getEtapes().get(j);
                index++;
            }
        }
        Itineraire portion;
        for (int i = 0; i < nbChemins; i++) {
            portion = dijkstra2(todo[i][0], todo[i][1]);
            chemins.get(todo[i][0]).put(todo[i][1], new Itineraire(portion));
            chemins.get(todo[i][1]).put(todo[i][0], chemins.get(todo[i][0]).get(todo[i][1]).inverse());
        }
        for (Ville v : prefs.getEtapesObligatoires().getEtapes()) {
            portion = dijkstra2(prefs.getVilleDepart(), v);
            chemins.get(prefs.getVilleDepart()).put(v, new Itineraire(portion));
            portion = dijkstra2(v, prefs.getVilleArrivee());
            chemins.get(v).put(prefs.getVilleArrivee(), new Itineraire(portion));
        }
        solution = meilleurItineraire(chemins);
        for (Ville e : prefs.getEtapesObligatoires().getEtapes()) {
            for (Troncon t : e.getTroncons()) {
                if (t.getEvaluations() == null) {
                    t.setEvaluations(new Evaluations(prefs, t));
                    calculateur.addEvaluation(t.getEvaluations());
                }
                t.getEvaluations().setScore(2);
            }
        }
        return solution;
    }

    private Itineraire dijkstra2(Ville debut, Ville fin) {
        solution = new Itineraire(debut, Integer.MAX_VALUE, new LinkedList<Troncon>());
        poidsVilles = new HashMap<Ville, Integer>();
        poidsVilles.put(debut, 0);
        villesTri = new HashMap<Ville, PriorityQueue<Ville>>();
        villesTri.put(debut, new PriorityQueue<Ville>(10, new Comparator<Ville>() {

            public int compare(Ville v1, Ville v2) {
                if (getPoidsVille(v1) < getPoidsVille(v2)) return -1; else if (v1.equals(v2)) return 0;
                return 1;
            }
        }));
        villesTri.put(fin, new PriorityQueue<Ville>(10, new Comparator<Ville>() {

            public int compare(Ville v1, Ville v2) {
                if (getPoidsVille(v1) < getPoidsVille(v2)) return -1; else if (v1.equals(v2)) return 0;
                return 1;
            }
        }));
        villesTri.get(debut).add(debut);
        villesTri.get(fin).add(fin);
        predecesseurs = new HashMap<Ville, HashMap<Ville, Troncon>>();
        predecesseurs.put(debut, new HashMap<Ville, Troncon>());
        predecesseurs.put(fin, new HashMap<Ville, Troncon>());
        villesAtteintes = new HashMap<Ville, HashSet<Ville>>();
        villesAtteintes.put(debut, new HashSet<Ville>());
        villesAtteintes.put(fin, new HashSet<Ville>());
        tronconsAtteints = new HashMap<Ville, HashSet<Troncon>>();
        tronconsAtteints.put(debut, new HashSet<Troncon>());
        tronconsAtteints.put(fin, new HashSet<Troncon>());
        best = debut;
        boolean stop = false;
        int iteration = 0;
        while (!stop) {
            pasDijkstra(debut, fin, iteration);
            pasDijkstra(fin, debut, iteration);
            iteration++;
            stop = (derniereAmelioration > -1) && (iteration - derniereAmelioration > PAS);
        }
        villesTri.clear();
        predecesseurs.clear();
        poidsVilles.clear();
        villesTri = null;
        predecesseurs = null;
        poidsVilles = null;
        return solution;
    }

    private int pasDijkstra(Ville etape, Ville but, int iteration) {
        best = villesTri.get(etape).poll();
        if (!villesVoisines.contains(best)) villesVoisines.add(best);
        for (Troncon t : best.getTroncons()) {
            if (t.getEvaluations() == null) {
                t.setEvaluations(new Evaluations(prefs, t));
                calculateur.addEvaluation(t.getEvaluations());
            }
            int score = getPoidsVille(best) + t.getEvaluations().getScore();
            Ville direction = t.getExtremite(best);
            if (!villesVoisines.contains(direction)) villesVoisines.add(direction);
            if (!predecesseurs.get(etape).containsKey(best) && (!predecesseurs.get(etape).containsKey(direction) || !predecesseurs.get(etape).get(direction).equals(t)) && !tronconsAtteints.get(etape).contains(t) && t.getEvaluations().getScore() > 0 && score < getPoidsVille(direction)) {
                poidsVilles.put(direction, score);
                villesTri.get(etape).add(direction);
                predecesseurs.get(etape).put(direction, t);
                villesAtteintes.get(etape).add(direction);
                tronconsAtteints.get(etape).add(t);
            }
        }
        if (villesAtteintes.get(but).contains(best)) {
            Itineraire it = getItineraire(best, etape, but);
            if (it.getScore() < solution.getScore()) {
                solution = new Itineraire(it);
                derniereAmelioration = iteration;
            }
            return 0;
        }
        return -1;
    }

    private Itineraire getItineraire(Ville collision, Ville etape, Ville but) {
        LinkedList<Troncon> sol = new LinkedList<Troncon>();
        Troncon t = predecesseurs.get(etape).get(collision);
        Ville v = collision;
        while (t != null) {
            sol.add(t);
            v = t.getExtremite(v);
            t = predecesseurs.get(etape).get(v);
        }
        t = predecesseurs.get(but).get(collision);
        v = collision;
        while (t != null && !v.equals(but)) {
            sol.add(t);
            v = t.getExtremite(v);
            t = predecesseurs.get(but).get(v);
        }
        return new Itineraire(etape, sol);
    }

    private int getPoidsVille(Ville v) {
        if (poidsVilles.containsKey(v)) {
            return poidsVilles.get(v);
        }
        return Integer.MAX_VALUE;
    }

    private Itineraire meilleurItineraire(HashMap<Ville, HashMap<Ville, Itineraire>> chemins) {
        LinkedList<Ville> etapes = prefs.getEtapesObligatoires().getEtapes();
        int nbEtapes = etapes.size();
        int fact = factorielle(nbEtapes);
        int indiceEtapes;
        int indice;
        Ville villePrecedente;
        Ville villeCourante;
        ArrayList<Ville> visites = new ArrayList<Ville>(nbEtapes);
        int meilleurScore = Integer.MAX_VALUE;
        ArrayList<Itineraire> itineraireCourant = new ArrayList<Itineraire>();
        ArrayList<Itineraire> meilleur = new ArrayList<Itineraire>();
        int scoreCourant = 0;
        boolean inutile = false;
        for (int i = 0; i < fact; i++) {
            itineraireCourant.clear();
            villePrecedente = prefs.getVilleDepart();
            for (int j = 0; j < nbEtapes && !inutile; j++) {
                indiceEtapes = (int) Math.floor((1.0 * i / factorielle(nbEtapes - j) * (nbEtapes - j)) % (nbEtapes - j));
                indice = 0;
                for (int k = 0; k < nbEtapes && indiceEtapes >= 0; k++) {
                    if (!visites.contains(etapes.get(k))) indiceEtapes--;
                    if (indiceEtapes >= 0) indice++;
                }
                if (indice >= nbEtapes) indice--;
                villeCourante = etapes.get(indice);
                itineraireCourant.add(chemins.get(villePrecedente).get(villeCourante));
                scoreCourant += chemins.get(villePrecedente).get(villeCourante).getScore();
                inutile = scoreCourant >= meilleurScore;
                if (inutile && j < nbEtapes - 1) {
                    i += factorielle(nbEtapes - j - 1) - 1;
                }
                villePrecedente = villeCourante;
                visites.add(etapes.get(indice));
            }
            if (!inutile) {
                itineraireCourant.add(chemins.get(villePrecedente).get(prefs.getVilleArrivee()));
                scoreCourant += chemins.get(villePrecedente).get(prefs.getVilleArrivee()).getScore();
            }
            inutile = scoreCourant >= meilleurScore;
            if (!inutile) {
                meilleurScore = scoreCourant;
                meilleur = new ArrayList<Itineraire>(itineraireCourant);
            }
            scoreCourant = 0;
            inutile = false;
            visites.clear();
        }
        return new Itineraire(prefs.getVilleDepart(), meilleur);
    }

    @Override
    public Initialisation getInitialisation() {
        return null;
    }

    private int factorielle(int nb) {
        int fact = 1;
        for (int i = 1; i <= nb; i++) fact *= i;
        return fact;
    }
}
