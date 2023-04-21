package nuts.core.lang;

import java.util.HashMap;
import java.util.Map;
import nuts.core.lang.StringUtils;
import junit.framework.TestCase;

/**
 * test class for StringUtils
 */
public class StringUtilsTest extends TestCase {

    /**
	 * test method: wildMatch
	 * @throws Exception if an error occurs
	 */
    public void testWildMatch() throws Exception {
        assertTrue(StringUtils.wildMatch("ab", "ab"));
        assertFalse(StringUtils.wildMatch("ab", "abc"));
        assertTrue(StringUtils.wildMatch("a?b", "aab"));
        assertFalse(StringUtils.wildMatch("a?b", "abc"));
        assertTrue(StringUtils.wildMatch("a*b", "aaaccdsbbb"));
        assertFalse(StringUtils.wildMatch("a*z", "aaaccdsbbb"));
    }

    /**
	 * test method: translate
	 * @throws Exception if an error occurs
	 */
    public void testTranslate() throws Exception {
        Map<String, String> m = new HashMap<String, String>();
        m.put("a", "1");
        m.put("a.b", "2");
        m.put("a.b.c", "3");
        assertEquals("1.23-${8}-xx", StringUtils.translate("${a}.${a.b}${a.b.c}-${8}-xx", m));
    }
}
