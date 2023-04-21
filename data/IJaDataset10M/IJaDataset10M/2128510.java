package cloudware.protocols.tman;

import java.util.Comparator;

/**
 * 
 *  
 * @author Erik Dassi
*/
public class TreeComparator implements Comparator {

    private int bits = 0;

    private long baseId = 0;

    public TreeComparator(int bits, long baseId) {
        this.bits = bits;
        this.baseId = baseId;
    }

    /** Compare two nodes based on their epidemicID and binary tree criteria.*/
    public int compare(Object arg0, Object arg1) {
        long id1 = ((TManDescriptor) arg0).getRankingID();
        long id2 = ((TManDescriptor) arg1).getRankingID();
        long dist1 = distance(baseId, id1);
        long dist2 = distance(baseId, id2);
        if (dist1 > dist2) {
            return 1;
        } else if (dist1 == dist2) {
            return 0;
        }
        return -1;
    }

    private long distance(long id1, long id2) {
        long level1 = bits, level2 = bits;
        long mask = (long) 1 << (bits - 1);
        while ((mask & id1) == 0) {
            id1 <<= 1;
            level1--;
        }
        while ((mask & id2) == 0) {
            id2 <<= 1;
            level2--;
        }
        long maxCommonPrefixLength = Math.min(level1, level2);
        long commonPrefix = 0;
        while (((mask & (~(id1 ^ id2))) != 0) && (maxCommonPrefixLength > 0)) {
            commonPrefix++;
            maxCommonPrefixLength--;
            id1 <<= 1;
            id2 <<= 1;
        }
        return (level1 - commonPrefix) + (level2 - commonPrefix);
    }
}
