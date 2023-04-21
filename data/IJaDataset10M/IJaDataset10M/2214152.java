package sma.pathFinding;

/**
 * A heuristic that uses the tile that is closest to the target as the next best
 * tile.
 * 
 */
public class ClosestHeuristic {

    /**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
    public float getCost(GameMap map, UnitMover mover, int x, int y, int tx, int ty) {
        float dx = tx - x;
        float dy = ty - y;
        float result = (float) (Math.sqrt((dx * dx) + (dy * dy)));
        return result;
    }
}
