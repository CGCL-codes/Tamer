package se.chalmers.doit.util.implementation;

import se.chalmers.doit.core.ITask;
import se.chalmers.doit.util.IComparatorStrategy;

/**
 * An implementation of IComparatorStrategy that compares the names of two
 * ITasks
 * 
 * Default sorting order for names is ascending.
 * 
 * @author Karl Bristav
 * 
 */
public class NameComparatorStrategy implements IComparatorStrategy {

    private static final long serialVersionUID = -1055558718854705999L;

    private final boolean invertedSortOrder;

    /**
	 * @param invertedSortOrder
	 *            Boolean stating whether the sorting order is to be reversed
	 */
    public NameComparatorStrategy(final boolean invertedSortOrder) {
        this.invertedSortOrder = invertedSortOrder;
    }

    @Override
    public int compare(final ITask t1, final ITask t2) {
        int ret = 0;
        final String n1 = t1.getName();
        final String n2 = t2.getName();
        if (n1.compareToIgnoreCase(n2) > 0) {
            ret = 1;
        } else if (n1.compareToIgnoreCase(n2) < 0) {
            ret = -1;
        }
        if (invertedSortOrder) {
            ret *= -1;
        }
        return ret;
    }
}
