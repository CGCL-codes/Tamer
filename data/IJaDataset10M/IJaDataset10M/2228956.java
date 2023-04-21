package drjava.freegle.tests;

import java.sql.SQLException;
import drjava.freegle.*;

/**
 *
 *
 * @version $Revision: 1.2 $
 * @author $Author: drjava $
 */
public interface DBFactory {

    public DBInterface createDB() throws SQLException;
}
