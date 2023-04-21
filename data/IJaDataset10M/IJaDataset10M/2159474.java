package com.cerner.system.rest.internal.arguments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import com.cerner.system.rest.internal.arguments.NotBlankValidator;

/**
 * Unit tests for {@link NotBlankValidator} class.
 * 
 * @author Alex Horn
 */
public class NotBlankValidatorTest {

    private NotBlankValidator validator;

    @Before
    public void setup() {
        this.validator = new NotBlankValidator();
    }

    @Test
    public void cannotValidate() {
        assertFalse(this.validator.canValidate(new Object()));
    }

    @Test
    public void canValidate() {
        assertTrue(this.validator.canValidate("some.string"));
    }

    @Test
    public void invalid() {
        assertFalse(this.validator.isValid("  "));
    }

    @Test
    public void valid() {
        assertTrue(this.validator.isValid("no.whitespace"));
    }

    @Test
    public void message() {
        assertEquals("Arguments 'one' and 'two' must contain at least one non-whitespace character", this.validator.message(Arrays.asList("one", "two")));
    }
}
