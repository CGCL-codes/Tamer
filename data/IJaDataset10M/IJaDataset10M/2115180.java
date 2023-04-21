package jp.go.aist.six.oval.model.windows;

import jp.go.aist.six.oval.model.sc.EntityItemStringType;

/**
 * The EntityItemSharedResourceTypeType defines the different values 
 * that are valid for the type entity of a shared resource item.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: EntityItemSharedResourceTypeType.java 2192 2012-01-30 04:06:00Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class EntityItemSharedResourceTypeType extends EntityItemStringType {

    /**
     * Constructor.
     */
    public EntityItemSharedResourceTypeType() {
    }

    public EntityItemSharedResourceTypeType(final String content) {
        super(content);
    }

    @Override
    public void setContent(final String content) {
        if (content != null) {
            SharedResourceTypeEnumeration.fromValue(content);
        }
        super.setContent(content);
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
        if (!(obj instanceof EntityItemSharedResourceTypeType)) {
            return false;
        }
        return super.equals(obj);
    }
}
