package com.jchapman.jempire.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.jchapman.jempire.actions.AttackAction;
import com.jchapman.jempire.cities.CityInfo;
import com.jchapman.jempire.cities.CityInfoModel;
import com.jchapman.jempire.events.AttackEvent;
import com.jchapman.jempire.events.MoveUnitEvent;
import com.jchapman.jempire.events.MoveUnitEventHandler;
import com.jchapman.jempire.map.MapUtils;
import com.jchapman.jempire.units.Unit;
import com.jchapman.jempire.units.UnitsModel;
import com.jchapman.jempire.utils.JEmpireConstants;
import org.yasl.arch.application.YASLSwingApplication;
import org.yasl.arch.errors.YASLApplicationException;

/**
 *
 * @author Jeff Chapman
 * @version 1.0
 */
public class MoveUnitModel {

    private final YASLSwingApplication swingApp;

    private final MoveUnitEventHandler moveUnitEventHandler;

    private final LogsModel logsModel;

    public MoveUnitModel(MoveUnitEventHandler moveUnitEventHandler, YASLSwingApplication swingApp) throws YASLApplicationException {
        super();
        this.swingApp = swingApp;
        this.moveUnitEventHandler = moveUnitEventHandler;
        this.logsModel = (LogsModel) swingApp.getSingleton(JEmpireConstants.APPKEY_LOGS_MODEL);
    }

    /**
     * Checks if unit being transported can exit transport.
     *
     * @param unit Unit
     * @return boolean
     */
    public boolean isUnitMoveAvailable(Unit unit, MapUtils mapUtils) {
        boolean moveAvailable = false;
        int unitLoc = unit.getLocation();
        int[] adjacentLocs = mapUtils.getAdjacentLocations(unitLoc);
        for (int idx = 0; idx < adjacentLocs.length && !moveAvailable; idx++) {
            moveAvailable = isAllowedTerrain(unit, mapUtils.getTerrain(adjacentLocs[idx]));
        }
        return moveAvailable;
    }

    public boolean moveUnit(MoveUnitEvent muEvent, boolean asyncAttack) throws YASLApplicationException {
        MapUtils mapUtils = (MapUtils) swingApp.getSingleton(JEmpireConstants.APPKEY_MAP_UTILS);
        UnitsModel unitsModel = (UnitsModel) swingApp.getSingleton(JEmpireConstants.APPKEY_UNITS_MODEL);
        CityInfoModel citiesModel = (CityInfoModel) swingApp.getSingleton(JEmpireConstants.APPKEY_CITIES_MODEL);
        Unit unit = muEvent.getUnit();
        int unitLocation = unit.getLocation();
        int targetLocation = muEvent.getNewLocation();
        boolean moveUnit = unitLocation != targetLocation;
        boolean moveSuccessful = false;
        while (moveUnit) {
            moveUnit = moveUnit && unit.getCurrentMovement() > 0;
            NewLocationInfo newLocInfo = null;
            if (moveUnit) {
                newLocInfo = getNewLocation(unit, mapUtils, citiesModel, unitsModel, targetLocation);
                int newLocInfoState = newLocInfo.getState();
                if (newLocInfoState == NewLocationInfo.VALID_LOCATION || newLocInfoState == NewLocationInfo.INVALID_LOCATION) {
                    moveUnit = newLocInfoState == NewLocationInfo.VALID_LOCATION;
                } else if (newLocInfoState == NewLocationInfo.LOADING_UNIT) {
                    moveUnit = false;
                    Unit transportingUnit = newLocInfo.getTransportingUnit();
                    if (transportingUnit.hasTransportCapacity(unit)) {
                        transportingUnit.loadUnitForTransport(unit);
                        int newLocation = newLocInfo.getNewLocation();
                        unitsModel.moveUnit(unit, newLocation);
                        MoveUnitEvent muDoneEvent = new MoveUnitEvent(this, MoveUnitEvent.EVENT_UNIT_MOVE_COMPLETE, unit, newLocation);
                        moveUnitEventHandler.fireMoveUnitEvent(muDoneEvent);
                    } else if (transportingUnit.getCapacity() == 0) {
                        Object[] args = { transportingUnit.getDisplayName() };
                        logsModel.logMessage("game.mssg.transportfull", args);
                    } else {
                        Object[] args = { transportingUnit.getDisplayName(), unit.getDisplayNameWithArticle() };
                        logsModel.logMessage("game.mssg.lowcapacity", args);
                    }
                } else if (newLocInfoState == NewLocationInfo.UNIT_LOCATION) {
                    moveUnit = citiesModel.isCityLocation(newLocInfo.getNewLocation());
                } else if (newLocInfoState == NewLocationInfo.ENEMY_UNIT_LOCATION) {
                    moveUnit = false;
                    Unit defendingUnit = newLocInfo.getAdjacentUnit();
                    unit.setVisibility(defendingUnit.getOwnerShip());
                    defendingUnit.setVisibility(unit.getOwnerShip());
                    boolean canAttack = AttackModel.canAttack(unit, defendingUnit);
                    boolean hasMunitions = unit.isMunitionsDependent() ? unit.getCurrentMutionsCount() > 0 : true;
                    if (canAttack && hasMunitions) {
                        AttackEvent attackEvent = new AttackEvent(this, AttackEvent.EVENT_ATTACK, unit, newLocInfo.getNewLocation());
                        if (asyncAttack) {
                            AttackAction attackAction = (AttackAction) swingApp.getActionHandler().getActionByKey(JEmpireConstants.ACTIONKEY_ATTACK);
                            attackAction.actionPerformed(attackEvent);
                        } else {
                            AttackModel attackModel = (AttackModel) swingApp.getSingleton(JEmpireConstants.APPKEY_ATTACK_MODEL);
                            attackModel.handleAttack(attackEvent);
                        }
                        MoveUnitEvent muDoneEvent = new MoveUnitEvent(this, MoveUnitEvent.EVENT_UNIT_MOVE_COMPLETE, unit, unit.getLocation());
                        moveUnitEventHandler.fireMoveUnitEvent(muDoneEvent);
                    } else if (!canAttack) {
                        Object[] args = { unit.getDisplayNameWithArticle(), defendingUnit.getDisplayNameWithArticle() };
                        logsModel.logMessage("game.mssg.units.noattack", args);
                    } else if (!hasMunitions) {
                        Object[] args = { unit.getDisplayName(), unit.getMunitionsDisplay() };
                        logsModel.logMessage("game.mssg.units.nomunitions", args);
                    }
                } else if (newLocInfoState == NewLocationInfo.CITY_LOCATION) {
                } else if (newLocInfoState == NewLocationInfo.ENEMY_CITY_LOCATION) {
                    moveUnit = false;
                    CityInfo cityInfo = newLocInfo.getAdjacentCity();
                    unit.setVisibility(cityInfo.getOwnerShip());
                    boolean canAttack = AttackModel.canAttack(unit, cityInfo);
                    boolean hasMunitions = unit.isMunitionsDependent() ? unit.getCurrentMutionsCount() > 0 : true;
                    if (canAttack && hasMunitions) {
                        AttackEvent attackEvent = new AttackEvent(this, AttackEvent.EVENT_ATTACK, unit, newLocInfo.getNewLocation());
                        if (asyncAttack) {
                            AttackAction attackAction = (AttackAction) swingApp.getActionHandler().getActionByKey(JEmpireConstants.ACTIONKEY_ATTACK);
                            attackAction.actionPerformed(attackEvent);
                        } else {
                            AttackModel attackModel = (AttackModel) swingApp.getSingleton(JEmpireConstants.APPKEY_ATTACK_MODEL);
                            attackModel.handleAttack(attackEvent);
                        }
                        MoveUnitEvent muDoneEvent = new MoveUnitEvent(this, MoveUnitEvent.EVENT_UNIT_MOVE_COMPLETE, unit, unit.getLocation());
                        moveUnitEventHandler.fireMoveUnitEvent(muDoneEvent);
                    } else if (!canAttack) {
                        Object[] args = { unit.getDisplayNameWithArticle() };
                        logsModel.logMessage("game.mssg.units.noattack.city", args);
                    } else if (!hasMunitions) {
                        Object[] args = { unit.getDisplayName(), unit.getMunitionsDisplay() };
                        logsModel.logMessage("game.mssg.units.nomunitions", args);
                    }
                }
            }
            moveSuccessful = moveUnit;
            if (moveUnit) {
                if (unit.isUnderTransport()) {
                    Unit transportUnit = unit.getTransportingUnit();
                    transportUnit.unloadUnitFromTransport(unit);
                }
                int newLocation = newLocInfo.getNewLocation();
                unitsModel.moveUnit(unit, newLocation);
                mapUtils.setVisibilityForLocation(newLocation, unit.getOwnerShip());
                int eventId = unit.getCurrentMovement() > 0 ? MoveUnitEvent.EVENT_UNIT_MOVE_INCOMPLETE : MoveUnitEvent.EVENT_UNIT_MOVE_COMPLETE;
                MoveUnitEvent muDoneEvent = new MoveUnitEvent(this, eventId, unit, newLocation);
                moveUnitEventHandler.fireMoveUnitEvent(muDoneEvent);
                unitLocation = unit.getLocation();
                moveUnit = unitLocation != targetLocation;
            }
        }
        return moveSuccessful;
    }

    private Unit getTransportUnit(CityInfoModel citiesModel, UnitsModel unitsModel, Unit unitToTransport, int targetLoc) {
        Unit transportUnit = null;
        if (!citiesModel.isCityLocation(targetLoc) && unitsModel.isUnitLocation(targetLoc)) {
            transportUnit = unitsModel.getTransportAtLocation(targetLoc, unitToTransport);
        }
        return transportUnit;
    }

    private NewLocationInfo getNewLocation(Unit unit, MapUtils mapUtils, CityInfoModel citiesModel, UnitsModel unitsModel, int targetLocation) {
        NewLocationInfo newLocInfo;
        int unitLoc = unit.getLocation();
        int[] adjacentLocs = mapUtils.getAdjacentLocations(unitLoc);
        Arrays.sort(adjacentLocs);
        boolean isAdjacent = Arrays.binarySearch(adjacentLocs, targetLocation) >= 0;
        if (isAdjacent) {
            Unit transportUnit = getTransportUnit(citiesModel, unitsModel, unit, targetLocation);
            if (transportUnit != null) {
                newLocInfo = new NewLocationInfo(NewLocationInfo.LOADING_UNIT, targetLocation, transportUnit);
            } else {
                newLocInfo = new NewLocationInfo(NewLocationInfo.VALID_LOCATION, targetLocation, unit, mapUtils.getTerrain(targetLocation), citiesModel, unitsModel);
            }
        } else {
            newLocInfo = getNLIForMultiMove(unit, mapUtils, citiesModel, unitsModel, targetLocation, unitLoc);
        }
        return newLocInfo;
    }

    private NewLocationInfo getNLIForMultiMove(Unit unit, MapUtils mapUtils, CityInfoModel citiesModel, UnitsModel unitsModel, int targetLocation, int unitLoc) {
        NewLocationInfo newLocInfo;
        int newLocation = MapUtils.OFFMAP_LOCATION;
        Point unitRC = mapUtils.getRowColumnForLocation(unitLoc);
        Point trgtRC = mapUtils.getRowColumnForLocation(targetLocation);
        List adjRCs = mapUtils.getAdjacentRowColumnsForLocation(unitLoc, unit.isSeaBased(), unit.isLandBased());
        List mupoints = new ArrayList();
        Iterator iter = adjRCs.iterator();
        while (iter.hasNext()) {
            Point point = (Point) iter.next();
            mupoints.add(new MoveUnitPoint(point.x, point.y, trgtRC));
        }
        Collections.sort(mupoints);
        Point closestPoint = (Point) mupoints.remove(0);
        newLocation = mapUtils.getLocation(closestPoint.x, closestPoint.y);
        newLocInfo = new NewLocationInfo(newLocation == MapUtils.OFFMAP_LOCATION ? NewLocationInfo.INVALID_LOCATION : NewLocationInfo.VALID_LOCATION, newLocation, unit, mapUtils.getTerrain(newLocation), citiesModel, unitsModel);
        int newLocState = newLocInfo.getState();
        boolean keepLooking = newLocState != NewLocationInfo.VALID_LOCATION;
        while (keepLooking && !mupoints.isEmpty()) {
            closestPoint = (Point) mupoints.remove(0);
            newLocation = mapUtils.getLocation(closestPoint.x, closestPoint.y);
            newLocInfo = new NewLocationInfo(newLocation == MapUtils.OFFMAP_LOCATION ? NewLocationInfo.INVALID_LOCATION : NewLocationInfo.VALID_LOCATION, newLocation, unit, mapUtils.getTerrain(newLocation), citiesModel, unitsModel);
            keepLooking = newLocState != NewLocationInfo.VALID_LOCATION;
        }
        return newLocInfo;
    }

    private boolean isAllowedTerrain(Unit unit, int terrain) {
        boolean allowed = true;
        switch(terrain) {
            case JEmpireConstants.VALUE_SEA:
                allowed = unit.isSeaBased();
                break;
            case JEmpireConstants.VALUE_GRASSLAND:
                allowed = unit.isLandBased();
                break;
        }
        return allowed;
    }

    private static final class NewLocationInfo {

        public static final int INVALID_LOCATION = -1;

        public static final int VALID_LOCATION = -2;

        public static final int LOADING_UNIT = -3;

        public static final int CITY_LOCATION = -4;

        public static final int UNIT_LOCATION = -5;

        public static final int ENEMY_CITY_LOCATION = -6;

        public static final int ENEMY_UNIT_LOCATION = -7;

        private final int state;

        private final int newLocation;

        private final Unit transportingUnit;

        private final Unit adjacentUnit;

        private final CityInfo adjacentCity;

        NewLocationInfo(int state, int newLoc, Unit unit, int terrain, CityInfoModel citiesModel, UnitsModel unitsModel) {
            this.transportingUnit = null;
            int ownership = unit.getOwnerShip();
            Unit tmpUnit = null;
            CityInfo tmpCity = null;
            if (state == VALID_LOCATION) {
                if (unitsModel.isUnitLocation(newLoc)) {
                    tmpUnit = unitsModel.getUnitAtLocation(newLoc);
                    state = tmpUnit.getOwnerShip() == ownership ? UNIT_LOCATION : ENEMY_UNIT_LOCATION;
                } else if (citiesModel.isCityLocation(newLoc)) {
                    tmpCity = citiesModel.getCityAt(newLoc);
                    state = tmpCity.getOwnerShip() == ownership ? CITY_LOCATION : ENEMY_CITY_LOCATION;
                } else if (!isAllowedTerrain(unit, terrain)) {
                    state = INVALID_LOCATION;
                }
                this.state = state;
            } else {
                this.state = state;
            }
            this.newLocation = newLoc;
            this.adjacentUnit = tmpUnit;
            this.adjacentCity = tmpCity;
        }

        NewLocationInfo(int state, int newLoc, Unit transUnit) {
            this.state = state;
            this.newLocation = newLoc;
            this.transportingUnit = transUnit;
            this.adjacentCity = null;
            this.adjacentUnit = null;
        }

        int getState() {
            return state;
        }

        int getNewLocation() {
            return newLocation;
        }

        Unit getTransportingUnit() {
            return transportingUnit;
        }

        Unit getAdjacentUnit() {
            return adjacentUnit;
        }

        CityInfo getAdjacentCity() {
            return adjacentCity;
        }

        private boolean isAllowedTerrain(Unit unit, int terrain) {
            boolean allowed = true;
            switch(terrain) {
                case JEmpireConstants.VALUE_SEA:
                    allowed = unit.isSeaBased();
                    break;
                case JEmpireConstants.VALUE_GRASSLAND:
                    allowed = unit.isLandBased();
                    break;
            }
            return allowed;
        }
    }

    private static final class MoveUnitPoint extends Point implements Comparable {

        private final int diff;

        private final int internalDiff;

        MoveUnitPoint(int x, int y, Point target) {
            super(x, y);
            int dx = Math.abs(target.x - x);
            int dy = Math.abs(target.y - y);
            diff = dx + dy;
            internalDiff = Math.abs(dx - dy);
        }

        int getDifference() {
            return diff;
        }

        int getInternalDifference() {
            return internalDiff;
        }

        public int compareTo(Object o) {
            MoveUnitPoint mupoint = (MoveUnitPoint) o;
            int comp = this.diff - mupoint.diff;
            if (comp == 0) {
                comp = this.internalDiff - mupoint.internalDiff;
            }
            return comp;
        }
    }
}
