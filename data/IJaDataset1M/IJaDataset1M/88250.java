package net.sourceforge.webflowtemplate.appuser.mgr.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import net.sourceforge.webflowtemplate.appuser.bean.AppUser;
import net.sourceforge.webflowtemplate.appuser.bean.SecurityRoleDatum;
import net.sourceforge.webflowtemplate.appuser.bean.wrapper.AppUserListWrapper;
import net.sourceforge.webflowtemplate.appuser.criteria.UserMgrSearchCriteria;
import net.sourceforge.webflowtemplate.constants.db.DBProduct;
import net.sourceforge.webflowtemplate.constants.db.postgresql.PostgresqlDBSchema;
import net.sourceforge.webflowtemplate.db.DBExceptionTranslator;
import net.sourceforge.webflowtemplate.utility.appuser.PostgresqlAppUserHelp;
import net.sourceforge.webflowtemplate.utility.appuser.AppUserHelp;
import net.sourceforge.webflowtemplate.utility.jdbc.DBResourceManagement;
import org.postgresql.jdbc4.Jdbc4Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

class PostgreSQLUserMgrDAO {

    private final Logger LOGGER = LoggerFactory.getLogger(PostgreSQLUserMgrDAO.class);

    PostgreSQLUserMgrDAO() {
    }

    AppUserListWrapper getUsers(Jdbc4Connection pJdbc4Connection, PostgresqlDBSchema pSchema, String pFunctionName, UserMgrSearchCriteria pUserMgrSearchCriteria) throws DataAccessException {
        final String METHOD_NAME = "getUsers()";
        final String DB_STATEMENT = "{? = call " + pSchema.getSchemaName() + "." + pFunctionName + "(?, ?, CASE ? WHEN 0 THEN NULL ELSE ? END, CASE ? WHEN 0 THEN NULL ELSE ? END, CASE ? WHEN 0 THEN NULL ELSE ? END)}";
        CallableStatement cstmt = null;
        ResultSet rs = null;
        AppUserListWrapper appUserListWrapper = new AppUserListWrapper();
        LOGGER.debug("{} -> called -> DB statement -> {}", METHOD_NAME, DB_STATEMENT);
        try {
            cstmt = pJdbc4Connection.prepareCall(DB_STATEMENT);
            LOGGER.debug("{} -> db statement prepared", METHOD_NAME);
            cstmt.registerOutParameter(1, Types.OTHER);
            cstmt.setString(2, pUserMgrSearchCriteria.getUsername());
            cstmt.setString(3, pUserMgrSearchCriteria.getIsAccountEnabled());
            cstmt.setInt(4, pUserMgrSearchCriteria.getDbrlId());
            cstmt.setInt(5, pUserMgrSearchCriteria.getDbrlId());
            cstmt.setInt(6, pUserMgrSearchCriteria.getLowerRecordLimit());
            cstmt.setInt(7, pUserMgrSearchCriteria.getLowerRecordLimit());
            cstmt.setInt(8, pUserMgrSearchCriteria.getUpperRecordLimit());
            cstmt.setInt(9, pUserMgrSearchCriteria.getUpperRecordLimit());
            LOGGER.debug("{} -> variables bound", METHOD_NAME);
            cstmt.execute();
            LOGGER.debug("{} -> db statement executed", METHOD_NAME);
            rs = (ResultSet) cstmt.getObject(1);
            appUserListWrapper.setApplicationUsers(PostgresqlAppUserHelp.getAppUserList(rs));
            appUserListWrapper.setCanUserDBActivityBeLogged(false);
            appUserListWrapper.setAreUserProfilesAvailable(false);
            LOGGER.debug("{} -> completed", METHOD_NAME);
            return appUserListWrapper;
        } catch (SQLException sqle) {
            LOGGER.error("{} -> exception -> {} -> bound variables: Username -> {}; Is account enabled -> {}; Is tracing enabled -> {}; DB Role -> {}", new Object[] { METHOD_NAME, sqle.toString(), pUserMgrSearchCriteria.getUsername(), pUserMgrSearchCriteria.getIsAccountEnabled(), pUserMgrSearchCriteria.getIsTracingEnabled(), Integer.toString(pUserMgrSearchCriteria.getDbrlId()) });
            throw new DBExceptionTranslator(DBProduct.POSTGRESQL).translate("Retrieve list of application users", null, sqle);
        } finally {
            DBResourceManagement.close(DBProduct.POSTGRESQL, rs);
            DBResourceManagement.close(DBProduct.POSTGRESQL, cstmt);
        }
    }

    List<SecurityRoleDatum> getUserRoles(Jdbc4Connection pJdbc4Connection, PostgresqlDBSchema pSchema, String pFunctionName, int pUserId) throws DataAccessException {
        final String METHOD_NAME = "getUserRoles(" + pUserId + ")";
        final String DB_STATEMENT = "{? = call " + pSchema.getSchemaName() + "." + pFunctionName + "(?)}";
        CallableStatement cstmt = null;
        ResultSet rs = null;
        List<SecurityRoleDatum> roles = null;
        LOGGER.debug("{} -> called -> DB statement -> {}", METHOD_NAME, DB_STATEMENT);
        try {
            cstmt = pJdbc4Connection.prepareCall(DB_STATEMENT);
            LOGGER.debug("{} -> db statement prepared", METHOD_NAME);
            cstmt.registerOutParameter(1, Types.OTHER);
            cstmt.setInt(2, pUserId);
            LOGGER.debug("{} -> variables bound", METHOD_NAME);
            cstmt.execute();
            LOGGER.debug("{} -> db statement executed", METHOD_NAME);
            rs = (ResultSet) cstmt.getObject(1);
            roles = AppUserHelp.getUserRoleList(rs);
            LOGGER.debug("{} -> completed", METHOD_NAME);
            return roles;
        } catch (SQLException sqle) {
            LOGGER.error("{} -> exception -> {} -> bound variables: User id -> {}", new Object[] { METHOD_NAME, sqle.toString(), Integer.toString(pUserId) });
            throw new DBExceptionTranslator(DBProduct.POSTGRESQL).translate(METHOD_NAME + " -> Retrieve user roles", null, sqle);
        } finally {
            DBResourceManagement.close(DBProduct.POSTGRESQL, rs);
            DBResourceManagement.close(DBProduct.POSTGRESQL, cstmt);
        }
    }

    void updateUsers(Jdbc4Connection pJdbc4Connection, PostgresqlDBSchema pSchema, String pFunctionName, List<AppUser> pApplicationUsers) throws DataAccessException {
        final String METHOD_NAME = "updateUsers()";
        final String DB_STATEMENT = "{call " + pSchema.getSchemaName() + "." + pFunctionName + "(?, ?, ?)}";
        CallableStatement cstmt = null;
        LOGGER.debug("{} -> called -> DB statement -> {}", METHOD_NAME, DB_STATEMENT);
        try {
            cstmt = pJdbc4Connection.prepareCall(DB_STATEMENT);
            LOGGER.debug("{} -> db statement prepared", METHOD_NAME);
            for (AppUser appUser : pApplicationUsers) {
                if (appUser.getHasBeenModified()) {
                    cstmt.setInt(1, appUser.getUserId());
                    cstmt.setString(2, appUser.getIsAccountEnabled());
                    cstmt.setTimestamp(3, appUser.getUpdatedTimestamp());
                    LOGGER.debug("{} -> variables bound -> User id -> {}; Is account enabled -> {}", new Object[] { METHOD_NAME, appUser.getUserId(), appUser.getIsAccountEnabled() });
                    cstmt.execute();
                    LOGGER.debug("{} -> db statement executed", METHOD_NAME);
                }
            }
            LOGGER.debug("{} -> completed", METHOD_NAME);
        } catch (SQLException sqle) {
            LOGGER.error("{} -> exception -> {}", METHOD_NAME, sqle.toString());
            throw new DBExceptionTranslator(DBProduct.POSTGRESQL).translate("Updating user details", null, sqle);
        } finally {
            DBResourceManagement.close(DBProduct.POSTGRESQL, cstmt);
        }
    }

    void updateUserRoles(Jdbc4Connection pJdbc4Connection, PostgresqlDBSchema pSchema, String pFunctionName, int pUserId, List<SecurityRoleDatum> pApplicationUserRoles) {
        final String METHOD_NAME = "updateUserRoles()";
        final String DB_STATEMENT = "{call " + pSchema.getSchemaName() + "." + pFunctionName + "(?, ?, ?)}";
        CallableStatement cstmt = null;
        LOGGER.debug("{} -> called -> DB statement -> {}", METHOD_NAME, DB_STATEMENT);
        try {
            cstmt = pJdbc4Connection.prepareCall(DB_STATEMENT);
            LOGGER.debug("{} -> db statement prepared", METHOD_NAME);
            for (SecurityRoleDatum role : pApplicationUserRoles) {
                if (role.getHasBeenModified()) {
                    cstmt.setInt(1, pUserId);
                    cstmt.setInt(2, role.getId());
                    cstmt.setString(3, (role.getIsAssignedToUser() == true ? "Y" : "N"));
                    LOGGER.debug("{} -> variables bound -> User id -> {}; Role id -> {}; Is role assigned to user -> {}", new Object[] { METHOD_NAME, pUserId, role.getId(), role.getIsAssignedToUser() });
                    cstmt.execute();
                    LOGGER.debug("{} -> db statement executed", METHOD_NAME);
                }
            }
        } catch (SQLException sqle) {
            LOGGER.error("{} -> exception -> {} -> bound variables: user ID -> {}", new Object[] { METHOD_NAME, sqle.toString(), pUserId });
            throw new DBExceptionTranslator(DBProduct.POSTGRESQL).translate(METHOD_NAME + " -> Update user's roles", null, sqle);
        } finally {
            DBResourceManagement.close(DBProduct.POSTGRESQL, cstmt);
        }
    }
}
