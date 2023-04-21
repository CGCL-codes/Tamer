package de.uni_bremen.informatik.sopra.backend;

/**
 * Interface f&uuml;r Evolution&auml;re Objekte
 * 
 * @author TruBlu
 */
public interface Evolvable {

    /**
	 * Mutation
	 */
    void mutate();

    /**
	 * Rekombination zweier Individuen zu einem neuen Individuum
	 * 
	 * @param e
	 *            anderes Individuum, mit dem rekombiniert wird
	 * @returns rekombinierts Individuum
	 */
    void recombine(Evolvable e);

    /**
	 * Elternselektion Ein Element aus der darunter liegenden Klasse wird
	 * geeignet ausgew&auml;hlt.
	 * 
	 * @returns selektiertes Element
	 */
    Evolvable selectParent();

    /**
	 * Fitnesswertberechnung
	 * 
	 * @returns Fitness des Individuums
	 */
    int fitness();
}
