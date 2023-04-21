package org.pcomeziantou.volleylife.model.universe;

import java.util.List;
import org.pcomeziantou.volleylife.model.universe.biosphere.Equipe;
import org.pcomeziantou.volleylife.model.universe.biosphere.Joueur;

/**
 * Englobe le contenu de l'entra�nement d'une �quipe.
 * 
 * @author Philippe Coloigner
 * 
 */
public class SeanceEntrainement {

    private Entrainement entrainement;

    private Equipe equipe;

    private List<Exercice> exercices;

    public SeanceEntrainement(Entrainement entrainement) {
        this.entrainement = entrainement;
        this.equipe = null;
    }

    public SeanceEntrainement(Entrainement entrainement, Equipe equipe) {
        this.entrainement = entrainement;
        this.equipe = equipe;
    }

    public List<Joueur> getJoueurs() {
        return equipe.getJoueurs();
    }

    public List<Exercice> getExercices() {
        return exercices;
    }

    public void addExercice(Exercice exercice) {
        exercices.add(exercice);
    }
}
