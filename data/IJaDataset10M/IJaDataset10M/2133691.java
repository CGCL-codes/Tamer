package net.merfer.vnv.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractQuery extends AbstractStatement implements Query {

    public AbstractQuery(String sql) {
        super(sql);
    }

    public AbstractQuery(String sql, List args) {
        super(sql, args);
    }

    protected Object execute(PreparedStatement stmt) throws Exception {
        return handleResults(stmt.executeQuery());
    }

    public Object executeQuery() throws Exception {
        return execute();
    }

    protected abstract Object handleResults(ResultSet rs) throws SQLException;
}
