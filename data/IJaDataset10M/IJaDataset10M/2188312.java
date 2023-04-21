package fr.ign.cogit.appli.geopensim.feature.meso;

import org.apache.log4j.Logger;
import fr.ign.cogit.appli.geopensim.feature.ElementRepresentation;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;

/**
 * @author Julien Perret
 *
 */
public abstract class MesoRepresentation extends ElementRepresentation {

    static Logger logger = Logger.getLogger(MesoRepresentation.class.getName());

    /**
	 * Constructeur de représentations Géographiques.
	 */
    public MesoRepresentation() {
        super();
    }

    /**
	 * Constructeur de représentations méso-Géographiques à partir d'une géométrie
	 * @param geom géométrie de la représentation méso-Géographique
	 */
    public MesoRepresentation(IGeometry geom) {
        super(geom);
    }

    @Override
    public void qualifier() {
    }
}
