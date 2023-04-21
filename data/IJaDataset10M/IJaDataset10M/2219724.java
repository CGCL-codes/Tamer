package games.midhedava.server.entity.npc;

import games.midhedava.server.entity.Entity;
import games.midhedava.server.entity.RPEntity;
import games.midhedava.server.pathfinder.Path;
import games.midhedava.server.pathfinder.FixedPath;
import games.midhedava.server.pathfinder.Nodes;
import games.midhedava.server.pathfinder.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import marauroa.common.Log4J;
import marauroa.common.game.AttributeNotFoundException;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPObject;
import org.apache.log4j.Logger;

public abstract class NPC extends RPEntity {

    /** the logger instance. */
    private static final Logger logger = Log4J.getLogger(NPC.class);

    /**
	 * The NPC's current idea/thought.
	 */
    private String idea;

    public static void generateRPClass() {
        try {
            RPClass npc = new RPClass("npc");
            npc.isA("rpentity");
            npc.add("class", RPClass.STRING);
            npc.add("subclass", RPClass.STRING);
            npc.add("text", RPClass.LONG_STRING, RPClass.VOLATILE);
            npc.add("idea", RPClass.STRING, RPClass.VOLATILE);
            npc.add("outfit", RPClass.INT);
            npc.add("race", RPClass.STRING);
            npc.add("sex", RPClass.STRING);
        } catch (RPClass.SyntaxException e) {
            logger.error("cannot generate RPClass", e);
        }
    }

    public NPC(RPObject object) throws AttributeNotFoundException {
        super(object);
        update();
    }

    public NPC() throws AttributeNotFoundException {
        super();
        put("type", "npc");
        put("x", 0);
        put("y", 0);
    }

    /**
	 * Set the NPC's idea/thought.
	 *
	 * @param	idea		The idea mnemonic, or <code>null</code>.
	 */
    public void setIdea(String idea) {
        if (idea != null) {
            if (!idea.equals(this.idea)) {
                put("idea", idea);
            }
        } else if (has("idea")) {
            remove("idea");
        }
        this.idea = idea;
    }

    /**
	 * Get the NPC's idea/thought.
	 *
	 * @return	The idea mnemonic, or <code>null</code>.
	 */
    public String getIdea() {
        return idea;
    }

    public void setOutfit(String outfit) {
        put("outfit", outfit);
    }

    public void say(String text) {
        put("text", text);
    }

    /**
	 * Moves to the given entity. When the distance to the destination more than
	 * <code>max</code> and this entity does not have a path already one is
	 * searched and saved. If the destination is less than min <code>min</code>
	 * the path is removed.
	 * <p>
	 * 
	 * @param destEntity
	 *            the destination entity
	 * @param min
	 *            minimum distance to have a path
	 * @param max
	 *            minimum distance to find a path
	 */
    public void setAsynchonousMovement(Entity destEntity, double min, double max) {
        setAsynchonousMovement(destEntity, min, max, -1.0);
    }

    /**
	 * Moves to the given entity. When the distance to the destination more than
	 * <code>max</code> and this entity does not have a path already one is
	 * searched and saved. If the destination is less than min <code>min</code>
	 * the path is removed.
	 * <p>
	 * 
	 * @param destEntity
	 *            the destination entity
	 * @param min
	 *            minimum distance to have a path
	 * @param max
	 *            minimum distance to find a path
	 * @param maxPathRadius
	 *            the maximum radius in which a path is searched
	 */
    public void setAsynchonousMovement(Entity destEntity, double min, double max, double maxPathRadius) {
        int destX = destEntity.getX();
        int destY = destEntity.getY();
        if (nextTo(destX, destY, min) && hasPath()) {
            clearPath();
        }
        if ((squaredDistance(destX, destY) > max) && !hasPath()) {
            Path.searchPathAsynchonous(this, destEntity);
        }
    }

    /**
	 * moves to the given entity. When the distance to the destination is
	 * between <code>min</code> and <code>max</code> and this entity does
	 * not have a path already one is searched and saved.
	 * <p>
	 * <b>Note:</b> When the distance to the destination is less than
	 * <code>min</code> the path is removed. <b>Warning:</b> The pathfinder
	 * is not asynchonous, so this thread is blocked until a path is found.
	 * 
	 * @param destEntity
	 *            the destination entity
	 * @param min
	 *            minimum distance to the destination entity
	 * @param max
	 *            maximum distance to the destination entity
	 * @param maxPathRadius
	 *            the maximum radius in which a path is searched
	 */
    public void setMovement(Entity destEntity, double min, double max, double maxPathRadius) {
        if (nextTo(destEntity.getX(), destEntity.getY(), min) && hasPath()) {
            logger.debug("Removing path because nextto(" + destEntity.getX() + "," + destEntity.getY() + "," + min + ") of (" + getX() + "," + getY() + ")");
            clearPath();
        }
        if ((squaredDistance(destEntity.getX(), destEntity.getY()) > max) && !hasPath()) {
            logger.debug("Creating path because (" + getX() + "," + getY() + ") distance(" + destEntity.getX() + "," + destEntity.getY() + ")>" + max);
            List<Path.Node> path = Path.searchPath(this, destEntity, maxPathRadius);
            setPath(path, false);
        }
    }

    /** follows the calculated path. */
    public void moveto(double speed) {
        if (hasPath() && Path.followPath(this, speed)) {
            stop();
            clearPath();
        }
    }

    /**
	 * Set a random destination as a path and start moving.
	 *
	 * @param	speed		The speed to move at.
	 */
    public void moveRandomly(final double speed) {
        setRandomPathFrom(getX(), getY(), 10);
        setSpeed(speed);
    }

    /**
	 * Set a random destination as a path.
	 *
	 * @param	distance	The maximum axis distance to move.
	 * @param	x		The origin X coordinate for placement.
	 * @param	y		The origin Y coordinate for placement.
	 */
    public void setRandomPathFrom(final int x, final int y, final int distance) {
        Random rand = new Random();
        int dist2_1 = distance + distance + 1;
        int dx = rand.nextInt(dist2_1) - distance;
        int dy = rand.nextInt(dist2_1) - distance;
        List<Path.Node> path = new ArrayList<Path.Node>(1);
        path.add(new Path.Node(x + dx, y + dy));
        setPath(path, false);
    }

    /**
	 * Returns true if this RPEntity is attackable
	 */
    @Override
    public boolean isAttackable() {
        return false;
    }

    public void setRace(String race) {
        put("race", race);
    }

    public void setSex(String sex) {
        put("sex", sex);
    }
}
