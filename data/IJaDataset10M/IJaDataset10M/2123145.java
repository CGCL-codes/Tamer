package jp.go.aist.six.oval.model.windows;

import jp.go.aist.six.oval.model.sc.EntityItemStringType;

/**
 * The EntityItemNamingContextType restricts a string value
 * to a specific set of values: domain, configuration, and schema.
 * These values describe the different naming context found withing Active Directory.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: EntityItemNamingContextType.java 2172 2011-12-27 04:20:24Z nakamura5akihito $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class EntityItemNamingContextType extends EntityItemStringType {

    /**
     * Constructor.
     */
    public EntityItemNamingContextType() {
    }

    public EntityItemNamingContextType(final String content) {
        super(content);
    }

    @Override
    public void setContent(final String content) {
        if (content != null) {
            NamingContextEnumeration.fromValue(content);
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
        if (!(obj instanceof EntityItemNamingContextType)) {
            return false;
        }
        return super.equals(obj);
    }
}
