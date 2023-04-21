package org.oclc.da.ndiipp.agent.pvt;

import org.oclc.da.exceptions.DAException;
import org.oclc.da.ndiipp.common.pvt.lock.LockManager;
import org.oclc.da.ndiipp.common.pvt.NullCallerIdentity;

/**
  * ClearLocksAgent
  *
  * This agent clears leftover locks when the system is restarted.
  * 
  * @author JCG
  * @version 1.0, 
  * @created 04/15/2005
  */
public class ClearLocksAgent extends StartupAgent {

    /** (non-Javadoc)
     * @see StartupAgent#runTask()
     */
    public void runTask() throws DAException {
        NullCallerIdentity nullIdentity = new NullCallerIdentity();
        LockManager mgr = new LockManager(nullIdentity);
        mgr.unlock(null);
    }
}
