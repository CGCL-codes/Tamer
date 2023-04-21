package mrusanov.fantasyruler.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mrusanov.fantasyruler.game.GameContainer;
import mrusanov.fantasyruler.map.TerrainMap;
import mrusanov.fantasyruler.map.TerrainUnit;
import mrusanov.fantasyruler.map.pathfinding.AStarPathFinder;
import mrusanov.fantasyruler.map.pathfinding.Path;
import mrusanov.fantasyruler.map.pathfinding.PathFinder;
import mrusanov.fantasyruler.objects.PlacedOwnedObject;
import mrusanov.fantasyruler.objects.buildings.City;
import mrusanov.fantasyruler.objects.units.Ship;
import mrusanov.fantasyruler.objects.units.Unit;
import mrusanov.fantasyruler.objects.units.Unit.AttackType;
import mrusanov.fantasyruler.objects.units.battle.Battle;
import mrusanov.fantasyruler.objects.units.battle.BattleResult;
import mrusanov.fantasyruler.objects.units.battle.UnitWithFirstStrike;
import mrusanov.fantasyruler.player.Player;
import mrusanov.fantasyruler.player.UnitProperty;
import mrusanov.fantasyruler.player.diplomacy.DiplomaticPermission;
import mrusanov.fantasyruler.player.diplomacy.Relations;
import mrusanov.fantasyruler.player.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameModel implements ImmutableGameModel {

    private static final Logger LOG = LoggerFactory.getLogger(GameModel.class);

    private final GameContainer gameContainer;

    public GameModel(GameContainer gameContainer) {
        this.gameContainer = gameContainer;
    }

    public GameContainer getGameContainer() {
        return gameContainer;
    }

    @Override
    public int getCurrentTurn() {
        return gameContainer.getTurnNumber();
    }

    public Unit hireUnit(City city, Class<? extends Unit> unitType, int size, TerrainUnit terrainUnit) {
        LOG.debug("Trying to hire unit of type {} in size {} on terrain unit {}", new Object[] { unitType, size, terrainUnit });
        if (!validateUnitHiring(city, unitType, size, terrainUnit)) {
            return null;
        }
        Unit hiredUnit = city.hireUnit(unitType, size, terrainUnit);
        hiredUnit.clearRemainingMovePoints();
        LOG.debug("Hired unit of type {} in size {} on terrain unit {}", new Object[] { unitType, size, terrainUnit });
        return hiredUnit;
    }

    @Override
    public boolean validateUnitHiring(City city, Class<? extends Unit> unitType, int size, TerrainUnit terrainUnit) {
        if (size == 0) {
            LOG.debug("Not able to hire unit which has 0 size");
            return false;
        }
        if (!city.hiredUnitCanBePlacedOnTerrainUnit(unitType, terrainUnit, size)) {
            LOG.debug("Not able to hire unit of type {} because it cann't be placed on terrainUnit {}", unitType, terrainUnit);
            return false;
        }
        if (city.unitsCanBeHired(unitType) < size) {
            LOG.debug("Unable to hire unit because city has only {} units and requried size is {} units", city.unitsCanBeHired(unitType), size);
            return false;
        }
        if (!canAccessTerrainUnitDueToDiplomaticReasons(city.getOwner(), city.getTerrainUnit(), terrainUnit)) {
            LOG.debug("Newly hired unit of player {} is unable to access terrain unit of player {} due to diplomatic reasons", city.getOwner(), terrainUnit.getProvince().getOwner());
            return false;
        }
        return true;
    }

    private PathFinder getPathFinder() {
        return new AStarPathFinder(new TerrainMapAdapter(this, getGameContainer().getTerrainMap()));
    }

    /**
	 * returns path in terrain units, that unit should cover to get to target
	 * terrain unit. null if there is no path
	 */
    public Path performMoveToTerrainUnit(Unit unit, TerrainUnit target) {
        LOG.debug("Trying to perform move for unit {} to terrainUnit {}", unit, target);
        if (!validateUnitMove(unit, target)) {
            return null;
        }
        Path path = calculatePathForUnit(unit, target);
        if (!path.isEmpty()) {
            LOG.debug("Unit moves {} on path {}", unit, path);
            unit.moveToAnotherTerrainUnit(path);
        } else {
            LOG.debug("Not able to reach target unit {} for unit {}", target, unit);
        }
        return path;
    }

    @Override
    public boolean validateUnitMove(Unit unit, TerrainUnit target) {
        if (target.getUnit() != null && target != unit.getTerrainUnit()) {
            LOG.debug("Not able to reach terrainUnit {} because it is already occupied by {}", target, target.getUnit());
            return false;
        }
        Path path = calculatePathForUnit(unit, target);
        if (path == null) {
            LOG.debug("TerrainUnit {} is not reachable for unit {}.", target, unit);
            return false;
        }
        if (path.isEmpty()) {
            LOG.debug("No need to move unit {} because it is already on its destination terrainUnit", unit);
            return true;
        }
        if (!unit.canMoveToTerrainUnit(path)) {
            LOG.debug("Unit {} doesn't have enough move points to reach terrainUnit {}", unit, target);
            return false;
        }
        return true;
    }

    public Unit performUnitDividing(Unit unit, int size, TerrainUnit terrainUnit) {
        LOG.debug("Trying to divide unit {} and send {} soldiers to terrainUnit {}", new Object[] { unit, size, terrainUnit });
        if (!validateUnitDividing(unit, size, terrainUnit)) {
            return null;
        }
        Path path = calculatePathForUnit(unit, terrainUnit);
        Unit createdUnit = unit.sendSoldiers(size, path);
        createdUnit.getOwner().addPlacedOwnedObject(createdUnit);
        return createdUnit;
    }

    @Override
    public boolean validateUnitDividing(Unit unit, int size, TerrainUnit terrainUnit) {
        Path path = calculatePathForUnit(unit, terrainUnit);
        if (path == null) {
            LOG.debug("TerrainUnit {} is not reachable for unit {} so it is impossible to divide", terrainUnit, unit);
            return false;
        }
        if (path.isEmpty()) {
            LOG.debug("Unit {} is not able to divide units because target unit is his own terrain unit", unit);
            return false;
        }
        if (!unit.canSendSoldiers(terrainUnit, size, false)) {
            LOG.debug("Unit {} is not able to send {} soldiers to terrainUnit for some reason. Target terrainUnit has following unit: {}", new Object[] { unit, size, terrainUnit.getUnit() });
            return false;
        }
        if (!unit.canMoveToTerrainUnit(path)) {
            LOG.debug("Unit {} is not able to send soldiers on terrainUnit {} because it doesn't have enough move points", unit, terrainUnit);
            return false;
        }
        return true;
    }

    public void allPlayersMadeTheirTurns() {
        for (Player player : getGameContainer().getPlayers()) {
            for (PlacedOwnedObject placedOwnedObject : player.getPlacedOwnedObjects()) {
                placedOwnedObject.turnEnded();
            }
        }
    }

    @Override
    public int getPriceForUnit(Class<? extends Unit> unitType, Player player) {
        return player.getRealmValues().getUnitValue(unitType, UnitProperty.PRICE);
    }

    public Path loadOnShip(Unit unit, int size, Ship ship) {
        LOG.debug("Trying to load unit {} on ship {} with size {}", new Object[] { unit, ship, size });
        if (!validateLoadOnShip(unit, size, ship)) {
            return null;
        }
        Path path = new Path();
        if (!unit.getTerrainUnit().containsNeighbour(ship.getTerrainUnit())) {
            path = getShortestPathToUnoccupiedNeighbourOfTerrainUnit(unit, unit.getTerrainUnit(), ship.getTerrainUnit());
        }
        int movePointsNeededForLoadOnShip = Unit.getPathMovePointsCost(path) + Unit.MOVE_POINTS_LOAD_ON_SHIP;
        if (movePointsNeededForLoadOnShip > unit.getMovePointsRemain()) {
            LOG.debug("Unit {} is not able to load on ship {} because unit doesn't have enough move points", unit, ship);
            return null;
        }
        if (movePointsNeededForLoadOnShip > ship.getMovePointsRemain()) {
            LOG.debug("Unit {} is not able to load on ship {} because ship doesn't have enough move points", unit, ship);
            return null;
        }
        Unit loadedUnit = unit;
        if (!path.isEmpty()) {
            if (size == unit.getSize()) {
                unit.moveToAnotherTerrainUnit(path);
            } else {
                loadedUnit = unit.sendSoldiers(size, path);
            }
        }
        ship.loadUnit(loadedUnit, size);
        LOG.debug("Unit {} loaded on ship {} with size {}", new Object[] { unit, ship, size });
        return path;
    }

    @Override
    public boolean validateLoadOnShip(Unit unit, int size, Ship ship) {
        if (!ship.canLoadUnit(unit, size, false)) {
            LOG.debug("Ship {} can not load unit {} with size {}", new Object[] { ship, unit, size });
            return false;
        }
        Path path = new Path();
        if (!unit.getTerrainUnit().containsNeighbour(ship.getTerrainUnit())) {
            path = getShortestPathToUnoccupiedNeighbourOfTerrainUnit(unit, unit.getTerrainUnit(), ship.getTerrainUnit());
        }
        if (path == null) {
            LOG.debug("Unit {} is not able to load to the ship {} because it cann't reach ship", unit, ship);
            return false;
        }
        return true;
    }

    public UnloadingFromShipInfo unloadFromShip(Ship ship, int size, TerrainUnit target) {
        if (!validateUnloadFromShip(ship, size, target)) {
            return null;
        }
        Path path = new Path();
        if (!ship.getTerrainUnit().containsNeighbour(target)) {
            path = getShortestPathToUnoccupiedNeighbourOfTerrainUnit(ship, ship.getTerrainUnit(), target);
        }
        int movePointsNeededToUnloadFromShip = Unit.getPathMovePointsCost(path) + Unit.MOVE_POINTS_UNLOAD_FROM_SHIP;
        if (ship.getMovePointsRemain() < movePointsNeededToUnloadFromShip) {
            LOG.debug("Not able to unload unit from ship {} because it doesn't have enough move points", ship);
            return null;
        }
        if (ship.getLoaded().getMovePointsRemain() < Unit.MOVE_POINTS_UNLOAD_FROM_SHIP) {
            LOG.debug("Not able to unload unit from ship {} because loaded unit doesn't have enough move points", ship);
            return null;
        }
        ship.moveToAnotherTerrainUnit(path);
        Unit unloaded = ship.unloadLandUnit(target, size);
        unloaded.getOwner().addPlacedOwnedObject(unloaded);
        LOG.debug("Unit {} unloaded from ship {} with size {}", new Object[] { unloaded, ship, size });
        return new UnloadingFromShipInfo(unloaded, path);
    }

    @Override
    public boolean validateUnloadFromShip(Ship ship, int size, TerrainUnit target) {
        if (!ship.canUnloadUnit(target, size, false)) {
            LOG.debug("Unable to unload unit from ship {} on target {}", ship, target);
            return false;
        }
        Path path = new Path();
        if (!ship.getTerrainUnit().containsNeighbour(target)) {
            path = getShortestPathToUnoccupiedNeighbourOfTerrainUnit(ship, ship.getTerrainUnit(), target);
        }
        if (path == null) {
            LOG.debug("Unable to unload unit from ship {} on target {} becuase unable to reach target terrain unit", ship, target);
            return false;
        }
        return true;
    }

    public Battle performAttack(Unit attacker, Unit defender) {
        LOG.debug("Unit {} is trying to attack unit {}", attacker, defender);
        if (!validateUnitAttack(attacker, defender)) {
            return null;
        }
        Path pathToDefendersNeighbour = null;
        if (attacker.getTerrainUnit().containsNeighbour(defender.getTerrainUnit())) {
            pathToDefendersNeighbour = new Path();
        } else {
            pathToDefendersNeighbour = getShortestPathToUnoccupiedNeighbourOfTerrainUnit(attacker, attacker.getTerrainUnit(), defender.getTerrainUnit());
            int movePointsAttackCost = Unit.getPathMovePointsCost(pathToDefendersNeighbour) + Unit.ATTACK_MOVE_POINTS_COST;
            if (attacker.getMovePointsRemain() < movePointsAttackCost) {
                LOG.debug("Unit {} is not able to attack unit {} because it doesnt have enough move points", attacker, defender);
                return null;
            } else {
                attacker.moveToAnotherTerrainUnit(pathToDefendersNeighbour);
            }
        }
        Battle battle = attacker.attack(defender, getUnitWithFirstStrike(attacker, defender, false));
        LOG.debug("Unit {} attacked unit {} and battle result is {}", new Object[] { attacker, defender, battle.getBattleResult() });
        battle.setPathBeforeAttack(pathToDefendersNeighbour);
        if (battle.getBattleResult() == BattleResult.ATTACKER_DIED) {
            attacker.getOwner().removePlacedOwnedObject(attacker);
        } else if (battle.getBattleResult() == BattleResult.DEFENDER_DIED) {
            defender.getOwner().removePlacedOwnedObject(defender);
        }
        return battle;
    }

    @Override
    public boolean validateUnitAttack(Unit attacker, Unit defender) {
        if (belongToSamePlayer(attacker.getOwner(), defender.getOwner())) {
            LOG.debug("Unit {} cannot attack unit {} because they belong to the same player", attacker, defender);
            return false;
        }
        if (!canAttackUnitDueToDiplomaticReasons(attacker, defender)) {
            LOG.debug("Unit {} is not able to attack unit {} because of diplomatic reasons", attacker, defender);
            return false;
        }
        if (attacker.getMovePointsRemain() < Unit.ATTACK_MOVE_POINTS_COST) {
            LOG.debug("Unit {} is not able to attack unit {} because it doesn't have enough move points", attacker, defender);
            return false;
        }
        if (attacker.getAttackType() == AttackType.MELEE && !attacker.acceptTerrainTypeOfUnit(defender.getTerrainUnit())) {
            LOG.debug("Unit {} is not able to attack unit {} because target unit has " + "terrain unit which type is not acceptable for attacker " + "and attacker has attack type mellee", attacker, defender);
            return false;
        }
        return true;
    }

    public Battle performAttackFromShip(Ship ship, int size, TerrainUnit terrainUnit) {
        LOG.debug("Trying to perform attack from ship {} on terrainUnit {} with attacker size {}", new Object[] { ship, terrainUnit, size });
        if (!validateAttackFromShip(ship, terrainUnit)) {
            return null;
        }
        Path path = getShortestPathToUnoccupiedNeighbourOfTerrainUnit(ship, ship.getTerrainUnit(), terrainUnit);
        ship.moveToAnotherTerrainUnit(path);
        UnitWithFirstStrike unitWithFirstStrike = getUnitWithFirstStrike(ship.getLoaded(), terrainUnit.getUnit(), true);
        Battle battle = ship.attackLandFromShip(size, terrainUnit, unitWithFirstStrike);
        battle.setPathBeforeAttack(path);
        LOG.debug("Attack from ship {} on terrainUnit {} performed", ship, terrainUnit);
        return battle;
    }

    @Override
    public boolean validateAttackFromShip(Ship ship, TerrainUnit terrainUnit) {
        if (!ship.canAttackFromShip(terrainUnit, false)) {
            LOG.debug("Ship {} is unable to execute attack from ship on terrainUnit {}", ship, terrainUnit);
            return false;
        }
        if (!canAttackUnitDueToDiplomaticReasons(ship, terrainUnit.getUnit())) {
            LOG.debug("Ship {} is unable to execute attack from ship on terrainUnit {} because of diplomatic reasons", ship, terrainUnit);
            return false;
        }
        if (belongToSamePlayer(ship.getOwner(), terrainUnit.getUnit().getOwner())) {
            LOG.debug("Ship {} is not able to attack terrain unit {} with unit {} because ship and unit under attack belong to same player", new Object[] { ship, terrainUnit, terrainUnit.getUnit() });
            return false;
        }
        Path path = getShortestPathToUnoccupiedNeighbourOfTerrainUnit(ship, ship.getTerrainUnit(), terrainUnit);
        if (path == null) {
            LOG.debug("Ship {} is unable to execute attack from ship on terrainUnit {} because it is not reachable", ship, terrainUnit);
            return false;
        }
        int movePoints = Unit.getPathMovePointsCost(path) + Unit.MOVE_POINTS_UNLOAD_FROM_SHIP;
        if (ship.getMovePointsRemain() < movePoints) {
            LOG.debug("Ship {} is unable to execute attack from ship on terrainUnit {} because it doesn't have enough move points", ship, terrainUnit);
            return false;
        }
        return true;
    }

    /**
	 * @param joining
	 *            unit which is going to unite with another unit.
	 * @param size
	 *            size of unit, who is going to join another unit. It can not be
	 *            bigger than joining unit size.
	 * @param targetUnit
	 *            unit which will unite with another unit but will keep its
	 *            place
	 * @return joining path. Returns null if joining is impossible for some
	 *         reason. (e.g. lack of move points, different unit types)
	 */
    public Path performJoining(Unit joining, int size, Unit targetUnit) {
        LOG.debug("Trying to perform units joining: unit {} gives {} soldiers to unit {}", new Object[] { joining, size, targetUnit });
        if (!validateJoining(joining, size, targetUnit)) {
            return null;
        }
        TerrainUnit source = joining.getTerrainUnit();
        TerrainUnit destination = targetUnit.getTerrainUnit();
        Path path = calculatePathForUnit(joining, source, destination);
        joining.sendSoldiers(size, path);
        LOG.debug("Giving {} soldiers from unit {} to unit {} performed successfully", new Object[] { size, joining, targetUnit });
        return path;
    }

    @Override
    public boolean validateJoining(Unit joining, int size, Unit targetUnit) {
        Path path = calculatePathForUnit(joining, joining.getTerrainUnit(), targetUnit.getTerrainUnit());
        if (path == null) {
            LOG.debug("Unable to join unit {} with unit {} because joining unit cann't reach target unit", joining, targetUnit);
            return false;
        }
        if (!targetUnit.getOwner().equals(joining.getOwner())) {
            LOG.debug("Unable to join unit {} with unit {} because they have different owners", joining, targetUnit);
            return false;
        }
        if (size + targetUnit.getSize() > targetUnit.getOwner().getRealmValues().getArmyMaxSize()) {
            LOG.debug("Unable to join unit {} with unit {} with joining size {} because result size of target unit will exceed army max size for this player", new Object[] { joining, targetUnit, size });
            return false;
        }
        if (!joining.getClass().equals(targetUnit.getClass())) {
            LOG.debug("Unable to join unit {} with unit {} because they have different classes", joining, targetUnit);
            return false;
        }
        if (joining.getSize() < size) {
            LOG.debug("Unable to send {} soldiers from unit {} because it has less soldiers", size, joining);
            return false;
        }
        int neededMovePoints = Unit.getPathMovePointsCost(path);
        if (joining.getMovePointsRemain() < neededMovePoints) {
            LOG.debug("Unable to join unit {} with unit {} because joining unit doesn't have enough move points", joining, targetUnit);
            return false;
        }
        return true;
    }

    private boolean belongToSamePlayer(Player attacker, Player defender) {
        return attacker.equals(defender);
    }

    /**
	 * 
	 * @param unit
	 * @param start
	 * @param finish
	 * @return null if path wasn't found, otherwise returns shortest path
	 */
    public Path getShortestPathToUnoccupiedNeighbourOfTerrainUnit(Unit unit, TerrainUnit start, TerrainUnit finish) {
        List<Path> possiblePaths = new ArrayList<Path>();
        for (TerrainUnit neighbour : finish.getNeighbours().values()) {
            if (neighbour.getUnit() != null) {
                continue;
            }
            Path path = calculatePathForUnit(unit, start, neighbour);
            if (path != null) {
                possiblePaths.add(path);
            }
        }
        if (possiblePaths.isEmpty()) {
            return null;
        }
        Path bestPath = Collections.min(possiblePaths);
        return bestPath;
    }

    private UnitWithFirstStrike getUnitWithFirstStrike(Unit attacker, Unit defender, boolean attackFromShip) {
        if (attackFromShip) {
            return UnitWithFirstStrike.DEFENDER;
        }
        if (defender.getTerrainUnit().getBuilding() != null) {
            return UnitWithFirstStrike.DEFENDER;
        } else {
            return UnitWithFirstStrike.ATTACKER;
        }
    }

    @Override
    public TerrainMap getTerrainMap() {
        return getGameContainer().getTerrainMap();
    }

    public Path calculatePathForUnit(Unit unit, TerrainUnit target) {
        TerrainUnit start = unit.getTerrainUnit();
        return calculatePathForUnit(unit, start, target);
    }

    public Path calculatePathForUnit(Unit unit, TerrainUnit start, TerrainUnit finish) {
        return getPathFinder().findPath(unit, start.getX(), start.getY(), finish.getX(), finish.getY());
    }

    private boolean canAccessTerrainUnitDueToDiplomaticReasons(Player player, TerrainUnit source, TerrainUnit destination) {
        if (destination.getProvince().getOwner().equals(player)) {
            return true;
        }
        Relations relations = player.getDiplomacy().getRelationsByPlayer(destination.getProvince().getOwner());
        if (relations.getState().getPermissions().contains(DiplomaticPermission.MOVE)) {
            return true;
        }
        if (destination.getProvince().getOwner().equals(source.getProvince().getOwner())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canAccessTerrainUnitDueToDiplomaticReasons(Unit unit, TerrainUnit destination) {
        return canAccessTerrainUnitDueToDiplomaticReasons(unit.getOwner(), unit.getTerrainUnit(), destination);
    }

    private boolean canAttackUnitDueToDiplomaticReasons(Unit attacker, Unit defender) {
        if (attacker.getOwner().equals(defender.getOwner())) {
            return false;
        }
        if (!canAccessTerrainUnitDueToDiplomaticReasons(attacker, defender.getTerrainUnit())) {
            return false;
        }
        Relations relations = attacker.getOwner().getDiplomacy().getRelationsByPlayer(defender.getOwner());
        return relations.getState().getPermissions().contains(DiplomaticPermission.ATTACK);
    }

    @Override
    public Player getCurrentPlayer() {
        return getGameContainer().getCurrentPlayer();
    }

    public boolean nextPlayerTurn() {
        getCurrentPlayer().getMessageBox().moveEnded(getCurrentTurn());
        boolean turnEnded = getGameContainer().nextPlayerTurn();
        if (turnEnded) {
            for (Player player : getGameContainer().getPlayers()) {
                player.turnEnded(getGameContainer().getTurnNumber());
            }
        }
        return turnEnded;
    }

    @Override
    public boolean blocksWay(Unit mover, Unit blocker) {
        return !mover.getOwner().equals(blocker.getOwner());
    }

    public boolean disbandUnit(Unit unit) {
        if (validateDisbandUnit(unit)) {
            unit.die();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean validateDisbandUnit(Unit unit) {
        return unit.getOwner().equals(getCurrentPlayer());
    }

    public void sendMessage(Message message) {
        Player.sendMessage(message, getCurrentTurn());
    }

    public void executeMessage(Message message, int answerNumber) {
        getCurrentPlayer().executeMessage(message, answerNumber, getCurrentTurn());
    }

    public Battle simulateBattle(Unit attacker, Unit defender) {
        TerrainUnit attackerTerrainUnit = attacker.getTerrainUnit();
        TerrainUnit defenderTerrainUnit = defender.getTerrainUnit();
        Player attackerOwner = attacker.getOwner();
        Player defenderOwner = defender.getOwner();
        int attackerSize = attacker.getSize();
        int defenderSize = defender.getSize();
        int attackerMovePoints = attacker.getMovePointsRemain();
        int defenderMovePoints = defender.getMovePointsRemain();
        Battle battle = attacker.attack(defender, getUnitWithFirstStrike(attacker, defender, false));
        attacker.setSize(attackerSize);
        attacker.setOwner(attackerOwner);
        attacker.setMovePointsRemain(attackerMovePoints);
        attacker.teleportToAnotherTerrainUnit(attackerTerrainUnit);
        attackerOwner.addPlacedOwnedObject(attacker);
        defender.setSize(defenderSize);
        defender.setMovePointsRemain(defenderMovePoints);
        defender.teleportToAnotherTerrainUnit(defenderTerrainUnit);
        defender.setOwner(defenderOwner);
        defenderOwner.addPlacedOwnedObject(defender);
        return battle;
    }
}
