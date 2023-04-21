package net.sf.dynxform.action;

import junit.framework.TestCase;
import net.sf.dynxform.util.CurrentClassloader;
import org.apache.commons.lang.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @version $Revision: 1.5 $ $Date: 2004/08/11 17:32:58 $
 */
public class ActionExecutorTest extends TestCase {

    private static final String ACTION_ID = "callStoredFunction";

    public void testProcedureSequenceAction() throws Exception {
        final Properties props = new Properties();
        props.load(CurrentClassloader.getResourceAsStream(ACTION_ID + ".action-list.test.properties"));
        final HashMap parameters = new HashMap(props);
        final DatabaseActionExecutor actionExecutor = new DatabaseActionExecutor();
        final Map outParameters = actionExecutor.executeAction(ACTION_ID, "insert", parameters);
        assertNotNull(outParameters);
        assertNotNull(outParameters.get("entityid"));
    }

    public void testActionWithOutNotNullParameter() throws Exception {
        final Properties props = new Properties();
        props.load(CurrentClassloader.getResourceAsStream(ACTION_ID + ".action-list.test.properties"));
        final HashMap parameters = new HashMap(props);
        parameters.put("parententityid", null);
        final DatabaseActionExecutor actionExecutor = new DatabaseActionExecutor();
        try {
            actionExecutor.executeAction(ACTION_ID, "insert", parameters);
        } catch (Exception ex) {
            assertEquals(StringUtils.deleteWhitespace("Fail to build resultset for action insertform:(" + ACTION_ID + ")\n" + "NestedException: net.sf.dynxform.exception.business.BusinessException: Failed to create stored procedure SpInsertFollowUp\n" + "NestedException: net.sf.dynxform.exception.business.SQLStatementException: Parameter \"parameter:parententityid\"(pCustomerID) cannot be null!"), StringUtils.deleteWhitespace(ex.getMessage()));
        }
    }

    public void testActionWithOutNullParameter() throws Exception {
        final Properties props = new Properties();
        props.load(CurrentClassloader.getResourceAsStream(ACTION_ID + ".action-list.test.properties"));
        final HashMap parameters = new HashMap(props);
        parameters.put("resultid", null);
        final DatabaseActionExecutor actionExecutor = new DatabaseActionExecutor();
        final Map outParameters = actionExecutor.executeAction(ACTION_ID, "insert", parameters);
        assertNotNull(outParameters);
        assertNotNull(outParameters.get("entityid"));
        assertTrue(Integer.parseInt((String) outParameters.get("entityid")) > 0);
    }
}
