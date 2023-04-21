package org.liris.schemerger.utils;

import java.util.Comparator;
import org.liris.schemerger.core.pattern.IChronicle;
import org.liris.schemerger.core.pattern.IEpisode;
import org.liris.schemerger.core.pattern.ITypeDec;

/**
 * Implements a total order on chronicles. Only compares chronicles that
 * complete (no empty constraint).
 * 
 * @author Damien Cram
 * 
 * @param <T>
 *            the type of event declaration in episodes of the chronicles to
 *            compare
 */
public class ChronicleTotalComparator<T extends ITypeDec> implements Comparator<IChronicle<T>> {

    public int compare(IChronicle<T> c1, IChronicle<T> c2) {
        IEpisode<T> episode1 = c1.getEpisode();
        IEpisode<T> episode2 = c2.getEpisode();
        int comp = episode1.compareTo(episode2);
        if (comp != 0) return comp;
        for (int i = 0; i < c1.getEpisode().size(); i++) {
            for (int j = i + 1; j < c1.getEpisode().size(); j++) {
                comp = c1.getConstraint(i, j).compareTo(c2.getConstraint(i, j));
                if (comp != 0) return comp;
            }
        }
        return 0;
    }
}
