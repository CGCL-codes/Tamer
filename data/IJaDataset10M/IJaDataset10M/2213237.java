package dsrwebserver.tables;

import java.sql.SQLException;
import java.sql.Timestamp;
import ssmith.dbs.MySQLConnection;
import ssmith.dbs.SQLFuncs;

public class MessagesTable extends AbstractTable {

    public MessagesTable(MySQLConnection sqldbs) {
        super(sqldbs, "Messages", "MessageID");
    }

    public static void CreateTable(MySQLConnection dbs) throws SQLException {
        if (dbs.doesTableExist("Messages") == false) {
            dbs.runSQL("CREATE TABLE Messages (MessageID INTEGER AUTO_INCREMENT KEY, FromID INTEGER, ToID INTEGER, Subject VARCHAR(1024), Message VARCHAR(4096), MsgRead TINYINT, DateCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");
        }
    }

    public static void SendMsg(MySQLConnection dbs, int fromid, int toid, String subject, String msg) throws SQLException {
        dbs.RunIdentityInsert("INSERT INTO Messages (FromID, ToID, Subject, Message) VALUES (" + fromid + ", " + toid + ", " + SQLFuncs.s2sql(subject) + ", " + SQLFuncs.s2sql(msg) + ")");
    }

    public int getFromID() throws SQLException {
        return rs.getInt("FromID");
    }

    public Timestamp getDateSent() throws SQLException {
        return rs.getTimestamp("DateCreated");
    }

    public int getToID() throws SQLException {
        return rs.getInt("ToID");
    }

    public String getSubject() throws SQLException {
        return rs.getString("Subject");
    }

    public String getMessage() throws SQLException {
        return rs.getString("Message");
    }

    public static int GetNumUnreadMessages(MySQLConnection dbs, int loginid) throws SQLException {
        return dbs.getScalarAsInt("SELECT Count(*) FROM Messages WHERE ToID = " + loginid + " AND COALESCE(MsgRead, 0) = 0");
    }

    public void markAsRead() throws SQLException {
        dbs.runSQLUpdate("UPDATE Messages SET MsgRead = 1 WHERE MessageID = " + this.getID());
    }
}
