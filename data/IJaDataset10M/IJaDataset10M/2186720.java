package maltcms.commands.fragments.alignment.peakCliqueAlignment;

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.tuple.Tuple2D;
import cross.datastructures.tuple.TupleND;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.peak.Peak;

/**
 *
 * @author nils
 */
@Slf4j
public class BBHFinder {

    /**
     * @param al
     * @param fragmentToPeaks
     */
    public List<Peak> findBiDiBestHits(final TupleND<IFileFragment> al, final HashMap<String, List<Peak>> fragmentToPeaks) {
        final Set<Peak> matchedPeaks = new HashSet<Peak>();
        for (final Tuple2D<IFileFragment, IFileFragment> t : al.getPairs()) {
            final List<Peak> lhsPeaks = fragmentToPeaks.get(t.getFirst().getName());
            final List<Peak> rhsPeaks = fragmentToPeaks.get(t.getSecond().getName());
            log.debug("lhsPeaks: {}", lhsPeaks.size());
            log.debug("rhsPeaks: {}", rhsPeaks.size());
            for (final Peak plhs : lhsPeaks) {
                for (final Peak prhs : rhsPeaks) {
                    log.debug("Checking peaks {} and {}", plhs, prhs);
                    if (plhs.isBidiBestHitFor(prhs)) {
                        log.debug("Found a bidirectional best hit: {} and {}", plhs, prhs);
                        matchedPeaks.add(plhs);
                        matchedPeaks.add(prhs);
                        prhs.retainSimilarityRemoveRest(plhs);
                        plhs.retainSimilarityRemoveRest(prhs);
                    }
                }
            }
        }
        log.info("Retained {} matched peaks!", matchedPeaks.size());
        log.debug("Counting and removing unmatched peaks!");
        int peaks = 0;
        List<Peak> unmatchedPeaks = new ArrayList<Peak>();
        for (final IFileFragment t : al) {
            final List<Peak> lhsPeaks = fragmentToPeaks.get(t.getName());
            log.debug("lhsPeaks: {}", lhsPeaks.size());
            ListIterator<Peak> liter = lhsPeaks.listIterator();
            while (liter.hasNext()) {
                final Peak plhs = liter.next();
                if (!matchedPeaks.contains(plhs)) {
                    unmatchedPeaks.add(plhs);
                    peaks++;
                    liter.remove();
                }
            }
        }
        log.info("Removed {} unmatched peaks!", peaks);
        return unmatchedPeaks;
    }
}
