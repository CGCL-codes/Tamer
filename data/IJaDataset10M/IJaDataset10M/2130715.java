package org.goet.datamodel;

import java.util.*;

/**
 * The representation of a Node's type, as per the DAML+OIL definition of a
 * Class. A NodeClass is itself a {@link Node} of type
 * {@link org.goet.datamodel.reflect.ClassClass ClassClass} (and possibly
 * other types).<p>
 *
 * This class provides a number of convenience methods for fetching predefined
 * NodeClass info (for example, the NodeClass's super classes). All of this
 * info is available by querying the NodeClass through normal
 * {@link Node#getPropertyValues} calls.
 *
 * @author John Richter
 * @see org.goet.datamodel.Node
 * @see org.goet.datamodel.Property
 * @see org.goet.datamodel.Restriction
 * @see <a href='http://www.daml.org/2001/03/reference.html#Class-def'>http://www.daml.org/2001/03/reference.html#Class-def</a>
 */
public interface NodeClass extends Type, Node {

    /**
     * Returns the super classes of this NodeClass (and its super classes, if
     * specified)
     *
     * @return super classes
     * @param closed whether to close the result over all the super classes of
     * this NodeClass
     * @see <a href='http://www.daml.org/2001/03/reference.html#subClassOf-def'>http://www.daml.org/2001/03/reference.html#subClassOf-def</a>
     */
    public Set getSuperClasses(boolean closed);

    /**
     * Returns whether or not this NodeClass is abstract. Abstract classes
     * may not be instantiated. Note that this property is not supported by
     * DAML+OIL, but is a GOET extension.
     *
     * @return whether or not this NodeClass is abstract
     */
    public boolean isAbstract();

    /**
     * Returns the allowed values for this class, if this is an enumerated
     * class. If it is not, null is returned.
     * @return the allowed values for this class
     * @see <a href='http://www.daml.org/2001/03/reference.html#oneOf-def'>http://www.daml.org/2001/03/reference.html#oneOf-def</a>
     */
    public Set getAllowedValues();

    /**
     * This method returns a set of all the properties that are defined for 
     * instances of this NodeClass. This includes the properties that list
     * this NodeClass (or all of its super classes, if specified) as the
     * domain, AND all properties that have a null domain. This method should
     * not be confused with {@link Node#getProperties}. This method returns the
     * properties that are defined BY this NodeClass, not defined FOR this
     * NodeClass.<p>
     *
     * @return a set of {@link Property Properties}
     * @param closed whether to close the result over all the super classes
     * of this NodeClass
     */
    public Set getProperties(boolean closed);

    /**
     * Returns a set of restrictions defined on this NodeClass
     * (or all of its super classes, if specified) for a certain
     * Property.
     *
     * @return a set of {@link Restriction Restrictions}
     * @param closed whether to close the result over all the super classes
     * of this NodeClass
     */
    public Set getRestrictions(Property property, boolean closed);
}
