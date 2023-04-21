package javachess;

import java.util.Arrays;
import java.util.Comparator;

public class jcHistoryTable {

    int History[][][];

    int CurrentHistory[][];

    private static jcHistoryTable theInstance;

    private jcMoveComparator MoveComparator;

    /***********************************************************************
	 * STATIC BLOCK
	 ***********************************************************************/
    static {
        theInstance = new jcHistoryTable();
    }

    /***********************************************************************
	 * jcMoveComparator - Inner class used in sorting moves
	 **********************************************************************/
    class jcMoveComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            jcMove mov1 = (jcMove) o1;
            jcMove mov2 = (jcMove) o2;
            if (CurrentHistory[mov1.SourceSquare][mov1.DestinationSquare] > CurrentHistory[mov2.SourceSquare][mov2.DestinationSquare]) return -1; else return 1;
        }
    }

    public static jcHistoryTable GetInstance() {
        return theInstance;
    }

    public boolean SortMoveList(jcMoveListGenerator theList, int movingPlayer) {
        CurrentHistory = History[movingPlayer];
        Arrays.sort(theList.GetMoveList().toArray(), 0, theList.Size(), MoveComparator);
        return true;
    }

    public boolean AddCount(int whichPlayer, jcMove mov) {
        History[whichPlayer][mov.SourceSquare][mov.DestinationSquare]++;
        return true;
    }

    public boolean Forget() {
        for (int i = 0; i < 2; i++) for (int j = 0; j < 64; j++) for (int k = 0; k < 64; k++) History[i][j][k] = 0;
        return true;
    }

    /************************************************************************
	 * PRIVATE METHODS
	 ***********************************************************************/
    private jcHistoryTable() {
        History = new int[2][64][64];
        MoveComparator = new jcMoveComparator();
    }
}
