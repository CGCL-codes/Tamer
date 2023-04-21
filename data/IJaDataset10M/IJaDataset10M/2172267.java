package starcraft.comm.tests;

import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import starcraft.comm.client.StarCraftMessageClient;
import starcraft.gameclient.StarCraftMessageClientEventHandler;
import starcraft.gamemodel.model.player.OrderTokens;
import starcraft.gamemodel.model.races.protoss.TassadarFaction;
import starcraft.gamemodel.model.races.protoss.units.DragoonUnitType;
import starcraft.gamemodel.model.races.protoss.units.ZealotUnitType;
import starcraft.gamemodel.model.races.terrans.JimRaynorFaction;
import starcraft.gamemodel.model.races.terrans.units.FirebatUnitType;
import starcraft.gamemodel.model.races.terrans.units.MarineUnitType;
import starcraft.gamemodel.model.races.zerg.TheOvermindFaction;
import starcraft.gamemodel.model.races.zerg.units.HydraliskUnitType;
import starcraft.gamemodel.model.races.zerg.units.ZerglingUnitType;

public class TestScenario1 {

    public static void main(String[] args) throws Exception {
        final String player_1 = "Nicolas";
        final String player_2 = "Marc";
        final String player_3 = "Nico";
        StarCraftMessageClient sc_1 = new StarCraftMessageClient(new StarCraftMessageClientEventHandler());
        sc_1.connect("127.0.0.1", 8000, 2000);
        sc_1.login(player_1);
        StarCraftMessageClient sc_2 = new StarCraftMessageClient(new StarCraftMessageClientEventHandler());
        sc_2.connect("127.0.0.1", 8000, 2000);
        sc_2.login(player_2);
        StarCraftMessageClient sc_3 = new StarCraftMessageClient(new StarCraftMessageClientEventHandler());
        sc_3.connect("127.0.0.1", 8000, 2000);
        sc_3.login(player_3);
        waitFor(2000);
        sc_1.createGame("battle ground");
        waitFor(1000);
        sc_1.joinGame("battle ground");
        waitFor(1000);
        sc_2.joinGame("battle ground");
        waitFor(1000);
        sc_3.joinGame("battle ground");
        waitFor(1000);
        sc_1.setPlayerFaction(TheOvermindFaction.INSTANCE);
        waitFor(1000);
        sc_2.setPlayerFaction(JimRaynorFaction.INSTANCE);
        waitFor(1000);
        sc_3.setPlayerFaction(TassadarFaction.INSTANCE);
        waitFor(1000);
        sc_1.setStartPlayer("Nicolas");
        waitFor(1000);
        sc_1.setPlayerReady(true);
        waitFor(1000);
        sc_2.setPlayerReady(true);
        waitFor(1000);
        waitFor(1000);
        Map<String, String[]> drawnPlanets = sc_1.getDrawnPlanets();
        String[] drawnPlanets_1 = drawnPlanets.get(player_1);
        String[] drawnPlanets_2 = drawnPlanets.get(player_2);
        String[] drawnPlanets_3 = drawnPlanets.get(player_3);
        sc_1.placePlanet(drawnPlanets_1[0], 0, 0, 0, 0);
        sc_1.doneWithTurn();
        waitFor(1000);
        sc_2.placePlanet(drawnPlanets_2[0], -1, 0, 0, null);
        sc_2.doneWithTurn();
        waitFor(1000);
        sc_3.placePlanet(drawnPlanets_3[0], 1, 1, 0, null);
        sc_3.doneWithTurn();
        waitFor(1000);
        sc_3.placePlanet(drawnPlanets_3[1], 1, 2, 0, 1);
        sc_3.doneWithTurn();
        waitFor(1000);
        sc_2.placePlanet(drawnPlanets_2[1], 2, 1, 0, 2);
        sc_2.doneWithTurn();
        waitFor(1000);
        sc_1.placePlanet(drawnPlanets_1[1], 2, 2, 0, 2);
        sc_1.doneWithTurn();
        waitFor(1000);
        sc_1.placeStartingForce(new String[] { ZerglingUnitType.INSTANCE.getName(), ZerglingUnitType.INSTANCE.getName(), HydraliskUnitType.INSTANCE.getName() }, new byte[] { 0, 0, 1 }, drawnPlanets_1[0], (byte) 1);
        sc_1.doneWithTurn();
        waitFor(1000);
        sc_2.placeStartingForce(new String[] { MarineUnitType.INSTANCE.getName(), MarineUnitType.INSTANCE.getName(), FirebatUnitType.INSTANCE.getName() }, new byte[] { 0, 0, 1 }, drawnPlanets_3[1], (byte) 0);
        sc_2.doneWithTurn();
        waitFor(1000);
        sc_3.placeStartingForce(new String[] { ZealotUnitType.INSTANCE.getName(), ZealotUnitType.INSTANCE.getName(), DragoonUnitType.INSTANCE.getName() }, new byte[] { 0, 0, 1 }, drawnPlanets_2[1], (byte) 1);
        sc_3.doneWithTurn();
        waitFor(1000);
        sc_1.placeOrderToken(drawnPlanets_1[1], OrderTokens.BuildOrder);
        waitFor(1000);
        sc_2.placeOrderToken(drawnPlanets_2[1], OrderTokens.BuildOrder);
        waitFor(1000);
        sc_3.placeOrderToken(drawnPlanets_3[1], OrderTokens.BuildOrder);
        waitFor(1000);
        sc_1.placeOrderToken(drawnPlanets_1[1], OrderTokens.MobilizeOrder);
        waitFor(1000);
        sc_2.placeOrderToken(drawnPlanets_2[1], OrderTokens.MobilizeOrder);
        waitFor(1000);
        sc_3.placeOrderToken(drawnPlanets_3[1], OrderTokens.MobilizeOrder);
        waitFor(1000);
        sc_1.placeOrderToken(drawnPlanets_1[0], OrderTokens.ResearchOrder);
        waitFor(1000);
        sc_2.placeOrderToken(drawnPlanets_2[0], OrderTokens.ResearchOrder);
        waitFor(1000);
        sc_3.placeOrderToken(drawnPlanets_3[0], OrderTokens.ResearchOrder);
        waitFor(1000);
        sc_1.placeOrderToken(drawnPlanets_1[0], OrderTokens.BuildOrder);
        waitFor(1000);
        sc_2.placeOrderToken(drawnPlanets_2[0], OrderTokens.BuildOrder);
        waitFor(1000);
        sc_3.placeOrderToken(drawnPlanets_3[0], OrderTokens.BuildOrder);
        waitFor(1000);
        sc_1.revealOrderToken(drawnPlanets_1[1]);
        showBlockingDialog();
        sc_1.disconnect();
        waitFor(1000);
        sc_2.disconnect();
        waitFor(1000);
        sc_3.disconnect();
        waitFor(1000);
    }

    private static void waitFor(int milliseconds) throws InterruptedException {
        Thread thread = Thread.currentThread();
        synchronized (thread) {
            thread.wait(milliseconds);
        }
    }

    private static void showBlockingDialog() {
        JFrame frame = new JFrame(TestScenario1.class.getName());
        frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        JOptionPane.showConfirmDialog(frame, "waiting...");
        frame.setVisible(false);
        frame.dispose();
    }
}
