package fr.fg.server.test.action.system;

import org.json.JSONObject;
import fr.fg.server.data.Area;
import fr.fg.server.data.DataAccess;
import fr.fg.server.data.Player;
import fr.fg.server.data.StarSystem;
import fr.fg.server.test.action.TestAction;

public class TestBuyFleet extends TestAction {

    public static final String URI = "buyfleet";

    public StarSystem system;

    public Player buyer;

    public Area area;

    public void init() {
        buyer = new Player("testBuyer", "testBuyer", "testBuyer@testBuyer.com", "", "");
        DataAccess.save(buyer);
        area = new Area("testBuyArea", 100, 100, 0, 0, Area.AREA_START, 2, 0);
        DataAccess.save(area);
        system = new StarSystem("testBuyPlace", 50, 50, false, 1, 1, 3, new int[] { 1, 1, 1, 1, 1, 1, 1, 1 }, area.getId(), buyer.getId());
        system.setResource(10000, StarSystem.TITANIUM);
        DataAccess.save(system);
    }

    public void testBuyFleetOk() throws Exception {
        setPlayer("testBuyer");
        JSONObject answer = doRequest(URI, "system=" + system.getId());
        System.out.println(answer.toString(2));
        assertEquals(SUCCESS, answer.get("type"));
    }

    public void cleanUp() {
        area.delete();
        buyer.delete();
    }
}
