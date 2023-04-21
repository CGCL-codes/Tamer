package tetz42.test;

import static tetz42.test.AutyDB.*;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class AutyDBTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_sample() throws Exception {
        useConnection(new Proc() {

            @Override
            public void run(Connection con) throws SQLException {
                prepareDB(getClass(), "test_sample", "T_MST_BUMON");
                assertDB(getClass(), "test_sample", "T_MST_BUMON");
            }
        });
    }

    @AfterClass
    public static void bye() throws SQLException {
        restoreAll();
    }
}
