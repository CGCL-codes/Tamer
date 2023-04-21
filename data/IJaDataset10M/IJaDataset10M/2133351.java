package sg.edu.ntu.sci.blackboard.agilix;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sg.edu.ntu.sci.blackboard.agilix.model.Doc;
import sg.edu.ntu.sci.blackboard.agilix.util.IOUtil;

/**
 *
 * @author ngocgiang
 */
public class LODocumentResolverTest {

    public LODocumentResolverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of resolve method, of class LODocumentResolver.
     */
    @Test
    public void testRegex() {
        try {
            String regex = "http://[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%;/\\.\\w]+)?";
            String fragmentContent = IOUtil.readFile("C:\\Documents and Settings\\Lenovo\\Application Data\\Blackboard\\Backpack\\1\\_628357_1.doc_0\\fragment.htm");
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fragmentContent);
            assertTrue(matcher.find());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
