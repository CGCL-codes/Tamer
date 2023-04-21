package tauwarstests;

import junit.framework.TestCase;
import java.net.*;
import java.io.*;
import java.util.*;
import ui.*;
import entities.*;
import tauwars.*;

public class TestServerUI extends TestCase {

    Thread m_serverThread;

    boolean m_gameOver;

    private UI m_ui;

    final String XML_TAG = "<?xml version=\"1.0\"?>";

    Socket m_socket;

    private class ServerThread extends Thread {

        public ServerThread() {
        }

        public void run() {
            try {
                m_ui.start();
            } catch (Exception e) {
                System.out.println("UI Server initiation failed -- " + e.toString());
            }
        }
    }

    public TestServerUI(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        m_ui = new ServerUI(new UIDrivenEngine());
        m_serverThread = new TestServerUI.ServerThread();
        m_serverThread.start();
        m_gameOver = false;
        m_socket = this.connect();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (!m_gameOver) this.endGame();
        try {
            m_socket.close();
        } catch (Exception e) {
            fail("Could not close socket -- " + e.toString());
        }
        m_ui.stop();
        m_serverThread.join();
    }

    public Socket connect() {
        Socket s = new Socket();
        try {
            s.connect(new InetSocketAddress("localhost", 9001));
        } catch (Exception e) {
            fail("Failed to connect to server");
        }
        return s;
    }

    private void printToSocket(Socket s, String xml) {
        try {
            PrintStream ps = new PrintStream(s.getOutputStream());
            ps.print(xml + (char) 0);
            ps.flush();
        } catch (Exception e) {
            fail("Could not write to socket -- " + e.toString());
        }
    }

    private String readFromSocket(Socket s) {
        try {
            BufferedReader rs = new BufferedReader(new InputStreamReader(s.getInputStream()));
            return rs.readLine();
        } catch (Exception e) {
            fail("Did not get server response -- " + e.toString());
        }
        return null;
    }

    private String sendCommand(Socket s, String command) {
        String xml = null;
        this.printToSocket(s, XML_TAG + command);
        xml = this.readFromSocket(s);
        xml.trim();
        return xml;
    }

    private String sendCommand(String command) {
        return this.sendCommand(m_socket, command);
    }

    private void sendAtomicCommand(Socket s, String command, String action) throws Exception {
        String xml = this.sendCommand(s, command);
        if (!xml.equals(XML_TAG + "<OK action=\"" + action + "\" />")) throw new Exception(command + " failed, response -- " + xml);
    }

    private void sendAtomicCommand(String command, String action) throws Exception {
        this.sendAtomicCommand(m_socket, command, action);
    }

    private void loginGuest() throws Exception {
        this.sendAtomicCommand("<Login user=\"guest\" " + "pass=\"guest\" />", "LoginUser");
    }

    private void registerPlayer(Socket s, Player p) throws Exception {
        this.sendAtomicCommand(s, "<CreatePlayer name=\"" + p.getName() + "\" " + "id=\"" + p.getId() + "\" " + "money=\"" + p.getMoney() + "\" />", "CreatePlayer");
    }

    private void registerPlayer(Player p) throws Exception {
        this.registerPlayer(m_socket, p);
    }

    private void endGame() {
        try {
            this.sendAtomicCommand("<EndGame />", "EndGame");
        } catch (Exception e) {
            fail("Could not end game -- " + e.toString());
        }
        m_gameOver = true;
    }

    private void endRound(Board b) {
        if (b != null) b.endRound();
        try {
            this.sendAtomicCommand("<EndRound />", "EndRound");
        } catch (Exception e) {
            fail("Could not end round -- " + e.toString());
        }
    }

    public void startGame() throws Exception {
        this.sendAtomicCommand("<StartGame />", "StartGame");
    }

    private String getPlayersXML() {
        return this.sendCommand("<GetPlayers />");
    }

    private String getBoardXML() {
        return this.sendCommand("<GetBoard />");
    }

    private void registerNewGame(int humanPlayers, int computerPlayers) {
        String xml = "<NewGame humanPlayers=\"" + humanPlayers + "\" " + "computerPlayers=\"" + computerPlayers + "\" />";
        try {
            this.sendAtomicCommand(xml, "NewGame");
        } catch (Exception e) {
            fail("Could not register game -- " + e.toString());
        }
    }

    private void compareToRemoteBoard(Board b) {
        String xml1, xml2;
        xml1 = XML_TAG + b.toXML();
        xml2 = this.getBoardXML();
        assertEquals(xml1, xml2);
    }

    private Board setupBoards() {
        Board b;
        List<Player> lp = new ArrayList<Player>();
        Player p1 = new Player("TestPlayer", 10, 0), p2 = new Player("TestPlayer2", 10, 1);
        LoadSaveGame lsg;
        lp.add(p1);
        lp.add(p2);
        b = new Board(lp, "territories.csv");
        lsg = new LoadSaveGame(b);
        lsg.saveGame("testboard.csv");
        try {
            this.sendAtomicCommand("<LoadGame filename=\"testboard.csv\" />", "LoadGame");
        } catch (Exception e) {
            fail("Could not load game -- " + e.toString());
        }
        try {
            this.startGame();
        } catch (Exception e) {
            fail("Could not start game -- " + e.toString());
        }
        return b;
    }

    /******************************************************
	 *                    T E S T S                       *
	 ******************************************************/
    public void testRegisterUsers() {
        Player p1 = new Player("TestPlayer", 1), p2 = new Player("TestPlayer2", 2);
        String str, xml;
        try {
            this.registerPlayer(p1);
            this.registerPlayer(p2);
        } catch (Exception e) {
            fail("Failed registration -- " + e.toString());
        }
        str = XML_TAG + "<Players>" + p1.toXML() + p2.toXML() + "</Players>";
        xml = this.getPlayersXML();
        assertEquals(str, xml);
        this.endGame();
    }

    public void testIllegalRegisterUsers() {
        Player p1 = new Player("TestPlayer", 1);
        try {
            this.registerPlayer(p1);
        } catch (Exception e) {
            fail("Could not register first player -- " + e.toString());
        }
        try {
            this.registerPlayer(p1);
            fail("Managed to register Player1 twice");
        } catch (Exception e) {
        }
        try {
            Player p2 = new Player("TestPlayer2", 2), p3 = new Player("TestPlayer3", 3), p4 = new Player("TestPlayer4", 4), p5 = new Player("TestPlayer5", 5);
            this.registerPlayer(p2);
            this.registerPlayer(p3);
            this.registerPlayer(p4);
            this.registerPlayer(p5);
            fail("Managed to register more than 4 players");
        } catch (Exception e) {
        }
        this.endGame();
    }

    public void testStartGameNoPlayers() {
        try {
            this.startGame();
            fail("Managed to start a game with no players...");
        } catch (Exception e) {
        }
    }

    public void testStartGameWithPlayers() {
        Player p1 = new Player("TestPlayer", 1), p2 = new Player("TestPlayer2", 2);
        try {
            this.registerPlayer(p1);
            this.registerPlayer(p2);
        } catch (Exception e) {
            fail("Failed registration -- " + e.toString());
        }
        try {
            this.startGame();
        } catch (Exception e) {
            fail("Could not start game -- " + e.toString());
            e.printStackTrace();
        }
        this.endGame();
    }

    public void testGetBoard() {
        Board b;
        b = this.setupBoards();
        this.compareToRemoteBoard(b);
        this.endGame();
    }

    public void testMultiPlayerConnection() {
        Socket s;
        Player p1 = new Player("TestPlayer", 10, 0), p2 = new Player("TestPlayer2", 10, 1);
        s = this.connect();
        assertNotNull(s);
        assertTrue(s.isConnected());
        try {
            this.registerPlayer(p1);
            this.registerPlayer(s, p2);
        } catch (Exception e) {
            fail("Failed to register players from more than one socket --- " + e.toString());
        }
        try {
            this.startGame();
        } catch (Exception e) {
            fail("Could not start game -- " + e.toString());
        }
        this.endGame();
        try {
            s.close();
        } catch (Exception e) {
            fail("Could not close socket -- " + e.toString());
        }
    }

    public void testNewGame() {
        Player p1 = new Player("TestPlayer", 10, 0), p2 = new Player("TestPlayer2", 10, 1), p3 = new Player("TestPlayer3", 10, 2), p4 = new Player("TestPlayer4", 10, 3);
        this.registerNewGame(3, 0);
        try {
            this.registerPlayer(p1);
            this.registerPlayer(p2);
        } catch (Exception e) {
            fail("Failed to register player --- " + e.toString());
        }
        try {
            this.startGame();
            fail("managed to start game before the declared players were registered");
        } catch (Exception e) {
        }
        try {
            this.registerPlayer(p3);
        } catch (Exception e) {
            fail("Failed to register player --- " + e.toString());
        }
        try {
            this.registerPlayer(p4);
            fail("Managed to register more players than declared in NewGame");
        } catch (Exception e) {
        }
        try {
            this.startGame();
        } catch (Exception e) {
            fail("Could not start game -- " + e.toString());
        }
        this.endGame();
    }

    public void testLoadGame() {
        Board b;
        LoadSaveGame lsg;
        b = new Board();
        lsg = new LoadSaveGame(b);
        lsg.loadGame("start.csv");
        try {
            this.sendAtomicCommand("<LoadGame filename=\"start.csv\" />", "LoadGame");
        } catch (Exception e) {
            fail("Could not load game -- " + e.toString());
        }
        this.compareToRemoteBoard(b);
        this.endGame();
    }

    public void testLoadTheme() {
        Board b;
        LoadSaveGame lsg;
        b = new Board();
        lsg = new LoadSaveGame(b);
        lsg.loadGame("start.csv");
        try {
            this.sendAtomicCommand("<LoadGame filename=\"start.csv\" />", "LoadGame");
        } catch (Exception e) {
            fail("Could not load game -- " + e.toString());
        }
        this.compareToRemoteBoard(b);
        try {
            lsg.loadTheme("biu.thm");
            this.sendAtomicCommand("<LoadTheme filename=\"biu.thm\" />", "LoadTheme");
        } catch (Exception e) {
            fail("Could not load theme -- " + e.toString());
        }
        this.compareToRemoteBoard(b);
        this.endGame();
    }

    class EndRoundThread extends Thread {

        public void run() {
            try {
                sendAtomicCommand(connect(), "<EndRound />", "EndRound");
            } catch (Exception e) {
                fail("Could not end round -- " + e.toString());
            }
        }
    }

    public void testCommandMove() {
        Board b;
        Player p;
        CommandMove c;
        Territory src, dst = null;
        Iterator<Territory> it;
        b = this.setupBoards();
        p = b.getPlayer(0);
        it = p.getTerritories();
        assertTrue(it.hasNext());
        src = it.next();
        while (it.hasNext()) {
            Territory t = it.next();
            if (t.isNeighborTerritory(src)) {
                dst = t;
                break;
            }
        }
        assertNotNull(dst);
        try {
            c = new CommandMove(p, 5, src, dst);
            c.execute();
            this.sendAtomicCommand(c.toXML(), "Command");
        } catch (Exception e) {
            fail("Could not create, excute or send command -- " + e.toString());
        }
        new EndRoundThread().start();
        this.endRound(b);
        this.compareToRemoteBoard(b);
        this.endGame();
    }

    public void testCommandBuy() {
        Board b;
        Player p;
        CommandBuy c;
        Territory dst = null;
        b = this.setupBoards();
        p = b.getPlayer(0);
        dst = p.getTerritory(0);
        assertNotNull(dst);
        try {
            c = new CommandBuy(p, 5, dst);
            c.execute();
            this.sendAtomicCommand(c.toXML(), "Command");
        } catch (Exception e) {
            fail("Could not create, excute or send command -- " + e.toString());
        }
        new EndRoundThread().start();
        this.endRound(b);
        this.compareToRemoteBoard(b);
        this.endGame();
    }

    public void testCommandSurrender() {
        Board b;
        Player p;
        CommandSurrender c;
        b = this.setupBoards();
        p = b.getPlayer(0);
        try {
            c = new CommandSurrender(p, b);
            c.execute();
            this.sendAtomicCommand(c.toXML(), "Command");
        } catch (Exception e) {
            fail("Could not create, excute or send command -- " + e.toString());
        }
        new EndRoundThread().start();
        this.endRound(b);
        this.compareToRemoteBoard(b);
        this.endGame();
    }

    public void testEndRound() {
        Board b;
        b = this.setupBoards();
        new EndRoundThread().start();
        this.endRound(b);
        this.compareToRemoteBoard(b);
        this.endGame();
    }

    public void testCommandAttack() {
        Board b;
        Player p;
        CommandAttack c;
        Territory src, dst = null;
        Iterator<Territory> it;
        b = this.setupBoards();
        p = b.getPlayer(0);
        src = p.getTerritory(0);
        it = b.getTerritories();
        while (it.hasNext()) {
            Territory t = it.next();
            if (t.isNeighborTerritory(src) && t.getPlayer().getId() != p.getId()) {
                dst = t;
                break;
            }
        }
        assertNotNull(dst);
        try {
            c = new CommandAttack(p, 5, src, dst);
            c.execute();
            this.sendAtomicCommand(c.toXML(), "Command");
        } catch (Exception e) {
            fail("Could not create, excute or send command -- " + e.toString());
        }
        new EndRoundThread().start();
        this.endRound(b);
        this.compareToRemoteBoard(b);
        this.endGame();
    }

    public void testGetCommands() {
        Board b;
        Player p;
        Command c1 = null, c2 = null;
        Territory src, dst = null;
        Iterator<Territory> it;
        String msg, expected;
        b = this.setupBoards();
        p = b.getPlayer(0);
        src = p.getTerritory(0);
        it = b.getTerritories();
        while (it.hasNext()) {
            Territory t = it.next();
            if (t.isNeighborTerritory(src) && t.getPlayer().getId() != p.getId()) {
                dst = t;
                break;
            }
        }
        assertNotNull(dst);
        try {
            c1 = new CommandBuy(p, 5, src);
            this.sendAtomicCommand(c1.toXML(), "Command");
            c2 = new CommandAttack(p, 5, src, dst);
            this.sendAtomicCommand(c2.toXML(), "Command");
        } catch (Exception e) {
            fail("Could not create, excute or send command -- " + e.toString());
        }
        msg = this.sendCommand("<GetCommands />");
        expected = XML_TAG + "<Commands>" + c1.toXML() + c2.toXML() + "</Commands>";
        assertEquals(expected, msg);
        new EndRoundThread().start();
        this.endRound(b);
        this.endGame();
    }

    public void testGetGameStatus() {
        Board b;
        Player p1, p2;
        CommandSurrender c;
        String status, expected;
        b = this.setupBoards();
        expected = "<?xml version=\"1.0\"?><GameRunning />";
        status = this.sendCommand("<GetGameStatus />");
        assertEquals(expected, status);
        p1 = b.getPlayer(0);
        p2 = b.getPlayer(1);
        try {
            c = new CommandSurrender(p1, b);
            c.execute();
            this.sendAtomicCommand(c.toXML(), "Command");
        } catch (Exception e) {
            fail("Could not create, excute or send command -- " + e.toString());
        }
        new EndRoundThread().start();
        this.endRound(b);
        expected = "<?xml version=\"1.0\"?>" + "<GameOver winner=\"" + p2.getId() + "\" />";
        status = this.sendCommand("<GetGameStatus />");
        assertEquals(expected, status);
        this.endGame();
    }
}
