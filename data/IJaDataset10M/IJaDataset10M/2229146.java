package com.loribel.commons.util.filter;

import junit.framework.TestCase;

/**
 * Test GB_StringFilterWildcards.
 *
 * @author Gr�gory Borelli
 */
public class GB_StringFilterWildcardsTest extends TestCase {

    public GB_StringFilterWildcardsTest(String a_name) {
        super(a_name);
    }

    public void testFilter() {
        String l_value = null;
        String l_expression = null;
        boolean l_result = false;
        testFilter("1", l_value, l_expression, l_result);
        l_value = "toto";
        l_expression = "?oto";
        l_result = true;
        testFilter("2", l_value, l_expression, l_result);
        l_value = "ToTo";
        l_expression = "T*A";
        l_result = false;
        testFilter("3", l_value, l_expression, l_result);
        l_value = "ToTo";
        l_expression = "T*o";
        l_result = true;
        testFilter("4", l_value, l_expression, l_result);
        l_value = "un TOTO";
        l_expression = "*TOTO";
        l_result = true;
        testFilter("5", l_value, l_expression, l_result);
    }

    private void testFilter(String a_index, String a_value, String a_expression, boolean a_result) {
        GB_StringFilterWildcards l_filter = new GB_StringFilterWildcards(a_expression);
        boolean l_result = l_filter.accept(a_value);
        assertEquals(a_index + ".1", a_result, l_result);
        l_filter = new GB_StringFilterWildcards(a_expression, true, false);
        l_result = l_filter.accept(a_value);
        if (a_value == null) {
            assertEquals(a_index + ".2a", true, l_result);
        } else {
            assertEquals(a_index + ".2b", a_result, l_result);
        }
        l_filter = new GB_StringFilterWildcards(a_expression, false, false);
        l_result = l_filter.accept(a_value);
        if (a_value == null) {
            assertEquals(a_index + ".3a", false, l_result);
        } else {
            assertEquals(a_index + ".3b", a_result, l_result);
        }
        l_filter = new GB_StringFilterWildcards(a_expression, true, true);
        l_result = l_filter.accept(a_value);
        if (a_value == null) {
            assertEquals(a_index + ".4a", true, l_result);
        } else {
            assertEquals(a_index + ".4b", !a_result, l_result);
        }
    }
}
