package jp.go.aist.six.oval.model.windows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import jp.go.aist.six.oval.model.Component;
import jp.go.aist.six.oval.model.Family;
import jp.go.aist.six.oval.model.definitions.EntityObjectStringType;
import jp.go.aist.six.oval.model.definitions.Filter;
import jp.go.aist.six.oval.model.definitions.Set;
import jp.go.aist.six.oval.model.definitions.SystemObjectType;

/**
 * The regkeyauditedpermissions53 object is used by a registry key audited permissions test
 * to define the objects used to evalutate against the specified state.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: RegkeyAuditedPermissions53Object.java 2280 2012-04-04 02:05:07Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class RegkeyAuditedPermissions53Object extends SystemObjectType {

    private Set set;

    private RegkeyAuditPermissions53Behaviors behaviors;

    private EntityObjectRegistryHiveType hive;

    private EntityObjectStringType key;

    private EntityObjectStringType trustee_sid;

    private final Collection<Filter> filter = new ArrayList<Filter>();

    /**
     * Constructor.
     */
    public RegkeyAuditedPermissions53Object() {
        this(null, 0);
    }

    public RegkeyAuditedPermissions53Object(final String id, final int version) {
        super(id, version);
        _oval_family = Family.WINDOWS;
        _oval_component = Component.REGKEYAUDITEDPERMISSIONS53;
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
    public void setBehaviors(final RegkeyAuditPermissions53Behaviors behaviors) {
        this.behaviors = behaviors;
    }

    public RegkeyAuditPermissions53Behaviors getBehaviors() {
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
    public void setTrusteeSid(final EntityObjectStringType trustee_sid) {
        this.trustee_sid = trustee_sid;
    }

    public EntityObjectStringType getTrusteeSid() {
        return trustee_sid;
    }

    /**
     */
    public void setFilter(final Collection<? extends Filter> filters) {
        if (filter != filters) {
            filter.clear();
            if (filters != null && filters.size() > 0) {
                filter.addAll(filters);
            }
        }
    }

    public boolean addFilter(final Filter filter) {
        if (filter == null) {
            return false;
        }
        return this.filter.add(filter);
    }

    public Collection<Filter> getFilter() {
        return filter;
    }

    public Iterator<Filter> iterateFilter() {
        return filter.iterator();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof RegkeyAuditedPermissions53Object)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "regkeyauditedpermissions53_object[" + super.toString() + ", set=" + getSet() + ", behaviors=" + getBehaviors() + ", hive=" + getHive() + ", key=" + getKey() + ", trustee_sid=" + getTrusteeSid() + ", filter=" + getFilter() + "]";
    }
}
