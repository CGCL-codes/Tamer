package edu.lcmi.grouppac.rm;

import java.util.Map;
import org.omg.CORBA.FT.*;
import org.omg.CosNaming.NameComponent;
import edu.lcmi.grouppac.Main;
import edu.lcmi.grouppac.util.Debug;

/**
 * This runnable implements all steps necessary to notify the logging group (infrastructure) of a
 * change in a group membership.  Creation date: (30/03/2002 16:29:11)
 * 
 * @version $Revision: 1.8 $
 * @author <a href="mailto:padilha@das.ufsc.br">Ricardo Sangoi Padilha</a>, <a
 *         href="http://www.das.ufsc.br/">UFSC, Florian�polis, SC, Brazil</a>
 */
public class NotifyLoggings implements Runnable {

    private ObjectGroupManagerImpl group;

    private String operation;

    private ReplicationManagerImpl rm;

    /**
	 * Creates a new NotifyLoggings object.
	 * 
	 * @param rm
	 * @param group
	 * @param operation
	 */
    public NotifyLoggings(ReplicationManagerImpl rm, ObjectGroupManagerImpl group, String operation) {
        this.rm = rm;
        this.group = group;
        this.operation = operation;
    }

    /**
	 * Return true if the group should be logged automatically
	 * 
	 * @param group
	 * @return boolean
	 */
    public boolean isConsistencyAutomatic(ObjectGroupManagerImpl group) {
        Map p = getProperties(group);
        if (p == null) return false;
        int cs = Integer.parseInt((String) p.get(Main.CS));
        return (cs == CONS_INF_CTRL.value);
    }

    /**
	 * Description
	 * 
	 * @param rm
	 * @return ObjectGroupManagerImpl
	 */
    public ObjectGroupManagerImpl getLoggings(ReplicationManagerImpl rm) {
        try {
            return rm.getGroups().get(RM.LR_ID);
        } catch (ObjectGroupNotFound e) {
            return null;
        }
    }

    /**
	 * Return the properties for the group
	 * 
	 * @param group
	 * @return Map
	 */
    public Map getProperties(ObjectGroupManagerImpl group) {
        try {
            return Main.toMap(rm.getPropertyManager().getGroupProperties(group.getId()));
        } catch (ObjectGroupNotFound e) {
            return null;
        }
    }

    /**
	 * Return true if the group is updateable
	 * 
	 * @param group
	 * @return boolean
	 */
    public boolean isPassive(ObjectGroupManagerImpl group) {
        Map p = getProperties(group);
        if (p == null) return false;
        int rs = Integer.parseInt((String) p.get(Main.RS));
        return (rs == COLD_PASSIVE.value) || (rs == WARM_PASSIVE.value);
    }

    public boolean isUpdateable(ObjectGroupManagerImpl group) {
        String[] ids = group.getIds();
        int size = ids.length;
        for (int i = 0; i < size; i++) {
            String id = ids[i];
            if (id.equals("IDL:omg.org/CORBA/FT/Updateable:1.0")) {
                return true;
            }
        }
        return false;
    }

    public boolean isLogging(ObjectGroupManagerImpl group) {
        boolean islogging = false;
        try {
            islogging = group.is_a("IDL:omg.org/CORBA/FT/LoggingRecovery:1.0");
        } catch (Exception ex) {
        }
        return islogging;
    }

    /**
	 * Update the group information in all logging objects.
	 */
    public void run() {
        synchronized (group) {
            if (group.getNumberOfObjects() == 0) return;
            if (group.getIds() == null) return;
            boolean isLogging = isLogging(group);
            if (!isLogging) {
                if (!isConsistencyAutomatic(group)) return;
                if (!isPassive(group)) return;
                if (!isUpdateable(group)) return;
            }
            if (isLogging) {
                operation = Main.UPDATE_LOGGING;
            }
            ObjectGroupManagerImpl loggings = getLoggings(rm);
            if (loggings == null) return;
            NameComponent[][] locations = loggings.locationsOfMembers();
            if (locations.length == 0) return;
            org.omg.CORBA.Object target = group.getReference();
            LoggingRecovery logging;
            for (int i = 0; i < locations.length; i++) {
                try {
                    logging = LoggingRecoveryHelper.narrow(loggings.getMember(locations[i]));
                    if (operation.equals(Main.STOP_LOGGING)) logging.stop_logging(target); else if (operation.equals(Main.ADD_IOGR)) logging.start_logging(target); else if (operation.equals(Main.UPDATE_IOGR)) logging.update_reference(target); else if (operation.equals(Main.UPDATE_LOGGING)) logging.update_logging_group(target);
                } catch (Exception ex) {
                    Debug.output(3, "RM: The logging at: " + locations[i][0].id + " wasn't notified.");
                    Debug.output(3, ex);
                }
            }
        }
    }
}
