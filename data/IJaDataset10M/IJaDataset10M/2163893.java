package reports.utility.datamodel.administration;

import java.util.Hashtable;
import reports.utility.StaticValues;

/**
 *
 * @author Administrator
 */
public class LIBRARY_MANAGER {

    /** Creates a new instance of LIBRARY_MANAGER */
    public LIBRARY_MANAGER() {
    }

    public LIBRARY load(org.hibernate.Session session, LIBRARY_KEY pkey) {
        return (LIBRARY) session.load(LIBRARY.class, pkey);
    }

    public Hashtable getAcquisitionSatelliteLibraries(String libid) {
        Hashtable ht = new Hashtable();
        ht.put(StaticValues.getInstance().getLoginLibraryId(), StaticValues.getInstance().getLoginLibraryName());
        return ht;
    }
}
