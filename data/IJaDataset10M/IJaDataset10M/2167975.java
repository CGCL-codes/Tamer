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
 * The serviceeffectiverights object is used by the serviceeffectiverights test
 * to define the objects used to evalutate against the specified state.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: VolumeObject.java 2280 2012-04-04 02:05:07Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class VolumeObject extends SystemObjectType {

    private Set set;

    private EntityObjectStringType rootpath;

    private final Collection<Filter> filter = new ArrayList<Filter>();

    /**
     * Constructor.
     */
    public VolumeObject() {
        this(null, 0);
    }

    public VolumeObject(final String id, final int version) {
        super(id, version);
        _oval_family = Family.WINDOWS;
        _oval_component = Component.VOLUME;
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
    public void setRootpath(final EntityObjectStringType rootpath) {
        this.rootpath = rootpath;
    }

    public EntityObjectStringType getRootpath() {
        return rootpath;
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
        if (!(obj instanceof VolumeObject)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "volume_object[" + super.toString() + ", set=" + getSet() + ", rootpath=" + getRootpath() + ", filter=" + getFilter() + "]";
    }
}
