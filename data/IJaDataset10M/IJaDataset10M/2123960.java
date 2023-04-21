package com.technoetic.tornado.engine;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.sql.Driver;
import java.sql.SQLException;
import org.junit.Test;
import com.technoetic.tornado.PersistenceException;

public class TestAbstractDatabaseEngine {

    private AbstractDatabaseEngine engine = new AbstractDatabaseEngine() {

        @Override
        public String getName() {
            return "TEST_ENGINE";
        }

        @Override
        public String getSelectForUpdateString() {
            return "TEST_UPDATE";
        }

        @Override
        public Driver loadDriver() throws SQLException {
            return mock(Driver.class);
        }
    };

    @Test
    public void getString() {
        assertThat(engine.toString(), is("<DatabaseEngine: TEST_ENGINE>"));
    }

    @Test
    public void handleBadDriverRequest() {
        try {
            engine.getDriver("java.lang.String");
            fail("no exception");
        } catch (SQLException e) {
            assertThat(e.getCause(), instanceOf(ClassCastException.class));
        }
    }

    @Test
    public void getDriver() throws SQLException {
        assertNotNull(engine.getDriver("org.postgresql.Driver"));
    }

    @Test
    public void translateException() {
        PersistenceException translateException = engine.translateException("FOO", new SQLException("TEST"));
        assertThat(translateException.getMessage(), is("FOO: TEST"));
        assertThat(translateException.getCause(), instanceOf(SQLException.class));
    }
}
