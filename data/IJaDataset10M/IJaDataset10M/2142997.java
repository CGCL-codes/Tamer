package com.idna.common.remote;

import com.idna.common.dao.CommsLogDao;
import com.idna.common.domain.CommsLog;
import com.idna.common.dao.impl.CommsLogDaoImpl;

/**
 * An interface.   
 * 
 * @author  Raymond Chan
 * @version     %I%, %G%
 * @since       1.0
 */
public interface CommsLogService {

    public String logServiceCall(CommsLog commsLog);

    public CommsLog getCommsLog();

    public CommsLogDao getCommsLogDao();
}
