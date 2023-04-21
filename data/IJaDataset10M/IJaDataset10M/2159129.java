package net.sourceforge.domian.specification;

import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import static net.sourceforge.domian.test.domain.Testdata.maleCustomer;

public class EqualIgnoreCaseStringSpecificationTest {

    @Test
    public void testShouldNotAcceptNullAsValue() {
        try {
            new EqualIgnoreCaseStringSpecification(null);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Value parameter cannot be null";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testEqualIgnoreCaseStringSpecification_equals() {
        Specification<String> spec = new EqualIgnoreCaseStringSpecification("Hey Chica!");
        assertFalse(spec.equals(new EqualIgnoreCaseStringSpecification("")));
        assertFalse(spec.equals(new EqualIgnoreCaseStringSpecification("         ")));
        assertFalse(spec.equals(new EqualIgnoreCaseStringSpecification("Hey Chico!")));
        assertTrue(spec.equals(new EqualIgnoreCaseStringSpecification("Hey Chica!")));
        assertFalse(spec.equals(new EqualIgnoreCaseStringSpecification("hey chica!")));
        assertFalse(new EqualIgnoreCaseStringSpecification("").equals(spec));
        assertFalse(new EqualIgnoreCaseStringSpecification("         ").equals(spec));
        assertFalse(new EqualIgnoreCaseStringSpecification("Hey Chico!").equals(spec));
        assertTrue(new EqualIgnoreCaseStringSpecification("Hey Chica!").equals(spec));
        assertFalse(new EqualIgnoreCaseStringSpecification("hey chica!").equals(spec));
        assertFalse(new EqualSpecification<String>("hey chica!").equals(spec));
        assertFalse(new EqualSpecification<String>("Hey Chica!").equals(spec));
    }

    @Test
    public void testEqualIgnoreCaseStringSpecification() {
        Specification<String> spec = new EqualIgnoreCaseStringSpecification("Hey Chica!");
        assertFalse(spec.isSatisfiedBy(null));
        assertFalse(spec.isSatisfiedBy(""));
        assertFalse(spec.isSatisfiedBy("         "));
        assertFalse(spec.isSatisfiedBy("Hey Chico!"));
        assertTrue(spec.isSatisfiedBy("Hey Chica!"));
        assertTrue(spec.isSatisfiedBy("hey Chica!"));
        assertTrue(spec.isSatisfiedBy("Hey chica!"));
    }

    @Test
    public void testEqualIgnoreCaseStringSpecification_Raw() {
        Specification spec = new EqualIgnoreCaseStringSpecification("Hey Chica!");
        assertFalse(spec.isSatisfiedBy(null));
        assertFalse(spec.isSatisfiedBy(""));
        assertFalse(spec.isSatisfiedBy("         "));
        assertFalse(spec.isSatisfiedBy("Hey Chico!"));
        assertTrue(spec.isSatisfiedBy("Hey Chica!"));
        assertTrue(spec.isSatisfiedBy("hey Chica!"));
        assertTrue(spec.isSatisfiedBy("Hey chica!"));
        try {
            assertFalse(spec.isSatisfiedBy(0));
            fail("Should have thrown exception");
        } catch (ClassCastException e) {
            assertEquals("java.lang.Integer cannot be cast to java.lang.String", e.getMessage());
        }
        try {
            assertFalse(spec.isSatisfiedBy(new Date()));
            fail("Should have thrown exception");
        } catch (ClassCastException e) {
            assertEquals("java.util.Date cannot be cast to java.lang.String", e.getMessage());
        }
        try {
            assertFalse(spec.isSatisfiedBy(maleCustomer));
            fail("Should have thrown exception");
        } catch (ClassCastException e) {
            assertEquals("net.sourceforge.domian.test.domain.Customer cannot be cast to java.lang.String", e.getMessage());
        }
    }
}
