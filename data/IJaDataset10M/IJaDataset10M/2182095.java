package library.database;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import library.enums.Library;
import org.hibernate.SessionFactory;

public abstract class DefaultLibraryDatabaseTest extends AbstractLibraryDatabaseTest {

    protected AbstractLibraryDatabase delegateDb;

    {
        delegateDb = delegateDbInit();
    }

    protected abstract AbstractLibraryDatabase delegateDbInit();

    @Override
    protected Connection getConnection(Library library) throws SQLException {
        try {
            Method delegate = delegateDb.getClass().getDeclaredMethod("getConnection", Library.class);
            delegate.setAccessible(true);
            Connection conn = (Connection) delegate.invoke(delegateDb, library);
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Library getLibrary(Connection conn) throws SQLException {
        try {
            Method delegate = delegateDb.getClass().getDeclaredMethod("getLibrary", Connection.class);
            delegate.setAccessible(true);
            Library library = (Library) delegate.invoke(delegateDb, conn);
            return library;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Process getProcessSingleton() {
        try {
            Method delegate = delegateDb.getClass().getDeclaredMethod("getProcessSingleton");
            delegate.setAccessible(true);
            Process process = (Process) delegate.invoke(delegateDb);
            return process;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, SessionFactory> getSessionFactories() {
        try {
            Method delegate = delegateDb.getClass().getDeclaredMethod("getSessionFactories");
            delegate.setAccessible(true);
            Map<String, SessionFactory> facts = (Map<String, SessionFactory>) delegate.invoke(delegateDb);
            return facts;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setLibrary(Library library, Connection conn) throws SQLException {
        try {
            Method delegate = delegateDb.getClass().getDeclaredMethod("setLibrary", Library.class, Connection.class);
            delegate.setAccessible(true);
            delegate.invoke(delegateDb, library, conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
