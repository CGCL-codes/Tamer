package org.dctmutils.common.methods.test;

import java.util.HashMap;
import java.util.Map;
import org.dctmutils.common.methods.object.MethodServerArguments;
import org.dctmutils.common.test.DctmUtilsTestCase;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @author <a href="mailto:luther@dctmutil.org">Luther Birdzell</a>
 */
public class MethodServerArgumentsTest extends DctmUtilsTestCase {

    private static final Log log = LogFactory.getLog(MethodServerArgumentsTest.class);

    /**
     * Test method for {@link org.dctmutils.common.methods.object.MethodServerArguments#getCustomArg(java.lang.String)}.
     */
    public void testGetCustomArg() {
        try {
            String docbase = "myDocbase";
            String mode = "1";
            String packageId = "111111112222222";
            String ticket = "foobar";
            String user = "looth";
            String custom = "custom";
            Map parameters = new HashMap();
            parameters.put(MethodServerArguments.DOCBASE_NAME, docbase);
            parameters.put(MethodServerArguments.MODE, mode);
            parameters.put(MethodServerArguments.PACKAGE_ID, packageId);
            parameters.put(MethodServerArguments.TICKET, ticket);
            parameters.put(MethodServerArguments.USER, user);
            parameters.put(custom + "arg", custom);
            MethodServerArguments args = new MethodServerArguments(parameters);
            assertTrue(StringUtils.equals(args.getDocbase(), docbase));
            assertTrue(StringUtils.equals(args.getMode(), mode));
            assertTrue(StringUtils.equals(args.getPackageId(), packageId));
            assertTrue(StringUtils.equals(args.getTicket(), ticket));
            assertTrue(StringUtils.equals(args.getUser(), user));
            assertTrue(StringUtils.equals(args.getCustomArg(custom + "arg"), custom));
            assertTrue(StringUtils.equals(args.getCustomArg(MethodServerArguments.DOCBASE_NAME), docbase));
            assertTrue(StringUtils.equals(args.getCustomArg(MethodServerArguments.MODE), mode));
            assertTrue(StringUtils.equals(args.getCustomArg(MethodServerArguments.PACKAGE_ID), packageId));
            assertTrue(StringUtils.equals(args.getCustomArg(MethodServerArguments.TICKET), ticket));
            assertTrue(StringUtils.equals(args.getCustomArg(MethodServerArguments.USER), user));
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            fail(t.getMessage());
        }
    }
}
