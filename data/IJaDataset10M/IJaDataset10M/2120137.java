package org.opencube.oms;

import java.util.Comparator;

/**
 * @author <a href="mailto:maciek@fingo.pl">Maciej Mroczko - FINGO</a>
 *
 * TODO comments
 */
class ElementComparator implements Comparator {

    /**
	 * 
	 */
    public ElementComparator() {
        super();
    }

    public int compare(Object o1, Object o2) {
        if (o1 instanceof OMSElement) {
            if (o2 instanceof OMSElement) {
                return ((OMSElement) o1).getLevel() - ((OMSElement) o2).getLevel();
            } else {
                return -1;
            }
        }
        if (o2 instanceof OMSElement) {
            return 1;
        } else {
            return 0;
        }
    }
}
