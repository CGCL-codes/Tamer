package de.boardgamesonline.bgo2.labyrinth.test.server;

import de.boardgamesonline.bgo2.labyrinth.server.GameServer;
import de.boardgamesonline.bgo2.labyrinth.server.GameEngine;
import de.boardgamesonline.bgo2.labyrinth.server.NewStatus;
import de.boardgamesonline.bgo2.labyrinth.server.Point;
import junit.framework.TestCase;

/**
 * Testklasse f�r GameServer.
 * 
 * @author Leo
 * 
 */
public class GameServerTest extends TestCase {

    /**
	 * Konstruktor
	 */
    public GameServerTest() {
        super();
    }

    /**
     * 
     * @param fName
     *        String Testname
     */
    public GameServerTest(String fName) {
        super(fName);
    }

    /**
     * Der Testserver.
     */
    private GameServer server;

    /**
     * Ein Status-Objekt zur Überprüfung.
     */
    private NewStatus status;

    /**
     * 
     * @param r
     *        Array of Strings
     * @param s
     *        Array of Strings
     * @return true, wenn alle Strings paarweise gleich (an Stelle i)
     */
    public boolean compare(String[] r, String[] s) {
        int l = r.length;
        if (l == s.length) {
            if (l != 0) {
                for (int i = 0; i < l; i++) {
                    if (!r[i].equals(s[i])) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Legt die Reihenfolge der Tests fest.
     * 
     * @throws Exception
     *         if setUp() fails.
     */
    public void runTest() throws Exception {
        setUp();
        testAddPlayer();
        testGetPlayers();
        testGetSpectators();
        testCanStart();
        testStartGame();
        testGetNewStatus();
        testRotatePushCard();
        testPush();
        testMovePlayer();
        testCanClose();
        testCloseGame();
    }

    /**
     * Die Methode wird einmal aussgefuehrt bevor die Tests beginnen.
     * 
     * @throws Exception
     *             Falls das setUp mislingt.
     */
    public void setUp() throws Exception {
        server = new GameServer();
    }

    /**
     * Testet register(int clientID, int gameID).
     */
    public void testAddPlayer() {
        assertEquals(0, server.addLocalPlayer("client1", "game1", "1", false));
        assertEquals(GameServer.ALREADY_JOINED, server.addLocalPlayer("client1", "game1", "1", false));
        assertEquals(1, server.addLocalPlayer("client2", "game1", "2", false));
        assertEquals(2, server.addLocalPlayer("client3", "game1", "3", false));
        assertEquals(3, server.addLocalPlayer("client4", "game1", "4", false));
        assertEquals(GameServer.CANNOT_JOIN, server.addLocalPlayer("client5", "game1", "5", false));
        assertEquals(GameEngine.SPECTATOR_ID, server.addLocalPlayer("client5", "game1", "5", true));
        assertEquals(GameEngine.SPECTATOR_ID, server.addLocalPlayer("client6", "game1", "6", true));
        assertEquals(0, server.addLocalPlayer("client7", "game2", "7", false));
        assertEquals(1, server.addLocalPlayer("client8", "game2", "8", false));
        assertEquals(0, server.addLocalPlayer("client9", "game3", "9", false));
        assertNull(server.getNewStatus("client1"));
    }

    /**
     * Testet getPlayers(gameID)
     */
    public void testGetPlayers() {
        String[] clients1 = { "1", "2", "3", "4" };
        String[] clients2 = { "7", "8" };
        String[] clients3 = { "9" };
        assertNull(server.getPlayers(""));
        assertNull(server.getSpectators(""));
        assertNotNull(server.getPlayers("game1"));
        assertNotNull(server.getSpectators("game1"));
        assertTrue(compare(clients1, server.getPlayers("game1")));
        assertTrue(compare(clients2, server.getPlayers("game2")));
        assertTrue(compare(clients3, server.getPlayers("game3")));
    }

    /**
     * Testet getSpectators(gameID)
     */
    public void testGetSpectators() {
        String[] specs1 = { "5", "6" };
        String[] empty = new String[0];
        assertTrue(compare(specs1, server.getSpectators("game1")));
        assertTrue(compare(empty, server.getSpectators("game2")));
        assertTrue(compare(empty, server.getSpectators("game3")));
    }

    /**
     * Testet canStart()
     */
    public void testCanStart() {
        assertFalse(server.canStart("", "game1"));
        assertTrue(server.canStart("client1", "game1"));
        assertTrue(server.canStart("client7", "game2"));
        assertFalse(server.canStart("client2", "game1"));
        assertFalse(server.canStart("client3", "game1"));
        assertFalse(server.canStart("client4", "game1"));
        assertFalse(server.canStart("client8", "game2"));
        assertFalse(server.canStart("client5", "game1"));
        assertFalse(server.canStart("client6", "game1"));
        assertTrue(server.canStart("client9", "game3"));
        assertFalse(server.canStart("client1", ""));
        assertFalse(server.canStart("client2", ""));
        assertFalse(server.canStart("client3", ""));
        assertFalse(server.canStart("client4", ""));
        assertFalse(server.canStart("client5", ""));
        assertFalse(server.canStart("client6", ""));
        assertFalse(server.canStart("client7", ""));
        assertFalse(server.canStart("client8", ""));
        assertFalse(server.canStart("client9", ""));
    }

    /**
     * Testet startGame(clientID, gameID)
     */
    public void testStartGame() {
        assertEquals(GameServer.CANNOT_START, server.startGame("", ""));
        assertEquals(GameServer.CANNOT_START, server.startGame("client2", "game1"));
        assertEquals(GameServer.STARTING, server.startGame("client1", "game1"));
        assertEquals(GameServer.CANNOT_START, server.startGame("client9", "game3"));
    }

    /**
     * Testet getNewStatus(clientID)
     */
    public void testGetNewStatus() {
        assertNull(server.getNewStatus(""));
        status = server.getNewStatus("client1");
        assertEquals(0, status.getActivePlayer());
        assertNotNull(server.getNewStatus("client5"));
    }

    /**
     * Testet rotatePushCard(clientID, steps)
     */
    public void testRotatePushCard() {
        server.rotatePushCard("", 1);
        server.rotatePushCard("client2", 1);
        server.rotatePushCard("client3", 1);
        server.rotatePushCard("client1", 1);
        for (int i = 0; i <= 3; i++) {
            status = server.getNewStatus("client1");
            if (status.getPushCard().hasNorthExit()) {
                break;
            } else {
                server.rotatePushCard("client1", 1);
            }
        }
    }

    /**
     * Testet push(clientID)
     */
    public void testPush() {
        assertFalse(server.push("", new Point(0, 1)));
        assertFalse(server.push("client2", new Point(0, 1)));
        assertFalse(server.push("client5", new Point(0, 1)));
        assertTrue(server.push("client1", new Point(0, 1)));
    }

    /**
     * Testet movePlayer(clientID)
     */
    public void testMovePlayer() {
        assertFalse(server.movePlayer("", new Point(0, 1)));
        assertFalse(server.movePlayer("client2", new Point(0, 1)));
        assertFalse(server.movePlayer("client5", new Point(0, 1)));
        assertTrue(server.movePlayer("client1", new Point(0, 1)));
    }

    /**
     * Teste canClose(clientID, gameID)
     *
     */
    public void testCanClose() {
        assertTrue(server.canClose("client1", "game1"));
        assertTrue(server.canClose("client7", "game2"));
        assertTrue(server.canClose("client9", "game3"));
        assertFalse(server.canClose("client1", ""));
        assertFalse(server.canClose("client1", "game2"));
        assertFalse(server.canClose("client1", "game3"));
        assertFalse(server.canClose("client7", "game1"));
        assertFalse(server.canClose("client7", "game3"));
        assertFalse(server.canClose("client9", "game1"));
        assertFalse(server.canClose("client9", "game2"));
        assertFalse(server.canClose("client2", "game1"));
        assertFalse(server.canClose("client3", "game1"));
        assertFalse(server.canClose("client4", "game1"));
        assertFalse(server.canClose("client5", "game1"));
        assertFalse(server.canClose("client6", "game1"));
        assertFalse(server.canClose("client8", "game2"));
        assertFalse(server.canClose("client2", "game2"));
        assertFalse(server.canClose("client3", "game3"));
        assertFalse(server.canClose("client4", "game3"));
        assertFalse(server.canClose("client5", "game2"));
        assertFalse(server.canClose("client6", "game2"));
        assertFalse(server.canClose("client8", "game1"));
    }

    /**
     * Testet closeGame(clientID)
     */
    public void testCloseGame() {
        server.closeGame("client2", "game1");
        assertNotNull(server.getNewStatus("client1"));
        assertNotNull(server.getNewStatus("client1"));
        server.closeGame("client1", "game1");
        assertNotNull(server.getNewStatus("client1"));
        assertNotNull(server.getNewStatus("client2"));
        assertNotNull(server.getNewStatus("client3"));
        assertNotNull(server.getNewStatus("client4"));
        assertNotNull(server.getNewStatus("client5"));
        assertNotNull(server.getNewStatus("client6"));
        assertNull(server.getNewStatus("client1"));
        assertNull(server.getNewStatus("client2"));
        assertNull(server.getNewStatus("client3"));
        assertNull(server.getNewStatus("client4"));
        assertNull(server.getNewStatus("client5"));
        assertNull(server.getNewStatus("client6"));
        server.closeGame("client7", "game2");
        assertNull(server.getPlayers("game2"));
        server.closeGame("client9", "game3");
        assertNull(server.getPlayers("game3"));
    }
}
