package maltcms.datastructures.constraint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Concrete implementation of an IndexRange for RetentionIndices or Anchors.
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 */
public class RetentionIndexConstraint implements IIndexRange {

    private LinkedList<ArrayList<Integer>> al = null;

    private int steps = 0;

    public RetentionIndexConstraint(final Integer[] arrayShape, final LinkedList<ArrayList<Integer>> al1) {
        checkZeroAndEndPresent(arrayShape, al1);
    }

    public boolean allowed(final Integer dim, final Integer index) {
        final int lb = getLowerBound(dim);
        final int ub = getUpperBound(dim);
        if (((lb <= index) && (ub > index))) {
            System.out.println("Yes");
            return true;
        }
        System.out.println("No");
        return false;
    }

    protected void checkZeroAndEndPresent(final Integer[] arrayShape, final LinkedList<ArrayList<Integer>> ll) {
        int k = 0;
        for (final List<Integer> l : ll) {
            for (int i = 0; i < l.size(); i++) {
                l.set(i, l.get(i) + 1);
            }
            if (l.get(0) != 0) {
                l.add(0, 0);
            }
            if (l.get(l.size() - 1) != arrayShape[k]) {
                l.add(arrayShape[k]);
            }
            k++;
        }
        this.al = ll;
    }

    public Integer getLowerBound(final Integer k) {
        return this.al.get(k).get(this.steps);
    }

    public Integer getUpperBound(final Integer k) {
        return this.al.get(k).get(this.steps + 1);
    }

    public boolean next(final Integer... is) {
        boolean next = false;
        for (int k = 0; k < this.al.size(); k++) {
            if (is[k] == getUpperBound(k) - 1) {
                next = true;
            } else {
                next = false;
                return false;
            }
        }
        if (next) {
            this.steps++;
            System.out.println("At index " + (this.steps) + " with bounds: ");
            for (int k = 0; k < this.al.size(); k++) {
                System.out.println("Dim " + k + " Lower: " + getLowerBound(k) + " upper: " + getUpperBound(k));
            }
            return true;
        }
        return false;
    }
}
