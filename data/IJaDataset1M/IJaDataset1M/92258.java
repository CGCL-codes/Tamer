package bomberman.server;

import bomberman.server.api.Session;
import bomberman.util.CHAP;
import bomberman.client.api.ServerListenerInterface;
import bomberman.net.Event;
import bomberman.server.api.GameInfo;
import bomberman.server.gui.ServerControlPanel;
import bomberman.server.gui.UserListTableModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Main KC Bomberman Server class. This huge singleton class contains most
 * of the server's logic.
 * @author Christian Lins
 * @author Kai Ritterbusch
 */
public class Server {

    private static volatile Server instance = null;

    /**
   * @return Current instance of Server.
   */
    public static Server getInstance() {
        if (instance == null) {
            synchronized (Server.class) {
                instance = new Server();
            }
        }
        return instance;
    }

    private CHAP chap = new CHAP();

    /**
   * Stores the Session => Player relation.
   * This Map is instance of ConcurrentHashMap and therefor thread-safe.
   */
    private Map<Session, Player> players = new ConcurrentHashMap<Session, Player>();

    /**
   * Stores the Session => ServerListenerInterface relation.
   * This Map is instance of ConcurrentHashMap and therefor thread-safe.
   */
    private Map<Session, ServerListenerInterface> clients = new ConcurrentHashMap<Session, ServerListenerInterface>();

    /**
   * Stores the Game name => Game instance relation.
   * This Map is instance of ConcurrentHashMap and therefor thread-safe.
   */
    private Map<String, Game> games = new ConcurrentHashMap<String, Game>();

    /**
   * Stores the Session => Game relation.
   * This Map is instance of ConcurrentHashMap and therefor thread-safe.
   * TODO: Is this necessary as we store the Sessions in the Game instances as well?
   */
    private Map<Session, Game> playerToGame = new ConcurrentHashMap<Session, Game>();

    /**
   * Stores the Session => IP-Address relation.
   * This Map is instance of ConcurrentHashMap and therefor thread-safe.
   * TODO: Is that still necessary?
   */
    private Map<Session, String> playerToIP = new ConcurrentHashMap<Session, String>();

    /** 
   * Queue that stores the explosion events for processing.
   * This queue is automatically synchronized and therefor thread-safe.
   * A linked queue is probably faster than an array queue for our purposes of FIFO.
   */
    private BlockingQueue<List<Object>> explosions = new LinkedBlockingQueue<List<Object>>(25);

    private Logger logger = new Logger();

    private Database database = null;

    private Highscore highscore = null;

    private Server() {
        try {
            XStream xstream = new XStream(new DomDriver());
            this.database = (Database) xstream.fromXML(new FileInputStream(ShutdownThread.DATABASE_FILE));
            this.highscore = (Highscore) xstream.fromXML(new FileInputStream(ShutdownThread.HIGHSCORE_FILE));
        } catch (Exception ex) {
            this.database = new Database();
            this.highscore = new Highscore();
            System.out.println(ex.getLocalizedMessage());
            System.out.println("No persistent database/highscore found!");
        }
        Thread mainLoop = new ServerLoop(this);
        mainLoop.setDaemon(true);
        mainLoop.start();
        Thread explosionConsumer = new ExplosionConsumer(this);
        explosionConsumer.setDaemon(true);
        explosionConsumer.start();
        if (ServerControlPanel.getInstance() != null) ServerControlPanel.getInstance().addLogMessages("ServerInstanz erstellt");
        System.out.println("ServerInstanz erstellt");
    }

    /**
   * @return Map storing the Session to ServerListenerInterface relation.
   */
    Map<Session, ServerListenerInterface> getClients() {
        return this.clients;
    }

    /**
   * @return Queue storing the explosion events.
   */
    BlockingQueue<List<Object>> getExplosions() {
        return this.explosions;
    }

    /**
   * @return Map containing the Game name to Game instance relation.
   */
    Map<String, Game> getGames() {
        return this.games;
    }

    /**
   * @return Map containing the Session to Player relation.
   */
    Map<Session, Player> getPlayers() {
        return this.players;
    }

    /**
   * @return Map containing the Session to Game relation.
   */
    Map<Session, Game> getPlayerToGame() {
        return this.playerToGame;
    }

    /**
   * Adds a new explosion to the explosion event queue.
   * @param game
   * @param x
   * @param y
   * @param distance
   */
    void notifyExplosion(Game game, int x, int y, int distance) throws InterruptedException {
        List<Object> data = new ArrayList<Object>();
        data.add(game);
        data.add(x);
        data.add(y);
        data.add(distance);
        this.explosions.put(data);
    }

    /**
   * Sends a game list update to all client that are not playing.
   */
    private void gameListUpdate() {
        List<GameInfo> gameList = new ArrayList<GameInfo>();
        Set<String> gameNames = this.games.keySet();
        for (String str : gameNames) {
            Game game = this.games.get(str);
            Player creator = players.get(game.getCreator());
            if (creator == null) {
                stopGame(game);
            } else gameList.add(new GameInfo(str, creator.getNickname(), game.isRunning(), game.getPlayerCount()));
        }
        Set<Session> sessions = this.clients.keySet();
        for (Session session : sessions) {
            try {
                ServerListenerInterface client = this.clients.get(session);
                client.gameListUpdate(new Event(new Object[] { gameList }));
            } catch (Exception ex) {
                System.err.println("Exception while notifying client: " + ex.getLocalizedMessage());
            }
        }
    }

    /**
   * Logout with username. This method is used by the Server GUI.
   */
    public void logout(String userName) {
        for (Entry<Session, Player> ent : players.entrySet()) {
            if (ent.getValue().getNickname().equals(userName)) {
                Game game = playerToGame.get(ent.getKey());
                if (game != null) {
                    if (game.getCreator().equals(ent.getKey())) stopGame(game);
                }
                this.clients.get(ent.getKey()).loggedOut(new Event(new Object[0]));
                players.remove(ent.getKey());
                clients.remove(ent.getKey());
                refresh();
            }
        }
    }

    public void leaveGame(Session session) {
        Game game = playerToGame.get(session);
        playerToGame.remove(session);
        game.removePlayer(session);
        game.forcePlaygroundUpdate();
    }

    /**
   * Logs out a client. 
   */
    public void logout(Session session) {
        if (!clients.containsKey(session)) return;
        if (ServerControlPanel.getInstance() != null) {
            ServerControlPanel.getInstance().addLogMessages(players.get(session).getNickname() + " logout");
            ((UserListTableModel) ServerControlPanel.getInstance().getTblUserList().getModel()).setDataForUsername(players.get(session).getNickname(), "offline");
        }
        System.out.println(players.get(session).getNickname() + " logout");
        Game game = playerToGame.get(session);
        if (game != null && !game.isRunning() && game.getCreator().equals(session)) stopGame(game);
        players.remove(session);
        clients.remove(session);
        playerToGame.remove(session);
        playerToIP.remove(session);
        refresh();
    }

    /**
   * Stopps a game and sends a gameStopped() message to all players
   * playing this specific game. Additionally this method stops all
   * AIPlayerThreads running with this game.
   * @param game
   */
    public void stopGame(Game game) {
        game.setRunning(false);
        for (Entry<Session, Game> e : playerToGame.entrySet()) {
            if (e.getValue().equals(game.toString())) playerToGame.remove(e);
        }
        for (Session sess : game.getPlayerSessions()) {
            ServerListenerInterface sli = clients.get(sess);
            if (sli != null) clients.get(sess).gameStopped(new Event(new Object[] { 1 }));
        }
        for (Session sess : game.getSpectatorSessions()) {
            ServerListenerInterface sli = clients.get(sess);
            if (sli != null) clients.get(sess).gameStopped(new Event(new Object[] { 1 }));
        }
        if (ServerControlPanel.getInstance() != null) {
            ServerControlPanel.getInstance().removeGame(games.get(game.toString()));
            ServerControlPanel.getInstance().addLogMessages("Spiel: " + game.toString() + " wurde durch Server beendet");
        }
        games.remove(game.toString());
        refresh();
    }

    /**
   * Notifies the Server of a Client movement.
   * @param session
   * @param x
   * @param y
   * @return
   */
    public void move(Session session, int x, int y) {
        if (x == y) return;
        if (!clients.containsKey(session)) return;
        Game game = playerToGame.get(session);
        Player player = players.get(session);
        game.movePlayer(player, x, y);
    }

    /**
   * A client player wants to place a bomb at his current position.
   * @param session
   */
    public boolean placeBomb(Session session) {
        if (!clients.containsKey(session)) return false;
        Player player = this.players.get(session);
        if (player != null) {
            player.placeBomb();
            return true;
        } else return false;
    }

    /**
   * Client wants to send a chat message to a public channel.
   * @param session
   * @param message
   */
    public void sendChatMessage(Session session, String message) {
        if (!clients.containsKey(session)) return;
        String answer = players.get(session) + ": " + message;
        for (Session sess : clients.keySet()) clients.get(sess).receiveChatMessage(new Event(new Object[] { answer }));
    }

    /**
   * Client wants to login with the given username. This method is
   * part one of the Challenge Handshake Authentification Protocol (CHAP)
   * and returns a challenge that is valid for a few seconds (default: 30s).
   * @param username
   * @return A challenge > 0 if the username is valid, otherwise 0 is
   * returned.
   */
    public long login1(String username) {
        String pw = this.database.getPassword(username);
        if (pw == null) return 0; else {
            return this.chap.createChallenge(username);
        }
    }

    /**
   * Second part of the Challenge Handshake Authentification Protocol (CHAP).
   * If the login is successful the Server will transmit a Session object
   * through the given @see{ServerListenerInterface}.
   * @param username
   * @param hash
   * @return
   */
    public boolean login2(String username, long hash, ServerListenerInterface sli) {
        if (this.chap.isValid(username)) {
            String pw = this.database.getPassword(username);
            if (pw == null) return false; else {
                long challenge = this.chap.getChallenge(username);
                long myHash = CHAP.createChecksum(challenge, pw);
                if (myHash == hash) return login(username, pw, sli); else return false;
            }
        } else return false;
    }

    /**
   * This method is only used internally by login2 method.
   * @param nickname
   * @param password
   * @param sli
   * @param ip
   * @return
   */
    private boolean login(String nickname, String password, ServerListenerInterface sli) {
        try {
            Session session = new Session();
            if (database.getPassword(nickname) == null) return false; else if (!database.getPassword(nickname).equals(password)) return false;
            Player player = new Player(null, nickname);
            players.put(session, player);
            if (ServerControlPanel.getInstance() != null) {
                ((UserListTableModel) ServerControlPanel.getInstance().getTblUserList().getModel()).setDataForUsername(nickname, "online");
            }
            if (ServerControlPanel.getInstance() != null) {
                ServerControlPanel.getInstance().addLogMessages(nickname + " hat sich eingeloggt");
            }
            clients.put(session, sli);
            sli.loggedIn(new Event(new Object[] { session }));
            ArrayList<String> nicknames = new ArrayList<String>();
            for (Session sess : players.keySet()) nicknames.add(players.get(sess).getNickname());
            for (Session sess : clients.keySet()) clients.get(sess).userListUpdate(new Event(new Object[] { nicknames }));
            gameListUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
   * Sends both user and game list to all clients.
   */
    public void refresh() {
        gameListUpdate();
        List<String> nicknames = new ArrayList<String>();
        for (Session sess : players.keySet()) nicknames.add(players.get(sess).getNickname());
        for (Session sess : clients.keySet()) clients.get(sess).userListUpdate(new Event(new Object[] { nicknames }));
    }

    /**
   * Stopps and deletes a game.
   * @param gameName
   */
    public boolean stopGame(String gameName) {
        stopGame(games.get(gameName));
        return true;
    }

    /**
   * A spectator wants to join a game.
   * @param session
   * @param gameName
   */
    public void joinViewGame(Session session, String gameName) {
        if (!clients.containsKey(session)) return;
        logger.log("joinViewGame", playerToIP.get(session));
        Game game = games.get(gameName);
        game.getSpectatorSessions().add(session);
        this.clients.get(session).gameJoined(new Event(new Object[] { gameName }));
        if (game.isRunning()) {
            this.clients.get(session).gameStarted(new Event(new Object[] { true }));
            game.forcePlaygroundUpdate();
        }
    }

    /**
   * Client wants to join a game.
   * @param session
   * @param gameName
   * @return
   * @throws java.rmi.RemoteException
   */
    public void joinGame(Session session, String gameName) {
        if (!clients.containsKey(session)) return;
        Game game = games.get(gameName);
        if (game == null || game.isRunning()) return;
        Player player = players.get(session);
        if (!game.addPlayer(player)) return;
        playerToGame.put(session, game);
        logger.log("joinGame", playerToIP.get(session));
        game.getPlayerSessions().add(session);
        this.clients.get(session).gameJoined(new Event(new Object[] { gameName }));
        for (Session sess : game.getPlayerSessions()) {
            clients.get(sess).receiveChatMessage(new Event(new Object[] { players.get(session).getNickname() + " has joined Game" }));
        }
        if (game.getPlayerSessions().size() == 4) {
            for (Session sess : game.getPlayerSessions()) {
                this.clients.get(sess).gameStarted(new Event(new Object[] { false }));
            }
            for (Session sess : game.getSpectatorSessions()) {
                this.clients.get(sess).gameStarted(new Event(new Object[] { true }));
            }
        }
        gameListUpdate();
    }

    /**
   * Creates a new game identified by the given name.
   * @param session
   * @param gameName
   * @return
   */
    public boolean createGame(Session session, String gameName) {
        if (!clients.containsKey(session)) return false;
        logger.log("createGame", playerToIP.get(session));
        if (games.containsKey(gameName)) return false; else {
            Game game = new Game(gameName, session);
            games.put(gameName, game);
            if (ServerControlPanel.getInstance() != null) {
                ServerControlPanel.getInstance().addLogMessages("Spiel: " + gameName + " wurde erstellt");
                ServerControlPanel.getInstance().addGame(game);
            }
            gameListUpdate();
            return true;
        }
    }

    /**
   * The creator of a Game wants to start it.
   * @param session
   * @param gameName
   * @return
   */
    public boolean startGame(Session session, String gameName) {
        if (!clients.containsKey(session)) return false;
        logger.log("login", playerToIP.get(session));
        System.out.println("Session ok");
        if (!games.containsKey(gameName)) return false; else {
            System.out.println("Spiel vorhanden ok");
            Game game = this.games.get(gameName);
            if (game.getCreator().equals(session)) {
                System.out.println("Creator ok");
                List<Session> sessions = game.getPlayerSessions();
                List<Session> specSessions = game.getSpectatorSessions();
                for (Session sess : sessions) {
                    ServerListenerInterface client = this.clients.get(sess);
                    client.gameStarted(new Event(new Object[] { false }));
                }
                for (Session sess : specSessions) {
                    ServerListenerInterface client = this.clients.get(sess);
                    client.gameStarted(new Event(new Object[] { true }));
                }
                game.forcePlaygroundUpdate();
                game.addAI();
                game.setRunning(true);
                gameListUpdate();
                return true;
            } else return false;
        }
    }

    /**
   * @return Associated Database instance.
   */
    public Database getDatabase() {
        return database;
    }

    /**
   * @return Associated Highscore instance.
   */
    public Highscore getHighscore() {
        return highscore;
    }

    /**
   * When stopping server logout all users.
   */
    public void logoutAll() {
        for (Session sess : clients.keySet()) {
            try {
                clients.get(sess).loggedOut(new Event(new Object[0]));
            } catch (Exception ex) {
                System.err.println(ex.getLocalizedMessage());
            }
        }
        clients.clear();
    }
}
