package net.sourceforge.olduvai.lrac.drawer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.olduvai.lrac.drawer.strips.Strip;

public class StripFocusGroup extends AbstractFocusGroup {

    public StripFocusGroup(String focusGroupName) {
        super(focusGroupName);
    }

    /**
	 * TODO performance: poor linear search performance.  May not be important enough to worry about
	 * since there are a small number of strips.
	 * 
	 */
    public void rebuildRangeList(AccordionLRACDrawer lrd) {
        if (group == null) group = new RangeList(-1);
        group.clear();
        final DataStore ds = lrd.getDataStore();
        List<Strip> stripOrderedList = ds.getStripOrderedList();
        Iterator<String> it = focusItems.iterator();
        while (it.hasNext()) {
            final String focusName = it.next();
            Iterator<Strip> strIt = stripOrderedList.iterator();
            Strip s = null;
            while (strIt.hasNext()) {
                s = strIt.next();
                if (s.getStripTitle().equals(focusName)) break;
            }
            if (s != null) {
                final int index = ds.getStripIndex(s) - 1;
                group.addRange(null, index, index);
            }
        }
        setStale(false);
    }

    public int getType() {
        return STRIPGROUP;
    }
}
