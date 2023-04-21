package hidb2.kern;

import java.sql.SQLException;

/**
 * Interface for extended types. Extended types are usually 'blob' or multivalued attributs 
 * that can not be stored in a single column.
 * 
 * Moreover, each AttrExtend type shall implements the following static methods:
 * - public static String[] getTableDecl()
 * - public static String[] getTableDelete()
 * - public static StatKit getStatements(DataStore das)
 * 
 */
public interface AttrExtend extends HIDBConst {

    /**
   * Set the ID (at creation time).
   * @param id
   */
    public void setID(long id);

    /**
   * Retrieve the uniq ID in the specific storage.
   * @return
   */
    public long getID();

    /**
   * Set the Checker (once at creation time).
   * @param checker
   */
    public void setChecker(AttrChecker chercker);

    public StatusCycle getStatus();

    public void setStatus(StatusCycle s);

    /**
   * Write (create, update or delete) the value in its own datatable.
   * ID is not always set.
   * 
   * @param das
   * @param vi
   * @return
   * @throws SQLException
   */
    public int write(DataStore das, ValuedInstance vi) throws SQLException;

    /**
   * Retrieve the attribut from its own datatable. Its uniq ID has been read and set 
   * at memory object creation time.
   * 
   * @param das
   * @return
   * @throws SQLException
   */
    public int read(DataStore das) throws SQLException;
}
