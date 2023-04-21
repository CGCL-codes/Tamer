package jssia.database.mysql.transactions;

import java.sql.SQLException;

public interface Transaction<T> {

    public T execute() throws SQLException;
}
