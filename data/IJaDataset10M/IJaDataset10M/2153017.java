package orbe.model;

import java.io.File;

/**
 * Creation de cartes.
 * 
 * @author Damien Coraboeuf
 */
public interface IOrbeMapFactory {

    /**
	 * Cr�ation d'une carte vierge
	 */
    OrbeMap createMap(OrbeMapConfig config);

    /**
	 * Lecture depuis un fichier
	 */
    OrbeMap readMap(File file);
}
