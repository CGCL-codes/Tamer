package com.acuityph.commons.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.lang.reflect.Field;
import org.junit.Test;
import test.MyEntity;

/**
 * JUnit test for {@link JpaUtils}.
 *
 * @author Alistair A. Israel
 * @since 0.3.1
 */
public final class JpaUtilsTest {

    /**
     * Test for {@link JpaUtils#constructSelectAllQuery(java.lang.String)} .
     */
    @Test
    public void testConstructSelectAllQuery() {
        final String[] entityNames = { "User", "BankAccount" };
        final String[] expected = { "SELECT user FROM User user", "SELECT bankAccount FROM BankAccount bankAccount" };
        for (int i = 0; i < expected.length; ++i) {
            final String actual = JpaUtils.constructSelectAllQuery(entityNames[i]);
            assertEquals(expected[i], actual);
        }
    }

    /**
     * Test for {@link JpaUtils#determineIdField(Class)}.
     */
    @Test
    public void testDetermineIdField() {
        final Field idField = JpaUtils.determineIdField(MyEntity.class);
        assertNotNull(idField);
        assertEquals("id", idField.getName());
    }
}
