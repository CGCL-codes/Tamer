package org.dllearner.utilities.owl;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import org.dllearner.core.owl.Description;

/**
 * A set of descriptions, which is bound by a maximum
 * size. Can be used by algorithms to store the most promising
 * n class descriptions.
 * 
 * @author Jens Lehmann
 *
 */
public class DescriptionSet {

    private ConceptComparator comp = new ConceptComparator();

    private SortedSet<Description> set = new TreeSet<Description>(comp);

    private int maxSize;

    public DescriptionSet(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(Description ed) {
        set.add(ed);
        if (set.size() > maxSize) {
            Iterator<Description> it = set.iterator();
            it.next();
            it.remove();
        }
    }

    public void addAll(Collection<Description> eds) {
        for (Description ed : eds) {
            add(ed);
        }
    }

    /**
	 * @return the set
	 */
    public SortedSet<Description> getSet() {
        return set;
    }
}
