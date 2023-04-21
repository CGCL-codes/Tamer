package objectif.lyon.data;

/**
 * Destinations du jeu. Les destinations sont reli�es par des routes et 
 * constituent les objectifs du jeu (cartes Destination).<br>
 * <br>
 * <u>Remarque :</u> Les destinations hors cadres (pouvant �tre li�es par 
 * diff�rents points) sont d�finies dans la classe CompositeDestination.
 * 
 * @author Fran�ois Vogt
 * @version 1.0 - 08/01/2012
 * @see CompositeDestination
 * @see Objectif
 * @see DestinationCard
 * @see Route
 */
public class Destination implements Comparable<Destination> {

    /**
     * Nom de la destination.
     */
    private String name = null;

    /**
     * Constructeur par d�faut de la destination.
     */
    public Destination() {
    }

    /**
     * Construit une destination.
     * @param name Nom de la destination.
     */
    public Destination(String name) {
        this.name = name;
    }

    /**
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Destination o) {
        if (o == null || o.getName() == null || getName() == null) {
            return hashCode() - o.hashCode();
        }
        return getName().compareTo(o.getName());
    }
}
