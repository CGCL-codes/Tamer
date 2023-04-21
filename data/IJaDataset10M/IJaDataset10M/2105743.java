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
 * The fileeffectiverights53 object is used by a file effective rights test
 * to define the objects used to evalutate against the specified state.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: FileEffectiveRights53Object.java 2280 2012-04-04 02:05:07Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class FileEffectiveRights53Object extends SystemObjectType {

    private Set set;

    private FileEffectiveRights53Behaviors behaviors;

    private EntityObjectStringType filepath;

    private EntityObjectStringType path;

    private EntityObjectStringType filename;

    private EntityObjectStringType trustee_sid;

    private final Collection<Filter> filter = new ArrayList<Filter>();

    /**
     * Constructor.
     */
    public FileEffectiveRights53Object() {
        this(null, 0);
    }

    public FileEffectiveRights53Object(final String id, final int version) {
        super(id, version);
        _oval_family = Family.WINDOWS;
        _oval_component = Component.FILEEFFECTIVERIGHTS53;
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
    public void setBehaviors(final FileEffectiveRights53Behaviors behaviors) {
        this.behaviors = behaviors;
    }

    public FileEffectiveRights53Behaviors getBehaviors() {
        return behaviors;
    }

    /**
     */
    public void setFilepath(final EntityObjectStringType filepath) {
        this.filepath = filepath;
    }

    public EntityObjectStringType getFilepath() {
        return filepath;
    }

    /**
     */
    public void setPath(final EntityObjectStringType path) {
        this.path = path;
    }

    public EntityObjectStringType getPath() {
        return path;
    }

    /**
     */
    public void setFilename(final EntityObjectStringType filename) {
        this.filename = filename;
    }

    public EntityObjectStringType getFilename() {
        return filename;
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
        if (!(obj instanceof FileEffectiveRights53Object)) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "fileeffectiverights53_object[" + super.toString() + ", set=" + getSet() + ", behaviors=" + getBehaviors() + ", filepath=" + getFilepath() + ", path=" + getPath() + ", filename=" + getFilename() + ", trustee_sid=" + getTrusteeSid() + ", filter=" + getFilter() + "]";
    }
}
