package com.myopa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
import com.myopa.util.LoggerUtil;

/**
 * This class is an implementation of LoginDAO for a PostgreSQL database.
 *
 * @author Clint Burns <c1burns@users.sourceforge.net>
 */
public class LoginDAOImplPostgre extends LoginDAO {

    private static Logger logger = LoggerUtil.getInstance().getLogger();

    private static final String LOGINDAOERROR = "Exception in LoginDAO: ";

    @Override
    public boolean authenticateUser(String userId) throws Exception {
        boolean authPassed = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        final String query = "select 1 from account where userid=?;";
        try {
            connection = ConnectionUtility.getInstance().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                authPassed = true;
            }
        } catch (Exception e) {
            logger.error(LOGINDAOERROR + e);
        } finally {
            ConnectionUtility.closeJDBCObjects(rs, ps, connection);
        }
        return authPassed;
    }
}
