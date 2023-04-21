package org.obe.server.j2ee.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.obe.server.j2ee.ejb.AbstractDAO;
import org.obe.server.j2ee.ejb.EJBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

final class EJBRepositoryDAO extends AbstractDAO {

    private static final EJBRepositoryDAO _theInstance = new EJBRepositoryDAO();

    private static final Log _logger = LogFactory.getLog(EJBRepositoryDAO.class);

    public static EJBRepositoryDAO getInstance() {
        return _theInstance;
    }

    private EJBRepositoryDAO() {
    }

    protected Log getLogger() {
        return _logger;
    }

    void executeSQLScript(String[] statements) throws SQLException {
        Connection con = EJBHelper.getConnection();
        for (int i = 0; i < statements.length; i++) {
            PreparedStatement stmt = con.prepareStatement(statements[i]);
            stmt.executeUpdate();
            stmt.close();
        }
        con.close();
    }
}
