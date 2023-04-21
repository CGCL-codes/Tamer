package backend.mapping.genericblast.comparators;

import java.util.Comparator;
import backend.query.advanced.output.Match;

public class CoverageOfSortestSequenceSorter implements Comparator<Match> {

    public int compare(Match o1, Match o2) {
        return o1.getCoverageOfSmallestSeqence().compareTo(o2.getCoverageOfSmallestSeqence());
    }
}
