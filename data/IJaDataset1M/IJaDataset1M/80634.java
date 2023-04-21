package net.sf.freecol.server;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import net.sf.freecol.FreeCol;
import net.sf.freecol.common.FreeColException;
import net.sf.freecol.common.io.FreeColSavegameFile;
import net.sf.freecol.common.model.Game;
import net.sf.freecol.common.model.Map;
import net.sf.freecol.common.networking.NoRouteToServerException;
import net.sf.freecol.server.control.ChangeSet;
import net.sf.freecol.server.control.Controller;
import net.sf.freecol.server.control.InGameController;
import net.sf.freecol.server.control.PreGameController;
import net.sf.freecol.server.model.ServerGame;
import net.sf.freecol.util.test.FreeColTestCase;
import net.sf.freecol.util.test.MockMapGenerator;

public final class ServerTestHelper {

    private static final String SERVER_NAME = "MyTestServer";

    private static final int SERVER_PORT = FreeCol.getDefaultPort();

    private static final String TEST_FILE = "test/data/test.fsg";

    private static FreeColServer server = null;

    private static Random random = null;

    public static FreeColServer getServer() {
        return server;
    }

    public static void setServer(FreeColServer newServer) {
        server = newServer;
    }

    public static InGameController getInGameController() {
        return server.getInGameController();
    }

    public static void stopServer(FreeColServer serv) {
        if (serv != null) {
            Controller c = serv.getController();
            assertNotNull(c);
            c.shutdown();
            serv = null;
        }
    }

    public static FreeColServer startServer(boolean publicServer, boolean singlePlayer) {
        return startServer(publicServer, singlePlayer, SERVER_PORT, SERVER_NAME);
    }

    public static FreeColServer startServer(boolean publicServer, boolean singleplayer, int port, String name) {
        stopServer(server);
        try {
            server = new FreeColServer(FreeColTestCase.spec(), publicServer, singleplayer, port, name);
        } catch (NoRouteToServerException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertNotNull(server);
        assertEquals(FreeColServer.GameState.STARTING_GAME, server.getGameState());
        return server;
    }

    public static FreeColServer startServer(File file, boolean publicServer, boolean singleplayer) {
        return startServer(file, publicServer, singleplayer, SERVER_PORT, SERVER_NAME);
    }

    public static FreeColServer startServer(File file, boolean publicServer, boolean singleplayer, int port, String name) {
        stopServer(server);
        try {
            server = new FreeColServer(new FreeColSavegameFile(file), port, name);
        } catch (NoRouteToServerException e) {
            fail(e.getMessage());
        } catch (FreeColException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertNotNull(server);
        assertEquals(FreeColServer.GameState.IN_GAME, server.getGameState());
        return server;
    }

    public static File createRandomSaveGame() {
        FreeColServer serv = startServer(false, true, SERVER_PORT, SERVER_NAME);
        Controller c = serv.getController();
        assertNotNull(c);
        assertTrue(c instanceof PreGameController);
        PreGameController pgc = (PreGameController) c;
        try {
            pgc.startGame();
        } catch (FreeColException e) {
            fail();
        }
        assertEquals(FreeColServer.GameState.IN_GAME, serv.getGameState());
        assertNotNull(serv.getGame());
        assertNotNull(serv.getGame().getMap());
        File file = new File(TEST_FILE);
        try {
            serv.saveGame(file, "user", null);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        assertTrue(file.exists());
        stopServer(serv);
        return file;
    }

    public static void newTurn() {
        ServerGame game = (ServerGame) server.getGame();
        game.csNewTurn(random, new ChangeSet());
    }

    public static Game startServerGame(Map map) {
        stopServerGame();
        FreeColServer serv = startServer(false, true);
        serv.setMapGenerator(new MockMapGenerator(map));
        PreGameController pgc = (PreGameController) serv.getController();
        try {
            pgc.startGame();
        } catch (FreeColException e) {
            fail("Failed to start game");
        }
        Game game = serv.getGame();
        FreeColTestCase.setGame(game);
        if (game.getCurrentPlayer() == null) {
            game.setCurrentPlayer(game.getFirstPlayer());
        }
        random = new Random();
        return game;
    }

    public static void stopServerGame() {
        stopServer(server);
        FreeColTestCase.setGame(null);
    }

    public static void setRandom(Random newRandom) {
        random = newRandom;
    }
}
