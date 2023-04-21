package jp.go.aist.six.oval.model.windows;

import jp.go.aist.six.oval.model.Component;
import jp.go.aist.six.oval.model.Family;
import jp.go.aist.six.oval.model.definitions.EntityObjectStringType;
import jp.go.aist.six.oval.model.definitions.Set;
import jp.go.aist.six.oval.model.definitions.SystemObjectType;

/**
 * The regkeyauditedpermissions object is used by a registry key audited permissions test
 * to define the objects used to evalutate against the specified state.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: RegkeyAuditedPermissionsObject.java 2280 2012-04-04 02:05:07Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 * @deprecated Deprecated as of version 5.3:
 *             Replaced by the regkeyauditedpermissions53 object and
 *             will be removed in version 6.0 of the language.
 */
@Deprecated
public class RegkeyAuditedPermissionsObject extends SystemObjectType {

    private Set set;

    private RegkeyAuditPermissionsBehaviors behaviors;

    private EntityObjectRegistryHiveType hive;

    private EntityObjectStringType key;

    private EntityObjectStringType trustee_name;

    /**
     * Constructor.
     */
    public RegkeyAuditedPermissionsObject() {
        this(null, 0);
    }

    public RegkeyAuditedPermissionsObject(final String id, final int version) {
        super(id, version);
        _oval_family = Family.WINDOWS;
        _oval_component = Component.REGKEYAUDITEDPERMISSIONS;
    }

    /**
     */
    public void setSet(final Set set) {
        this.set = set;
    }

    public Set getSet() {
        return set;
    }

    /**
     */
    public void setBehaviors(final RegkeyAuditPermissionsBehaviors behaviors) {
        this.behaviors = behaviors;
    }

    public RegkeyAuditPermissionsBehaviors getBehaviors() {
        return behaviors;
    }

    /**
     */
    public void setHive(final EntityObjectRegistryHiveType hive) {
        this.hive = hive;
    }

    public EntityObjectRegistryHiveType getHive() {
        return hive;
    }

    /**
     */
    public void setKey(final EntityObjectStringType key) {
        this.key = key;
    }

    public EntityObjectStringType getKey() {
        return key;
    }

    /**
     */
    public void setTrusteeName(final EntityObjectStringType trustee_name) {
        this.trustee_name = trustee_name;
    }

    public EntityObjectStringType getTrusteeName() {
        return trustee_name;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof RegkeyAuditedPermissionsObject)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "regkeyauditedpermissions_object[" + super.toString() + ", set=" + getSet() + ", behaviors=" + getBehaviors() + ", hive=" + getHive() + ", key=" + getKey() + ", trustee_name=" + getTrusteeName() + "]";
    }
}
