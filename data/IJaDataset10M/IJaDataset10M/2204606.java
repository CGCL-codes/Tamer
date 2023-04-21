package jdbchelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A BatchFeeder is used in executing batch statements on the database.
 * The implementations of this interface feeds the supplied prepared
 * statements while iterating over a data structure.
 */
public interface BatchFeeder {

    /**
    * This method should return true if there are more elements in the data structure
    * to be iterated over.
    * @return True if there are remaining elements
    */
    public boolean hasNext();

    /**
    * This method should set the properties on the PreparedStatement for the current item in
    * the iteration.
    *
    * @param stmt The prepared statement to be filled in
    * @return Should return true if the current item needs to be added as a batch job. False to skip this item.
    * @throws SQLException May be thrown by the underlying Jdbc driver
    */
    public boolean feedStatement(PreparedStatement stmt) throws SQLException;
}
