package org.jmove.java.model;

import org.jmove.core.*;
import java.util.Set;

/**
 * An Array has an element type and a dimension count.
 * It is used as type of fields, local variables or parameters.
 * While the element type of an array belongs to the model,
 * the array itself does not!
 *
 * @author Michael Juergens
 * @version $Revision: 1.6 $
 */
public class Array implements Thing {

    /**
     * the element type
     */
    private Thing myElementType = null;

    /**
     * the dimension count
     */
    private int myDimensionCount = 1;

    public Thing getElementType() {
        return myElementType;
    }

    public void setElementType(Thing anElementType) {
        myElementType = anElementType;
    }

    public int getDimensionCount() {
        return myDimensionCount;
    }

    public void setDimensionCount(int aDimensionCount) {
        myDimensionCount = aDimensionCount;
    }

    /**
     * The id of an array type consists of the id of its element type
     * concatenated by "[]" for each dimension.
     *
     * @return id of array, i.e. "java.lang.String[]" for one-dimensional
     *         array of strings.
     */
    public String id() {
        if (myElementType == null) {
            return null;
        }
        StringBuffer id = new StringBuffer(myElementType.id());
        for (int i = 0; i < myDimensionCount; i++) {
            id.append("[]");
        }
        return id.toString();
    }

    /**
     * The initialization is left blank.
     *
     * @param model an array belongs not to a model
     * @param id    the id of an array is build of its element type
     *              and dimension count.
     * @see Array#id
     */
    public void init(Model model, String id) {
    }

    /**
     * An array is not part of model!
     *
     * @return null
     */
    public Model model() {
        return null;
    }

    /**
     * Every link to an array is resolved to a link of its elemnt type.
     *
     * @param aLink the Linkage to add
     */
    public void addLink(Link aLink) {
    }

    /**
     * Array type have no aspects!
     */
    public Aspect keyAspect(Object key) {
        return null;
    }

    /**
     * Array type have no aspects, so there's no binding of aspects.
     *
     * @param key    the key to bind with the given value
     * @param aspect the value
     */
    public void bindKeyAspect(Object key, Aspect aspect) {
    }

    /**
     * Array type have no aspects, so there's no unbinding of aspects.
     *
     * @param key the key to unbind
     */
    public void unbindKeyAspect(Object key) {
    }

    /**
     * Array type have no aspects, so the result is false.
     *
     * @return false
     */
    public boolean isAspect(Aspect aspect) {
        return false;
    }

    /**
     * Array type have no aspects, so the result is null.
     *
     * @return null
     */
    public Set aspects() {
        return null;
    }

    /**
     * Array type have no aspects, so the result is null.
     *
     * @return null
     */
    public Set aspects(Class aspectType) {
        return null;
    }

    /**
     * Array type have no aspects, so there's nothing done.
     *
     * @param aspect ignored
     */
    public void addAspect(Aspect aspect) {
    }

    /**
     * Array type have no aspects, so there's nothing done.
     *
     * @param aspect ignored
     */
    public void removeAspect(Aspect aspect) {
    }

    /**
     * Returns all links of the element type.
     *
     * @return A set of links
     */
    public Set links() {
        return myElementType.links();
    }

    /**
     * Returns all links of the element type accepted by a {@link LinkFilter}.
     *
     * @param filter The filter.
     * @return The set of links.
     */
    public Set links(LinkFilter filter) {
        return myElementType.links(filter);
    }

    /**
     * Returns all things which are linked with the element type and
     * are accepted by the specified link filter.
     *
     * @param filter
     * @return The set of links.
     */
    public ThingSet linked(LinkFilter filter) {
        return myElementType.linked(filter);
    }

    /**
     * This method is comparable with the simple linked() method, but lets specify
     * a more complex behavior by specifying the transitive and reflexive search attributes.
     * These attributes are interpreted as properties of a relation in a mathematical
     * sense. So the method returns all thing wich are in relation with this thing
     * and the relation is not only defined by the filter, but also by the transitive
     * and reflexive properties. The simple linked() method behaves like specifying
     * false for both additional parameters.
     *
     * @param filter     The filter defines the basic relationships.
     * @param transitive If this attribute is true then all things which are reachable by
     *                   applying the filter recursively starting with the element type will
     *                   be contained in the result set.
     * @param reflexive  If this attribute is true then the element type will also be in the
     *                   result set.
     * @return The set of links.
     */
    public ThingSet linked(LinkFilter filter, boolean transitive, boolean reflexive) {
        return myElementType.linked(filter, transitive, reflexive);
    }

    /**
     * Returns the first thing linked with the element type which is accepted by the
     * specified filter. This method makes sense if the filter represents
     * a typical to one relationship within a concrete Thing implementation.
     * So remember that to one relations are only a special case of relations
     * within this model.
     *
     * @param filter
     * @return A single thing or null if no linked thing mathched the filter.
     */
    public Thing linkedThing(LinkFilter filter) {
        return myElementType.linkedThing(filter);
    }

    /**
     * Test whether a thing is linked to the element type under the specified filter
     * conditions.
     *
     * @param thing
     * @param filter
     * @return true, if linked, otherwise false.
     */
    public boolean isLinkedTo(Thing thing, LinkFilter filter) {
        return myElementType.isLinkedTo(thing, filter);
    }

    public boolean dependsOn(Thing aThing) {
        return myElementType.dependsOn(aThing);
    }

    public boolean equals(Object anObject) {
        if (anObject instanceof Array) {
            Array other = (Array) anObject;
            if (getDimensionCount() == other.getDimensionCount()) {
                if (getElementType() != null) {
                    return getElementType().equals(other.getElementType());
                }
            }
        }
        return super.equals(anObject);
    }

    public int hashCode() {
        if (getElementType() != null) {
            return toString().hashCode();
        }
        return super.hashCode();
    }

    public String toString() {
        if (getElementType() != null) {
            StringBuffer result = new StringBuffer();
            result.append(getElementType().toString());
            for (int i = 0; i < getDimensionCount(); i++) {
                result.append("[]");
            }
            return result.toString();
        }
        return super.toString();
    }
}
