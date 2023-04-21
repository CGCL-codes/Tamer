package cm.logic;

/**
 * Class that is used as a stub for not yet implemented features.
 * It implements methods returning some stub data that should allow to do development of dependant parts.
 *
 * @author: akorolev
 */
public class Stub {

    /**
	 * Returns array of ride numbers specified rides set consists of.
	 */
    public static int[] getRidesInSet(int ridesSetNum) {
        int[] retVal = { ridesSetNum };
        if (ridesSetNum == 4) {
            retVal = new int[2];
            retVal[0] = 4;
            retVal[1] = 5;
        }
        return retVal;
    }

    /**
	 * Returns count of rides for current competition.
	 */
    public static int getRideSetsCount() {
        return 5;
    }
}
