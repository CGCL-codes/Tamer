package net.sourceforge.webflowtemplate.reporting.securitymatrix.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import net.sourceforge.webflowtemplate.constants.db.DBResource;
import net.sourceforge.webflowtemplate.constants.db.oracle.OracleDBPackage;
import net.sourceforge.webflowtemplate.constants.security.SecurityRole;
import net.sourceforge.webflowtemplate.db.DBExceptionTranslator;
import net.sourceforge.webflowtemplate.db.connection.AbstractDBConnectionFactory;
import net.sourceforge.webflowtemplate.db.dao.oracle.AbstractOracleDAOImpl;
import net.sourceforge.webflowtemplate.reporting.securitymatrix.criteria.SecurityMatrixReportCriteria;
import net.sourceforge.webflowtemplate.reporting.securitymatrix.dao.SecurityMatrixReportDAO;
import net.sourceforge.webflowtemplate.utility.jdbc.DBResourceManagement;

class OracleSecurityMatrixReportDAOImpl extends AbstractOracleDAOImpl implements SecurityMatrixReportDAO {

    private Logger LOGGER = LoggerFactory.getLogger(OracleSecurityMatrixReportDAOImpl.class);

    OracleSecurityMatrixReportDAOImpl(DBResource pResource, String pUsername, String pPassword) throws DataAccessException {
        super(AbstractDBConnectionFactory.getConnection(pResource, pUsername, pPassword));
        LOGGER.debug("Constructed -> ({}, {}, password)", pResource, pUsername);
    }

    OracleSecurityMatrixReportDAOImpl(Connection pConnection) throws DataAccessException {
        super(pConnection);
        LOGGER.debug("Constructed -> (Connection)");
    }

    OracleSecurityMatrixReportDAOImpl(OracleConnection pOracleConnection) throws DataAccessException {
        super(pOracleConnection);
        LOGGER.debug("Constructed -> (OracleConnection)");
    }

    public Result getSecurityMatrixByRoleReport(SecurityMatrixReportCriteria pSecurityMatrixReportCriteria) throws DataAccessException {
        final String METHOD_NAME = "getSecurityMatrixByRoleReport";
        final OracleDBPackage DB_PACKAGE = OracleDBPackage.PKSECURITYMATRIXBYROLEREPORT;
        final SecurityRole DB_ROLE = DB_PACKAGE.getSecurityRole();
        final String DB_STATEMENT = " BEGIN  " + "   ? := " + DB_PACKAGE.getOwningUser().getDatabaseUser() + "." + DB_PACKAGE.getPackageName() + ".fnGetReport" + "(" + "  p_in_aur_id  => CASE ? WHEN 0 THEN NULL ELSE ? END" + ", p_in_dbrl_id => CASE ? WHEN 0 THEN NULL ELSE ? END" + ");" + " END;   ";
        final int AUR_ID = pSecurityMatrixReportCriteria.getAurId();
        final int DBRL_ID = pSecurityMatrixReportCriteria.getDbrlId();
        OracleCallableStatement oCstmt = null;
        ResultSet rs = null;
        LOGGER.debug("{} -> called -> DB statement -> {}", METHOD_NAME, DB_STATEMENT);
        try {
            enableRole(DB_ROLE);
            oCstmt = (OracleCallableStatement) getConnection().prepareCall(DB_STATEMENT);
            LOGGER.debug("{} -> db statement prepared", METHOD_NAME);
            oCstmt.registerOutParameter(1, OracleTypes.CURSOR);
            oCstmt.setInt(2, AUR_ID);
            oCstmt.setInt(3, AUR_ID);
            oCstmt.setInt(4, DBRL_ID);
            oCstmt.setInt(5, DBRL_ID);
            LOGGER.debug("{} -> variables bound", METHOD_NAME);
            oCstmt.execute();
            rs = (ResultSet) oCstmt.getCursor(1);
            LOGGER.debug("{} -> refcursor retrieved", METHOD_NAME);
            Result rst = ResultSupport.toResult(rs);
            LOGGER.debug("{} -> completed", METHOD_NAME);
            return rst;
        } catch (SQLException sqle) {
            LOGGER.error("{} -> exception -> {} -> bound variables -> user id -> {}; DB DB_ROLE id -> {}", new Object[] { METHOD_NAME, sqle.toString(), AUR_ID, DBRL_ID });
            throw new DBExceptionTranslator(getDBProduct()).translate(METHOD_NAME + " -> Run DB_ROLE-user-matrix report", null, sqle);
        } finally {
            DBResourceManagement.close(getDBProduct(), rs);
            DBResourceManagement.close(getDBProduct(), oCstmt);
        }
    }

    public Result getSecurityMatrixByUserReport(SecurityMatrixReportCriteria pSecurityMatrixReportCriteria) throws DataAccessException {
        final String METHOD_NAME = "getSecurityMatrixByUserReport";
        final OracleDBPackage DB_PACKAGE = OracleDBPackage.PKSECURITYMATRIXBYUSERREPORT;
        final SecurityRole DB_ROLE = DB_PACKAGE.getSecurityRole();
        final String DB_STATEMENT = " BEGIN  " + "   ? := " + DB_PACKAGE.getOwningUser().getDatabaseUser() + "." + DB_PACKAGE.getPackageName() + ".fnGetReport" + "(" + "  p_in_aur_id  => CASE ? WHEN 0 THEN NULL ELSE ? END" + ", p_in_dbrl_id => CASE ? WHEN 0 THEN NULL ELSE ? END" + ");" + " END;   ";
        final int AUR_ID = pSecurityMatrixReportCriteria.getAurId();
        final int DBRL_ID = pSecurityMatrixReportCriteria.getDbrlId();
        OracleCallableStatement oCstmt = null;
        ResultSet rs = null;
        LOGGER.debug("{} -> called -> DB statement -> {}", METHOD_NAME, DB_STATEMENT);
        try {
            enableRole(DB_ROLE);
            oCstmt = (OracleCallableStatement) getConnection().prepareCall(DB_STATEMENT);
            LOGGER.debug("{} -> db statement prepared", METHOD_NAME);
            oCstmt.registerOutParameter(1, OracleTypes.CURSOR);
            oCstmt.setInt(2, AUR_ID);
            oCstmt.setInt(3, AUR_ID);
            oCstmt.setInt(4, DBRL_ID);
            oCstmt.setInt(5, DBRL_ID);
            LOGGER.debug("{} -> variables bound", METHOD_NAME);
            oCstmt.execute();
            rs = (ResultSet) oCstmt.getCursor(1);
            LOGGER.debug("{} -> refcursor retrieved", METHOD_NAME);
            Result rst = ResultSupport.toResult(rs);
            LOGGER.debug("{} -> completed", METHOD_NAME);
            return rst;
        } catch (SQLException sqle) {
            LOGGER.error("{} -> exception -> {} -> bound variables -> user id -> {}; DB DB_ROLE id -> {}", new Object[] { METHOD_NAME, sqle.toString(), AUR_ID, DBRL_ID });
            throw new DBExceptionTranslator(getDBProduct()).translate(METHOD_NAME + " -> Run user-DB_ROLE-matrix report", null, sqle);
        } finally {
            DBResourceManagement.close(getDBProduct(), rs);
            DBResourceManagement.close(getDBProduct(), oCstmt);
        }
    }
}
