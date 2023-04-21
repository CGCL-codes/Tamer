package onepoint.persistence;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;
import java.util.TimeZone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import onepoint.project.modules.report.Report;
import onepoint.project.modules.report.ReportMethod;
import org.hibernate.Hibernate;

@Report
@XmlAccessorType(XmlAccessType.NONE)
public abstract class OpObject extends OpLocatableObject implements OpObjectIfc {

    public static final String CREATED = "Created";

    public static final String MODIFIED = "Modified";

    public static final long TRANSIENT_ID = 0;

    private Timestamp created;

    private Timestamp modified;

    /**
    * if somebody finds a better place for this one...
    * @param objects
    * @return
    */
    public static Collection<Long> getIdsFromObjects(Collection<? extends OpLocatable> objects) {
        Collection<Long> ids = new ArrayList<Long>(objects != null ? objects.size() : 0);
        if (objects == null) {
            return ids;
        }
        for (OpLocatable o : objects) {
            ids.add(new Long(o.getId()));
        }
        return ids;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @ReportMethod
    public Timestamp getCreated() {
        return created;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    @ReportMethod
    public Timestamp getModified() {
        return modified;
    }

    public static Collection<String> getLocatorStringsFromObjects(Collection<? extends OpLocatable> objects) {
        Collection<String> locs = new ArrayList<String>(objects.size());
        for (OpLocatable o : objects) {
            locs.add(o.locator());
        }
        return locs;
    }

    /**
    * Returns a timestamp with the current time in GMT format.
    * @return a <code>Timestamp</code> with the current time in GMT format.
    */
    public static Timestamp getCurrentTimeGMT() {
        TimeZone gmtTimezone = TimeZone.getTimeZone("GMT");
        if (!TimeZone.getDefault().equals(gmtTimezone)) {
            TimeZone.setDefault(gmtTimezone);
        }
        Calendar calendar = Calendar.getInstance(gmtTimezone);
        return new Timestamp(calendar.getTime().getTime());
    }

    public static double nullSafeDouble(Double d) {
        return d == null ? 0d : d.doubleValue();
    }

    public static long nullSafeID(OpSiteObjectIfc obj) {
        return obj == null ? TRANSIENT_ID : obj.getId();
    }

    public boolean isPersisted() {
        return getId() != TRANSIENT_ID;
    }

    public static boolean nullSafeEquals(Object o1, Object o2) {
        return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
    }

    public static Integer compareNull(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return null;
        }
        if (o1 == null) {
            return Integer.valueOf(1);
        }
        if (o2 == null) {
            return Integer.valueOf(-1);
        }
        return Integer.valueOf(0);
    }

    public OpObject() {
        super();
    }

    public static boolean isInitialized(Object thing) {
        if (thing == null) {
            return false;
        }
        return Hibernate.isInitialized(thing);
    }

    public static <C> boolean addIfInitialized(Set<C> coll, C obj) {
        return coll.add(obj);
    }

    public static <C> boolean removeIfInitialized(Set<C> coll, C obj) {
        return coll.remove(obj);
    }
}
