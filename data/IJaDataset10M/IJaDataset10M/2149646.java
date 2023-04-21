package jp.go.aist.six.oval.model.definitions;

import jp.go.aist.six.oval.model.common.DatatypeEnumeration;

/**
 * The EntityStateBoolType is extended by the entities of an individual OVAL State.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: EntityStateBoolType.java 2085 2011-10-06 02:21:09Z nakamura5akihito $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class EntityStateBoolType extends EntityStateSimpleBaseType {

    public static final DatatypeEnumeration FIXED_DATATYPE = DatatypeEnumeration.BOOLEAN;

    /**
     * Constructor.
     */
    public EntityStateBoolType() {
    }

    public EntityStateBoolType(final int data) {
        setContent(String.valueOf(data));
    }

    @Override
    public void setDatatype(final DatatypeEnumeration datatype) {
        if (datatype != null && datatype != FIXED_DATATYPE) {
            throw new IllegalArgumentException("invalid datatype: " + datatype);
        }
        super.setDatatype(datatype);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntityStateBoolType)) {
            return false;
        }
        return super.equals(obj);
    }
}
