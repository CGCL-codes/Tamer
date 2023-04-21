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
 * The group_sid object is used by a group_test
 * to define the specific group(s) (identified by SID) to be evaluated.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: GroupSidObject.java 2280 2012-04-04 02:05:07Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class GroupSidObject extends SystemObjectType {

    private Set set;

    private EntityObjectStringType group_sid;

    private final Collection<Filter> filter = new ArrayList<Filter>();

    /**
     * Constructor.
     */
    public GroupSidObject() {
        this(null, 0);
    }

    public GroupSidObject(final String id, final int version) {
        super(id, version);
        _oval_family = Family.WINDOWS;
        _oval_component = Component.GROUP_SID;
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
    public void setGroupSid(final EntityObjectStringType group_sid) {
        this.group_sid = group_sid;
    }

    public EntityObjectStringType getGroupSid() {
        return group_sid;
    }

    /**
     */
    public void setFilter(final Collection<? extends Filter> filterList) {
        if (filter != filterList) {
            filter.clear();
            if (filterList != null && filterList.size() > 0) {
                filter.addAll(filterList);
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
        if (!(obj instanceof GroupSidObject)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "group_sid_object[" + super.toString() + ", set" + getSet() + ", group_sid=" + getGroupSid() + ", filter=" + getFilter() + "]";
    }
}
