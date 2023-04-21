package uk.me.g4dpz.satellite;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.gtcgroup.testutil.TestUtil;

/**
 * @author David A. B. Johnson, g4dpz
 * 
 */
public final class DeepSpaceValueObjectTest {

    /**
     * Default Constructor.
     */
    public DeepSpaceValueObjectTest() {
    }

    @Test
    public void testDeepSpaceValueObject() {
        assertTrue(TestUtil.verifyMutable(new DeepSpaceValueObject(), "./src/uk/me/g4dpz/satellite/DeepSpaceValueObject.java", 0));
    }
}
