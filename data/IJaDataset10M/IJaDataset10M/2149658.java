package org.vikamine.swing.subgroup.visualization.overlap;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.vikamine.kernel.data.DataRecord;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.target.BooleanTarget;
import org.vikamine.kernel.subgroup.target.SGTarget;
import org.vikamine.swing.subgroup.AllSubgroupPluginController;

/**
 * @author Tobias Vogele
 */
public class InstancesSGComporator implements Comparator {

    private List sgs;

    public InstancesSGComporator() {
        sgs = new LinkedList();
    }

    public InstancesSGComporator(List sgs) {
        this.sgs = sgs;
    }

    public int compare(Object o1, Object o2) {
        DataRecord i1 = (DataRecord) o1;
        DataRecord i2 = (DataRecord) o2;
        return compare(i1, i2, sgs.iterator());
    }

    private int compare(DataRecord i1, DataRecord i2, Iterator sgsIter) {
        if (!sgsIter.hasNext()) {
            return compareByTarget(i1, i2);
        }
        SG sg = (SG) sgsIter.next();
        boolean b1 = sg.getSGDescription().isMatching(i1);
        boolean b2 = sg.getSGDescription().isMatching(i2);
        if (b1 && !b2) {
            return -1;
        } else if (!b1 && b2) {
            return 1;
        } else {
            return compare(i1, i2, sgsIter);
        }
    }

    private int compareByTarget(DataRecord i1, DataRecord i2) {
        SG sg = AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().getSubgroup();
        if (sg == null) {
            return 0;
        }
        SGTarget target = sg.getTarget();
        if (target instanceof BooleanTarget) {
            BooleanTarget boolTarget = (BooleanTarget) target;
            boolean p1 = boolTarget.isPositive(i1);
            boolean p2 = boolTarget.isPositive(i2);
            if (p1 && !p2) {
                return -1;
            } else if (!p1 && p2) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public List getSgs() {
        return sgs;
    }

    public void setSgs(List newSGs) {
        sgs.retainAll(newSGs);
        for (Iterator iter = newSGs.iterator(); iter.hasNext(); ) {
            SG sg = (SG) iter.next();
            if (!sgs.contains(sg)) {
                sgs.add(sg);
            }
        }
    }

    public void sortBy(SG sg) {
        boolean found = sgs.remove(sg);
        if (!found) {
            throw new NoSuchElementException("sg not found: " + sg);
        }
        sgs.add(0, sg);
    }

    public boolean isSortedBy(SG sg) {
        return sg.equals(getSortedBy());
    }

    public SG getSortedBy() {
        if (sgs.isEmpty()) {
            return null;
        }
        return (SG) sgs.get(0);
    }

    public int getSortIndex(SG sg) {
        return sgs.indexOf(sg);
    }
}
