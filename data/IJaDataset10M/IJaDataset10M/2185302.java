package anveshitha.config;

import java.io.IOException;
import java.util.Arrays;
import junit.framework.TestCase;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

/**
 * @author varun
 */
public class AnveshithaConstantsTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * Test method for {@link anveshitha.config.AnveshithaConstants#loadConfigData()}.
	 * @throws Exception 
	 */
    public void testLoadConfigData() throws Exception {
        AnveshithaConstants.loadConfigData("");
        assertNotNull(AnveshithaConstants.LUCENE_INDEX_DIR);
        System.out.println(AnveshithaConstants.LUCENE_INDEX_DIR);
        assertNotNull(AnveshithaConstants.JAVA_STOP_WORDS);
        System.out.println(Arrays.toString(AnveshithaConstants.JAVA_STOP_WORDS));
        assertNotNull(AnveshithaConstants.JAVA_SRC_DIRS);
        System.out.println(Arrays.toString(AnveshithaConstants.JAVA_SRC_DIRS));
    }
}
