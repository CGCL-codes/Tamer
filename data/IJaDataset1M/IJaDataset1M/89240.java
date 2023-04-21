package de.robowars.server;

import org.apache.log4j.Category;
import de.robowars.comm.transport.Concurrency;
import de.robowars.comm.transport.Movement;
import de.robowars.comm.transport.RotationTypes;

/**
 * Represents a special field element that can turn a robot standing on its field.
 * 
 * @author Helge Issel
 * @author Kai Koehne
 */
public class Gear extends FieldElement {

    private boolean left;

    /** The logger instance for this class. */
    private static Category log = Category.getInstance(Gear.class);

    /**
	 * Returns an object of the Crusher class and initialize it appropriately.
	 * @param pos position of the {@link de.robowars.server.Field Field} object that the ConveyorBelt contains
	 * @param left the rotation of the gear (true: turns left, false: turns right)
	 */
    public Gear(Position pos, boolean left) {
        super(pos);
        this.left = left;
        log.debug("Gear created");
    }

    /**
	 * Turns a robot standing on the same field 90� to the right or left.
 	 * @param turn the current turn of the round. This isn't used, since a gear is always active in each turn.
	 * @throws ServerException if the board is invalid, or something seriously fails
	 * @see de.robowars.server.FieldElement#action(int)
	 */
    public void action(int turn) throws ServerException {
        log.info("action called");
        Player player = ((Board.getInstance()).getField(pos)).getCurrentPlayer();
        if (player == null) {
            log.debug("no robot found");
            return;
        }
        DirectionS direction = player.getDir();
        if (left) {
            direction.turnLeft();
            log.debug("left rotation");
            Movement logmove = RoundProtocolFactory.createMovement(player, RotationTypes.LEFT);
            Concurrency logcur = RoundProtocolFactory.getCurrentConcurrency();
            logcur.getMovement().add(logmove);
        } else {
            direction.turnRight();
            log.debug("right rotation");
            Movement logmove = RoundProtocolFactory.createMovement(player, RotationTypes.RIGHT);
            Concurrency logcur = RoundProtocolFactory.getCurrentConcurrency();
            logcur.getMovement().add(logmove);
        }
        player.setDir(direction);
        log.debug("robot turned");
        log.info("action exciting");
    }

    /**
	 * Returns a string representing the state of the object. Use it for debugging/logging purposes
	 * @return state of the object
	 */
    public String toString() {
        return "Gear - " + super.toString() + " direction: " + ((left == true) ? "turnsLeft" : "turnsRight");
    }
}
