package fr.fg.server.test.action.ladder;

import org.json.JSONObject;
import fr.fg.server.test.action.TestAction;

public class TestGetLadder extends TestAction {

    public static final String URI = "ladder/get";

    public void testGetPlayerLadder() throws Exception {
        for (int i = 0; i < 5; i++) {
            JSONObject answer = doRequest(URI, "ladder=players&range=" + i + "&self=true");
            System.out.println(answer.toString(2));
            answer = doRequest(URI, "ladder=players&range=" + i + "&self=false");
            System.out.println(answer.toString(2));
        }
    }

    public void testGetAllyLadder() throws Exception {
        for (int i = 0; i < 5; i++) {
            JSONObject answer = doRequest(URI, "ladder=allies&range=" + i + "&self=true");
            System.out.println(answer.toString(2));
            answer = doRequest(URI, "ladder=allies&range=" + i + "&self=false");
            System.out.println(answer.toString(2));
        }
    }
}
