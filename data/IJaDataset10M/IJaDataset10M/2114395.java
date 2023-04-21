package fr.fg.server.test.action.system;

import org.json.JSONObject;
import fr.fg.server.data.DataAccess;
import fr.fg.server.test.action.TestAction;

public class TestGetSystem extends TestAction {

    public static final String URI = "systems/get";

    public void testGetSystem() throws Exception {
        setPlayer("veldryn");
        JSONObject answer = doRequest(URI, "system=" + DataAccess.getSystemsByOwner(getPlayer().getId()).get(0).getId());
        System.out.println(answer.toString(2));
        assertEquals("success", answer.get("type"));
    }
}
