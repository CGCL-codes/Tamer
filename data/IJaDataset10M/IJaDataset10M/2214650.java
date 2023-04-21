package org.robinfinch.clasj.organisation;

import java.util.Comparator;

/**
 * Compares two {@link org.robinfinch.clasj.organisation.Player players}
 * by rating and name.
 * @author Mark Hoogenboom
 */
public class ComparatorByRating implements Comparator<Player> {

    public int compare(Player p1, Player p2) {
        int c = p2.getRating() - p1.getRating();
        if (c == 0) {
            c = p1.getName().compareTo(p2.getName());
        }
        return c;
    }
}
