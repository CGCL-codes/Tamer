package fr.aliacom.dbmjui.components.instance;

import org.apache.log4j.Logger;
import com.sap.dbtech.powertoys.DBM;
import fr.aliacom.dbmjui.DbInstance;
import fr.aliacom.dbmjui.DbState;
import fr.aliacom.dbmjui.beans.InstanceInformations;
import fr.aliacom.dbmjui.driver.DriverException;
import fr.aliacom.dbmjui.driver.IInstanceHelper;

/**
 * @author tom
 *
 * (c) 2001, 2003 Thomas Cataldo
 */
public class InstanceHelper implements IInstanceHelper {

    private Logger logger;

    public InstanceHelper() {
        logger = Logger.getLogger(InstanceHelper.class);
    }

    public InstanceInformations updateInformations(DbInstance dbi) throws DriverException {
        InstanceInformations ii = dbi.getInformations();
        if (ii == null) {
            ii = new InstanceInformations();
            ii.setDbi(dbi);
        }
        int oldState = ii.getState();
        int newState = oldState;
        DBM aDbm = null;
        try {
            logger.debug("Reloading dbi state...");
            aDbm = dbi.getPlainDbmConnection();
            String[] lines = aDbm.cmd("db_state").split("\n");
            logger.debug("lines[1]='" + lines[1] + "'");
            if (lines[1].equals("WARM") || lines[1].equals("ONLINE")) {
                newState = DbState.WARM_STATE;
            } else if (lines[1].equals("COLD") || lines[1].equals("ADMIN")) {
                newState = DbState.COLD_STATE;
            } else {
                newState = DbState.STOP_STATE;
            }
            ii.setState(newState);
            if (newState == DbState.WARM_STATE) {
                aDbm.cmd("sql_connect");
                lines = aDbm.cmd("info state").split("\n");
                ii.setDataMax(parseIntLine(lines[19]));
                ii.setData(parseIntLine(lines[3]));
                ii.setLogMax(parseIntLine(lines[20]));
                ii.setLog(parseIntLine(lines[12]));
                ii.setSessionMax(parseIntLine(lines[23]));
                ii.setSession(parseIntLine(lines[15]));
            } else {
                ii.setDataMax(0);
                ii.setLogMax(0);
                ii.setSessionMax(0);
                ii.setData(0);
                ii.setLog(0);
                ii.setSession(0);
            }
            aDbm.release();
        } catch (Exception e) {
            throw new DriverException(e);
        }
        return ii;
    }

    private final int parseIntLine(String line) {
        String values[] = line.split("=");
        int ret = Integer.parseInt(values[1].trim());
        return ret;
    }

    /**
     * Method isAutologEnabled.
     * @return boolean
     */
    public boolean isAutologEnabled(DbInstance dbi) {
        boolean ret = false;
        try {
            DBM dbm = dbi.getPlainDbmConnection();
            String aLog = dbm.cmd("autolog_show");
            dbm.release();
            if (aLog.endsWith("ON")) {
                ret = true;
            }
        } catch (Exception e) {
        }
        return ret;
    }

    public void changeState(DbInstance dbi, int newState) {
        try {
            String cmd = "db_warm";
            switch(newState) {
                case DbState.COLD_STATE:
                    cmd = "db_cold -f";
                    break;
                case DbState.WARM_STATE:
                    cmd = "db_warm -f";
                    break;
                case DbState.STOP_STATE:
                    cmd = "db_stop";
                    break;
            }
            DBM dbm = dbi.getDbmConnection();
            dbm.cmd(cmd);
            dbm.release();
            dbi.getInformations().setState(newState);
            updateInformations(dbi);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }
}
