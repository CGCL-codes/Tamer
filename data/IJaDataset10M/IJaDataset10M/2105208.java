package sjrd.tricktakinggame.cards;

import java.util.*;
import java.io.*;

/**
 * Carte d'un jeu de cartes
 * @author sjrd
 */
public class Card implements Comparable<Card>, Serializable {

    /**
     * UID de sérialisation
     */
    private static final long serialVersionUID = 1;

    /**
     * Couleur de la carte (coeur, carreau, trèfle ou pique)
     */
    private Suit suit;

    /**
     * Force de la carte (0 est la plus faible)
     */
    private int force;

    /**
     * Nom de la carte (ex. : "As de Pique")
     */
    private String name;

    /**
     * ID de dessin
     */
    private String drawID;

    /**
     * Crée une nouvelle carte
     * @param aSuit Couleur de carte
     * @param aForce Force
     * @param aName Nom
     * @param aDrawID ID de dessins
     */
    public Card(Suit aSuit, int aForce, String aName, String aDrawID) {
        super();
        suit = aSuit;
        force = aForce;
        name = aName;
        drawID = aDrawID;
    }

    /**
     * Couleur de la carte
     * @return Couleur de la carte
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Force de la carte
     * @return Force de la carte
     */
    public int getForce() {
        return force;
    }

    /**
     * ID de la carte
     * <p>
     * L'ID d'une carte est toujours de la forme '<i>suit</i>-<i>force</i>', où
     * <i>suit</i> est la représentation chaînée de la valeur énumérée de
     * couleur, et <i>force</i> la conversion en chaîne de la valeur numérique
     * de force.
     * </p>
     * @return ID de la carte
     */
    public String getID() {
        return suit.name() + "-" + force;
    }

    /**
     * Nom de la carte
     * @return Nom de la carte
     */
    public String getName() {
        return name;
    }

    /**
     * Nom de long de la carte (ex. : "As de Coeur")
     * @return Nom de long de la carte (ex. : "As de Coeur")
     */
    public String getLongName() {
        return name + " de " + suit.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getLongName();
    }

    /**
     * ID de dessins
     * @return ID de dessins
     */
    public String getDrawID() {
        return drawID;
    }

    /**
     * @see java.util.Comparable#compareTo
     */
    public int compareTo(Card other) {
        int result = suit.compareTo(other.suit);
        if (result != 0) return result;
        return force - other.force;
    }

    /**
     * Comparateur à utiliser pour trier des cartes selon un ordre visuel
     * <p>
     * Ce comparateur arrange les cartes selon leur couleur, puis selon leur
     * force. L'ordre des couleurs alterne rouge et noir.
     * </p>
     * @author sjrd
     */
    public static class VisualHelpingComparator implements Comparator<Card> {

        /**
         * Comparateur de couleurs
         */
        protected final Comparator<Suit> suitComparator = new Suit.VisualHelpingComparator();

        /**
         * {@inheritDoc}
         */
        public int compare(Card left, Card right) {
            int suitComparison = suitComparator.compare(left.getSuit(), right.getSuit());
            if (suitComparison != 0) return suitComparison; else return left.getForce() - right.getForce();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == this.getClass();
        }
    }
}
