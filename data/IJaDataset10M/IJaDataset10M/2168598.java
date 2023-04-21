package pl.org.minions.stigma.pathfinding;

import pl.org.minions.stigma.game.path.Passable;
import pl.org.minions.stigma.game.path.Path;
import pl.org.minions.stigma.globals.Distance;
import pl.org.minions.stigma.globals.Position;

/**
 * Interface for path-finding algorithm. PathFinder should
 * be assigned to particular passable entity.
 */
public interface PathFinder {

    /**
     * Returns found path between given position.
     * @param start
     *            start position
     * @param target
     *            target position
     * @return path between given positions
     */
    Path generatePath(Position start, Position target);

    /**
     * Returns found path between given position.
     * @param start
     *            start position
     * @param target
     *            target position
     * @param range
     *            how many fields from target algorithm
     *            shall end generation path
     * @return path between given start position and 'range'
     *         from target
     */
    Path generatePath(Position start, Position target, Distance range);

    /**
     * Returns passable associated with this path finder.
     * @return passable
     */
    Passable getPassable();
}
