package de.boardgamesonline.bgo2.labyrinth.test.server;

import junit.framework.TestCase;
import java.util.Vector;
import de.boardgamesonline.bgo2.labyrinth.server.Avatar;
import de.boardgamesonline.bgo2.labyrinth.server.GameBoard;
import de.boardgamesonline.bgo2.labyrinth.server.GameEngine;
import de.boardgamesonline.bgo2.labyrinth.server.Item;
import de.boardgamesonline.bgo2.labyrinth.server.LabyrinthCard;
import de.boardgamesonline.bgo2.labyrinth.server.Point;
import de.boardgamesonline.bgo2.labyrinth.server.StoreStatus;

/**
 * @author thz
 * 
 */
public class GameEngineTest extends TestCase {

    /**
	 * Test-GameEngine
	 */
    private GameEngine engine;

    /**
	 * Test Spieler
	 */
    private Vector<Avatar> players;

    /**
	 * @throws java.lang.Exception
         *         Exception
	 */
    public void setUp() throws Exception {
        players = new Vector<Avatar>(4);
        for (int i = 0; i < 4; i++) {
            players.add(new Avatar(i, new String("Spieler " + (i + 1))));
        }
        engine = new GameEngine(players, 7, 7);
    }

    /**
	 * @throws java.lang.Exception
         *         Exception
	 */
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for
	 * {@link de.boardgamesonline.bgo2.labyrinth.server.GameEngine
	 * #GameEngine(java.util.Vector, int, int)}.
	 */
    public void testGameEngine() {
    }

    /**
	 * Test method for
	 * {@link de.boardgamesonline.bgo2.labyrinth.server.GameEngine
	 * #rotatePushCard(int, int)}.
	 */
    public void testRotatePushCard() {
        assertEquals(engine.rotatePushCard(1, 1), false);
        assertEquals(engine.rotatePushCard(0, 1), true);
        engine.push(0, new Point(0, 1));
        assertEquals(engine.rotatePushCard(0, 1), false);
    }

    /**
	 * Test method for
	 * {@link de.boardgamesonline.bgo2.labyrinth.server.GameEngine #push(int,
	 * de.boardgamesonline.bgo2.labyrinth.server.Point)}.
	 */
    public void testPush() {
        Point start0 = engine.getNewStatus(0).getPlayers()[0].getPosition();
        Point start1 = engine.getNewStatus(1).getPlayers()[1].getPosition();
        Point start2 = engine.getNewStatus(2).getPlayers()[2].getPosition();
        Point start3 = engine.getNewStatus(3).getPlayers()[3].getPosition();
        assertEquals(engine.push(0, new Point(0, 2)), false);
        assertEquals(engine.push(1, new Point(0, 1)), false);
        assertEquals(engine.push(0, new Point(0, 1)), true);
        assertEquals(engine.push(0, new Point(0, 1)), false);
        assertEquals(engine.movePlayer(0, start0), true);
        assertEquals(engine.push(1, new Point(6, 1)), false);
        assertEquals(engine.push(1, new Point(1, 0)), true);
        assertEquals(engine.movePlayer(1, start1), true);
        assertEquals(engine.push(2, new Point(1, 6)), false);
        assertEquals(engine.push(2, new Point(6, 3)), true);
        assertEquals(engine.movePlayer(2, start2), true);
        assertEquals(engine.push(3, new Point(0, 3)), false);
        assertEquals(engine.push(3, new Point(5, 6)), true);
        assertEquals(engine.movePlayer(3, start3), true);
        assertEquals(engine.push(0, new Point(5, 0)), false);
        assertEquals(engine.push(0, new Point(6, 3)), true);
        assertEquals(engine.movePlayer(0, start0), true);
    }

    /**
	 * Test method for
	 * {@link de.boardgamesonline.bgo2.labyrinth.server.GameEngine
	 * #movePlayer(int, de.boardgamesonline.bgo2.labyrinth.server.Point)}.
	 */
    public void testMovePlayer() {
        assertEquals(engine.movePlayer(0, new Point(1, 1)), false);
        assertEquals(engine.movePlayer(1, new Point(1, 1)), false);
        assertEquals(engine.push(0, new Point(0, 1)), true);
        assertEquals(engine.movePlayer(1, new Point(1, 1)), false);
        assertEquals(engine.movePlayer(0, new Point(0, 0)), true);
        assertEquals(engine.movePlayer(0, new Point(0, 0)), false);
        assertEquals(engine.push(1, new Point(0, 1)), true);
        assertEquals(engine.movePlayer(1, new Point(6, 0)), true);
        assertEquals(engine.push(2, new Point(0, 1)), true);
        assertEquals(engine.movePlayer(2, new Point(0, 6)), true);
        assertEquals(engine.push(3, new Point(0, 1)), true);
        assertEquals(engine.movePlayer(3, new Point(6, 6)), true);
        try {
            assertEquals(engine.push(4, new Point(0, 1)), false);
            fail("Avatararray duerfte keinen Eintrag fuer Spieler 4 enthalten." + "Also muesste eine ArrayIndexOutOfBounds-Exception entstehen.");
        } catch (RuntimeException e) {
            e = null;
        }
        assertEquals(engine.push(0, new Point(0, 1)), true);
        assertEquals(engine.movePlayer(0, new Point(0, 0)), true);
    }

    /**
	 * Test method for
	 * {@link de.boardgamesonline.bgo2.labyrinth.server.GameEngine
	 * #initPlayerPostions(int, int)}.
	 */
    public void testInitPlayerPositions() {
        LabyrinthCard[] spielbrett = engine.getNewStatus(0).getGameBoard();
        GameBoard board = new GameBoard(spielbrett, new LabyrinthCard());
        Avatar a1 = players.elementAt(0);
        Avatar a2 = players.elementAt(1);
        Avatar a3 = players.elementAt(2);
        Avatar a4 = players.elementAt(3);
        assertTrue(a1.getPosition().equals(new Point(0, 0)));
        assertTrue(a2.getPosition().equals(new Point(6, 0)));
        assertTrue(a3.getPosition().equals(new Point(0, 6)));
        assertTrue(a4.getPosition().equals(new Point(6, 6)));
        assertTrue((board.getCardAt(new Point(0, 0)).getPlayers()[0].equals(a1)));
        assertTrue((board.getCardAt(new Point(6, 0)).getPlayers()[1].equals(a2)));
        assertTrue((board.getCardAt(new Point(0, 6)).getPlayers()[2].equals(a3)));
        assertTrue((board.getCardAt(new Point(6, 6)).getPlayers()[3].equals(a4)));
    }

    /**
	 * Test method for
	 * {@link de.boardgamesonline.bgo2.labyrinth.server.GameEngine
	 * #initItems()}.
	 */
    public void testInitItems() {
        Vector<Vector<Item>> all = engine.getItems();
        Vector<Item> items;
        for (int i = 0; i < all.size(); i++) {
            System.out.print("Spieler" + i + ": ");
            items = all.elementAt(i);
            for (int j = 0; j < items.size(); j++) {
                System.out.print(items.elementAt(j).getName() + "(" + items.elementAt(j).getID() + "), ");
            }
            System.out.print("\n");
        }
    }

    public void testConvertStatus() {
        StoreStatus status = new StoreStatus();
        status.setBoard(new GameBoard());
        Avatar avatar1 = new Avatar(0, "Bob");
        avatar1.setPosition(new Point(2, 3));
        Avatar avatar2 = new Avatar(1, "Jim");
        avatar2.setPosition(new Point(3, 4));
        Vector<Avatar> players = new Vector<Avatar>();
        players.add(avatar1);
        players.add(avatar2);
        status.setPlayers(players);
        boolean[] activePlayers = new boolean[4];
        activePlayers[0] = true;
        activePlayers[1] = true;
        activePlayers[2] = false;
        activePlayers[3] = false;
        status.setActivePlayers(activePlayers);
        status.setNumActivePlayers(2);
        status.setWinner(-1);
        int[] ranking = new int[2];
        ranking[0] = -1;
        ranking[1] = -1;
        status.setRanking(ranking);
        Vector<Vector<Item>> items = new Vector<Vector<Item>>();
        Item eidechse = new Item("Eidechse", 1);
        Item reif = new Item("Goldarmreif", 2);
        Item geist = new Item("Geist", 4);
        Item uhu = new Item("Uhu", 5);
        Vector<Item> vec1 = new Vector<Item>();
        Vector<Item> vec2 = new Vector<Item>();
        vec1.add(eidechse);
        vec1.add(reif);
        vec2.add(geist);
        vec2.add(uhu);
        items.add(vec1);
        items.add(vec2);
        status.setItems(items);
        Vector<Vector<Item>> visitedItems = new Vector<Vector<Item>>();
        Item troll = new Item("Troll", 6);
        Item krone = new Item("Krone", 7);
        Item ritterhelm = new Item("Ritterhelm", 10);
        Item spinne = new Item("Spinne", 12);
        Vector<Item> vec3 = new Vector<Item>();
        Vector<Item> vec4 = new Vector<Item>();
        vec3.add(troll);
        vec3.add(krone);
        vec4.add(ritterhelm);
        vec4.add(spinne);
        visitedItems.add(vec3);
        visitedItems.add(vec4);
        status.setVisitedItems(visitedItems);
        status.setActivePlayer(1);
        status.setHasPushed(true);
        status.setLastActionType(1);
        status.setLastPush(new Point(0, 0));
        Point[] lastMove = new Point[2];
        lastMove[0] = new Point(1, 1);
        lastMove[1] = new Point(2, 3);
        status.setLastMove(lastMove);
        status.setGameStatus(1);
        GameEngine testEngine = new GameEngine(status);
        String serializedStatus = testEngine.convertStatus(status);
        StoreStatus restoredStatus = testEngine.convertStatus(serializedStatus);
        assertEquals(restoredStatus.getActivePlayer(), status.getActivePlayer());
        assertEquals(restoredStatus.getGameStatus(), status.getGameStatus());
        assertEquals(restoredStatus.getLastActionType(), status.getLastActionType());
        assertTrue(restoredStatus.getLastPush().equals(status.getLastPush()));
        assertEquals(restoredStatus.getNumActivePlayers(), status.getNumActivePlayers());
        assertEquals(restoredStatus.getWinner(), status.getWinner());
    }
}
