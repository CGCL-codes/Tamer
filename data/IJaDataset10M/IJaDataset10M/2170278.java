package net.sf.oval.test.constraints;

import net.sf.oval.constraint.AssertNullCheck;

/**
 * @author Sebastian Thomschke
 */
public class AssertNullTest extends AbstractContraintsTest {

    public void testNotNull() {
        final AssertNullCheck check = new AssertNullCheck();
        super.testCheck(check);
        assertTrue(check.isSatisfied(null, null, null, null));
        assertFalse(check.isSatisfied(null, "bla", null, null));
        assertFalse(check.isSatisfied(null, true, null, null));
        assertFalse(check.isSatisfied(null, 1, null, null));
        assertFalse(check.isSatisfied(null, "", null, null));
        assertFalse(check.isSatisfied(null, ' ', null, null));
        assertFalse(check.isSatisfied(null, " ", null, null));
    }
}
