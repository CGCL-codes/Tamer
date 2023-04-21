package jp.go.aist.six.oval.model.definitions;

import jp.go.aist.six.oval.model.common.CheckEnumeration;
import jp.go.aist.six.oval.model.common.DatatypeEnumeration;
import jp.go.aist.six.oval.model.common.OperationEnumeration;

/**
 * The EntityStateString type is extended by the entities
 * of an individual OVAL State.
 * This specific type describes simple string data.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: EntityStateStringType.java 2013 2011-09-21 01:47:22Z nakamura5akihito $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class EntityStateStringType extends EntityStateSimpleBaseType {

    public static final DatatypeEnumeration FIXED_DATATYPE = DatatypeEnumeration.STRING;

    /**
     * Constructor.
     */
    public EntityStateStringType() {
    }

    public EntityStateStringType(final String content) {
        super(content);
    }

    public EntityStateStringType(final DatatypeEnumeration datatype, final OperationEnumeration operation, final Boolean mask, final String var_ref, final CheckEnumeration var_check, final String content) {
        super(datatype, operation, mask, var_ref, var_check, content);
    }

    public EntityStateStringType(final String datatype, final String operation, final Boolean mask, final String var_ref, final String var_check, final String content) {
        super(datatype, operation, mask, var_ref, var_check, content);
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
        if (!(obj instanceof EntityStateStringType)) {
            return false;
        }
        return super.equals(obj);
    }
}
