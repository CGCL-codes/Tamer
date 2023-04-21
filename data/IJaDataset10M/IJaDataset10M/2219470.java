package jumble.mutation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.IOException;
import org.apache.bcel.classfile.JavaClass;
import java.util.Random;

/**
 * Tests the corresponding class.
 * 
 * @author Sean A. Irvine
 * @version $Revision: 345 $
 */
public class MutaterTest extends TestCase {

    public MutaterTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MutaterTest.class);
        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void testCountMutationPointsX0() {
        assertEquals(0, new Mutater().countMutationPoints("jumble.X0"));
    }

    public void testCountMutationPointsX0I() {
        assertEquals(-1, new Mutater().countMutationPoints("jumble.X0I"));
    }

    public void testCountMutationPointsX1() {
        assertEquals(1, new Mutater().countMutationPoints("jumble.X1"));
    }

    public void testCountMutationPointsX2() {
        assertEquals(9, new Mutater().countMutationPoints("jumble.X2"));
    }

    public void testCountMutationPointsX2r() {
        Mutater m = new Mutater();
        m.setMutateReturnValues(true);
        assertEquals(10, m.countMutationPoints("jumble.X2"));
    }

    public void testCountMutationPointsX2i() {
        Mutater m = new Mutater();
        m.setMutateInlineConstants(true);
        assertEquals(11, m.countMutationPoints("jumble.X2"));
    }

    public void testCountMutationPointsX2ir() {
        Mutater m = new Mutater();
        m.setMutateInlineConstants(true);
        m.setMutateReturnValues(true);
        assertEquals(12, m.countMutationPoints("jumble.X2"));
    }

    public void testCountMutationPointsLines() {
        Mutater m = new Mutater();
        assertEquals(3, m.countMutationPoints("DebugLines"));
    }

    public void testCountMutationPointsNone() {
        Mutater m = new Mutater();
        assertEquals(3, m.countMutationPoints("DebugNone"));
    }

    public void testCountNegs() {
        Mutater m = new Mutater();
        m.setMutateNegs(true);
        assertEquals(1, m.countMutationPoints("experiments.instruction.INeg"));
        assertEquals(1, m.countMutationPoints("experiments.instruction.DNeg"));
        assertEquals(1, m.countMutationPoints("experiments.instruction.FNeg"));
        assertEquals(1, m.countMutationPoints("experiments.instruction.LNeg"));
    }

    public void testDescriptionsNegs() throws IOException {
        testDescriptions(0, "experiments.instruction.INeg:10: removed negation", "experiments.instruction.INeg", false, false, true);
        testDescriptions(0, "experiments.instruction.DNeg:10: removed negation", "experiments.instruction.DNeg", false, false, true);
        testDescriptions(0, "experiments.instruction.FNeg:10: removed negation", "experiments.instruction.FNeg", false, false, true);
        testDescriptions(0, "experiments.instruction.LNeg:10: removed negation", "experiments.instruction.LNeg", false, false, true);
    }

    public void testDescriptionsX2() throws IOException {
        String className = "jumble.X2";
        testDescriptions(0, "jumble.X2:6: * -> /", className);
        testDescriptions(1, "jumble.X2:6: / -> *", className);
        testDescriptions(2, "jumble.X2:6: + -> -", className);
        testDescriptions(3, "jumble.X2:6: % -> *", className);
        testDescriptions(4, "jumble.X2:6: / -> *", className);
        testDescriptions(5, "jumble.X2:6: - -> +", className);
        testDescriptions(6, "jumble.X2:6: 5 -> -1", className);
        testDescriptions(7, "jumble.X2:6: >> -> <<", className);
        testDescriptions(8, "jumble.X2:6: << -> >>", className);
        testDescriptions(9, "jumble.X2:6: 57 (9) -> 58 (:)", className);
        testDescriptions(10, "jumble.X2:6: & -> |", className);
        testDescriptions(11, "jumble.X2:6: changed return value (ireturn)", className);
        testDescriptions(500, null, className);
    }

    private void testDescriptions(int x, String s, String className, boolean constants, boolean returns, boolean negs) throws IOException {
        Mutater m = new Mutater(x);
        assertEquals(null, m.getModification());
        m.setMutateInlineConstants(constants);
        m.setMutateReturnValues(returns);
        m.setMutateNegs(negs);
        m.jumbler(className);
        assertEquals(m.getModification(), s, m.getModification());
    }

    private void testDescriptions(int x, String s, String className) throws IOException {
        testDescriptions(x, s, className, true, true, false);
    }

    public void testDescriptionsX3() throws IOException {
        final String className = "jumble.X3";
        testDescriptions(0, "jumble.X3:6: 3 -> 4", className);
        testDescriptions(1, "jumble.X3:6: * -> /", className);
        testDescriptions(2, "jumble.X3:6: * -> /", className);
        testDescriptions(3, "jumble.X3:6: + -> -", className);
        testDescriptions(4, "jumble.X3:6: - -> +", className);
        testDescriptions(5, "jumble.X3:6: changed return value (areturn)", className);
        testDescriptions(500, null, className);
    }

    public void testFindsClass() {
        Mutater m = new Mutater();
        try {
            assertNotNull(m.jumbler("jumble.X3"));
        } catch (IOException e) {
            fail("IO problem");
        }
    }

    public void testDoesntFindClass() {
        Mutater m = new Mutater();
        try {
            m.jumbler("poxweed");
            fail("IO failed to fire");
        } catch (IOException e) {
        }
    }

    private void testDescriptions4(int x, String s) throws IOException {
        Mutater m = new Mutater(x);
        assertEquals(null, m.getModification());
        m.setMutateInlineConstants(true);
        m.setMutateReturnValues(true);
        m.jumbler("jumble.X4");
        assertEquals(m.getModification(), s, m.getModification());
    }

    public void testDescriptionsX4() throws IOException {
        testDescriptions4(0, "jumble.X4:6: * -> /");
        testDescriptions4(1, "jumble.X4:6: / -> *");
        testDescriptions4(2, "jumble.X4:6: + -> -");
        testDescriptions4(3, "jumble.X4:6: % -> *");
        testDescriptions4(4, "jumble.X4:6: / -> *");
        testDescriptions4(5, "jumble.X4:6: - -> +");
        testDescriptions4(6, "jumble.X4:6: 5 -> -1");
        testDescriptions4(7, "jumble.X4:6: >> -> <<");
        testDescriptions4(8, "jumble.X4:6: << -> >>");
        testDescriptions4(9, "jumble.X4:6: & -> |");
        testDescriptions4(10, "jumble.X4:6: changed return value (lreturn)");
        testDescriptions4(500, null);
    }

    public void hash(String s, int mp, long h) {
        try {
            Mutater m = new Mutater(mp);
            JavaClass c = m.jumbler(s);
            assertEquals(h, irvineHash(c.getBytes(), 0, c.getBytes().length));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testGetMutatedMethodName() {
        Mutater m = new Mutater();
        assertEquals("add(II)I", m.getMutatedMethodName("experiments.JumblerExperiment"));
        m = new Mutater(2);
        assertEquals("add(II)I", m.getMutatedMethodName("experiments.JumblerExperiment"));
        m = new Mutater(3);
        assertEquals("multiply(II)I", m.getMutatedMethodName("experiments.JumblerExperiment"));
        try {
            m = new Mutater(500);
            m.getMutatedMethodName("experiments.JumblerExperiment");
            fail();
        } catch (Exception e) {
            ;
        }
    }

    public void testGetMethodRelativeMutationPoint() {
        Mutater m = new Mutater();
        assertEquals(0, m.getMethodRelativeMutationPoint("experiments.JumblerExperiment"));
        m = new Mutater(2);
        assertEquals(2, m.getMethodRelativeMutationPoint("experiments.JumblerExperiment"));
        m = new Mutater(3);
        assertEquals(0, m.getMethodRelativeMutationPoint("experiments.JumblerExperiment"));
        try {
            m = new Mutater(500);
            m.getMethodRelativeMutationPoint("experiments.JumblerExperiment");
            fail();
        } catch (Exception e) {
            ;
        }
    }

    /** Randomly generated arrays used to compute irvineHash codes */
    private static final long[] HASH_BLOCKS;

    static {
        HASH_BLOCKS = new long[256];
        Random r = new Random(1L);
        for (int i = 0; i < 256; i++) {
            HASH_BLOCKS[i] = r.nextLong();
        }
    }

    /**
   * Returns a 64 bit hash of the given string. This hash function exhibits
   * better statistical behavior than String hashCode() and has speed comparable
   * to CRC32.
   * 
   * @param in
   *          bytes to checksum
   * @param start
   *          first byte
   * @param length
   *          length of input
   * @return a hash
   */
    private static long irvineHash(final byte[] in, final int start, final int length) {
        long r = 0L;
        for (int i = 0; i < length; i++) {
            final long sgn = (r & 0x8000000000000000L) >>> 63;
            r <<= 1;
            r |= sgn;
            r ^= HASH_BLOCKS[(in[i + start] + i) & 0xFF];
        }
        return r;
    }
}
