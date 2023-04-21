package j2meunit.core;

/**
 * @author Brunno
 */
public abstract class TestCase extends Assert implements Test {

    /**
     * @throws Exception a
     */
    public void setUp() throws Exception {
    }

    /**
     * @throws Exception a
     */
    public void tearDown() throws Exception {
    }

    /**
     * @return a
     */
    public final String toString() {
        return J2MEUnit.getName(getClass());
    }
}
