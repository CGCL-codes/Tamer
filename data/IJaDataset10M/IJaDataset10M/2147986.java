package org.tzi.use.gui.main.sorting;

import org.tzi.use.uml.mm.MAssociation;

/**
 *
 * @author <a href="mailto:gutsche@tzi.de">Fabian Gutsche</a>
 * @version $ProjectVersion: 0.393 $
 */
public class AlphabeticalAssociationComparator implements SortingComparator {

    private CompareUtil compareUtil;

    /**
     * Constructor of AlphabeticalAssociationComparator
     */
    public AlphabeticalAssociationComparator() {
        this.compareUtil = new CompareUtilImpl();
    }

    /**
     * Sets the compareUtil, which supplies the basic comparison
     * operations, to the given one
     * @param compareUtil the new compareUtil
     */
    public void setCompareUtil(final CompareUtil compareUtil) {
        this.compareUtil = compareUtil;
    }

    /**
     * Compares two associations by their name
     * @param object1 first association (has to be of type MAssociation)
     * @param object2 second association (has to be of type
     * MAssociation)
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    public int compare(final Object object1, final Object object2) {
        final String nameOfAssociation1 = ((MAssociation) object1).name();
        final String nameOfAssociation2 = ((MAssociation) object2).name();
        return compareUtil.compareString(nameOfAssociation1, nameOfAssociation2);
    }
}
