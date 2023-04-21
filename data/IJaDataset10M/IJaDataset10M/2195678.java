package jp.go.aist.six.oval.model.windows;

import jp.go.aist.six.oval.model.Component;
import jp.go.aist.six.oval.model.Family;
import jp.go.aist.six.oval.model.common.CheckEnumeration;
import jp.go.aist.six.oval.model.definitions.StateRefType;
import jp.go.aist.six.oval.model.definitions.SystemObjectRefType;
import jp.go.aist.six.oval.model.definitions.TestType;

/**
 * The file audit permissions test is used to check
 * the audit permissions associated with Windows files.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: FileAuditedPermissions53Test.java 2280 2012-04-04 02:05:07Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class FileAuditedPermissions53Test extends TestType {

    /**
     * Constructor.
     */
    public FileAuditedPermissions53Test() {
        this(null, 0);
    }

    public FileAuditedPermissions53Test(final String id, final int version) {
        this(id, version, null, null);
    }

    public FileAuditedPermissions53Test(final String id, final int version, final String comment, final CheckEnumeration check) {
        this(id, version, comment, check, null, null);
    }

    public FileAuditedPermissions53Test(final String id, final int version, final String comment, final CheckEnumeration check, final SystemObjectRefType object, final StateRefType[] stateList) {
        super(id, version, comment, check, object, stateList);
        _oval_family = Family.WINDOWS;
        _oval_component = Component.FILEAUDITEDPERMISSIONS53;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FileAuditedPermissions53Test)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "fileauditedpermissions53_test[" + super.toString() + "]";
    }
}
