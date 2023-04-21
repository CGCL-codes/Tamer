package de.tudresden.inf.rn.mobilis.server.locpairs.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import org.jivesoftware.smack.XMPPConnection;
import de.tudresden.inf.rn.mobilis.server.agents.MobilisAgent;
import de.tudresden.inf.rn.mobilis.server.services.MobilisService;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.Connection;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.GamePacketFilter;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.GamePacketListener;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.LocPairsServerTime;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.NetworkFingerprintDAO;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.Player;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.PlayerUpdateTimerTask;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.Round;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.RoundRestarter;
import de.tudresden.inf.rn.mobilis.server.locpairs.model.Team;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.locpairs.EndGameBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.locpairs.GameInformationBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.locpairs.StartGameBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.locpairs.StartRoundBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.locpairs.EndGameBean.EndType;
import de.tudresden.inf.rn.mobilis.xmpp.beans.locpairs.model.GeoPosition;
import de.tudresden.inf.rn.mobilis.xmpp.beans.locpairs.model.NetworkFingerPrint;
import de.tudresden.inf.rn.mobilis.xmpp.server.BeanIQAdapter;

/**
 * The Class Game. The main Class, that holds and controls everything necessary.
 * 
 * @author Reik Mueller
 */
public class Game extends MobilisService {

    private int startcount = 4;

    private Map<String, String> settings = new HashMap<String, String>();

    private NetworkFingerprintDAO fingerPrintWriter = null;

    private HighScoreDAO highscoreDAO = new HighScoreDAO();

    private String gameId = null;

    private Collection<String> barcodes = new ArrayList<String>();

    private Collection<String> imageURLs = new ArrayList<String>();

    private Map<String, GeoPosition> barcodePositions = new HashMap<String, GeoPosition>();

    private Map<String, String> pairs = new HashMap<String, String>();

    private Connection connection = null;

    private SortedSet<Team> teams = new TreeSet<Team>();

    private Set<Player> playersWithoutTeam = new HashSet<Player>();

    private Set<Player> quitedPlayers = new HashSet<Player>();

    private int playerReady = 0;

    private int joinedPlayers = 0;

    private Timer timer = new Timer();

    private int discoveredPairs = 0;

    private Round actualRound = new Round(this);

    private boolean isRunning = false;

    private Player opener = null;

    private TimerTask roundTask = null;

    /**
	 * Instantiates a new game.
	 */
    public Game() {
        registerPacketListener();
        init();
        System.out.println("Hallo!");
    }

    /**
	 * Sends the game information in return to an game information IQ back to
	 * the requesting client.
	 * 
	 * @param toJid
	 *            the jid where the result has to be send to
	 * @return true, if successful
	 */
    public boolean returnGameInfomation(String toJid) {
        if (isRunning) return false;
        Collection<String> playernames = new ArrayList<String>();
        for (Player p : getPlayers().keySet()) {
            playernames.add(p.getName());
        }
        GameInformationBean bean = new GameInformationBean(opener.getName(), playernames, startcount);
        bean.setFrom(gameId);
        bean.setTo(toJid);
        bean.setType(XMPPBean.TYPE_RESULT);
        connection.getConnection().sendPacket(new BeanIQAdapter(bean));
        return true;
    }

    /**
	 * Creates the player and a team every time two players without a team are
	 * available.
	 * 
	 * @param jid
	 *            the XMPP-jid of the XMPPClient the player-object represents.
	 * @param name
	 *            the name the nick name of the player
	 * @return true, if successful. false, if the player is already added or
	 *         something went wrong.
	 */
    public boolean createPlayer(String jid, String name) {
        System.out.println("Game.ceatePlayer()");
        Player player = new Player(jid, name, this);
        if (joinedPlayers == 0) {
            opener = player;
        }
        if (hasPlayer(player) || startcount == 0 || isRunning) {
            System.out.println("Anmeldung verweigert (" + player.getName() + "! startcount: " + startcount + " spiel l�uft bereits: " + isRunning + " bereits angemeldet: " + hasPlayer(player));
            return false;
        }
        joinedPlayers++;
        playersWithoutTeam.add(player);
        createTeam();
        player.joinGame();
        playerUpdate(false);
        System.out.println("teamsize: " + teams.size() + " playerswithout team: " + playersWithoutTeam.toString() + " quited players: " + quitedPlayers.toString());
        System.out.println("Alle Spieler: " + getPlayers().keySet().toString());
        if (teams.size() == startcount / 2) {
            System.out.println("Game.createPlayer() startGame() wird aufgerufen");
            startGame();
        }
        return true;
    }

    /**
	 * With this method the game becomes aware of the player that are ready to
	 * play. If all players have invoked this method the first round is started.
	 * 
	 * @param player
	 *            the player
	 * @return true, if successful
	 */
    public boolean playerReadyToPlay(Player player) {
        for (Team t : teams) {
            if (t.hasPlayer(player)) playerReady++;
        }
        if (startcount == playerReady) {
            return startRound();
        }
        return false;
    }

    /**
	 * Deletes a player, its team-mate and its team from the game and notifies
	 * the other players. If there are not enough teams left to go on with the
	 * game the game will be quited automatically and the remaining teams get
	 * the high-score.
	 * 
	 * @param player
	 *            the player
	 * @param reason
	 *            the reason
	 * @return true, if successful
	 */
    public boolean quitPlayer(Player player, String reason) {
        System.out.println("Game.deletePlayer() game is running: " + isRunning);
        Player teamMate = null;
        teamMate = player.getTeamMate();
        player.endGame(EndType.ENDBYTEAMMATEQUIT);
        quitedPlayers.add(player);
        if (player.getTeam() != null) {
            teams.remove(player.getTeam());
        }
        if (!isRunning) {
            joinedPlayers--;
            if (teamMate != null) {
                playersWithoutTeam.add(teamMate);
                System.out.println("playerswithout team: " + playersWithoutTeam.toString());
            } else {
                playersWithoutTeam.remove(player);
            }
        } else {
            teamMate.endGame(EndType.ENDBYTEAMMATEQUIT);
            if (player.isActive() && teams.size() >= 2) {
                startRound();
            }
            if (teams.size() < 2) {
                endGame();
            }
        }
        createTeam();
        playerUpdate(false);
        System.out.println("Alle Spieler: " + getPlayers().keySet().toString());
        return true;
    }

    public boolean deletePlayer(Player player) {
        quitedPlayers.remove(player);
        return true;
    }

    /**
	 * Signalizes the Game that an card has been uncovered. The uncovered card
	 * is notified to all players to be shown. If it is the second one the
	 * actual round will be stopped and a new round begins.
	 * 
	 * @param player
	 *            the player
	 * @return true, if successful
	 */
    public boolean uncoverCard(String barcode, Player player) {
        System.out.println("Game.uncoverCard()");
        actualRound.uncoverCard(pairs.get(barcode), player);
        showCard();
        if (actualRound.isFinished()) {
            for (Team t : teams) {
                if (t.hasPlayer(player)) {
                    if (actualRound.compareCards()) {
                        t.increaseScore();
                        t.gotPair(true);
                    } else {
                        t.gotPair(false);
                    }
                }
            }
            startRound();
        }
        return true;
    }

    /**
	 * Write network-fingerprint. It is used to persist the measured signal
	 * strengths to be analyzed later on.
	 * 
	 * @param fingerprint
	 *            the network-fingerprint
	 * @return true, if successful persisted.
	 */
    public boolean writeNetworkFingerprint(NetworkFingerPrint fingerprint, String barCode) {
        GeoPosition position = barcodePositions.get(barCode);
        if (position == null) return false;
        fingerprint.setPosition(position);
        return fingerPrintWriter.addFingerprint(fingerprint);
    }

    public boolean playerUpdate(boolean resend) {
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                player.sendPlayerUpdate(gameId, getPlayers());
            }
        }
        for (Player player : playersWithoutTeam) {
            player.sendPlayerUpdate(gameId, getPlayers());
        }
        if (resend) {
            this.timer.schedule(new PlayerUpdateTimerTask(this), PlayerUpdateTimerTask.delay);
        }
        return true;
    }

    private void init() {
        playerUpdate(true);
        LocPairsServerTime.getTime();
        fingerPrintWriter = new NetworkFingerprintDAO();
        barcodes.add("INFE001");
        barcodes.add("INFE005");
        barcodes.add("INFE006");
        barcodes.add("INFE007");
        barcodes.add("INFE008");
        barcodes.add("INFE009");
        barcodes.add("INFE010");
        barcodes.add("INFE015");
        for (String s : barcodes) {
            barcodePositions.put(s, new GeoPosition(0, 0, 0));
        }
        imageURLs.add("memory01");
        imageURLs.add("memory02");
        imageURLs.add("memory03");
        imageURLs.add("memory04");
        pairs.put("INFE001", "memory01");
        pairs.put("INFE005", "memory01");
        pairs.put("INFE006", "memory02");
        pairs.put("INFE007", "memory02");
        pairs.put("INFE008", "memory03");
        pairs.put("INFE009", "memory03");
        pairs.put("INFE010", "memory04");
        pairs.put("INFE015", "memory04");
        barcodePositions.put("INFE001", new GeoPosition(51.025336, 13.72356, 0));
        barcodePositions.put("INFE005", new GeoPosition(51.0253, 13.723499, 0));
        barcodePositions.put("INFE006", new GeoPosition(51.025218, 13.723461, 0));
        barcodePositions.put("INFE007", new GeoPosition(51.025139, 13.723425, 0));
        barcodePositions.put("INFE008", new GeoPosition(51.025058, 13.723428, 0));
        barcodePositions.put("INFE009", new GeoPosition(51.0251, 13.723445, 0));
        barcodePositions.put("INFE010", new GeoPosition(51.025188, 13.723486, 0));
        barcodePositions.put("INFE015", new GeoPosition(51.025074, 13.723265, 0));
    }

    @Override
    protected void registerPacketListener() {
        String username = null;
        String host = null;
        String password = null;
        for (String s : settings.keySet()) {
            if (s.equals("username")) username = settings.get(s);
            if (s.equals("password")) password = settings.get(s);
            if (s.equals("host")) host = settings.get(s);
        }
        if (password != null && username != null && host != null) {
            connection = new Connection(this, host, username, password);
        } else {
            connection = new Connection(this, "141.30.203.90", "server", "7Dj3S");
        }
        gameId = connection.getJid();
        GamePacketListener l = new GamePacketListener(this);
        connection.getConnection().addPacketListener(l, GamePacketFilter.getFilter());
    }

    private Team createTeam() {
        if (playersWithoutTeam.size() == 2) {
            int i = 0;
            Player player1 = null;
            Player player2 = null;
            for (Player playerT : playersWithoutTeam) {
                if (i == 1) {
                    player2 = playerT;
                    playersWithoutTeam.remove(playerT);
                    playersWithoutTeam.remove(player1);
                    break;
                }
                if (i == 0) {
                    player1 = playerT;
                    i++;
                }
            }
            System.out.println("Game.createTeam(Player1: " + player1.toString() + " player2: " + player2.toString() + ")");
            Team team = new Team(player1, player2, "test", teams.size() + 1);
            player1.setTeam(team);
            player2.setTeam(team);
            teams.add(team);
            return team;
        }
        return null;
    }

    private boolean startGame() {
        isRunning = true;
        for (Player p : playersWithoutTeam) {
            p.endGame(EndType.ENDBYREGULAREND);
            playersWithoutTeam.remove(p);
        }
        System.out.println("Game.startGame()");
        for (Team t : teams) {
            for (Player p : t.getPlayers()) {
                p.startGame(barcodePositions, pairs);
            }
        }
        return true;
    }

    protected boolean startRound() {
        System.out.println("Game.startRound()");
        if (roundTask != null) roundTask.cancel();
        if (discoveredPairs < pairs.size() / 2) {
            int i = 0;
            actualRound.increaseNumber();
            actualRound.setStartTime();
            actualRound.clear();
            System.out.println(teams.toString());
            for (Team team : teams) {
                if (i == 0) {
                    team.setLastActiveRound(actualRound.getNumber());
                    actualRound.setActiveTeam(team);
                    for (Player player : team.getPlayers()) {
                        player.setActive(true);
                        player.startRound(actualRound);
                    }
                } else {
                    for (Player player : team.getPlayers()) {
                        player.setActive(false);
                        player.startRound(actualRound);
                    }
                }
                i++;
            }
            SortedSet<Team> t2 = new TreeSet<Team>();
            for (Team t : teams) {
                t2.add(t);
            }
            teams = t2;
            roundTask = new RoundRestarter(this);
            timer.schedule(roundTask, new Long(actualRound.getDuration()));
        } else {
            endGame();
        }
        return true;
    }

    private boolean showCard() {
        for (Player p : getPlayers().keySet()) {
            if (actualRound.getUncoveredCard2() != null) {
                p.showCard(actualRound.getUncoveredCard2(), this.pairs.get(actualRound.getUncoveredCard2()), actualRound.getPlayer2());
            } else {
                if (actualRound.getUncoveredCard1() != null) p.showCard(actualRound.getUncoveredCard1(), this.pairs.get(actualRound.getUncoveredCard1()), actualRound.getPlayer1());
            }
        }
        return true;
    }

    private Map<Player, Team> getPlayers() {
        Map<Player, Team> players = new HashMap<Player, Team>();
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                players.put(player, team);
            }
        }
        for (Player player : playersWithoutTeam) {
            players.put(player, null);
        }
        return players;
    }

    private void actualiseHighscore() {
        highscoreDAO.actualiseHighscore(teams);
    }

    private void endGame() {
        actualiseHighscore();
        for (Player player : getPlayers().keySet()) {
            player.endGame(EndGameBean.EndType.ENDBYREGULAREND);
        }
        fingerPrintWriter.close();
        try {
            shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Gets the game id.
	 * 
	 * @return the game id
	 */
    public String getGameId() {
        if (gameId == null) gameId = connection.getJid();
        return gameId;
    }

    /**
	 * Sets the game id.
	 * 
	 * @param gameId
	 *            the new game id
	 */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
	 * Gets the barcodes.
	 * 
	 * @return the barcodes
	 */
    public Collection<String> getBarcodes() {
        return barcodes;
    }

    /**
	 * Sets the barcodes.
	 * 
	 * @param barcodes
	 *            the new barcodes
	 */
    public void setBarcodes(Collection<String> barcodes) {
        this.barcodes = barcodes;
    }

    /**
	 * Gets the image urls.
	 * 
	 * @return the image urls
	 */
    public Collection<String> getImageURLs() {
        return imageURLs;
    }

    /**
	 * Sets the image urls.
	 * 
	 * @param imageURLs
	 *            the new image ur ls
	 */
    public void setImageURLs(Collection<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    /**
	 * Gets the pairs.
	 * 
	 * @return the pairs
	 */
    public Map<String, String> getPairs() {
        return pairs;
    }

    /**
	 * Sets the pairs.
	 * 
	 * @param pairs
	 *            the pairs
	 */
    public void setPairs(Map<String, String> pairs) {
        this.pairs = pairs;
    }

    /**
	 * Gets the connection.
	 * 
	 * @return the connection
	 */
    public Connection getConnection() {
        return connection;
    }

    /**
	 * Sets the connection.
	 * 
	 * @param connection
	 *            the new connection
	 */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
	 * Gets the scores.
	 * 
	 * @return the scores
	 */
    public Map<Integer, Long> getScores() {
        Map<Integer, Long> scores = new HashMap<Integer, Long>();
        for (Team team : teams) {
            scores.put(new Integer(team.getNumber()), team.getScore());
        }
        return scores;
    }

    /**
	 * Gets the highscores. 50 in maximum
	 * 
	 * @return the highscores
	 */
    public Map<Long, String> getHighscores() {
        return highscoreDAO.getHighscore();
    }

    /**
	 * Checks for player.
	 * 
	 * @param player
	 *            the player
	 * @return true, if successful
	 */
    public boolean hasPlayer(Player player) {
        for (Player p : getPlayers().keySet()) {
            if (p.equals(player)) return true;
        }
        return false;
    }
}
