package org.decisiondeck.xmcda_oo.utils;

import java.util.Comparator;
import org.decisiondeck.xmcda_oo.structure.Criterion;

public class CompareCriteriaByIdAndName implements Comparator<Criterion> {

    @Override
    public int compare(final Criterion c1, final Criterion c2) {
        final int idComp = c1.getId().compareTo(c2.getId());
        if (idComp != 0) {
            return idComp;
        }
        if (c1.getName() == null) {
            if (c2.getName() == null) {
                return 0;
            }
            return -1;
        }
        if (c2.getName() == null) {
            return 1;
        }
        return c1.getName().compareTo(c2.getName());
    }
}
