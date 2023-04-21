package org.sltech.punchclock.server.ejb.services;

import java.util.List;
import javax.ejb.Remote;
import org.sltech.punchclock.server.ejb.PunchClockInterf;
import org.sltech.punchclock.server.exception.SLServerException;
import org.sltech.punchclock.server.to.ClkShiftTO;

/**
 * @author Juanjo
 *
 */
@Remote
public interface ClkShiftSrvBeanRemote extends PunchClockInterf {

    public static final String COMP_NAME = COMP_NAME_ROOT + "ClkShiftSrvBean/remote";

    public static final String JNDI_NAME = JNDI_NAME_ROOT + "ClkShiftSrvBean/remote";

    public ClkShiftTO create(ClkShiftTO to) throws SLServerException;

    public ClkShiftTO edit(ClkShiftTO to) throws SLServerException;

    public void remove(ClkShiftTO to) throws SLServerException;

    public ClkShiftTO find(Integer id) throws SLServerException;

    public List<ClkShiftTO> findAll() throws SLServerException;
}
