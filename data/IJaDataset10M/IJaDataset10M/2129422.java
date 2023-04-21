package com.brunchboy.util.swing.relativelayout;

import java.util.*;

/**
 * A Constraint that represents a fixed offset from an attribute of another
 * component (or list of components). In the case of a list, the attribute
 * is treated as coming from the bounding box of all components in the list.
 * This is an immutable class, and so it can be freely copied and shared in
 * a multithreaded environment.
 * 
 * @author  James Elliott, jim@brunchboy.com
 * @version $Id: AttributeConstraint.java,v 1.3 2003/03/10 20:03:43 jim Exp $
 **/
public class AttributeConstraint implements Constraint {

    /**
     * Provides access to the CVS version of this class.
     **/
    public static final String VERSION = "$Id: AttributeConstraint.java,v 1.3 2003/03/10 20:03:43 jim Exp $";

    /**
     * Tracks the comma-delimited list of components relative to which this
     * constraint will be computed.
     **/
    protected final String anchorList;

    /**
     * Track the attribute of the anchor components which we'll use to compute
     * our value.
     **/
    private final AttributeType attribute;

    /**
     * Track the offset to be added to the anchor attribute when computing
     * our value.
     **/
    private final int offset;

    /**
     * Keep a cached list of our dependencies, in order to speed up layout.
     **/
    private final List dependencies;

    /**
     * Constructor sets the immutable fields.
     * 
     * @param anchorList a comma-delimited list of the names the component(s)
     *        relative to which this constraint is computed.
     * 
     * @param attribute the attribute of the anchor component(s) which is the
     *        source of this constraint's value. When multiple components are
     *        combined, the attribute is treated as coming from their bounding
     *        box.
     * 
     * @param offset the offset, in pixels, to be added to the anchor
     *        attribute value to compute this constraint's value.
     **/
    public AttributeConstraint(String anchorList, AttributeType attribute, int offset) {
        this.anchorList = anchorList;
        this.attribute = attribute;
        this.offset = offset;
        List deps = new ArrayList();
        for (StringTokenizer st = new StringTokenizer(anchorList, ","); st.hasMoreTokens(); ) {
            String anchor = st.nextToken().trim();
            deps.add(new Attribute(anchor, attribute));
        }
        dependencies = Collections.unmodifiableList(deps);
    }

    /**
     * Simplified constructor usable when offset is zero.
     * 
     * @param anchorList a comma-delimited list of the names the component(s)
     *        relative to which this constraint is computed.
     * 
     * @param attribute the attribute of the anchor component(s) which is the
     *        source of this constraint's value. When multiple components are
     *        combined, the attribute is treated as coming from their bounding
     *        box.
     **/
    public AttributeConstraint(String anchorList, AttributeType attribute) {
        this(anchorList, attribute, 0);
    }

    /**
     * Return the attributes on which this constraint depends. <p>
     *
     * @return a list of {@link Attribute}s which must be resolved before this
     *         constraint can be evaluated.
     **/
    public List getDependencies() {
        return dependencies;
    }

    /**
     * Compute the value of the constraint, given the specifications on
     * on which it is based. Any dependencies must have been resolved
     * prior to calling this method, or the method will fail. <p>
     * 
     * @param attributes provides read access to all existing component
     *        attributes, for use in evaluating this constraint..
     * 
     * @return the value represented by this constraint, assuming any
     *         attributes of the anchor component on which it depends have been
     *         resolved already.
     * 
     * @throws IllegalStateException if any dependencies are not yet resolved.
     **/
    public int getValue(AttributeSource attributes) {
        long tally = 0;
        if (attribute == AttributeType.BOTTOM || attribute == AttributeType.RIGHT || attribute == AttributeType.WIDTH || attribute == AttributeType.HEIGHT) {
            tally = Integer.MIN_VALUE;
        } else if (attribute == AttributeType.TOP || attribute == AttributeType.LEFT) {
            tally = Integer.MAX_VALUE;
        }
        for (ListIterator iter = dependencies.listIterator(); iter.hasNext(); ) {
            Attribute anchorAttr = (Attribute) iter.next();
            int curValue = attributes.getValue(anchorAttr);
            if (attribute == AttributeType.BOTTOM || attribute == AttributeType.RIGHT || attribute == AttributeType.WIDTH || attribute == AttributeType.HEIGHT) {
                tally = Math.max(tally, curValue);
            } else if (attribute == AttributeType.TOP || attribute == AttributeType.LEFT) {
                tally = Math.min(tally, curValue);
            } else {
                tally += curValue;
            }
        }
        if (attribute == AttributeType.HORIZONTAL_CENTER || attribute == AttributeType.VERTICAL_CENTER) {
            tally /= dependencies.size();
        }
        return (int) tally + offset;
    }

    /**
     * Provide a textual representation of the constraint for debugging
     * purposes.
     * 
     * @return the description of this attribute constraint.
     **/
    public String toString() {
        return "AttributeConstraint: {anchors=" + anchorList + "; attribute=" + attribute + "; offset=" + offset + '}';
    }
}
