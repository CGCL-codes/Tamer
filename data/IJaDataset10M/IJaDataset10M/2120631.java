package bbalc.support.bbowl;

/**
 * This interface defines the possible player states.
 * @author dirk
 */
public interface IPlayerStatusJBB {

    /**
	 * JBBStatus - Standing.
	 */
    public static final int JBB_STATUS_NORMAL = 0;

    /**
	 * JBBStatus - Prone.
	 */
    public static final int JBB_STATUS_PRONE = 1;

    /**
	 * JBBStatus - Stunned.
	 */
    public static final int JBB_STATUS_STUNNED = 2;

    /**
	 * JBBStatus - Ball.
	 */
    public static final int JBB_STATUS_BALL = 3;

    /**
	 * JBBStatus - Selected.
	 */
    public static final int JBB_STATUS_SELECTED = 4;

    /**
	 * JBBStatus - Selected Ball.
	 */
    public static final int JBB_STATUS_SELECTED_BALL = 5;

    /**
	 * JBBStatus - Selected Prone.
	 */
    public static final int JBB_STATUS_SELECTED_PRONE = 6;

    /**
	 * JBBStatus - KO.
	 */
    public static final int JBB_STATUS_KNOCKED_OUT = 7;

    /**
	 * JBBStatus - Badly Hurt.
	 */
    public static final int JBB_STATUS_BADLY_HURT = 8;

    /**
	 * JBBStatus - Serious Injured.
	 */
    public static final int JBB_STATUS_SERIOUS_INJURY = 9;

    /**
	 * JBBStatus - Dead.
	 */
    public static final int JBB_STATUS_DEAD = 10;
}
