package gui;

import java.util.HashSet;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import project.Project;

/**
 * Implements a filter for missing values in the individuals table.
 * @author Paul A. Rubin <rubin@msu.edu>
 */
public class MissingValueFilter extends RowFilter<IndividualTableModel, Integer> {

    private HashSet<Integer> affinity;

    public MissingValueFilter(Project p) {
        this.affinity = p.getAffinityIndices();
    }

    @Override
    public boolean include(Entry<? extends IndividualTableModel, ? extends Integer> entry) {
        for (int i = 1; i < entry.getValueCount(); i++) {
            if (!affinity.contains(i)) {
                String v = entry.getStringValue(i);
                if (v == null || v.length() == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
