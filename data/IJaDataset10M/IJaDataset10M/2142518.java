package galaxiia.demo.intelligence.solution.campagne.apprentissage;

import galaxiia.configuration.Identification;
import galaxiia.jeu.intelligence.Intelligence;
import galaxiia.jeu.objectif.Objectif;
import galaxiia.jeu.objectif.type.Placement;
import galaxiia.jeu.terrain.InformateurTerrain;
import galaxiia.jeu.unite.ControleurUnite;
import galaxiia.jeu.unite.InformateurUnite;

@Identification(nom = "Solution 03", nomInterne = "Solution 03", auteur = "Valentin", description = "Intelligence terminant la mission 3", version = 1)
public class Mission03 implements Intelligence {

    private double[] positionPlacement;

    public void initialisation(InformateurUnite unite, InformateurTerrain terrain) {
        Objectif objectif = unite.objectifEnCours();
        Placement objectifPlacement = (Placement) (objectif);
        positionPlacement = objectifPlacement.lieuPlacement();
    }

    public void nouveauTour(ControleurUnite unite, InformateurTerrain terrain) {
        unite.accelerationVersPoint(positionPlacement);
    }
}
