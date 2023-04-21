package edu.harvard.iq.safe.saasystem.etl.dao;

import edu.harvard.iq.safe.saasystem.etl.util.lockss.*;

/**
 *
 * @author asone
 */
public interface PollsDAO {

    /**
     *
     */
    public void insertData();

    /**
     *
     * @param ldstTO
     */
    public void setLOCKSSDaemonStatusTableTO(LOCKSSDaemonStatusTableTO ldstTO);
}
