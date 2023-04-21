package jp.go.aist.six.oval.model.windows;

import jp.go.aist.six.oval.model.common.WindowsViewEnumeration;
import jp.go.aist.six.oval.model.definitions.EntityStateStringType;

/**
 * The EntityStateWindowsViewType restricts a string value
 * to a specific set of values: 32-bit and 64-bit.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: EntityStateWindowsViewType.java 2293 2012-04-05 08:56:20Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class EntityStateWindowsViewType extends EntityStateStringType {

    /**
     * Constructor.
     */
    public EntityStateWindowsViewType() {
    }

    @Override
    public void setContent(final String content) {
        if (content != null) {
            WindowsViewEnumeration.fromValue(content);
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
        if (!(obj instanceof EntityStateWindowsViewType)) {
            return false;
        }
        return super.equals(obj);
    }
}
