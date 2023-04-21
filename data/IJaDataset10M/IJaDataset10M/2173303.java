package org.j2eebuilder.util.ejb;

import org.j2eebuilder.view.BusinessDelegateException;
import org.j2eebuilder.view.SessionException;
import java.util.Collection;
import java.util.Date;
import org.j2eebuilder.view.Request;

/**
 * 
 * @author sdixit
 * pass in sessionless requestHelperBean - that way component name can be passed in as well
 */
public interface UtilityTimer {

    public String startTimer(Integer requesterID, Date pTimeOfCall, long intervalDuration, long pRemainingRepetitions) throws BusinessDelegateException, SessionException;

    public void stopTimer(Integer requesterID) throws BusinessDelegateException;

    public String getRunningTasks();
}
