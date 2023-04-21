package test.scotlandyard;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scotlandyard.engine.impl.Engine;
import scotlandyard.engine.impl.Game;
import scotlandyard.engine.spec.IBMap;
import scotlandyard.engine.spec.IPlayer;
import scotlandyard.engine.spec.IUser;

/**
 *
 * @author Admin
 */
public class GameTest {

    String mrxEmail = "something";

    String detectiveEmail = "something else";

    String mrxName = "random";

    String detectiveName = "other random";

    String mrxHash = Engine.md5(mrxEmail);

    String detectiveHash = Engine.md5(detectiveEmail);

    String gameId = "New Game";

    public GameTest() throws Exception {
    }

    @Test
    public void testEXPOSE() {
        final Game game = new Game();
        Assert.assertTrue(game.isRoundExposingMrX(3));
        Assert.assertTrue(game.isRoundExposingMrX(8));
        Assert.assertTrue(game.isRoundExposingMrX(13));
        Assert.assertTrue(game.isRoundExposingMrX(18));
        Assert.assertFalse(game.isRoundExposingMrX(23));
    }

    @Before
    public void setup() throws Exception {
        String mp = "web/maps/palmerstonNorth.xml";
        gameId = (gameId == null || "optional".equals(gameId) ? System.currentTimeMillis() + "" : gameId);
        Game game = new Game(gameId, mp);
        Engine myEngine = Engine.instance();
        myEngine.games.put(game.getId(), game);
        game.setMapPath("/maps/palmerstonNorth.xml");
        myEngine.icons.put("x1.gif", false);
        myEngine.icons.put("d1.gif", false);
        IUser mrx = myEngine.login(mrxEmail, mrxName, "0");
        IUser detective = myEngine.login(detectiveEmail, detectiveName, "1");
        game.addPlayer(mrx, true);
        game.addPlayer(detective, false);
        IPlayer player = game.getPlayer(mrxHash);
        Assert.assertNotNull(player);
        game.start(player);
    }

    @Test
    public void testStartPositions() {
        boolean detectiveEmailFound = false;
        boolean mrxEmailFound = false;
        Game game = Engine.instance().games.get(gameId);
        IBMap map = game.getMap();
        String[] positions = map.getPositions();
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] == detectiveEmail) {
                if (detectiveEmailFound) {
                    Assert.assertEquals("the detectives email is already there", false, true);
                }
                detectiveEmailFound = true;
            }
            if (positions[i] == mrxEmail) {
                if (mrxEmailFound) {
                    Assert.assertEquals("mrx's email is already there", false, true);
                }
                mrxEmailFound = true;
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        Engine.instance().logout(mrxHash);
        Engine.instance().logout(detectiveHash);
    }

    @Test
    public void testGetMoves() {
        Game game = Engine.instance().games.get(gameId);
        IPlayer player = game.getPlayer(mrxHash);
        player.setPosition(1);
        Integer[] moves = game.getLegalMoves(player.getEmail());
        Assert.assertTrue(contains(moves, 16));
    }

    @Test
    public void testGetMovesWithMissingTicket() {
        Game game = Engine.instance().games.get(gameId);
        IPlayer player = game.getPlayer(mrxHash);
        player.setTickets(4, 0);
        player.setPosition(1);
        Integer[] moves = game.getLegalMoves(player.getEmail());
        Assert.assertTrue(!contains(moves, 16));
        player.setTickets(0, 0);
        moves = game.getLegalMoves(player.getEmail());
        Assert.assertTrue(!contains(moves, 1));
    }

    @Test
    public void testincreaseMrxTokens() {
        Game game = Engine.instance().games.get(gameId);
        IPlayer mrXplayer = game.getPlayer(mrxHash);
        IPlayer detectivePlayer = game.getPlayer(detectiveHash);
        mrXplayer.setPosition(4);
        try {
            game.movePlayer(mrXplayer.getEmail(), 5, 2);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
        detectivePlayer.setPosition(2);
        try {
            int before = detectivePlayer.getTickets(1);
            game.movePlayer(detectiveEmail, 3, 1);
            int after = detectivePlayer.getTickets(1);
            if (after != (before - 1)) {
                Assert.assertEquals("detective tickets havent ben decreased properly", 2, 3);
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
            Assert.assertTrue(false);
        }
    }

    public static boolean contains(Integer[] array, Integer item) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(item)) {
                return true;
            }
        }
        return false;
    }
}
