package gnagck.actor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Properties describing how the Actor collides with other objects: Blocks and Actors.
 * @author royer
 *
 */
public class CollisionProperties {

    /**
	 * Actor/Actor collision type
	 * KILL_NONE: Actor does not kill other Actors
	 * KILL_OTHER: Actor kills all but Player Actors
	 * KILL_PLAYER: Actor kills Player Actors
	 * KILL_ALL: Actor kills all Actors
	 * INVINCIBLE: Actors cannot kill this Actor
	 */
    public enum ActorCollision {

        KILL_NONE, KILL_OTHER, KILL_PLAYER, KILL_ALL, INVINCIBLE
    }

    ;

    /**
	 * The Actor's collision type with other Actors
	 */
    private ActorCollision actorCollision;

    /**
	 * Does the Actor collide with Barrier Blocks
	 */
    private boolean barrier;

    /**
	 * Does the Actor collide with Deadly Blocks
	 */
    private boolean deadly;

    /**
	 * Does the Actor collide with Teleport Blocks
	 */
    private boolean teleport;

    /**
	 * Does the Actor collide with Ladder Blocks
	 */
    private boolean ladder;

    /**
	 * Constructor.
	 */
    public CollisionProperties() {
        actorCollision = ActorCollision.KILL_NONE;
        barrier = true;
        deadly = true;
        teleport = true;
        ladder = true;
    }

    /**
	 * Returns a distinct copy of this object.
	 * @return A distinct copy of this object
	 */
    public CollisionProperties clone() {
        CollisionProperties other = new CollisionProperties();
        other.actorCollision = actorCollision;
        other.barrier = barrier;
        other.deadly = deadly;
        other.teleport = teleport;
        other.ladder = ladder;
        return other;
    }

    /**
	 * Returns the Actor/Actor collision type.
	 * @return The Actor/Actor collision type
	 */
    public ActorCollision getActorCollision() {
        return actorCollision;
    }

    /**
	 * Sets the Actor/Actor collision type.
	 * @param actorCollision The Actor/Actor collision type
	 */
    public void setActorCollision(ActorCollision actorCollision) {
        this.actorCollision = actorCollision;
    }

    /**
	 * Does this Actor collide with Barrier Blocks
	 * @return true if this Actor collides with Barrier Blocks
	 */
    public boolean isBarrier() {
        return barrier;
    }

    /**
	 * Sets whether this Actor collides with Barrier Blocks
	 * @param barrier true if this Actor collides with Barrier Blocks
	 */
    public void setBarrier(boolean barrier) {
        this.barrier = barrier;
    }

    /**
	 * Does this Actor collide with Deadly Blocks
	 * @return true if this Actor collides with Deadly Blocks
	 */
    public boolean isDeadly() {
        return deadly;
    }

    /**
	 * Sets whether this Actor collides with Deadly Blocks
	 * @param deadly true if this Actor collides with Deadly Blocks
	 */
    public void setDeadly(boolean deadly) {
        this.deadly = deadly;
    }

    /**
	 * Does this Actor collide with Ladder Blocks
	 * @return true if this Actor collides with Ladder Blocks
	 */
    public boolean isLadder() {
        return ladder;
    }

    /**
	 * Sets whether this Actor collides with Ladder Blocks
	 * @param ladder true if this Actor collides with Ladder Blocks
	 */
    public void setLadder(boolean ladder) {
        this.ladder = ladder;
    }

    /**
	 * Does this Actor collide with Teleport Blocks
	 * @return true if this Actor collides with Teleport Blocks
	 */
    public boolean isTeleport() {
        return teleport;
    }

    /**
	 * Sets whether this Actor collides with Teleport Blocks
	 * @param teleport true if this Actor collides with Teleport Blocks
	 */
    public void setTeleport(boolean teleport) {
        this.teleport = teleport;
    }

    /**
	 * Persistence.  Writes the properties to persistent storage.
	 * @param dout The binary output stream
	 * @throws IOException
	 */
    public void write(DataOutput dout) throws IOException {
        dout.writeUTF(actorCollision.name());
        dout.writeBoolean(barrier);
        dout.writeBoolean(deadly);
        dout.writeBoolean(teleport);
        dout.writeBoolean(ladder);
    }

    /**
	 * Persistence.  Read the properties from persistent storage.
	 * @param din The binary input stream
	 * @throws IOException
	 */
    public void read(DataInput din) throws IOException {
        actorCollision = ActorCollision.valueOf(din.readUTF());
        barrier = din.readBoolean();
        deadly = din.readBoolean();
        teleport = din.readBoolean();
        ladder = din.readBoolean();
    }
}
