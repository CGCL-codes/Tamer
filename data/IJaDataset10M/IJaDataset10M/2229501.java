package net.sf.repbot.command;

import java.util.HashMap;
import java.util.Map;
import net.sf.repbot.FibsListener;
import net.sf.repbot.db.UserDBInterface;
import net.sf.repbot.server.*;

/**
 *
 *  @author Avi Kivity
 */
public class AlertOpinionCommand extends Toggle {

    private UserDBInterface db;

    private Map<String, Boolean> status = new HashMap<String, Boolean>();

    /** Creates a new instance of AlertCommand */
    public AlertOpinionCommand(FibsListener connection, Server server, UserDBInterface db) {
        super(server, "RepBot will alert you when opinions about you are cast.", "RepBot will not alert you when opinions about you are cast.");
        this.db = db;
    }

    /** Sets the toggle status to 'on'. */
    @Override
    protected void setStatusOn(String user) {
        try {
            db.setAlert(user, true);
            status.put(user, Boolean.TRUE);
        } catch (Exception e) {
            throw new RuntimeException("Failed setting alert status for " + user, e);
        }
    }

    /** Sets the toggle status to 'off'. */
    @Override
    protected void setStatusOff(String user) {
        try {
            db.setAlert(user, false);
            status.put(user, Boolean.FALSE);
        } catch (Exception e) {
            throw new RuntimeException("Failed setting alert status for " + user, e);
        }
    }

    /** Gets the toggle status. */
    @Override
    protected boolean getStatus(String user) {
        try {
            if (!status.containsKey(user)) status.put(user, new Boolean(db.getAlert(user)));
            return status.get(user).booleanValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed setting alert status for " + user, e);
        }
    }
}
