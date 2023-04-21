package org.bug4j.server.store;

import org.bug4j.server.store.jdbc.JdbcStore;
import org.junit.Assert;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestingStore extends JdbcStore {

    public static final String MEM_URL = "jdbc:derby:memory:tst;create=true";

    private String _url;

    private TestingStore(String url) {
        _url = url;
        initialize();
    }

    public static TestingStore createMemStore() {
        return new TestingStore(MEM_URL);
    }

    public static TestingStore createEmbeddedStore() {
        final File databasePath = getDatabasePath();
        Assert.assertNotNull("Could not find the database, started from " + new File(".").getAbsolutePath(), databasePath);
        return new TestingStore("jdbc:derby:" + databasePath.getAbsolutePath());
    }

    private static File getDatabasePath() {
        File base = new File(".").getAbsoluteFile();
        while (base != null) {
            final File file = new File(base, "out/test/ui-jsp/bug4jdb");
            if (file.exists()) {
                return file;
            }
            base = base.getParentFile();
        }
        return null;
    }

    @Override
    protected Connection getConnection() {
        final Connection ret;
        try {
            ret = DriverManager.getConnection(_url);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return ret;
    }
}
