package in.co.codedoc.sql;

import java.sql.Connection;

public interface DBConnectionProvider {

    Connection GetConnection();
}
