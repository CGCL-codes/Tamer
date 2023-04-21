package org.obe.runtime.strategy;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import org.obe.spi.runtime.AssignmentStrategy;

/**
 * An abstract assignment strategy that provides useful functionality to
 * subclasses.
 *
 * @author Adrian Price
 */
public abstract class AbstractAssignmentStrategy implements AssignmentStrategy {

    private static final Comparator _principalComparator = new Comparator() {

        public int compare(Object o1, Object o2) {
            return ((Principal) o1).getName().compareTo(((Principal) o2).getName());
        }
    };

    protected AbstractAssignmentStrategy() {
    }

    protected static Set expandGroup(Group group, Set participantSet) {
        if (participantSet == null) participantSet = new TreeSet(_principalComparator);
        for (Enumeration e = group.members(); e.hasMoreElements(); ) {
            Object member = e.nextElement();
            if (member instanceof Group) expandGroup((Group) member, participantSet); else participantSet.add(member);
        }
        return participantSet;
    }

    protected static Set expandGroup(Group group) {
        return expandGroup(group, null);
    }

    protected static Set expandGroups(Principal[] principals, Set participantSet) {
        if (participantSet == null) participantSet = new TreeSet(_principalComparator);
        for (int i = 0; i < principals.length; i++) {
            Principal principal = principals[i];
            if (principal instanceof Group) expandGroup((Group) principal, participantSet); else participantSet.add(principal);
        }
        return participantSet;
    }

    protected static Principal[] expandGroups(Principal[] principals) {
        Set participants = expandGroups(principals, null);
        if (principals.length != participants.size()) principals = new Principal[participants.size()];
        return (Principal[]) participants.toArray(principals);
    }
}
