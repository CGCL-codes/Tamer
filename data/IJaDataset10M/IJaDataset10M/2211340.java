package uk.ac.lkl.migen.system.ai.analysis.core.rhythm.selection;

import java.util.Iterator;
import java.util.Map;
import uk.ac.lkl.migen.system.ai.analysis.core.rhythm.TileWindow;

/**
 * A rhythm selector that chooses the rhythm with the most 
 * distances equal to 0.  
 * 
 * @author sergut
 *
 */
public class ZeroRhythmSelector implements RhythmSelector {

    /** 
     * Looks for the window with the most 0's in its 
     * table of distances to other windows.
     * 
     * If all distances are greater than 0, return null.
     * 
     * @return the window with the most 0's, or null if there are none.
     */
    public TileWindow selectRhythm(Map<TileWindow, Map<TileWindow, Integer>> windows) {
        if (windows == null) return null;
        TileWindow result = null;
        int minZeroCount = 0;
        for (Iterator<TileWindow> e = windows.keySet().iterator(); e.hasNext(); ) {
            TileWindow mainWin = e.next();
            Map<TileWindow, Integer> distanceTable = windows.get(mainWin);
            int counter = 0;
            for (Iterator<Integer> distances = distanceTable.values().iterator(); distances.hasNext(); ) {
                Integer distance = distances.next();
                if (distance.intValue() == 0) counter++;
            }
            if (counter > minZeroCount) {
                minZeroCount = counter;
                result = mainWin;
            }
        }
        return result;
    }
}
