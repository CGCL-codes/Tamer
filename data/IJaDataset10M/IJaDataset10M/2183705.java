package bouttime.sort;

import bouttime.model.Group;
import java.util.*;

/**
 * A class to sort Group objects :
 *     (1) by number of rounds
 *     (2) by number of bouts in round 1
 *
 * Sort order is highest to lowest.
 */
public class GroupRoundsBoutsSort implements Comparator<Group> {

    public int compare(Group g1, Group g2) {
        int rv;
        Integer g1Rounds = Integer.valueOf(g1.getNumRounds());
        Integer g2Rounds = Integer.valueOf(g2.getNumRounds());
        rv = g2Rounds.compareTo(g1Rounds);
        if (rv != 0) {
            return rv;
        } else {
            Integer g1Bouts = Integer.valueOf(g1.getBoutsByRound("1").size());
            Integer g2Bouts = Integer.valueOf(g2.getBoutsByRound("1").size());
            return g2Bouts.compareTo(g1Bouts);
        }
    }
}
