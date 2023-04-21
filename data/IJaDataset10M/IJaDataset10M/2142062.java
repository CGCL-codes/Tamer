package org.sltech.punchclock.server.ejb.dao;

import org.sltech.punchclock.entities.ClkGroup;
import org.sltech.punchclock.server.ejb.PunchClockInterf;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Juanjo
 */
@Remote
public interface ClkGroupFacadeRemote extends PunchClockInterf {

    public static final String COMP_NAME = COMP_NAME_ROOT + "ClkGroupFacade/remote";

    public static final String JNDI_NAME = JNDI_NAME_ROOT + "ClkGroupFacade/remote";

    public void create(ClkGroup clkGroup);

    public void edit(ClkGroup clkGroup);

    public void remove(ClkGroup clkGroup);

    public ClkGroup find(Object id);

    public List<ClkGroup> findAll();

    public List<ClkGroup> findRange(int[] range);

    public int count();
}
