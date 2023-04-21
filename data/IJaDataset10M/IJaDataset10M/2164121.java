package org.tmapiutils.impexp.cxtm;

import java.util.Comparator;
import org.tmapi.core.Association;

/**
 * A Comparator that compares two Association instances
 * according to the canonical sort order defined by 
 * the CXTM standard.
 */
public class AssociationComparator implements Comparator {

    private static final Comparator topicComp = new TopicComparator();

    private static final Comparator scopeComp = new SetComparator(topicComp);

    private static final Comparator rolesComp = new AssociationRoleComparator();

    public int compare(Object o1, Object o2) {
        Association a1 = (Association) o1;
        Association a2 = (Association) o2;
        if (o1 == null && o2 != null) return -1;
        if (o1 != null && o2 == null) return 1;
        if (o1 == null && o2 == null) return 0;
        int ret = topicComp.compare(a1.getType(), a2.getType());
        if (ret == 0) {
            ret = rolesComp.compare(a1.getAssociationRoles(), a2.getAssociationRoles());
        }
        if (ret == 0) {
            ret = rolesComp.compare(a1.getScope(), a2.getScope());
        }
        return 0;
    }
}
