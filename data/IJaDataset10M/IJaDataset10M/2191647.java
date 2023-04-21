package reports.utility.datamodel.circulation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import org.hibernate.Session;
import reports.utility.PrimaryKeys;

/**
 *
 * @author Administrator
 */
public class CIR_ODN_MANAGER {

    /** Creates a new instance of CIR_ODN_MANAGER */
    public CIR_ODN_MANAGER() {
    }

    public Integer getMaximumODNIdForTAID(Connection connection, Integer taid, Integer libraryId) {
        Integer maxODNID = null;
        try {
            Statement stat2 = connection.createStatement();
            ResultSet rs2 = stat2.executeQuery("select max(odn_id) from cir_odn where ta_id=" + taid + " and library_id=" + libraryId);
            int maxodnid = 0;
            while (rs2.next()) {
                maxodnid = rs2.getInt(1);
            }
            rs2.close();
            stat2.close();
            maxODNID = new Integer(maxodnid + 1);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return maxODNID;
    }

    public void saveCIRODN(Session session, CIR_ODN cir_odn) {
        session.save(cir_odn);
    }
}
