package cyclopean.world.actor;

import cyclopean.world.zone.Position;

/**
 * <br>
 *
 * @author Jaco van der Westhuizen
 */
public class PlayerPawn extends Walker {

    public static final float MASS = 70;

    public static final float SPEED = 1;

    /**
     * @param pos
     */
    public PlayerPawn(Position pos) {
        super(pos, 1f / MASS, SPEED);
    }
}
