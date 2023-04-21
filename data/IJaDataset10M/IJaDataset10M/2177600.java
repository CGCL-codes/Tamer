package redora.set;

import org.junit.Test;
import redora.db.Connection;
import redora.db.DatabaseFactory;
import redora.exceptions.ConnectException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DBConnectionTest {

    @Test
    public void configuration() throws ConnectException {
        assertEquals("utf8", DatabaseFactory.getDBProperty("default", "characterEncoding"));
    }

    @Test
    public void connection() throws Exception {
        Connection c = new Connection("default");
        assertNotNull("Empty connection returned", c);
        assertTrue(c.isConnected());
        c.close();
    }
}
