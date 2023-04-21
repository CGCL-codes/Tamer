package xbird.xquery.type;

import junit.framework.TestCase;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public class TypeTableTest extends TestCase {

    @org.junit.Test
    public void testCastingTable() {
        final byte[][] castingTable = TypeTable.castingTable;
        assert (castingTable.length == 23);
        for (int i = 0; i < 23; i++) {
            if (castingTable[i].length != 23) {
                throw new IllegalStateException("Illegal size of castingTable[" + i + "].length=" + castingTable[i].length);
            }
        }
    }
}
