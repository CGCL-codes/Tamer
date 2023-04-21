package researchgrants.parts.GrantRequest.statusChanges;

import java.awt.Window;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import researchgrants.parts.GrantRequest.GrantRequest;
import researchgrants.parts.db.Db;

/**
 * A single status change. Each subclass needs to define it own behavior.
 * However, each subclass also needs to add code here also.
 * @author DOStudent1
 */
public abstract class GrantRequestStatusChange {

    public abstract GrantRequestStatusChangeView getView(Window parent);

    public abstract String getStatusChangeTitle();

    public abstract String getStatusChangeCode();

    private int statusChangeId;

    protected GrantRequestStatusChange(int statusChangeId) {
        this.statusChangeId = statusChangeId;
    }

    public int getStatusChangeId() {
        return (statusChangeId);
    }

    static int createNewStatusChangeForGrantRequest(GrantRequest grantRequest, String type) {
        int newStatusChangeId = -1;
        try {
            Connection conn = Db.openDbConnection();
            PreparedStatement statement = Db.createPreparedStatement(conn, "INSERT INTO tblGrantRequestsStatusChanges (GrantRequestID, StatusChangeType) VALUES (?,?)");
            statement.setInt(1, grantRequest.getId());
            statement.setString(2, type);
            statement.executeUpdate();
            newStatusChangeId = Db.getLastIdentity();
            statement.close();
            Db.closeDbConnection(conn);
        } catch (SQLException ex) {
            Logger.getLogger(GrantRequestStatusChange.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (newStatusChangeId);
    }

    public void delete() {
        try {
            Connection conn = Db.openDbConnection();
            PreparedStatement statement = Db.createPreparedStatement(conn, "INSERT INTO tblGrantRequestsDeletedStatusChanges (GrantRequestID, GrantRequestStatusChangeID, InsertionDate, StatusChangeType) SELECT GrantRequestID, GrantRequestStatusChangeID, InsertionDate, StatusChangeType FROM tblGrantRequestsStatusChanges WHERE tblGrantRequestsStatusChanges.GrantRequestStatusChangeID=?");
            statement.setInt(1, statusChangeId);
            statement.executeUpdate();
            statement.close();
            statement = Db.createPreparedStatement(conn, "DELETE FROM tblGrantRequestsStatusChanges WHERE GrantRequestStatusChangeID=?");
            statement.setInt(1, statusChangeId);
            statement.executeUpdate();
            statement.close();
            Db.closeDbConnection(conn);
        } catch (SQLException ex) {
            Logger.getLogger(GrantRequestStatusChange.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getStatusChangeTitle(GrantRequestStatusChange lastGrantRequestStatusChange) {
        if (lastGrantRequestStatusChange == null) {
            return ("Unsubmitted");
        } else {
            return (lastGrantRequestStatusChange.getStatusChangeTitle());
        }
    }
}
