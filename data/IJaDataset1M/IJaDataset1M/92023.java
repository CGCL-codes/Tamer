package galaxiia.demo.intelligence.niveau4;

import galaxiia.jeu.intelligence.Intelligence;
import galaxiia.jeu.terrain.InformateurTerrain;
import galaxiia.jeu.unite.ClasseurUnite;
import galaxiia.jeu.unite.ControleurUnite;
import galaxiia.jeu.unite.InformateurUnite;

class Niveau4 implements Intelligence {

    private Module module;

    public void initialisation(InformateurUnite unite, InformateurTerrain terrain) {
        if (ClasseurUnite.estChasseur(unite.type())) {
            module = new ModuleChasseur();
            module.initialisationGenerale(unite, terrain);
        } else if (ClasseurUnite.estVaisseauMere(unite.type())) {
            module = new ModuleVaisseauMere();
            module.initialisationGenerale(unite, terrain);
        }
    }

    public void nouveauTour(ControleurUnite unite, InformateurTerrain terrain) {
        if (module != null) {
            module.evolutionGenerale(unite, terrain);
        } else {
            unite.autodestruction();
        }
    }
}
