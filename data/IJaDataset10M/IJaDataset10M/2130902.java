package edu.washington.mysms.server.extend;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import edu.washington.mysms.Message;
import edu.washington.mysms.coding.ColumnsDefinition;
import edu.washington.mysms.coding.ResultColumn;
import edu.washington.mysms.coding.ResultRow;
import edu.washington.mysms.coding.ResultTable;
import edu.washington.mysms.security.SqlAccount;
import edu.washington.mysms.security.ValidatedLogin;
import edu.washington.mysms.server.DatabaseInterface;
import edu.washington.mysms.server.InapplicableException;
import edu.washington.mysms.server.Indexed;
import edu.washington.mysms.server.Initialize;
import edu.washington.mysms.server.extend.DDFRepresentation.Subscription;

/**
 * A subscribe checker that assumes a particular MySMS
 * system database configuration.
 * 
 * @author Anthony Poon
 */
public class DatabaseSubscribeChecker implements SubscribeChecker {

    private SqlAccount mysmsSystemAccount;

    private DatabaseDDFChecker ddfChecker;

    public DatabaseSubscribeChecker(Initialize i, SqlAccount sysAccount) throws NumberFormatException, RemoteException, NotBoundException {
        this(sysAccount, new DatabaseDDFChecker(i, sysAccount));
    }

    private DatabaseSubscribeChecker(SqlAccount sysAccount, DatabaseDDFChecker ddfChecker) {
        this.mysmsSystemAccount = sysAccount;
        this.ddfChecker = ddfChecker;
    }

    /**
	 * Check to see if the given query is a subscribe statement and handle it
	 * accordingly if it is.  Returns a ResultTable if the subscribe was successfully
	 * handled.  Returns null if the subscription returned an acknowledgment.  May
	 * throw an exception if a subscribe statement was incorrectly formatted or an
	 * error occurred.
	 * 
	 * SUBSCRIBE [[LIST] | [EXPIRES IN number {MINS | HOURS | DAYS}] [TO] query_expr]
	 * 
	 * @param login The login information with which the given query is executed.
	 * @param m The message that containing meta-data for sending responses.
	 * @param query The query to examine.
	 * @return A ResultTable with the subscription id or null.
	 * @throws InapplicableException If the given query was not a subscription request.
	 * @throws QueryFormatException If the subscribe statement was incorrectly formatted.
	 * @throws GeneralSecurityException If the given login does not have valid permissions.
	 * @throws Exception If a general error occurred.
	 */
    public ResultTable checkSubscribe(ValidatedLogin login, Message<?> m, String query) throws InapplicableException, QueryFormatException, GeneralSecurityException, Exception {
        Timestamp expiration = null;
        SubscribeSyntax.TypeAndDDF tad = SubscribeSyntax.checkForSubscribe(query);
        switch(tad.type) {
            case SUBSCRIBE_LIST:
                return getOfferedSubscriptionsTable(login);
            case SUBSCRIBE_EXPIRING:
                expiration = tad.expiration;
            case SUBSCRIBE:
                Indexed<DDFRepresentation.Both> ddf = DatabaseDDFChecker.checkDDFNoExecute(this.mysmsSystemAccount, tad.ddf, true);
                DatabaseDDFChecker.checkForPermission(login, ddf.getObject());
                if (!login.hasPermission(SUBSCRIBE_PERMISSION_NAME)) throw new GeneralSecurityException("Given login does not have permission to use SUBSCRIBE statement.");
                addSubscription(this.mysmsSystemAccount, m, ddf, expiration);
                return this.ddfChecker.executeDDF(ddf.getObject(), true);
            default:
                throw new InapplicableException("Not a subscribe statement.");
        }
    }

    /**
	 * Add a subscription.  The subscription will be a response, in like form, to the given message, which
	 * defines the user's address, stream id, and other parameters.  The expiration, if given, determines
	 * if the subscription expires automatically.  The index of the DDF refers to the database primary key for
	 * the DDF definition in the system database.
	 *   
	 * @param sqlAccount
	 * @param message
	 * @param ddf
	 * @param expiration
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
    protected static void addSubscription(SqlAccount sqlAccount, Message<?> message, Indexed<DDFRepresentation.Both> ddf, Timestamp expiration) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        DatabaseInterface dbi = new DatabaseInterface(sqlAccount);
        String streamString = (message.getStream() < 0) ? "null" : "'" + message.getStream() + "'";
        String encryptString = (message.getEncryption()) ? "1" : "0";
        Long subscription_ID = null;
        if (expiration != null) {
            ResultTable identity = dbi.executeQuery("INSERT INTO `Subscriptions` " + "(DDF_ID, Address, Expiration, Stream, Message, Encrypt, Encode, Module, CharLimit, MsgEncode) " + "VALUES ('" + ddf.getIndex() + "', '" + message.getAddress() + "', '" + expiration + "', " + streamString + ", '0', '" + encryptString + "', '" + message.getEncode() + "', '" + message.getModule() + "', '" + message.getCharLimit() + "', '" + message.getMsgEncode() + "'); " + "SELECT @@IDENTITY;");
            subscription_ID = (Long) identity.get(0).get(0);
        } else {
            ResultTable identity = dbi.executeQuery("INSERT INTO `Subscriptions` " + "(DDF_ID, Address, Stream, Message, Encrypt, Encode, Module, CharLimit, MsgEncode) " + "VALUES ('" + ddf.getIndex() + "', '" + message.getAddress() + "', " + streamString + ", '0', '" + encryptString + "', '" + message.getEncode() + "', '" + message.getModule() + "', '" + message.getCharLimit() + "', '" + message.getMsgEncode() + "'); " + "SELECT @@IDENTITY;");
            subscription_ID = (Long) identity.get(0).get(0);
        }
        for (ResultRow row : ddf.getObject().getArguments()) {
            dbi.executeQuery("INSERT INTO `SubscriptionArgValues` (Subscription_ID, DDFArg_ID, Value) " + "VALUES ('" + subscription_ID + "', " + "(SELECT `DDFArg_ID` FROM `DDFArguments` AS a " + "WHERE a.`DDF_ID` = '" + ddf.getIndex() + "' " + "AND a.`Name` = '" + row.get("argName") + "' " + "AND a.`Type` = '" + row.get("argType") + "'), " + "'" + row.get("argValue") + "');");
        }
    }

    /**
	 * Check to see if the given query is an unsubscribe statement and handle it
	 * accordingly if it is.  May throw an exception if an unsubscribe statement
	 * is given but incorrectly formatted or another error occurred or given query
	 * is not a unsubscribe statement.
	 * 
	 * UNSUBSCRIBE [[LIST] | ALL | [TO] query_expr]
	 * 
	 * @param login The login information with which the given query is executed.
	 * @param address The address to unsubscribe.
	 * @param query The query to examine.
	 * @return Null for acknowledgment, or a table of possible subscriptions to unsubscribe.
	 * @throws InapplicableException If the given query was not a unsubscription request.
	 * @throws QueryFormatException If the subscribe statement was incorrectly formatted.
	 * @throws GeneralSecurityException If the given login does not have valid permissions.
	 * @throws Exception If a general error occurred.
	 */
    public ResultTable checkUnsubscribe(ValidatedLogin login, String address, String query) throws InapplicableException, QueryFormatException, GeneralSecurityException, Exception {
        Indexed<DDFRepresentation.Both> ddf = null;
        SubscribeSyntax.TypeAndDDF tad = SubscribeSyntax.checkForUnsubscribe(query);
        switch(tad.type) {
            case UNSUBSCRIBE_LIST:
                return getCurrentSubscriptionsTable(address);
            case UNSUBSCRIBE:
                ddf = DatabaseDDFChecker.checkDDFNoExecute(this.mysmsSystemAccount, tad.ddf, true);
            case UNSUBSCRIBE_ALL:
                if (!login.hasPermission(SUBSCRIBE_PERMISSION_NAME)) throw new GeneralSecurityException("Given login does not have permission to use SUBSCRIBE statement.");
                removeSubscription(this.mysmsSystemAccount, address, ddf);
                return null;
            default:
                throw new InapplicableException("Not an unsubscribe statement.");
        }
    }

    /**
	 * Remove the given subscription from the given user, where the user is represented by their given address.
	 * If the given subscription is null, then all subscriptions for the given user are removed.   The index of
	 * the DDF refers to the database primary key for the DDF definition in the system database.
	 * 
	 * @param sqlAccount The MySMS system SQL account.
	 * @param address The address of the user.
	 * @param ddf The subscription and its values to remove.
	 * @throws NullPointerException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
    protected static void removeSubscription(SqlAccount sqlAccount, String address, Indexed<DDFRepresentation.Both> ddf) throws NullPointerException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        DatabaseInterface dbi = new DatabaseInterface(sqlAccount);
        if (ddf == null) {
            ResultTable table = dbi.executeQuery("SELECT s.Subscription_ID " + "FROM `Subscriptions` AS s WHERE Address = '" + address + "';");
            for (ResultRow row : table) {
                dbi.executeQuery("DELETE FROM `SubscriptionArgValues` WHERE Subscription_ID = '" + (Long) row.get(0) + "';");
                dbi.executeQuery("DELETE FROM `Subscriptions` WHERE Subscription_ID = '" + (Long) row.get(0) + "';");
            }
            return;
        }
        ResultTable table = dbi.executeQuery("SELECT s.Subscription_ID, s.DDF_ID, v.ArgValue_ID, a.DDFArg_ID, a.Type, a.Name, v.Value " + "FROM `Subscriptions` AS s " + "LEFT JOIN `SubscriptionArgValues` AS v ON s.Subscription_ID = v.Subscription_ID " + "INNER JOIN `DDFArguments` AS a ON v.DDFArg_ID = a.DDFArg_ID " + "WHERE s.DDF_ID = '" + ddf.getIndex() + "' " + "AND s.Address = '" + address + "'" + "ORDER BY s.DDF_ID, s.Subscription_ID, a.Number, a.DDFArg_ID ASC;");
        if (table.size() == 0) {
            return;
        }
        int previous_Sub_ID = ((Long) table.get(0).get("Subscription_ID")).intValue();
        int current_Sub_ID = previous_Sub_ID;
        ArrayList<Indexed<DDFRepresentation.Both>> subscriptions = new ArrayList<Indexed<DDFRepresentation.Both>>(table.size());
        DDFRepresentation.Both current = new DDFRepresentation.Both(ddf.getObject().getFunctionName());
        for (ResultRow row : table) {
            current_Sub_ID = ((Long) row.get("Subscription_ID")).intValue();
            if (current_Sub_ID != previous_Sub_ID) {
                subscriptions.add(new Indexed<DDFRepresentation.Both>(previous_Sub_ID, current));
                current = new DDFRepresentation.Both(ddf.getObject().getFunctionName());
                previous_Sub_ID = current_Sub_ID;
            }
            if (row.get("ArgValue_ID") != null) {
                current.addArgument((String) row.get("Type"), (String) row.get("Name"), (String) row.get("Value"));
            }
        }
        subscriptions.add(new Indexed<DDFRepresentation.Both>(current_Sub_ID, current));
        for (Indexed<DDFRepresentation.Both> c : subscriptions) {
            if (c.getObject().equals(ddf.getObject())) {
                dbi.executeQuery("DELETE FROM `SubscriptionArgValues` WHERE Subscription_ID = '" + c.getIndex() + "'");
                dbi.executeQuery("DELETE FROM `Subscriptions` WHERE Subscription_ID = '" + c.getIndex() + "'");
            }
        }
    }

    /**
	 * Returns the possible subscriptions that the given login has permission to subscribe to.
	 * 
	 * @param login The validated login to check.
	 * @return A result table containing meta info of subscriptions.
	 */
    private ResultTable getOfferedSubscriptionsTable(ValidatedLogin login) throws Exception {
        ColumnsDefinition columns = new ColumnsDefinition();
        columns.add(new ResultColumn("Offered_Subscriptions", false, String.class));
        columns.setFinalized();
        ResultTable table = new ResultTable(columns);
        DatabaseInterface dbi = new DatabaseInterface(this.mysmsSystemAccount);
        ResultTable result = dbi.executeQuery("SELECT d.DDF_ID, d.Name, a.DDFArg_ID, a.Type, a.Name " + "FROM `DeveloperDefinedFunctions` AS d " + "LEFT JOIN `DDFArguments` AS a ON d.DDF_ID = a.DDF_ID " + "WHERE d.Subscription = '" + 1 + "' " + "ORDER BY d.DDF_ID, a.Number, a.DDFArg_ID ASC;");
        if (result.size() == 0) {
            return table;
        }
        int previous_DDF_ID = ((Long) result.get(0).get("DDF_ID")).intValue();
        int current_DDF_ID = previous_DDF_ID;
        ArrayList<DDFRepresentation.MetaInfo> metainfos = new ArrayList<DDFRepresentation.MetaInfo>(result.size());
        DDFRepresentation.MetaInfo metainfo = new DDFRepresentation.MetaInfo((String) result.get(0).get(1));
        for (ResultRow row : result) {
            current_DDF_ID = ((Long) row.get("DDF_ID")).intValue();
            if (current_DDF_ID != previous_DDF_ID) {
                metainfos.add(metainfo);
                metainfo = new DDFRepresentation.MetaInfo((String) row.get(1));
                previous_DDF_ID = current_DDF_ID;
            }
            if (row.get("DDFArg_ID") != null) {
                metainfo.addArgument((String) row.get("Type"), (String) row.get(4));
            }
        }
        metainfos.add(metainfo);
        for (DDFRepresentation.MetaInfo c : metainfos) {
            if (login.hasPermission(c.getSecurityName())) {
                ResultRow row = new ResultRow(columns);
                row.set(0, c.toString());
                table.add(row);
            }
        }
        return table;
    }

    /**
	 * Returns the subscriptions that the given address has.
	 * 
	 * @param address The address to check.
	 * @return A result table containing meta info of subscriptions.
	 */
    private ResultTable getCurrentSubscriptionsTable(String address) throws Exception {
        ColumnsDefinition columns = new ColumnsDefinition();
        columns.add(new ResultColumn("Current_Subscriptions", false, String.class));
        columns.setFinalized();
        ResultTable table = new ResultTable(columns);
        DatabaseInterface dbi = new DatabaseInterface(this.mysmsSystemAccount);
        ResultTable result = dbi.executeQuery("SELECT s.Subscription_ID, d.Name, s.Address, s.Expiration, s.Stream, s.Message, " + "s.Encrypt, s.Encode, s.Module, s.CharLimit, s.MsgEncode, a.Type, a.Name, v.Value " + "FROM `Subscriptions` AS s " + "INNER JOIN `DeveloperDefinedFunctions` AS d ON s.DDF_ID = d.DDF_ID " + "LEFT JOIN `SubscriptionArgValues` AS v ON s.Subscription_ID = v.Subscription_ID " + "INNER JOIN `DDFArguments` AS a ON v.DDFArg_ID = a.DDFArg_ID " + "WHERE Address = '" + address + "'" + "ORDER BY s.DDF_ID, a.Number, a.DDFArg_ID ASC;");
        if (result.size() == 0) {
            return table;
        }
        ArrayList<Subscription> subscriptions = new ArrayList<Subscription>(result.size());
        Long currentID = (Long) table.get(0).get("Subscription_ID");
        Subscription current = DatabaseDDFReturn.createCurrentSubscription(table.get(0));
        for (ResultRow row : table) {
            if (!(row.get("Subscription_ID").equals(currentID))) {
                subscriptions.add(current);
                currentID = (Long) row.get("Subscription_ID");
                current = DatabaseDDFReturn.createCurrentSubscription(row);
            }
            current.addArgument((String) row.get(11), (String) row.get(12), (String) row.get(13));
        }
        Subscription[] filtered = subscriptions.toArray(new Subscription[subscriptions.size()]);
        filtered = DatabaseDDFReturn.filterExpiredSubscriptions(this.mysmsSystemAccount, filtered);
        for (Subscription s : filtered) {
            ResultRow row = new ResultRow(columns);
            row.set(0, s.toString());
            table.add(row);
        }
        return table;
    }
}
