package net.sf.l2j.gameserver.model;

/**
 * This class permit to pass (x, y, z, heading) position data to method.<BR>
 * <BR>
 */
public final class L2CharPosition {

    public final int x, y, z, heading;

    /**
	 * Constructor of L2CharPosition.<BR>
	 * <BR>
	 */
    public L2CharPosition(int pX, int pY, int pZ, int pHeading) {
        x = pX;
        y = pY;
        z = pZ;
        heading = pHeading;
    }
}
