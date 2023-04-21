package com.loribel.commons.util;

import junit.framework.TestCase;
import com.loribel.commons.abstraction.GB_IndexList;
import com.loribel.commons.test.GB_AssertEqualsTools;

/**
 * Test GB_IndexListTools.
 *
 * @author Gr�gory Borelli
 */
public class GB_IndexListToolsTest extends TestCase {

    public GB_IndexListToolsTest(String a_name) {
        super(a_name);
    }

    /**
     * M�thode de test pour tester la m�thode : <tt>stringToBoolean</tt>.
     */
    public void test_parseString() {
        test_parseString("1", null, null, null, 1);
        test_parseString("2", "1", new int[] { 1 }, new int[0], 1);
        test_parseString("3", "-1", new int[0], new int[] { 1 }, 2);
        test_parseString("4", "1 2 5", new int[] { 1, 2, 5 }, new int[0], 1);
        test_parseString("5", "-1 -2 -5", new int[0], new int[] { 1, 2, 5 }, 3);
    }

    private void test_parseString(String a_index, String a_str, int[] a_includes, int[] a_excludes, int a_firstIndex) {
        test_parseString(a_index, a_str, " ", a_includes, a_excludes, a_firstIndex);
    }

    private void test_parseString(String a_index, String a_str, String a_separator, int[] a_includes, int[] a_excludes, int a_firstIndex) {
        GB_IndexList l_indexList = GB_IndexListTools.parseString(a_str, a_separator);
        int[] l_includes = l_indexList.getIncludes();
        int[] l_excludes = l_indexList.getExcludes();
        int l_firstIndex = l_indexList.getFirstIndexInclude();
        GB_AssertEqualsTools.assertEqualsArray(a_index + ".includes", a_includes, l_includes);
        GB_AssertEqualsTools.assertEqualsArray(a_index + ".excludes", a_excludes, l_excludes);
        assertEquals(a_index + ".first", a_firstIndex, a_firstIndex);
    }
}
