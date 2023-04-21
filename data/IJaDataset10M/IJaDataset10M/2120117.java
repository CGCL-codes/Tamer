package com.kenstevens.stratinit.server.daoservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kenstevens.stratinit.dao.GameDao;
import com.kenstevens.stratinit.dao.SectorDao;
import com.kenstevens.stratinit.dao.UnitDao;
import com.kenstevens.stratinit.model.AttackType;
import com.kenstevens.stratinit.model.City;
import com.kenstevens.stratinit.model.CityNukedBattleLog;
import com.kenstevens.stratinit.model.Game;
import com.kenstevens.stratinit.model.LaunchedSatellite;
import com.kenstevens.stratinit.model.Nation;
import com.kenstevens.stratinit.model.Sector;
import com.kenstevens.stratinit.model.SectorSeen;
import com.kenstevens.stratinit.model.Unit;
import com.kenstevens.stratinit.model.UnitAttackedBattleLog;
import com.kenstevens.stratinit.model.UnitBase;
import com.kenstevens.stratinit.move.Movement;
import com.kenstevens.stratinit.move.SectorView;
import com.kenstevens.stratinit.move.Supply;
import com.kenstevens.stratinit.move.WorldView;
import com.kenstevens.stratinit.server.ws.event.EventQueue;
import com.kenstevens.stratinit.type.CityType;
import com.kenstevens.stratinit.type.Constants;
import com.kenstevens.stratinit.type.RelationType;
import com.kenstevens.stratinit.type.SectorCoords;
import com.kenstevens.stratinit.type.SectorType;
import com.kenstevens.stratinit.type.UnitType;
import com.kenstevens.stratinit.world.World;

@Service
public class SectorDaoService {

    @Autowired
    GameDao gameDao;

    @Autowired
    SectorDao sectorDao;

    @Autowired
    UnitDao unitDao;

    @Autowired
    UnitDaoService unitDaoService;

    @Autowired
    GameDaoService gameDaoService;

    @Autowired
    LogDaoService logDaoService;

    @Autowired
    EventQueue eventQueue;

    public void survey(Nation nation) {
        List<City> cities = sectorDao.getCities(nation);
        List<Unit> units = unitDao.getUnits(nation);
        Set<Sector> seen = new HashSet<Sector>();
        seen.addAll(seeFromCities(nation, cities));
        seen.addAll(seeFromUnits(units));
        addSectorsSeen(null, nation, seen);
        for (Unit unit : units) {
            if (unit.isCanSeeSubs()) {
                addSectorsSeen(unit, nation, seen);
            }
        }
    }

    private Set<Sector> seeFromCities(Nation nation, List<City> cities) {
        Set<Sector> cseen = new HashSet<Sector>();
        for (City city : cities) {
            int radius = Constants.CITY_VIEW_DISTANCE;
            if (city.getType() == CityType.TECH) {
                radius = nation.getRadarRadius();
            }
            cseen.addAll(sectorDao.getSectorsWithin(city.getGameId(), city.getCoords(), radius));
        }
        return cseen;
    }

    private Set<Sector> seeFromUnits(List<Unit> units) {
        Set<Sector> unitSectors = new HashSet<Sector>();
        for (Unit unit : units) {
            unitSectors.addAll(getSectorsSeen(unit));
        }
        return unitSectors;
    }

    public void addSectorsSeen(Unit unitSeeing, Nation nation, Collection<Sector> seen) {
        for (Sector sector : seen) {
            addSectorSeen(unitSeeing, nation, sector);
        }
    }

    public void addSectorSeen(Unit unitSeeing, Nation nation, Sector sector) {
        sectorDao.addOrUpdate(nation, sector);
        List<Unit> unitsSeen = unitDao.getUnits(sector);
        for (Unit unit : unitsSeen) {
            if (unit.getNation().equals(nation)) {
                continue;
            }
            if (unit.isSubmarine()) {
                if (unitSeeing == null || !unitSeeing.isCanSeeSubs()) {
                    continue;
                }
            }
            unitDaoService.saveOrUpdate(nation, unit);
        }
    }

    public void recordSurveillance(Movement movement, SectorCoords target) {
        List<SectorCoords> path = movement.getDirectPath(target);
        Unit unit = movement.getUnit();
        Nation nation = unit.getNation();
        for (SectorCoords coords : path) {
            List<SectorView> neighbours = movement.getWorldView().getNeighbours(coords, unit.getSight());
            for (Sector sector : neighbours) {
                SectorSeen sectorSeen = new SectorSeen(nation, sector);
                sectorDao.persist(sectorSeen);
            }
        }
    }

    public boolean buildUnit(City city, Date buildTime) {
        boolean retval = false;
        UnitType unitType = city.getBuild();
        unitDaoService.buildUnit(city.getNation(), city.getCoords(), unitType, buildTime);
        UnitType nextBuild = city.getNextBuild();
        if (nextBuild != null) {
            city.setBuild(nextBuild);
            city.setNextBuild(null);
            retval = true;
        }
        city.setLastUpdated(buildTime);
        sectorDao.merge(city);
        return retval;
    }

    public World getWorld(int gameId) {
        Game game = gameDao.findGame(gameId);
        if (game == null) {
            return null;
        }
        return sectorDao.getWorld(game);
    }

    public WorldView getWorldView(Nation nation) {
        Game game = gameDaoService.findGame(nation.getGame().getId());
        List<Sector> sectors;
        sectors = sectorDao.getAllSectors(game);
        return getWorldView(nation, sectors);
    }

    public WorldView getSupplyWorldView(Unit unit) {
        return getRadiusWorldView(unit, Constants.SUPPLY_RADIUS);
    }

    private WorldView getRadiusWorldView(Unit unit, int distance) {
        return getRadiusWorldView(unit.getCoords(), unit.getNation(), distance);
    }

    private WorldView getRadiusWorldView(SectorCoords coords, Nation nation, int distance) {
        int gameId = nation.getGameId();
        List<Sector> sectors = sectorDao.getSectorsWithin(gameId, coords, distance);
        return getWorldView(nation, sectors);
    }

    public WorldView getInterdictionWorldView(Unit unit, Nation nation) {
        return getRadiusWorldView(unit.getCoords(), nation, Constants.SUPPLY_RADIUS + UnitBase.largestShipSight);
    }

    public WorldView getInterceptionWorldView(SectorCoords coords, Nation nation) {
        return getRadiusWorldView(coords, nation, Constants.INTERCEPTION_RADIUS);
    }

    public WorldView getSeenWorldView(Nation nation) {
        List<Sector> sectors = sectorDao.getNationSectorsSeenSectors(nation);
        return getWorldView(nation, sectors);
    }

    private WorldView getWorldView(Nation nation, List<Sector> sectors) {
        WorldView worldView = new WorldView(nation, gameDao.getMyRelationsAsMap(nation), gameDao.getTheirRelationsAsMap(nation));
        setSectorViewsFromCities(sectors, worldView);
        List<Unit> units = unitDao.getUnits(nation.getGame());
        setSectorViewsFromUnits(nation, worldView, units);
        return worldView;
    }

    private void setSectorViewsFromCities(List<Sector> sectors, WorldView worldView) {
        List<SectorView> friendlyCities = new ArrayList<SectorView>();
        for (Sector sector : sectors) {
            SectorView sectorView = new SectorView(sector);
            City city = sectorDao.getCity(sector);
            if (city != null) {
                Nation cityNation = city.getNation();
                sectorView.setNation(cityNation);
                setSectorViewRelations(worldView, sectorView, cityNation);
                sectorView.setCityType(city.getType());
                if (sectorView.onMyTeam()) {
                    friendlyCities.add(sectorView);
                }
            }
            worldView.setSectorView(sectorView);
        }
        for (SectorView city : friendlyCities) {
            if (worldView.isCoastal(city)) {
                city.setSuppliesBoth(true);
                city.setHoldsMyCapital(true);
            } else {
                city.setSuppliesLand(true);
            }
        }
    }

    private void setSectorViewsFromUnits(Nation nation, WorldView worldView, List<Unit> units) {
        for (Unit unit : units) {
            SectorView sectorView = worldView.getSectorView(unit.getCoords());
            if (sectorView == null) {
                continue;
            }
            Nation unitNation = unit.getNation();
            sectorView.setNation(unitNation);
            setSectorViewRelations(worldView, sectorView, unitNation);
            sectorView.addFlak(unit.getFlak());
            sectorView.setHoldsUnits(true);
            if (sectorView.isWater() && unitNation.equals(nation)) {
                if (unit.getType().equals(UnitType.TRANSPORT)) {
                    sectorView.setSuppliesLand(true);
                    int landUnitWeight = getLandUnitCount(sectorView);
                    if (landUnitWeight < UnitBase.getUnitBase(UnitType.TRANSPORT).getCapacity()) {
                        sectorView.setHoldsMyTransportWithRoom(true);
                    }
                } else if (unit.getType().equals(UnitType.SUPPLY)) {
                    sectorView.setSuppliesBoth(true);
                }
            }
            if (sectorView.isWater() && unit.isNavy()) {
                sectorView.setHoldsShipAtSea(true);
            }
            if (sectorView.onMyTeam()) {
                if (unit.getType().equals(UnitType.CARRIER)) {
                    sectorView.setHoldsFriendlyCarrier(true);
                }
                if (unit.isCapital()) {
                    sectorView.setHoldsMyCapital(true);
                }
            }
        }
    }

    private void setSectorViewRelations(WorldView worldView, SectorView sectorView, Nation sectorNation) {
        RelationType myRelation = worldView.getMyRelation(sectorNation);
        if (sectorView.getMyRelation() == null || sectorView.getMyRelation().compareTo(myRelation) > 0) {
            sectorView.setMyRelation(myRelation);
        }
        RelationType theirRelation = worldView.getTheirRelation(sectorNation);
        if (sectorView.getTheirRelation() == null || sectorView.getTheirRelation().compareTo(theirRelation) > 0) {
            sectorView.setTheirRelation(theirRelation);
        }
    }

    private int getLandUnitCount(SectorView sectorView) {
        List<Unit> units = unitDao.getUnits(sectorView);
        int retval = 0;
        for (Unit unit : units) {
            if (!unit.isLand()) {
                continue;
            }
            retval += unit.getWeight();
        }
        return retval;
    }

    public List<Sector> getSectorsSeen(Unit unit) {
        return sectorDao.getSectorsWithin(unit.getGameId(), unit.getCoords(), unit.getSight());
    }

    public void merge(City city) {
        sectorDao.merge(city);
    }

    public void persist(City city) {
        sectorDao.persist(city);
    }

    public Nation captureCity(Nation nation, Sector sector) {
        City city;
        Nation opponent = null;
        if (sector.getType() == SectorType.PLAYER_CITY) {
            city = sectorDao.getCity(sector);
            if (city == null) {
                throw new IllegalStateException("No city at " + sector);
            }
            opponent = city.getNation();
            city.setNation(nation);
            eventQueue.cancel(city);
            city.setBuild(UnitType.INFANTRY);
            eventQueue.schedule(city);
            sectorDao.merge(city);
        } else {
            city = new City(sector, nation, UnitType.INFANTRY);
            sectorDao.persist(city);
            Sector citySector = sectorDao.getSector(nation.getGameId(), sector.getCoords());
            citySector.setType(SectorType.PLAYER_CITY);
            sectorDao.merge(citySector);
        }
        explodeSupply(nation, sector.getCoords());
        return opponent;
    }

    public void explodeSupply(Nation nation, SectorCoords coords) {
        List<Unit> units = unitDao.getUnitsWithin(nation, coords, Constants.SUPPLY_RADIUS);
        WorldView worldView = getWorldView(nation);
        for (Unit unit : units) {
            Supply supply = new Supply(worldView);
            if (supply.inSupply(unit)) {
                unit.resupply();
                unitDao.merge(unit);
            }
        }
    }

    public void updateSeen(Unit unit) {
        unitSees(unit);
        unitSeen(unit, false);
    }

    public void unitSeen(Unit unit, boolean attacking) {
        List<Nation> nations;
        if (unit.isSubmarine() && !attacking) {
            nations = unitDao.getOtherNationsThatCanSeeThisSub(unit);
        } else {
            nations = sectorDao.getOtherNationsThatSeeThisSector(unit);
        }
        if (nations.isEmpty()) {
            nations = unitDao.getOtherNationsThatSeeThisUnit(unit);
            for (Nation nation : nations) {
                unitDaoService.remove(nation, unit);
            }
        } else {
            for (Nation nation : nations) {
                if (!nation.equals(unit.getNation())) {
                    unitDaoService.saveOrUpdate(nation, unit);
                }
            }
        }
    }

    private void unitSees(Unit unit) {
        addSectorsSeen(unit, unit.getNation(), getSectorsSeen(unit));
    }

    public void satelliteSees(LaunchedSatellite satellite) {
        addSectorsSeen(null, satellite.getNation(), sectorDao.getSectorsWithin(satellite.getGameId(), satellite.getCoords(), Constants.SATELLITE_SIGHT));
    }

    public SectorView refreshSectorView(Nation nation, WorldView worldView, SectorView targetSector) {
        List<Sector> sectors = new ArrayList<Sector>();
        sectors.add(targetSector);
        setSectorViewsFromCities(sectors, worldView);
        List<Unit> units = unitDao.getUnits(targetSector);
        setSectorViewsFromUnits(nation, worldView, units);
        return worldView.getSectorView(targetSector);
    }

    public Nation captureCity(Nation nation, SectorCoords city) {
        Sector sector = sectorDao.getSector(nation.getGameId(), city);
        return captureCity(nation, sector);
    }

    public void devastate(Unit attackerUnit, Sector sector) {
        if (sector.getType() == SectorType.PLAYER_CITY) {
            City city = sectorDao.getCity(sector);
            if (city == null) {
                throw new IllegalStateException("Cannot find player city at " + sector);
            }
            CityNukedBattleLog cityNukedBattleLog = new CityNukedBattleLog(attackerUnit, city.getNation(), sector.getCoords());
            logDaoService.persist(cityNukedBattleLog);
            eventQueue.cancel(city);
            sectorDao.remove(city);
        }
        List<Unit> units = unitDao.getUnits(sector);
        for (Unit unit : units) {
            unitDaoService.killUnit(unit);
            UnitAttackedBattleLog unitAttackedBattleLog = new UnitAttackedBattleLog(AttackType.NUKE, attackerUnit, unit, sector.getCoords());
            logDaoService.persist(unitAttackedBattleLog);
        }
    }
}
