package fr.insa_rennes.pcreator.editiongraphique.part;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import fr.insa_rennes.pcreator.Application;
import fr.insa_rennes.pcreator.editiongraphique.model.Chaine;
import fr.insa_rennes.pcreator.editiongraphique.model.Connexion;
import fr.insa_rennes.pcreator.editiongraphique.model.Element;
import fr.insa_rennes.pcreator.editiongraphique.model.GrapheMettableEnNiveaux;
import fr.insa_rennes.pcreator.editiongraphique.model.Noeud;
import fr.insa_rennes.pcreator.editiongraphique.model.NoeudMettableEnNiveaux;
import fr.insa_rennes.pcreator.editiongraphique.model.SousChaine;

/**
 * Classe interne pour la mise en niveaux
 * La classe est statique, car c'est plutot un ensemble de procedures.
 * 
 * @author Christophe Pincemaille
 */
public class MiseEnNiveaux {

    /**
	 * Permet de mettre en niveaux une Chaine
	 * @param c la chaine à mettre en niveaux.
	 */
    public static void miseEnNiveaux(Chaine c) {
        miseEnNiveauxEffective(c);
        List<SousChaine> lsch = c.getSousChaines();
        for (SousChaine sc : lsch) {
            miseEnNiveauxEffective(sc);
        }
        triParNiveau(c);
        Application.logger.debug("Mise en niveaux faite : actualisation de l'affichage");
    }

    private static void miseEnNiveauxEffective(GrapheMettableEnNiveaux g) {
        NoeudMettableEnNiveaux fin = ajouteSommetFictifEnFin(g);
        int niveauFin = miseEnNiveauxRec(fin);
        placeNoeudsSansSuccesseurALaFin(g, niveauFin);
        retireSommetFictifEnFin(g, fin);
    }

    /**
	 * Cette méthode place tous les noeuds qui n'ont pas de successeur à la fin. 
	 * Normalement cette méthode ne devrait jamais être appelée, car dans notre cas, 
	 * les services devraient toujours avoir au moins une sortie.
	 * @param g le graphe mettable en niveaux sur lequel effectuer l'opération.
	 * @param niveauFin le niveau le plus important dans le graphe.
	 */
    private static void placeNoeudsSansSuccesseurALaFin(GrapheMettableEnNiveaux g, int niveauFin) {
        List<NoeudMettableEnNiveaux> ln = g.getNoeuds();
        for (NoeudMettableEnNiveaux n : ln) {
            if ((n.getNiveau() < 0) && !n.aSuccesseurs()) {
                n.setNiveau(niveauFin);
            }
        }
    }

    private static int miseEnNiveauxRec(NoeudMettableEnNiveaux s) {
        int numNiveau = 0;
        if (s.aPredecesseurs()) {
            List<Noeud> predecesseurs = s.predecesseurs();
            for (Noeud ssch : predecesseurs) {
                numNiveau = Math.max(miseEnNiveauxRec((NoeudMettableEnNiveaux) ssch) + 1, numNiveau);
            }
        }
        s.setNiveau(numNiveau);
        return numNiveau;
    }

    /**
	 * Permet d'ajouter un noeud fictif en fin du graphe, 
	 * accroché à toutes les sorties du graphe, pour permettre
	 * d'initier la mise en niveaux récursive.
	 * @param g le graphe dans lequel ajouter le noeud fictif
	 * @return le noeud fictif ajouté.
	 */
    private static NoeudMettableEnNiveaux ajouteSommetFictifEnFin(GrapheMettableEnNiveaux g) {
        NoeudMettableEnNiveaux noeudFictif = g.fabriqueNoeudFictif();
        List<NoeudMettableEnNiveaux> ls = g.getNoeuds();
        for (NoeudMettableEnNiveaux s : ls) {
            if (s.estSommetTerminal()) {
                g.creerConnexion(s, noeudFictif);
            }
        }
        g.ajouteNoeud(noeudFictif);
        return noeudFictif;
    }

    /**
	 * Permet de retirer le noeud fictif en fin du graphe. 
	 * Retire également toutes les connexions à ce noeud.
	 * @param g le graphe pour lequel retirer le noeud fictif
	 * @param le noeud fictif à retirer.
	 */
    private static void retireSommetFictifEnFin(GrapheMettableEnNiveaux g, NoeudMettableEnNiveaux fin) {
        List<Connexion> lc = fin.getConnexionsEntrantes();
        for (Connexion c : lc) {
            fin.supprimeConnexion(c);
        }
        g.retireNoeud(fin);
    }

    /**
	 * Effectue le tri des controlleurs de sous chaine suivant leur niveau
	 * 
	 * @param children
	 */
    @SuppressWarnings("unchecked")
    private static void triParNiveau(Chaine c) {
        Comparator<Element> comparateur = new Comparator<Element>() {

            public int compare(Element ssch1, Element ssch2) {
                Integer niveau1 = new Integer(((SousChaine) ssch1).getNiveau());
                Integer niveau2 = new Integer(((SousChaine) ssch2).getNiveau());
                return niveau1.compareTo(niveau2);
            }
        };
        Collections.sort(c.getChildrenArray(), comparateur);
    }
}
