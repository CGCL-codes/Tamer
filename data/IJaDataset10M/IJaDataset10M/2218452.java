package org.sltech.punchclock.server.ejb.services;

import java.util.List;
import javax.ejb.Remote;
import org.sltech.punchclock.server.ejb.PunchClockInterf;
import org.sltech.punchclock.server.exception.SLServerException;
import org.sltech.punchclock.server.to.ClkPunchTO;

/**
 * 
 * @author Juan J. Garcia
 */
@Remote
public interface ClkPunchSrvBeanRemote extends PunchClockInterf {

    public static final String COMP_NAME = COMP_NAME_ROOT + "ClkPunchSrvBean/remote";

    public static final String JNDI_NAME = JNDI_NAME_ROOT + "ClkPunchSrvBean/remote";

    public ClkPunchTO create(ClkPunchTO to) throws SLServerException;

    public ClkPunchTO edit(ClkPunchTO to) throws SLServerException;

    public void remove(ClkPunchTO to) throws SLServerException;

    public ClkPunchTO find(Integer id) throws SLServerException;

    public List<ClkPunchTO> findAll() throws SLServerException;
}
