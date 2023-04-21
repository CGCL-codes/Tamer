package reports.utility.datamodel.administration;

/**
 *
 * @author Administrator
 */
public class LIBRARY_HOLDINGS_MANAGER_1 {

    /** Creates a new instance of LIBRARY_HOLDINGS_MANAGER */
    public LIBRARY_HOLDINGS_MANAGER_1() {
    }

    public LIBRARY_HOLDINGS load(org.hibernate.Session session, LIBRARY_HOLDINGS_KEY pkey) {
        return (LIBRARY_HOLDINGS) session.load(LIBRARY_HOLDINGS.class, pkey);
    }
}
