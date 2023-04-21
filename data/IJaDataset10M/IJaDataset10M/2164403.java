package syslog.database;

import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author deti
 */
public class JdbcMetadataDaoTest extends TestCase {

    public void testGetSeverity() {
        try {
            JdbcMetadataDao dao = new JdbcMetadataDao();
            List severity = dao.getFacility();
            System.out.println(severity.size());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
