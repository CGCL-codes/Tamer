package net.sf.wwusmart.offmanipulation;

/**
 * TODO comment
 *
 * @version $Rev: 497 $
 */
public class PolygonEdge {

    public int startIndex;

    public int endIndex;

    public PolygonEdge() {
        startIndex = 0;
        endIndex = 0;
    }

    public PolygonEdge(int i, int j) {
        if (i > j) {
            startIndex = j;
            endIndex = i;
        } else {
            startIndex = i;
            endIndex = j;
        }
    }
}
