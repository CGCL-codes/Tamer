package jp.go.aist.six.oval.model.definitions;

import jp.go.aist.six.oval.model.common.DatatypeEnumeration;

/**
 * The EntityStateVersion type is extended by the entities
 * of an individual OVAL State.
 * This specific type describes simple version data.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: EntityStateVersionType.java 2077 2011-10-05 07:59:40Z nakamura5akihito $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class EntityStateVersionType extends EntityStateSimpleBaseType {

    public static final DatatypeEnumeration FIXED_DATATYPE = DatatypeEnumeration.VERSION;

    /**
     * Constructor.
     */
    public EntityStateVersionType() {
    }

    public EntityStateVersionType(final String content) {
        super(content);
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
        if (!(obj instanceof EntityStateVersionType)) {
            return false;
        }
        return super.equals(obj);
    }
}
