package galaxiia.demo.intelligence.gaia.intelligences;

import galaxiia.configuration.Identification;
import galaxiia.demo.intelligence.gaia.OutilsGaia;
import galaxiia.jeu.intelligence.Intelligence;
import galaxiia.jeu.terrain.InformateurTerrain;
import galaxiia.jeu.unite.ControleurUnite;
import galaxiia.jeu.unite.InformateurUnite;

@Identification(nom = "Intelligence Gaia - Defense", nomInterne = "GaiaDefense", auteur = "2CIA", description = "Intelligence Gaia pour les missions.", version = 1)
public class GaiaDefense implements Intelligence {

    private GaiaDefense() {
    }

    public void initialisation(InformateurUnite unite, InformateurTerrain terrain) {
    }

    public void nouveauTour(ControleurUnite unite, InformateurTerrain terrain) {
        if (!OutilsGaia.freinageSiNecessaire(unite, terrain)) {
            if (!OutilsGaia.bouclierSiNecessaire(unite, terrain)) {
                unite.freinage();
            }
        }
    }

    public static Intelligence creationNouvelleIntelligence() {
        return new GaiaDefense();
    }
}
