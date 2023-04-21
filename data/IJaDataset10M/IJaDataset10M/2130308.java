package utils;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SpringUtilTest extends TestCase {

    /** The logger. */
    private static Log logger = LogFactory.getLog(SpringUtilTest.class.getName());

    public void testGetBean() {
        SpringUtil ctxUtil = SpringUtil.getInstance(null);
    }
}
