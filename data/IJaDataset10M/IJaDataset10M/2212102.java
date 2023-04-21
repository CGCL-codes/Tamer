package gov.usda.gdpc;

import java.io.ObjectStreamException;
import java.util.*;

/**
 * This is a database element representing a TaxonParent.
 *
 * @author  terryc
 */
public class TaxonParent extends AbstractDBElement {

    private static final long serialVersionUID = 813L;

    public static final String TYPE = "TaxonParent";

    /**
     * TaxonParent constructor.
     *
     * @param properties properties of this TaxonParent.  The keys are
     * the properties (type: TaxonParentProperty) and the values are the
     * corresponding value.  Properties can not have a value equal to
     * null.  It is not allowed to have two keys that are the same.
     * The TaxonParentProperty.PARENT, TaxonParentProperty.CHILD,
     * TaxonParentProperty.RECURRENT, and TaxonParentProperty.ROLE
     * properties must be defined.
     */
    private TaxonParent(Map<TaxonParentProperty, Object> properties) {
        super(properties, TaxonParentProperty.class);
    }

    /**
     * This returns the TaxonParent instance given specified list
     * of properties.  A new TaxonParent will be created if its not
     * found in the cache.
     *
     * @param properties properties of this TaxonParent.  The keys are
     * the properties (type: TaxonParentProperty) and the values are the
     * corresponding value.  Properties can not have a value equal to
     * null.  It is not allowed to have two keys that are the same.
     * The TaxonParentProperty.PARENT, TaxonParentProperty.CHILD,
     * TaxonParentProperty.RECURRENT, and TaxonParentProperty.ROLE
     * properties must be defined.
     *
     * @return TaxonParent
     */
    public static TaxonParent getInstance(Map<TaxonParentProperty, Object> properties) {
        return new TaxonParent(properties);
    }

    /**
     * This always returns null since taxon parents are
     * not cached.
     *
     * @return null
     */
    public static TaxonParent getCachedInstance(Map<TaxonParentProperty, Object> properties) {
        return null;
    }

    /**
     * Get type of this element.
     *
     * @return type
     */
    public String getType() {
        return TYPE;
    }

    public UniqueKey getKey() {
        return null;
    }

    /**
     * Return list of possible properties that this
     * element can have.  Property tokens in list must
     * be in order according to their assigned indices.
     *
     * @return list of possible properties.
     */
    public List getPossibleProperties() {
        return TaxonParentProperty.getPropList();
    }

    public List getRequiredProperties() {
        return TaxonParentProperty.getRequiredPropList();
    }

    public int getNumRequiredProperties() {
        return TaxonParentProperty.getNumRequiredProperties();
    }

    public Taxon getChild() {
        return (Taxon) getProperty(TaxonParentProperty.CHILD);
    }

    public Taxon getParent() {
        return (Taxon) getProperty(TaxonParentProperty.PARENT);
    }

    public String getRole() {
        return (String) getProperty(TaxonParentProperty.ROLE);
    }

    public Integer getRecurrent() {
        return (Integer) getProperty(TaxonParentProperty.RECURRENT);
    }

    /**
     * Returns the name of this locus.
     *
     * @return the name
     */
    public String getName() {
        return toString();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        List props = TaxonParentProperty.getPropList();
        Iterator itr = props.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            TaxonParentProperty current = (TaxonParentProperty) itr.next();
            if (first) {
                first = false;
            } else {
                buffer.append(";");
            }
            buffer.append(current);
            buffer.append(": ");
            buffer.append(getProperty(current));
        }
        return buffer.toString();
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance(this);
    }
}
