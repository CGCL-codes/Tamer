package libs.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class prepared {

    private PreparedStatement prep;

    private Connection conn;

    public prepared(String statement, Connection conn) throws Exception {
        prep = conn.prepareStatement(statement);
        this.conn = conn;
    }

    public void addBatch() throws Exception {
        prep.addBatch();
    }

    public void setString(int num, String s) throws Exception {
        prep.setString(num, s);
    }

    public void executeBatch() throws Exception {
        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);
    }
}
