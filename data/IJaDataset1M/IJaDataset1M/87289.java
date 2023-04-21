package dbgate;

import dbgate.ermanagement.context.IEntityContext;
import dbgate.ermanagement.exceptions.PersistException;
import java.sql.Connection;

/**
  writable interface
 */
public interface ServerDBClass extends ServerRODBClass, IDBClass {

    void persist(Connection con) throws PersistException;
}
