package curso;

import junit.framework.TestCase;
import com.opensymphony.xwork2.Action;

/**
 * 
 */
public class HelloWorldActionTest extends TestCase {

    public void testHelloWorldAction() throws Exception {
        HelloWorldAction action = new HelloWorldAction();
        String result = action.execute();
        assertEquals(Action.SUCCESS, result);
    }
}
