package net.sf.querytool;

import java.util.regex.Matcher;
import junit.framework.TestCase;

/**
 * @author nihy
 * @since 2007-3-8-下午07:35:35
 */
public class UtilTest extends TestCase {

    public void testReplaceRegex() {
        assertEquals("a\\.b", Util.replaceRegexSpecifialCharactes("a.b"));
        assertEquals("a\\.\\.b", Util.replaceRegexSpecifialCharactes("a..b"));
        assertEquals("a\\.\\?b", Util.replaceRegexSpecifialCharactes("a.?b"));
        assertEquals("a\\\\b", Util.replaceRegexSpecifialCharactes("a\\b"));
    }

    public void testRegexMatch() {
        String sql = "select id,name from A a left join b  where a.id = ? and b.name like ? and b.name like ' ? and & * ^ $ ' and b.name like \" ? and & * ^ $ \"";
        Matcher contant_str_m = FilterTag.SINGLE_QUOTE_CONSTANT_STR_PATTERN.matcher(sql);
        sql = contant_str_m.replaceAll("''");
        contant_str_m = FilterTag.DOUBLE_QUOTE_CONSTANT_STR_PATTERN.matcher(sql);
        sql = contant_str_m.replaceAll("\"\"");
        System.out.println(sql);
    }

    public void testReplaceSpaceWithRegexSyntax() {
        String s = " ";
        assertEquals("\\s", s.replaceAll(" ", "\\\\s"));
    }
}
