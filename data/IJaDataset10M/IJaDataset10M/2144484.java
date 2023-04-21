package tests.com.rise.rois.server;

import java.sql.SQLException;
import tests.com.rise.rois.DBTestCase;
import com.rise.rois.server.db.util.DBException;
import com.rise.rois.server.db.util.DBUtils;
import com.rise.rois.server.db.util.TestUtils;
import com.rise.rois.server.util.SessionUtil;
import com.rise.rois.server.util.Timer;

public class TestSessionCreationSpeed extends DBTestCase {

    public void testCreation() throws DBException, SQLException {
        TestUtils.dropAndRecreateTable(connection, DBUtils.INDIVIDUAL_SESSION, "individual_session_table.sql");
        assertTrue("Table Session can be found in Database", DBUtils.tableExistsInDB(DBUtils.INDIVIDUAL_SESSION));
        int computer_id = computerManager.createComputer("computer3", "10.10.10.3", "1113", true);
        int user_id = userManager.createUser("bob", "smith");
        Timer.start("Create Session");
        sessionManager.createSession(user_id, computer_id, SessionUtil.INDIVIDUAL_ID, "password", 60000, "speed test", "");
        Timer.stop();
        computer_id = computerManager.createComputer("computer4", "10.10.10.4", "1114", true);
        user_id = userManager.createUser("joe", "bloggs");
        Timer.start("Create Second Session");
        sessionManager.createSession(user_id, computer_id, SessionUtil.INDIVIDUAL_ID, "password", 60000, "speed test", "");
        Timer.stop();
    }
}
