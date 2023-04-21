package org.fudaa.dodico.crue.io.log;

import java.util.ArrayList;
import java.util.List;
import org.fudaa.dodico.crue.io.dao.AbstractCrueDao;
import org.fudaa.dodico.crue.io.log.CrueDaoStructureLOG.Log;

/**
 * @author CANEL Christophe (Genesis)
 *
 */
public class CrueDaoLOG extends AbstractCrueDao {

    public String Description;

    public List<Log> Logs = new ArrayList<Log>();
}
