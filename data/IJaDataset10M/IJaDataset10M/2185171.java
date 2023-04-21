package org.dllearner.core.owl;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.dllearner.utilities.owl.RoleComparator;

/**
 * Represents a hierarchy of datatype properties.
 * 
 * TODO: Currently, the role hierarchy pruning algorithm (analogous to the
 * subsumption hierarchy) is not implemented.  
 * 
 * @author Jens Lehmann
 *
 */
public class DatatypePropertyHierarchy {

    RoleComparator rc = new RoleComparator();

    TreeMap<DatatypeProperty, SortedSet<DatatypeProperty>> roleHierarchyUp;

    TreeMap<DatatypeProperty, SortedSet<DatatypeProperty>> roleHierarchyDown;

    TreeSet<DatatypeProperty> mostGeneralRoles = new TreeSet<DatatypeProperty>(rc);

    TreeSet<DatatypeProperty> mostSpecialRoles = new TreeSet<DatatypeProperty>(rc);

    public DatatypePropertyHierarchy(Set<DatatypeProperty> atomicRoles, TreeMap<DatatypeProperty, SortedSet<DatatypeProperty>> roleHierarchyUp, TreeMap<DatatypeProperty, SortedSet<DatatypeProperty>> roleHierarchyDown) {
        this.roleHierarchyUp = roleHierarchyUp;
        this.roleHierarchyDown = roleHierarchyDown;
        for (DatatypeProperty role : atomicRoles) {
            if (getMoreGeneralRoles(role).size() == 0) mostGeneralRoles.add(role);
            if (getMoreSpecialRoles(role).size() == 0) mostSpecialRoles.add(role);
        }
    }

    public SortedSet<DatatypeProperty> getMoreGeneralRoles(DatatypeProperty role) {
        return new TreeSet<DatatypeProperty>(roleHierarchyUp.get(role));
    }

    public SortedSet<DatatypeProperty> getMoreSpecialRoles(DatatypeProperty role) {
        return new TreeSet<DatatypeProperty>(roleHierarchyDown.get(role));
    }

    @Override
    public String toString() {
        String str = "";
        for (DatatypeProperty role : mostGeneralRoles) {
            str += toString(roleHierarchyDown, role, 0);
        }
        return str;
    }

    private String toString(TreeMap<DatatypeProperty, SortedSet<DatatypeProperty>> hierarchy, DatatypeProperty role, int depth) {
        String str = "";
        for (int i = 0; i < depth; i++) str += "  ";
        str += role.toString() + "\n";
        Set<DatatypeProperty> tmp = hierarchy.get(role);
        if (tmp != null) {
            for (DatatypeProperty c : tmp) str += toString(hierarchy, c, depth + 1);
        }
        return str;
    }

    /**
	 * @return The most general roles.
	 */
    public TreeSet<DatatypeProperty> getMostGeneralRoles() {
        return mostGeneralRoles;
    }

    /**
	 * @return The most special roles.
	 */
    public TreeSet<DatatypeProperty> getMostSpecialRoles() {
        return mostSpecialRoles;
    }
}
