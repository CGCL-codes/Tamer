package uk.ac.ebi.rhea.domain.util;

import java.util.Comparator;
import uk.ac.ebi.rhea.domain.Reaction;

/**
 * Comparator for reactions according to their status, prevailing the most
 * public/valid.
 * @author rafalcan
 *
 */
public class ReactionStatusComparator implements Comparator<Reaction> {

    public int compare(Reaction r1, Reaction r2) {
        return r1.getStatus().ordinal() - r2.getStatus().ordinal();
    }
}
