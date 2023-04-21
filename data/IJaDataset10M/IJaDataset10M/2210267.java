package net.sf.jardevil.reporting.grouping;

import net.sf.jardevil.ClassId;
import net.sf.jardevil.reporting.DependencyEndpoint;
import net.sf.jardevil.scanning.ClassSelectionPattern;

/**
 * Groups classes by their classpath location.
 * Returns DependencyParts of type TYPE_CLASSPATH_ENTRY
 * 
 * @author Achim Huegen
 */
public class LocationGroup extends Group {

    public LocationGroup(ClassSelectionPattern selectionPattern) {
        super(selectionPattern);
    }

    /**
     * @see net.sf.jardevil.reporting.grouping.Group#process(net.sf.jardevil.ClassId)
     */
    public DependencyEndpoint process(ClassId classId) {
        return new DependencyEndpoint(DependencyEndpoint.TYPE_CLASSPATH_ENTRY, classId.getLocation().getName());
    }
}
