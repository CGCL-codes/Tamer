package com.kenstevens.stratinit.server.ws.attack;

import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.kenstevens.stratinit.model.FlakBattleLog;
import com.kenstevens.stratinit.model.Unit;
import com.kenstevens.stratinit.server.daoservice.SectorDaoService;
import com.kenstevens.stratinit.server.ws.TwoPlayerBase;
import com.kenstevens.stratinit.type.SectorCoords;
import com.kenstevens.stratinit.type.UnitType;
import com.kenstevens.stratinit.ws.Result;
import com.kenstevens.stratinit.ws.model.SIUpdate;

public class FlakTest extends TwoPlayerBase {

    @Autowired
    protected SectorDaoService sectorDaoService;

    private static final SectorCoords ATT = new SectorCoords(0, 1);

    private static final SectorCoords FORT = new SectorCoords(1, 4);

    private static final SectorCoords SEA = new SectorCoords(3, 0);

    @Test
    public void flakKillsPlane() {
        declareWar();
        Unit fighter = unitDaoService.buildUnit(NATION_ME, ATT, UnitType.FIGHTER);
        fighter.setHp(1);
        unitDao.merge(fighter);
        unitDaoService.buildUnit(NATION_THEM, FORT, UnitType.INFANTRY);
        Result<SIUpdate> result = stratInit.moveUnits(GAME_ID, makeUnitList(fighter), FORT);
        assertFalseResult(result);
        List<FlakBattleLog> logs = logDao.getFlakBattleLogs(NATION_ME);
        assertTrue(logs.size() > 0);
        assertTrue(logs.get(0).getFlakDamage() > 0);
    }

    @Test
    public void shipHasFlak() {
        declareWar();
        Unit fighter = unitDaoService.buildUnit(NATION_ME, ATT, UnitType.FIGHTER);
        fighter.setHp(1);
        unitDaoService.buildUnit(NATION_THEM, SEA, UnitType.CARRIER);
        Result<SIUpdate> result = stratInit.moveUnits(GAME_ID, makeUnitList(fighter), SEA);
        assertFalseResult(result);
        List<FlakBattleLog> logs = logDao.getFlakBattleLogs(NATION_ME);
        assertTrue(logs.size() > 0);
        assertTrue(logs.get(0).getFlakDamage() > 0);
    }
}
