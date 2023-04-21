package uk.org.beton.ftpsync.model;

import junit.framework.TestCase;
import uk.org.beton.BeanTester;

/**
 * Module test vectors for {@link UserOptions}.
 *
 * @author Rick Beton
 * @version $Id: HtmlSplitterTest.java 513 2007-03-07 21:33:19Z rick $
 */
public class UserOptionsTest extends TestCase {

    public void test1() throws Exception {
        final char[] c1 = { 'a', 'b' };
        final char[] c2 = { 'c' };
        BeanTester.addFixture(char[].class, new Object[] { c1, c2 });
        BeanTester.test(new UserOptions(), new String[] { "action", "passive", "recursive", "cleaning", "forceBinary", "forceText", "owDiffSize", "threads", "tries", "infoLevel", "baseDir", "dir", "username", "password" });
    }
}
