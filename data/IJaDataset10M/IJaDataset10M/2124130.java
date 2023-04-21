package test.scotlandyard;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import scotlandyard.engine.impl.Engine;
import scotlandyard.servlets.users.dologin;
import scotlandyard.servlets.users.dologout;
import scotlandyard.servlets.web.reset_game_engine;
import web.JavascriptEval;

public class dologoutTest {

    private static String engine_status;

    final String[][] users = { { "Hussain", "hussain" }, { "Ali", "ali" } };

    @BeforeClass
    public static void beforeClass() throws Exception {
        engine_status = new reset_game_engine().processRequest("-");
    }

    private String getUserLoginOutput(int i) throws Exception {
        final String[] user = users[i];
        dologin servlogin = new dologin(user[0], user[1]);
        return servlogin.processRequest("-");
    }

    @Test
    public void testProcessRequest() throws Exception {
        assertEquals("test if the engine has been reset or not", "the engine is reset now", JavascriptEval.getJSONproperty(engine_status, "msg"));
        assertEquals(Engine.instance().users.size(), 0);
        assertEquals("test if user [0] has logged in", Engine.md5("hussain"), JavascriptEval.getJSONproperty(getUserLoginOutput(0), "msg"));
        assertEquals("test if user [1] can be logged in", Engine.md5("ali"), JavascriptEval.getJSONproperty(getUserLoginOutput(1), "msg"));
        assertEquals(Engine.instance().users.size(), 2);
        assertEquals("OK", JavascriptEval.getJSONproperty(new dologout(Engine.md5("hussain")).processRequest("-"), "msg"));
        assertEquals(Engine.instance().users.size(), 1);
        assertEquals("EXCEPTION : Can not logout: user is not in the users list", JavascriptEval.getJSONproperty(new dologout(Engine.md5("Ali")).processRequest("-"), "msg"));
        assertEquals(Engine.instance().users.size(), 1);
        assertEquals("OK", JavascriptEval.getJSONproperty(new dologout(Engine.md5("ali")).processRequest("-"), "msg"));
        assertEquals(Engine.instance().users.size(), 0);
    }
}
