package fr.fg.server.test.action.trade;

import fr.fg.server.data.DataAccess;
import fr.fg.server.data.Fleet;
import fr.fg.server.data.GameConstants;
import fr.fg.server.data.Player;
import fr.fg.server.data.Ship;
import fr.fg.server.data.Slot;
import fr.fg.server.data.StellarObject;
import fr.fg.server.data.Tradecenter;
import fr.fg.server.test.action.TestAction;

public class TestChange extends TestAction {

    public static final String URI = "trade/change";

    private Player owner;

    private Fleet fleet;

    private StellarObject object;

    private Tradecenter tradecenter;

    public void init() {
        owner = new Player("testplayer", "", "", "", "");
        owner.save();
        fleet = new Fleet("fleet", 10, 10, owner.getId(), 4);
        fleet.setSlot(new Slot(Ship.MAMMOTH, 10, true), 0);
        fleet.save();
        object = new StellarObject(10, 10, StellarObject.TYPE_TRADECENTER, 0, 4);
        object.save();
        tradecenter = new Tradecenter(object.getId(), 0.00001, .05);
        tradecenter.setRate(-0.01, 0);
        tradecenter.save();
    }

    public void testChange() throws Exception {
        setPlayer(owner.getLogin());
        owner = DataAccess.getEditable(owner);
        owner.setCredits(1000);
        owner.save();
        doRequest(URI, "fleet=" + fleet.getId() + "&resource=" + 0 + "&count=-1000");
        fleet = DataAccess.getFleetById(fleet.getId());
        tradecenter = DataAccess.getTradecenterById(tradecenter.getIdTradecenter());
        owner = DataAccess.getPlayerById(owner.getId());
        System.out.println("Player credits: " + owner.getCredits());
        System.out.print("Fleet resources: ");
        System.out.println();
        System.out.print("Tradecenter rates: ");
        for (int i = 0; i < GameConstants.RESOURCES_COUNT; i++) System.out.print(tradecenter.getRate(i) + " ");
        System.out.println();
    }

    @Override
    public void cleanUp() {
        owner.delete();
        object.delete();
    }
}
