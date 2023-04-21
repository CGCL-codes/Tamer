package cyclopean.world;

import cyclopean.world.WorldScreen.Action;

/**
 * 
 *
 * @author Jaco van der Westhuizen
 */
class Player extends Controller {

    private final boolean[] action = new boolean[Action.values().length];

    private int rotXAccum = 0;

    private int rotYAccum = 0;

    protected Player() {
        super();
    }

    @Override
    public synchronized void update() {
        byte strafe = (byte) ((action[Action.STRAFE_LEFT.ordinal()] ? -1 : 0) + (action[Action.STRAFE_RIGHT.ordinal()] ? +1 : 0));
        byte walk = (byte) ((action[Action.FORWARD.ordinal()] ? +1 : 0) + (action[Action.BACKWARD.ordinal()] ? -1 : 0));
        byte jump = (byte) ((action[Action.UPWARD.ordinal()] ? +1 : 0) + (action[Action.DOWNWARD.ordinal()] ? -1 : 0));
        pawn.move(walk, strafe, jump);
        pawn.turn(-rotXAccum * 0.01f, -rotYAccum * 0.01f, 0);
        rotXAccum = 0;
        rotYAccum = 0;
    }

    synchronized void setAction(Action act, boolean value) {
        action[act.ordinal()] = value;
    }

    synchronized void accumMouseLook(int dx, int dy) {
        rotXAccum += dx;
        rotYAccum += dy;
    }
}
