package org.objectstyle.cayenne.property;

/**
 * A Property that represents an "arc" connecting source node to the target node in the
 * graph.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public interface ArcProperty extends Property {

    /**
     * Returns a complimentary reverse ArcProperty or null if no reverse arc exists.
     */
    ArcProperty getComplimentaryReverseArc();

    /**
     * Returns a ClassDescriptor for the type of graph nodes pointed to by this arc
     * property. Note that considering that a target object may be a subclass of the class
     * handled by the descriptor, users of this method may need to call
     * {@link ClassDescriptor#getSubclassDescriptor(Class)} before using the descriptor to
     * access objects.
     */
    ClassDescriptor getTargetDescriptor();

    /**
     * Returns whether a target node connected to a given object is an unresolved fault.
     */
    boolean isFault(Object target);
}
