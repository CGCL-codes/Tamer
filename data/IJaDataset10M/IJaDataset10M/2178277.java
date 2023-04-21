package org.jcvi.tasker;

import java.util.List;
import org.jcvi.common.core.Range;
import org.jcvi.glk.Well;

public interface WellResolver {

    /**
     * Get the list of Wells from the database
     * that have primers that are designed
     * for the given region (segment) and
     * intersect the given range on that region.
     * @param region the region name this could
     * also be the segment in a segmented sample.
     * @param range the range on the region to look for.
     * @return a list of wells for that region; will never
     * be null.  If there are no wells found, then an 
     * empty list will be returned.
     * @throws NullPointerException if the given parameters
     * are null.
     */
    List<Well> getWellsFor(String region, Range range);
}
