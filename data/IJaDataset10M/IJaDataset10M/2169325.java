package net.sourceforge.squirrel_sql.fw.dialects;

import org.junit.After;
import org.junit.Before;

public class PostgreSQLDialectTest extends AbstractDialectExtTest {

    @Before
    public void setUp() throws Exception {
        classUnderTest = new PostgreSQLDialectExt();
    }

    @After
    public void tearDown() throws Exception {
        classUnderTest = null;
    }
}
