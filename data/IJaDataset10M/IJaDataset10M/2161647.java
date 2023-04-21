package org.fudaa.dodico.crue.io.rcal;

import java.util.List;
import org.fudaa.dodico.crue.io.common.ContexteSimulationDao;
import org.fudaa.dodico.crue.io.dao.AbstractCrueDao;
import org.fudaa.dodico.crue.io.rcal.CrueDaoStructureRCAL.ResultatsCalculPermanentRef;
import org.fudaa.dodico.crue.io.rcal.CrueDaoStructureRCAL.ResultatsPasDeTempsRef;
import org.fudaa.dodico.crue.io.rcal.CrueDaoStructureRCAL.StructureResultats;

public class CrueDaoRCAL extends AbstractCrueDao {

    ContexteSimulationDao ContexteSimulation;

    StructureResultats StructureResultats;

    List<ResultatsCalculPermanentRef> ResultatsCalculsPermanents;

    List<ResultatsPasDeTempsRef> ResultatsCalculTransitoire;
}
