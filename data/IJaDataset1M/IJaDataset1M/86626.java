package com.idna.batchid.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.idna.batchid.dao.MappedProceduresDao;
import com.idna.batchid.model.database.CustomerProfile;

public class BidLogger {

    private MappedProceduresDao logDao;

    protected final Log logger = LogFactory.getLog(getClass());

    /**
	 * A helper method that wraps up calls to the DAO mapped to the bidLog
	 * table.
	 * 
	 * @param event
	 *            The kind of action being taken.
	 * @param profile
	 *            The profile for which the action is being taken.
	 * @param message
	 *            Any additional information about the action being taken.
	 * 
	 * @see com.idna.batchid.model.database.LogAction
	 * @see com.idna.batchid.model.database.CustomerProfile
	 * @see com.idna.batchid.dao.MappedProceduresDao
	 */
    public void appendToLog(String event, CustomerProfile profile, String message) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("customerid", profile.getCustomerId());
        parameters.put("loginid", profile.getLoginId());
        parameters.put("event", event.toString());
        parameters.put("description", message);
        logDao.create(parameters);
    }

    /**
	 * Injection point for the DAO responsible for handling log insertions.
	 * 
	 * @param dao
	 */
    public void setLogDao(MappedProceduresDao dao) {
        logDao = dao;
    }
}
