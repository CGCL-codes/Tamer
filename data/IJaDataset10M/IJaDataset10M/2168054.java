package org.sltech.punchclock.server.ejb.dao;

import java.util.List;
import javax.ejb.Local;
import org.sltech.punchclock.entities.ClkLocation;
import org.sltech.punchclock.server.ejb.PunchClockInterf;

/**
 * @author Juan J. Garcia
 *
 */
@Local
public interface ClkLocationFacadeLocal extends PunchClockInterf {

    public static final String COMP_NAME = COMP_NAME_ROOT + "ClkLocationFacade/local";

    public static final String JNDI_NAME = JNDI_NAME_ROOT + "ClkLocationFacade/local";

    public void create(ClkLocation clkLocation);

    public void edit(ClkLocation clkLocation);

    public void remove(ClkLocation clkLocation);

    public ClkLocation find(Object id);

    public List<ClkLocation> findAll();

    public List<ClkLocation> findRange(int[] range);

    public int count();
}
