package ho.module.teamAnalyzer.manager;

import ho.module.teamAnalyzer.vo.Match;
import java.util.Comparator;
import java.util.Date;

/**
 * Comparator that orders based on match date
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
class MatchComparator implements Comparator<Match> {

    /**
     * Compare two objects
     *
     * @param o1
     * @param o2
     *
     * @return
     */
    public int compare(Match o1, Match o2) {
        Date s1 = o1.getMatchDate();
        Date s2 = o2.getMatchDate();
        return s2.compareTo(s1);
    }
}
