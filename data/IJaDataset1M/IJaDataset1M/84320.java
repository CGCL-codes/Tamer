package net.sf.gridarta.model.validation;

import junit.framework.TestCase;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.utils.TestActionBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * Test for {@link AbstractValidator}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class AbstractValidatorTest extends TestCase {

    /**
     * Object Under Test: A AbstractValidator.
     */
    @Nullable
    private Validator<?, ?, ?> oUT;

    /**
     * {@inheritDoc}
     * @noinspection ProhibitedExceptionDeclared
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        TestActionBuilder.initialize();
        final ValidatorPreferences validatorPreferences = new TestValidatorPreferences();
        oUT = new AbstractValidator<TestGameObject, TestMapArchObject, TestArchetype>(validatorPreferences, DelegatingMapValidator.DEFAULT_KEY) {
        };
    }

    /**
     * {@inheritDoc}
     * @noinspection ProhibitedExceptionDeclared
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        oUT = null;
    }

    /**
     * Test case for {@link AbstractValidator#setEnabled(boolean)}.
     */
    public void testEnabled() {
        assert oUT != null;
        oUT.setEnabled(false);
        assert oUT != null;
        assertFalse(oUT.isEnabled());
        assert oUT != null;
        oUT.setEnabled(true);
        assert oUT != null;
        assertTrue(oUT.isEnabled());
    }
}
