package net.sf.flophase.core.exception;

import static org.junit.Assert.fail;
import java.math.BigDecimal;
import org.junit.Test;

/**
 * This class tests the {@link FloEntryAmountValidator} class.
 */
public class FloEntryAmountValidatorTest {

    /**
     * The validator to be tested.
     */
    private final FloEntryAmountValidator validator = new FloEntryAmountValidator();

    /**
     * Tests validation with a null parameter.
     */
    @Test
    public void testNull() {
        try {
            validator.validate(null);
            fail("Expected InvalidEntryAmountException");
        } catch (InvalidEntryAmountException e) {
            ;
        }
    }

    /**
     * Test validation with a valid parameter.
     */
    @Test
    public void testValid() {
        try {
            validator.validate(BigDecimal.ONE);
        } catch (InvalidEntryAmountException e) {
            fail("InvalidEntryAmountException should not have been thrown.");
        }
    }
}
