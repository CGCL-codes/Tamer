package sol.admin.systemmanagement.base.account;

import java.sql.*;
import java.util.*;
import sol.admin.systemmanagement.db.*;
import sol.admin.systemmanagement.util.*;

/**
 * Class implementing the methods for managing the Account class
 * as specified in the DatabaseObject Interface.
 * There is only one instance of this Manager (singleton).
 * @author Markus Hammori
 */
class AccountDatabaseObjectManager extends AccountDatabaseManager implements DatabaseObjectManager {

    protected static AccountDatabaseObjectManager _manager;

    public static AccountDatabaseObjectManager getInstance() {
        if (_manager == null) {
            _manager = new AccountDatabaseObjectManager();
        }
        return _manager;
    }

    protected AccountDatabaseObjectManager() {
        super();
    }

    public boolean correctSubjectClass(DatabaseObject o) {
        if (o != null) {
            return (o instanceof Account);
        }
        return false;
    }

    public String getManagerName() {
        return "AccountDatabaseObjectManager";
    }

    /**
     *
     */
    public void writeDB(DatabaseObject o) throws SQLException {
        if (correctSubjectClass(o)) {
            Account a = (Account) o;
            Statement stmt = getConnection().createStatement();
            String qString = "insert into accounts " + "(uid, login, passwd, shell, homedir," + "pwdlastchange, enabled, subnet, modemserial," + "modemip, ownConnection, pwdnextchange, pwdallowchange ) " + "values('" + a.getUID() + "','" + a.getLogin() + "','" + a.getPasswordData().getPassword() + "','" + a.getLoginShell() + "','" + a.getHomeDir() + "'," + DatabaseUtils.getDatabaseFormatedDateString(a.getPasswordData().getLastChange()) + ",'" + a.getEnabled() + "'," + DatabaseUtils.getDatabaseStringForMaybeNullString(a.getSubNet()) + ",'" + a.getModemID() + "'," + DatabaseUtils.getDatabaseStringForMaybeNullString(a.getModemIP()) + ",'" + a.getOwnConnection() + "'," + a.getPasswordData().getChangeIntervalInDays() + ",'" + a.getPasswordData().getAllowChange() + "')";
            Logger.getLogger().log("Account", qString, Logger.DEBUG);
            stmt.executeUpdate(qString);
            stmt.close();
        } else {
            logManagerMessage("Wrong usage of Manager for object of class " + o.getClass().getName(), Logger.ALERT);
            System.exit(1);
        }
    }

    public void updateDB(DatabaseObject o) throws SQLException {
        if (correctSubjectClass(o)) {
            Account a = (Account) o;
            Statement stmt = getConnection().createStatement();
            String qString = "UPDATE accounts SET " + "login = '" + a.getLogin() + "', " + "enabled= '" + a.getEnabled() + "', " + "deleted= '" + a.getDeleted() + "', " + "subnet= " + DatabaseUtils.getDatabaseStringForMaybeNullString(a.getSubNet()) + ", " + "homedir= '" + a.getHomeDir() + "', " + "pwdlastchange= " + DatabaseUtils.getDatabaseFormatedDateString(a.getPasswordData().getLastChange()) + ", " + "modemserial= '" + a.getModemID() + "', " + "shell= '" + a.getLoginShell() + "', " + "passwd= '" + a.getPasswordData().getPassword() + "'," + "modemip= " + DatabaseUtils.getDatabaseStringForMaybeNullString(a.getModemIP()) + ", " + "ownconnection= '" + a.getOwnConnection() + "', " + "pwdnextchange= " + a.getPasswordData().getChangeIntervalInDays() + ", " + "pwdallowchange= '" + a.getPasswordData().getAllowChange() + "' " + "WHERE uid = '" + a.getUID() + "'";
            Logger.getLogger().log("Account", qString, Logger.DEBUG);
            stmt.executeUpdate(qString);
            stmt.close();
        } else {
            logManagerMessage("Wrong usage of Manager for object of class " + o.getClass().getName(), Logger.ALERT);
            System.exit(1);
        }
    }

    public void deleteDB(DatabaseObject o) throws SQLException {
    }

    public Collection findByForeignKey(DatabaseObject o) {
        logManagerMessage("Wrong usage of Manager for foreign key: " + o.getClass().getName(), Logger.ALERT);
        return null;
    }
}
