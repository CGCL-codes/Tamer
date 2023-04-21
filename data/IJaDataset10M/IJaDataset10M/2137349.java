package hotciv.variants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import hotciv.common.*;
import hotciv.framework.*;
import hotciv.strategies.AttackStrategy;
import hotciv.strategies.MoveUnitStrategy;

public class AlphaCivMoveUnitStrategy implements MoveUnitStrategy {

    /**
	 * Will calculate the distance between two tiles, so distance 0 returns 0, distance 1 returns 1 and distance >1 returns an integer >1 (not nessesarily the right distance).
	 * Precondition: from and to are not null
	 * @param from position
	 * @param to position
	 * @return the distance between the two tiles so dist 0 ret 0, dist 1 ret 1 but dist >1 ret and int >1 (not nessesarily the right distance).
	 */
    public int distanceBetweenTiles(ModifiableGame game, Position from, Position to) {
        int dx = Math.abs(to.getRow() - from.getRow());
        int dy = Math.abs(to.getColumn() - from.getColumn());
        int dist = (int) Math.floor(Math.sqrt(dx * dx + dy * dy));
        return dist;
    }

    /**
	 * Returns whether a tile is walkable by a unit 
	 * @param p - the position to check
	 * @return whether the tile at position p is walkable
	 */
    public boolean isWalkableTile(ModifiableGame game, Position p) {
        Tile tile = game.getTileAt(p);
        if (tile.getTypeString() == GameConstants.OCEANS) return false;
        if (tile.getTypeString() == GameConstants.MOUNTAINS) return false;
        return true;
    }

    public boolean unitOccupiedTile(ModifiableGame game, Position p) {
        Unit u = game.getUnitAt(p);
        if (u == null) return false;
        return true;
    }

    @Override
    public boolean moveUnit(ModifiableGame game, Position from, Position to, AttackStrategy ccs) {
        if (isWalkableTile(game, to) == false) return false;
        if (unitOccupiedTile(game, to)) {
            Player fromUnitOwner = game.getUnitAt(from).getOwner();
            Player toUnitOwner = game.getUnitAt(to).getOwner();
            if (fromUnitOwner == toUnitOwner) return false;
        }
        Map<Position, UnitImpl> units = game.getUnits();
        UnitImpl u = units.get(from);
        int distance = distanceBetweenTiles(game, from, to);
        if (u.getMoveCount() < distance) return false;
        if (game.getPlayerInTurn() != u.getOwner()) return false;
        if (game.getUnitAt(to) != null) {
            boolean theAttackerIsTheWinner = ccs.calculateOutcome(game, from, to);
            if (theAttackerIsTheWinner == false) {
                units.remove(from);
                return true;
            }
            Map<Integer, Collection<Player>> attacks = game.getWonAttacks();
            int thisAge = game.getAge();
            Collection<Player> thisYearsAttacks = attacks.get(thisAge);
            if (thisYearsAttacks == null) {
                thisYearsAttacks = new ArrayList<Player>();
            }
            thisYearsAttacks.add(game.getUnitAt(from).getOwner());
            attacks.put(thisAge, thisYearsAttacks);
        }
        units.remove(from);
        units.put(to, u);
        u.setMoveCount(0);
        City movedToCity = game.getCityAt(to);
        if (movedToCity != null && movedToCity.getOwner() != u.getOwner()) {
            ((CityImpl) movedToCity).setOwner(u.getOwner());
        }
        return true;
    }
}
