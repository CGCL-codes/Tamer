package hotciv.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hotciv.factories.AbstractFactory;
import hotciv.framework.*;
import hotciv.strategies.*;

/** Implementation of HotCiv.

	This implementation is made as a project related to a course in software architecture. The source code is based on the book
		"Flexible, Reliable Software: Using Patterns and Agile Development" published 2010 by CRC Press.
		
	Authors:
		Lea Nissen, Søren Løbner and Tobias Ansbak Louv 
		Department of Computer Science
		University of Aarhus
   
*/
public class HotCiv implements ModifiableGame {

    private Player currentPlayer = Player.RED;

    private int age = -4000;

    private int round = 0;

    private final Map<Position, CityImpl> cities;

    private final Map<Position, Integer> cityProductionStashes;

    private final Map<Position, UnitImpl> units;

    private final Map<Position, TileImpl> tiles;

    private final Map<Integer, Collection<Player>> attacks = new HashMap<Integer, Collection<Player>>();

    private WinnerStrategy winnerStrategy;

    private AgingStrategy agingStrategy;

    private UnitActionStrategy unitActionStrategy;

    private MoveUnitStrategy moveUnitStrategy;

    private AttackStrategy attackStrategy;

    private List<GameObserver> observers = new ArrayList<GameObserver>();

    /**
	 * 
	 * @param ws - the winner strategy in this game
	 * @param as - the age strategy in this game
	 * @param ls - the layout strategy in this game
	 * @param uas - the unit action strategy in this game
	 * @param mus - the move unit strategy in this game
	 * @param atks - the attack strategy in this game
	 */
    public HotCiv(AbstractFactory factory) {
        winnerStrategy = factory.createWinnerStrategy();
        agingStrategy = factory.createAgingStrategy();
        unitActionStrategy = factory.createUnitActionStrategy();
        moveUnitStrategy = factory.createMoveUnitStrategy();
        attackStrategy = factory.createAttackStrategy();
        LayoutStrategy ls = factory.createLayoutStrategy();
        cities = ls.getCities();
        cityProductionStashes = ls.getCityProductionStashes();
        units = ls.getUnits();
        tiles = ls.getTiles();
        for (Position p : cities.keySet()) {
            cityProductionStashes.put(p, 0);
        }
    }

    /**
	 * Returns the Tile given at a certain Position
	 * A second call to same Position, returns the same object
	 * @return Tile
	 */
    @Override
    public Tile getTileAt(Position p) {
        TileImpl tile = tiles.get(p);
        if (tile == null) {
            tile = new TileImpl(p);
            tiles.put(p, tile);
        }
        return tile;
    }

    @Override
    public Unit getUnitAt(Position p) {
        return units.get(p);
    }

    @Override
    public City getCityAt(Position p) {
        return cities.get(p);
    }

    @Override
    public Player getPlayerInTurn() {
        return currentPlayer;
    }

    @Override
    public Player getWinner() {
        return winnerStrategy.getWinner(this);
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean moveUnit(Position from, Position to) {
        boolean isMoved = moveUnitStrategy.moveUnit(this, from, to, attackStrategy);
        notifyWorldChange(from);
        notifyWorldChange(to);
        return isMoved;
    }

    @Override
    public void endOfTurn() {
        if (currentPlayer == Player.RED) {
            currentPlayer = Player.BLUE;
        } else {
            endOfRound();
            currentPlayer = Player.RED;
        }
        for (Position p : units.keySet()) {
            notifyWorldChange(p);
        }
        notifyEndOfTurn();
    }

    @Override
    public void changeWorkForceFocusInCityAt(Position p, String balance) {
        notifyWorldChange(p);
    }

    @Override
    public void changeProductionInCityAt(Position p, String unitType) {
        CityImpl c = (CityImpl) getCityAt(p);
        if (unitType == GameConstants.ARCHER) c.setProduction(unitType);
        if (unitType == GameConstants.LEGION) c.setProduction(unitType);
        if (unitType == GameConstants.SETTLER) c.setProduction(unitType);
        notifyWorldChange(p);
    }

    @Override
    public void performUnitActionAt(Position p) {
        unitActionStrategy.performUnitActionAt(this, p);
        notifyWorldChange(p);
    }

    @Override
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    @Override
    public void setTileFocus(Position position) {
        notifyTileChange(position);
    }

    @Override
    public int getProductionStashInCityAt(Position p) {
        Integer m = cityProductionStashes.get(p);
        return m.intValue();
    }

    @Override
    public Collection<CityImpl> getCities() {
        return cities.values();
    }

    @Override
    public void addCityAt(CityImpl c, Position p) {
        cities.put(p, c);
        cityProductionStashes.put(p, 0);
    }

    @Override
    public void removeUnitAt(Position p) {
        units.remove(p);
    }

    @Override
    public Map<Position, UnitImpl> getUnits() {
        return units;
    }

    @Override
    public Map<Integer, Collection<Player>> getWonAttacks() {
        return attacks;
    }

    @Override
    public void spawnUnitAt(Position pos, String type, Player owner) {
        Position nextFreeTile = nextFreeTile(pos);
        if (nextFreeTile != null) {
            units.put(nextFreeTile, new UnitImpl(owner, type));
            notifyWorldChange(nextFreeTile);
        }
    }

    @Override
    public int getRoundNumber() {
        return round;
    }

    /**
	 * Does what should be done to end the round
	 */
    private void endOfRound() {
        round = round + 1;
        age = agingStrategy.newAge(age);
        increaseProductionStashInCities();
        restoreUnitMoveCount();
        produceNewUnitsInAllCities();
    }

    /**
	 * Increases the production stash in all cities.
	 */
    private void increaseProductionStashInCities() {
        for (Map.Entry<Position, Integer> e : cityProductionStashes.entrySet()) {
            cityProductionStashes.put(e.getKey(), e.getValue() + 6);
        }
    }

    /**
	 * Restores all units move count.
	 */
    private void restoreUnitMoveCount() {
        for (UnitImpl u : units.values()) {
            u.restoreMoveCount();
        }
    }

    /**
	 * Produces units in cities which has enough production
	 */
    private void produceNewUnitsInAllCities() {
        for (Position p : cityProductionStashes.keySet()) {
            int prod = cityProductionStashes.get(p);
            if (prod >= 9) {
                City city = getCityAt(p);
                String unitType = city.getProduction();
                Player owner = city.getOwner();
                spawnUnitAt(p, unitType, owner);
                cityProductionStashes.put(p, prod - 9);
            }
        }
    }

    /**
	 * Tests whether a tile position relative to current p
	 * is usable for spawning a unit
	 * @param p - input position
	 * @return q - next free position
	 */
    private Position nextFreeTile(Position p) {
        Position q;
        if (!moveUnitStrategy.unitOccupiedTile(this, p)) {
            q = p;
            return q;
        }
        q = p.goNorth();
        if (q.inWorld() && !moveUnitStrategy.unitOccupiedTile(this, q) && moveUnitStrategy.isWalkableTile(this, q)) return q;
        q = p.goNorthEast();
        if (q.inWorld() && !moveUnitStrategy.unitOccupiedTile(this, q) && moveUnitStrategy.isWalkableTile(this, q)) return q;
        q = p.goEast();
        if (q.inWorld() && !moveUnitStrategy.unitOccupiedTile(this, q) && moveUnitStrategy.isWalkableTile(this, q)) return q;
        q = p.goSouthEast();
        if (q.inWorld() && !moveUnitStrategy.unitOccupiedTile(this, q) && moveUnitStrategy.isWalkableTile(this, q)) return q;
        q = p.goSouth();
        if (q.inWorld() && !moveUnitStrategy.unitOccupiedTile(this, q) && moveUnitStrategy.isWalkableTile(this, q)) return q;
        q = p.goSouthWest();
        if (q.inWorld() && !moveUnitStrategy.unitOccupiedTile(this, q) && moveUnitStrategy.isWalkableTile(this, q)) return q;
        q = p.goWest();
        if (q.inWorld() && !moveUnitStrategy.unitOccupiedTile(this, q) && moveUnitStrategy.isWalkableTile(this, q)) return q;
        q = p.goNorthWest();
        if (q.inWorld() && !moveUnitStrategy.unitOccupiedTile(this, q) && moveUnitStrategy.isWalkableTile(this, q)) return q;
        return null;
    }

    private void notifyTileChange(Position p) {
        for (GameObserver go : observers) {
            go.tileFocusChangedAt(p);
        }
    }

    private void notifyWorldChange(Position p) {
        for (GameObserver go : observers) {
            go.worldChangedAt(p);
        }
    }

    private void notifyEndOfTurn() {
        for (GameObserver go : observers) {
            go.turnEnds(getPlayerInTurn(), getAge());
        }
    }
}
