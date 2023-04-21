package com.be.dao;

import com.be.vo.ConnectionPropertiesVO;
import com.core.util.BaseDAO;
import java.sql.*;

public class AnnualFinancialStatementDAO extends BaseDAO {

    private String sql = "select sys.be_annual_financial_statement(?)";

    public AnnualFinancialStatementDAO(ConnectionPropertiesVO cp) {
        super(cp);
    }

    public void execute(long userID) throws SQLException {
        Connection conn = acquire();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, (int) userID);
                stmt.execute();
            } catch (SQLException e) {
                throw e;
            } finally {
                release(conn);
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
