package net.sourceforge.swimp.test.coin;

import org.apache.commons.lang.ArrayUtils;
import net.sourceforge.swimp.coin.CoinPackedVector;
import net.sourceforge.swimp.coin.CoinPackedVectorBase;
import net.sourceforge.swimp.test.util.Check;
import net.sourceforge.swimp.test.util.SwimpBaseTestCase;

/**
 * @author schickin
 *
 */
public abstract class AbstractTestCoinPackedVectorBase extends SwimpBaseTestCase {

    protected static final int indices[] = { 1, 3, 5 };

    protected static final double values[] = { 10, 5, -1 };

    protected static final double denseValues[] = { 0, 10, 0, 5, 0, -1 };

    private static final double oneNorm = 16;

    private static final double twoNormSquare = 126;

    private static final double infNorm = 10;

    private static final double sumValues = 14;

    private CoinPackedVectorBase vec = null;

    protected void setUp() throws Exception {
        super.setUp();
        vec = getVector();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for AbstractTestCoinPackedVectorBase.
     * @param arg0
     */
    public AbstractTestCoinPackedVectorBase(String arg0) {
        super(arg0);
    }

    /**
     * Return a vector based on the static indices and values
     * for the fixture.
     * 
     * @return a vector for the fixture
     */
    protected abstract CoinPackedVectorBase getVector();

    public void testGetNumElements() {
        assertEquals(values.length, vec.getNumElements());
    }

    public void testGetIndices() {
        Check.checkArray(indices, vec.getIndicesVec());
    }

    public void testGetElements() {
        Check.checkArray(values, vec.getElementsVec());
    }

    public void testGetElement() {
        for (int i = 0; i < values.length; i++) {
            assertEquals("element #" + i + " must be identical", values[i], vec.getElement(i), 0.0);
        }
    }

    public void testGetIndex() {
        for (int i = 0; i < indices.length; i++) {
            assertEquals("element #" + i + " must be identical", indices[i], vec.getIndex(i), 0.0);
        }
    }

    public void testDenseVectorAccess() {
        Check.checkArray(denseValues, vec.denseJavaVec());
    }

    public void testGetItem() {
        for (int i = 0; i < denseValues.length; i++) {
            assertEquals("element #" + i + " must be identical", denseValues[i], vec.getItem(i), 0.0);
        }
    }

    public void testMinMaxIndex() {
        assertEquals(5, vec.getMaxIndex());
        assertEquals(1, vec.getMinIndex());
    }

    public void testIsExistingIndex() {
        assertFalse(vec.isExistingIndex(2));
        assertTrue(vec.isExistingIndex(3));
    }

    public void testFindIndex() {
        assertEquals(1, vec.findIndex(3));
    }

    public void testEquals() {
        CoinPackedVector expected = CoinPackedVector.create(denseValues);
        assertTrue(expected.isEquivalent(getVector()));
        assertTrue(getVector().isEquivalent(expected));
        assertTrue(expected.equals(getVector()));
        assertTrue(getVector().equals(expected));
        assertEquals(expected, getVector());
        assertEquals(getVector(), expected);
    }

    public void testEquivalent() {
        int[] indicesRev = ArrayUtils.clone(indices);
        ArrayUtils.reverse(indicesRev);
        double[] valuesRev = ArrayUtils.clone(values);
        ArrayUtils.reverse(valuesRev);
        CoinPackedVector expected = CoinPackedVector.create(indicesRev, valuesRev);
        assertTrue(expected.isEquivalent(getVector()));
        assertTrue(getVector().isEquivalent(expected));
        assertFalse(expected.equals(getVector()));
        assertFalse(getVector().equals(expected));
    }

    public void testOneNorm() {
        assertEquals(oneNorm, getVector().oneNorm(), 0.0);
    }

    public void testTwoNorm() {
        assertEquals(twoNormSquare, getVector().normSquare(), 0.0);
    }

    public void testInfNorm() {
        assertEquals(infNorm, getVector().infNorm(), 0.0);
    }

    public void testSumValues() {
        assertEquals(sumValues, getVector().sum(), 0.0);
    }

    public void testDotProd() {
        assertEquals(twoNormSquare, getVector().dotProductVec(denseValues), 0.0);
    }
}
