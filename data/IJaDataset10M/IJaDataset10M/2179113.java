package des;

import java.util.ArrayList;
import java.util.zip.DataFormatException;
import stream.Block;
import framework.Permutation;
import framework.PermutationTable;
import junit.framework.TestCase;

public class TestE extends TestCase {

    private ArrayList<Block> blocklist;

    public void setUp() throws DataFormatException {
        this.blocklist = new ArrayList<Block>();
        this.blocklist.add(new Block("11010011110110110100111011000010"));
        this.blocklist.add(new Block("11111010110010110110011111010010"));
        this.blocklist.add(new Block("10001100001101110001000110101110"));
        this.blocklist.add(new Block("11110000101010101111000010101010"));
        PermutationTable table = new E();
        Permutation permutation = new Permutation(table);
        for (int i = 0; i < this.blocklist.size(); i++) {
            this.blocklist.set(i, permutation.expand((Block) this.blocklist.get(i)));
        }
    }

    public void testBlock0() {
        assertTrue(this.blocklist.get(0).toString().equals("011010100111111011110110101001011101011000000101"));
    }

    public void testBlock1() {
        assertTrue(this.blocklist.get(1).toString().equals("011111110101011001010110101100001111111010100101"));
    }

    public void testBlock2() {
        assertTrue(this.blocklist.get(2).toString().equals("010001011000000110101110100010100011110101011101"));
    }

    public void testBlock3() {
        assertTrue(this.blocklist.get(3).toString().equals("011110100001010101010101011110100001010101010101"));
    }
}
