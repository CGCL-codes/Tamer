package fr.ign.cogit.appli.geopensim.feature.macro;

import fr.ign.cogit.appli.geopensim.feature.meso.ZoneElementaireUrbaine;

/**
 * Populations de zones élémentaires.
 * Ces populations peuvent évèntuellement être associées à une date donnée.
 * @author Julien Perret
 *
 */
public class PopulationZonesElementaires extends MacroRepresentation<ZoneElementaireUrbaine> {

    /**
	 * Constructeur de populations de zones élémentaires.
	 */
    public PopulationZonesElementaires() {
        super(ZoneElementaireUrbaine.class);
    }

    /**
	 * Constructeur de populations de zones élémentaires existant à une date donnée.
	 * @param date date associée à la population de zones élémentaires
	 */
    public PopulationZonesElementaires(int date) {
        super(ZoneElementaireUrbaine.class, date);
    }

    @Override
    public void qualifier() {
        for (ZoneElementaireUrbaine zone : this) {
            zone.qualifier();
        }
    }
}
