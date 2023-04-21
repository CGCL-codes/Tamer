package com.kenstevens.stratinit.server.ws.attack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.kenstevens.stratinit.model.City;
import com.kenstevens.stratinit.model.Sector;
import com.kenstevens.stratinit.model.Unit;
import com.kenstevens.stratinit.server.daoservice.SectorDaoService;
import com.kenstevens.stratinit.server.ws.TwoPlayerBase;
import com.kenstevens.stratinit.type.SectorCoords;
import com.kenstevens.stratinit.type.UnitType;
import com.kenstevens.stratinit.ws.Result;
import com.kenstevens.stratinit.ws.model.SIUpdate;

@SuppressWarnings("unused")
public class AttackTakeCityTest extends TwoPlayerBase {

    @Autowired
    protected SectorDaoService sectorDaoService;

    private static final SectorCoords ATTACK_FROM = new SectorCoords(1, 1);

    private static final SectorCoords LAND = new SectorCoords(2, 1);

    private static final SectorCoords CITY = new SectorCoords(2, 2);

    private static final SectorCoords BESIDE = new SectorCoords(3, 2);

    @Test
    public void takeCityFromTransport() {
        Unit inf = unitDaoService.buildUnit(NATION_ME, BESIDE, UnitType.INFANTRY);
        Unit trans = unitDaoService.buildUnit(NATION_ME, BESIDE, UnitType.TRANSPORT);
        sectorDaoService.captureCity(NATION_THEM, CITY);
        declareWar();
        Result<SIUpdate> result = stratInit.moveUnits(GAME_ID, makeUnitList(inf), CITY);
        assertResult(result);
        inf = unitDao.findUnit(inf.getId());
        assertFalse(inf.isAlive());
        List<Unit> units = unitDao.getUnits(NATION_ME);
        assertFalse(units.contains(inf));
        assertTookCity(result);
    }

    @Test
    public void cannotTakeOccuppiedCityFromTransport() {
        Unit inf = unitDaoService.buildUnit(NATION_ME, BESIDE, UnitType.INFANTRY);
        Unit trans = unitDaoService.buildUnit(NATION_ME, BESIDE, UnitType.TRANSPORT);
        Unit einf = unitDaoService.buildUnit(NATION_THEM, CITY, UnitType.TRANSPORT);
        sectorDaoService.captureCity(NATION_THEM, CITY);
        declareWar();
        Result<SIUpdate> result = stratInit.moveUnits(GAME_ID, makeUnitList(inf), CITY);
        assertFalseResult(result);
        inf = unitDao.findUnit(inf.getId());
        assertEquals(BESIDE, inf.getCoords());
    }

    @Test
    public void toLandFromTransport() {
        Unit inf = unitDaoService.buildUnit(NATION_ME, BESIDE, UnitType.INFANTRY);
        Unit trans = unitDaoService.buildUnit(NATION_ME, BESIDE, UnitType.TRANSPORT);
        sectorDaoService.captureCity(NATION_THEM, CITY);
        Result<SIUpdate> result = stratInit.moveUnits(GAME_ID, makeUnitList(inf), LAND);
        assertResult(result);
        inf = unitDao.findUnit(inf.getId());
        assertEquals(LAND, inf.getCoords());
    }

    private void assertTookCity(Result<SIUpdate> result) {
        List<City> cities = sectorDao.getCities(NATION_ME);
        Sector citySector = sectorDao.getSector(GAME_ID, CITY);
        City city = sectorDao.getCity(citySector);
        assertTrue(result.toString(), cities.contains(city));
    }

    @Test
    public void takeCity() {
        Unit inf = unitDaoService.buildUnit(NATION_ME, ATTACK_FROM, UnitType.INFANTRY);
        sectorDaoService.captureCity(NATION_THEM, CITY);
        declareWar();
        Result<SIUpdate> result = stratInit.moveUnits(GAME_ID, makeUnitList(inf), CITY);
        assertResult(result);
        inf = unitDao.findUnit(inf.getId());
        assertFalse(inf.isAlive());
        List<Unit> units = unitDao.getUnits(NATION_ME);
        assertFalse(units.contains(inf));
        assertTookCity(result);
    }

    @Test
    public void planeAndInfTakeCity() {
        Unit plane = unitDaoService.buildUnit(NATION_ME, ATTACK_FROM, UnitType.FIGHTER);
        Unit inf = unitDaoService.buildUnit(NATION_ME, ATTACK_FROM, UnitType.INFANTRY);
        sectorDaoService.captureCity(NATION_THEM, CITY);
        declareWar();
        Result<SIUpdate> result = stratInit.moveUnits(GAME_ID, makeUnitList(new Unit[] { plane, inf }), CITY);
        assertResult(result);
        inf = unitDao.findUnit(inf.getId());
        assertFalse(result.toString(), inf.isAlive());
        List<Unit> units = unitDao.getUnits(NATION_ME);
        assertFalse(units.contains(inf));
        assertTookCity(result);
        assertEquals(ATTACK_FROM, plane.getCoords());
    }
}
