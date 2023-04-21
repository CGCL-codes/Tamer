package net.sf.nebulacards;

import java.net.*;
import java.io.*;
import java.util.*;
import net.sf.nebulacards.main.*;
import net.sf.nebulacards.rules.*;
import net.sf.nebulacards.netpkt.*;

public class serv extends Thread implements PacketListener {

    public static final boolean verbose = true;

    JoinManager jm = null;

    LeaveManager lm = null;

    ComManager cm = new ComManager(4);

    net.sf.nebulacards.main.Queue[] packets = new net.sf.nebulacards.main.Queue[4];

    Vector[] persistentPackets = new Vector[4];

    Player[] players = new Player[4];

    boolean gameInProgress = false;

    Rules rules = null;

    PileOfCards beenPlayed;

    Tableau tableau;

    PileOfCards[] hands = new PileOfCards[4];

    PileOfCards[] cardsWon = new PileOfCards[4];

    public serv(Rules r, int port) {
        rules = r;
        for (int i = 0; i < 4; i++) {
            players[i] = new Player("");
            packets[i] = new net.sf.nebulacards.main.Queue();
            persistentPackets[i] = new Vector();
        }
        jm = new JoinManager(port);
        jm.start();
        lm = new LeaveManager();
        lm.start();
    }

    public void run() {
        try {
            synchronized (cm) {
                while (!gameInProgress) cm.wait();
            }
            conductGame();
            jm.end();
            try {
                jm.join();
            } catch (InterruptedException e) {
            }
            synchronized (cm) {
                while (cm.howManyActive() > 0) cm.wait();
            }
            lm.end();
            lm.join();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            return;
        }
    }

    void dealCards() throws IOException {
        int i;
        for (i = 0; i < 4; i++) {
            cardsWon[i] = new PileOfCards();
        }
        beenPlayed = new PileOfCards();
        tableau = new Tableau();
        hands = rules.deal();
        for (i = 0; i < 4; i++) hands[i].sort();
        synchronized (cm) {
            for (i = 0; i < 4; i++) cm.send(new DealPkt(hands[i]), i);
            cm.broadcast(new TrumpPkt(rules.getTrump(), rules.getTrumpName()));
        }
    }

    void conductPassing(int whoStarts) throws IOException, InterruptedException {
        int[] howmany = new int[4], where = new int[4];
        PileOfCards[] pass = new PileOfCards[4];
        boolean[] done = new boolean[4];
        for (int i = 0; i < 4; i++) {
            if ((howmany[i] = ((PassingRules) rules).numPass(i)) > 0) {
                where[i] = ((PassingRules) rules).wherePass(i);
                done[i] = false;
                cm.sendClear(new PassNotifyPkt(howmany[i], where[i]), i);
            } else done[i] = true;
        }
        while (!(done[0] && done[1] && done[2] && done[3])) {
            synchronized (cm) {
                cm.wait();
            }
            for (int i = 0; i < 4; i++) {
                Object o = dequeue(i);
                if (o != null) {
                    if (o instanceof PassPkt) {
                        pass[i] = ((PassPkt) o).getCards();
                        if (pass[i].howManyCards() == howmany[i]) {
                            done[i] = true;
                            for (int j = 0; j < pass[i].howManyCards(); j++) {
                                if (hands[i].search(pass[i].getCard(j)) == PileOfCards.NOT_FOUND) done[i] = false;
                            }
                        }
                        if (!done[i]) {
                            cm.setResend(true, i);
                        }
                    }
                } else {
                    if (howmany[i] > 0) {
                        cm.resend(new PassNotifyPkt(howmany[i], where[i]), i);
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            if (howmany[i] > 0) {
                hands[i].remove(pass[i]);
                if (where[i] >= 0 && where[i] < hands.length) hands[where[i]].addToTop(pass[i]);
            }
        }
        for (int i = 0; i < 4; i++) {
            hands[i].sort();
            cm.send(new DealPkt(hands[i]), i);
        }
    }

    void conductBidding(int whoStarts) throws IOException, InterruptedException {
        int stopCount = 0;
        for (int w = whoStarts; stopCount < 4; w = (w + 1) % 4) {
            if (!((BiddingRules) rules).shouldBid(w)) {
                stopCount++;
                continue;
            }
            stopCount = 0;
            cm.sendClear(new YourTurnPkt(YourTurnPkt.BID), w);
            for (; ; ) {
                synchronized (cm) {
                    while (allEmpty()) {
                        cm.wait();
                        cm.resend(new YourTurnPkt(YourTurnPkt.BID), w);
                    }
                }
                Object o = dequeue(w);
                if (o != null) {
                    if (o instanceof BidPkt) {
                        players[w].setBid(((BidPkt) o).getBid());
                        ((BiddingRules) rules).reportBid(w, ((BidPkt) o).getBid());
                        break;
                    }
                }
            }
            cm.broadcast(new BidPkt(players[w].getBid(), w));
        }
        ((BiddingRules) rules).doneBidding();
    }

    /**
	 * Implement the DirectCom hack.
	 */
    void conductDirectCom() throws IOException, InterruptedException {
        DirectComRules dcr = (DirectComRules) rules;
        while (dcr.getState() == dcr.READY || dcr.getState() == dcr.WAITING) {
            if (dcr.getState() == dcr.READY) {
                DirectComPacket pkt = dcr.getPacket();
                if (pkt != null) {
                    int who = pkt.getDestination();
                    Object pktData = pkt.getData();
                    if (who >= 0 && who < 4 && !(pktData == null) && !(pktData instanceof JoinPkt) && !(pktData instanceof LeavePkt) && !(pktData instanceof BootPkt) && pktData instanceof java.io.Serializable) {
                        synchronized (cm) {
                            cm.noResend(pktData, who);
                            if (pkt.isPersistent()) persistentPackets[who].addElement(pktData);
                        }
                    } else if (!(pktData instanceof java.io.Serializable)) {
                        System.err.println("DirectCom: Packet not Serializable");
                    } else {
                        System.err.println("DirectCom: Invalid packet.");
                    }
                }
            }
            synchronized (cm) {
                for (int i = 0; i < 4; i++) {
                    if (cm.getResend(i)) {
                        dcr.resend(i);
                        cm.setResend(false, i);
                    }
                    if (dcr.getState() == dcr.WAITING) {
                        cm.wait(100);
                        Object o = dequeue(i);
                        if (o != null) dcr.packetReceived(o, i);
                    }
                }
            }
        }
    }

    /**
	 * Ask a player to pick trump.
	 */
    void conductTrumpDiscovery() throws IOException, InterruptedException {
        Object o = null;
        do {
            int who = ((AskTrumpRules) rules).whoPicksTrump();
            cm.sendClear(new QueryPkt("Please choose the trump."), who);
            for (; ; ) {
                synchronized (cm) {
                    while (packets[who].isEmpty()) cm.wait();
                    o = dequeue(who);
                }
                if (o != null && o instanceof ResponsePkt) break;
                cm.resend(new QueryPkt("Please choose the trump."), who);
            }
        } while (!((AskTrumpRules) rules).trumpResponse(((ResponsePkt) o).toString()));
        cm.broadcast(new TrumpPkt(rules.getTrump(), rules.getTrumpName()));
    }

    void conductGame() throws IOException, InterruptedException {
        int whoStarts = 0, whoseTurn = whoStarts;
        boolean notified = false;
        for (int i = 0; i < 4; i++) players[i].setScoreAndBags(0, 0);
        for (; ; ) {
            dealCards();
            if (rules instanceof BiddingRules) conductBidding(whoStarts);
            if (rules instanceof PassingRules) conductPassing(whoStarts);
            if (rules instanceof DirectComRules) conductDirectCom();
            cm.broadcast(new TrumpPkt(rules.getTrump(), rules.getTrumpName()));
            tableau.setLead(whoStarts);
            while (hands[0].howManyCards() > 0) {
                for (whoseTurn = tableau.getLead(); whoseTurn < tableau.getLead() + 4; whoseTurn++) {
                    int w = whoseTurn % 4;
                    cm.sendClear(new YourTurnPkt(YourTurnPkt.PLAY), w);
                    for (; ; ) {
                        synchronized (cm) {
                            while (packets[w].isEmpty()) {
                                cm.resend(new YourTurnPkt(YourTurnPkt.PLAY), w);
                                cm.wait();
                            }
                        }
                        Object o = dequeue(w);
                        if (o != null) {
                            if (o instanceof PlayPkt) {
                                PlayingCard c = ((PlayPkt) o).getCard();
                                if (!rules.validPlay(beenPlayed, tableau, hands[w], c) || hands[w].search(c) == PileOfCards.NOT_FOUND) {
                                    cm.noResend(new RejectPkt(), w);
                                    cm.sendClear(new YourTurnPkt(YourTurnPkt.PLAY), w);
                                    continue;
                                }
                                synchronized (cm) {
                                    cm.noResend(new AcceptPkt(YourTurnPkt.PLAY), w);
                                    beenPlayed.addToTop(c);
                                    tableau.addToTop(c);
                                    hands[w].remove(c);
                                    cm.broadcast(new PlayPkt(c, w));
                                    cm.setResend(false, w);
                                }
                                break;
                            }
                        }
                    }
                }
                synchronized (cm) {
                    tableau.setLead(rules.whoWinsTrick(tableau));
                    cardsWon[tableau.getLead()].addToTop(tableau);
                    tableau.clear();
                    cm.broadcast(new TrickPkt(tableau.getLead()));
                }
            }
            synchronized (cm) {
                rules.score(cardsWon, players);
                broadcastTable();
                cm.broadcast(new EndHandPkt());
                GameResult gr = rules.done(players);
                if (gr.done) {
                    String res = "Game is complete.\nWinners are: ";
                    if (gr.winners.length > 0) res += players[gr.winners[0]].getName();
                    for (int i = 1; i < gr.winners.length; i++) res += " & " + players[gr.winners[i]].getName();
                    res += "\n";
                    cm.broadcast(new ChatPkt(res));
                    cm.broadcast(gr);
                    break;
                }
                for (int i = 0; i < 4; i++) {
                    players[i].clearBid();
                    persistentPackets[i].removeAllElements();
                }
            }
            whoStarts++;
            whoStarts %= 4;
            whoseTurn = whoStarts;
        }
        if (verbose) {
            for (int count = 0; count < 4; count++) {
                System.out.print(players[count].getName() + ": ");
                System.out.print(players[count].getScore());
                System.out.print("_");
                System.out.println(players[count].getBags());
            }
        }
    }

    private void doJoin(Socket s) throws IOException {
        s.setTcpNoDelay(true);
        synchronized (cm) {
            int where = cm.add(s, true);
            if (where < 0) {
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                os.writeObject(new RejectPkt());
                os.flush();
                os.close();
                s.close();
            } else {
                packets[where].removeAll();
                cm.getThread(where).addPacketListener(this);
                cm.getThread(where).addPacketListener(new ChatManager());
                cm.getThread(where).start();
                long timeout = (new Date()).getTime() + 10000;
                while (packets[where].empty() && (new Date()).getTime() < timeout) {
                    synchronized (packets[where]) {
                        try {
                            packets[where].wait(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                Object o = dequeue(where);
                String name = "";
                if (o instanceof JoinPkt) name = ((JoinPkt) o).getName();
                if (o == null || !(o instanceof JoinPkt) || name.length() < 1 || name.length() > 100) {
                    if (verbose) {
                        System.err.print("Bad connection from " + s.getInetAddress());
                        System.err.println(o);
                    }
                    cm.getThread(where).removePacketListener(this);
                    cm.remove(where);
                    System.err.println(o);
                } else {
                    players[where].setName(name);
                    cm.send(new AcceptPkt(where), where);
                    if (verbose) {
                        System.err.println(players[where].getName() + " is joining the game from " + s.getInetAddress());
                    }
                    broadcastTable();
                    if (gameInProgress) {
                        PileOfCards tmp = new PileOfCards(beenPlayed);
                        tmp.remove(tableau);
                        cm.setResend(true, where);
                        cm.send(players, where);
                        cm.send(new DealPkt(hands[where]), where);
                        cm.send(new TrumpPkt(rules.getTrump(), rules.getTrumpName()), where);
                        cm.send(new SofarPkt(tmp), where);
                        for (int i = 0; i < 4; i++) {
                            PlayingCard c = tableau.getTableCard(i);
                            if (c != null) {
                                cm.send(new PlayPkt(c, i), where);
                            }
                        }
                        for (int i = 0; i < players.length; i++) {
                            if (players[i].hasBid()) cm.send(new BidPkt(players[i].getBid(), i), where);
                        }
                        Enumeration pp = persistentPackets[where].elements();
                        while (pp.hasMoreElements()) {
                            Object pkt = pp.nextElement();
                            cm.send(pkt, where);
                        }
                    }
                }
            }
            if (cm.howManyActive() == 4) gameInProgress = true;
            cm.notifyAll();
        }
        synchronized (this) {
            notifyAll();
        }
    }

    private Object dequeue(int i) {
        Object o = null;
        synchronized (cm) {
            o = packets[i].dequeue();
        }
        return o;
    }

    void doLeave(int who) {
        if (verbose) System.out.println(players[who].getName() + " is leaving the game.");
        removePlayer(who);
        broadcastTable();
    }

    protected void doBoot(int who, String why) {
        if (verbose) System.out.println("Initiating boot of " + who + " because " + why);
        if (cm.getThread(who) == null) return;
        cm.send(new BootPkt(why), who);
        removePlayer(who);
        broadcastTable();
    }

    private void removePlayer(int w) {
        synchronized (cm) {
            players[w].setName("");
            cm.remove(w);
            packets[w].removeAll();
            cm.notifyAll();
            if (verbose) System.out.println("Server: Players remaining: " + cm.howManyActive());
            synchronized (cm) {
                cm.notifyAll();
            }
        }
        synchronized (this) {
            notifyAll();
        }
    }

    void broadcastTable() {
        Player[] bt = new Player[4];
        for (int i = 0; i < 4; i++) {
            if (players[i] != null) bt[i] = players[i]; else bt[i] = new Player("");
        }
        cm.broadcast(bt);
        cm.broadcast(new GameNamePkt(rules.name()));
    }

    public int howMany() {
        return cm.howManyActive();
    }

    public static void rest(int m) {
        Thread.yield();
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
        }
    }

    public String[] getNames() {
        String[] names = new String[players.length];
        for (int i = 0; i < names.length; i++) names[i] = players[i].getName();
        return names;
    }

    public void tellBoot(int who, String why) {
        doBoot(who, why);
    }

    public void packetReceived(int id, Object o) {
        if (id >= 0 && id < 4) {
            if (o instanceof LeavePkt) {
                lm.tellLeave(id);
            } else if (o instanceof ChatPkt) {
                return;
            } else if (o instanceof JoinPkt) {
                synchronized (packets[id]) {
                    packets[id].addElement(o);
                    packets[id].notifyAll();
                }
            } else {
                synchronized (cm) {
                    packets[id].addElement(o);
                    cm.notifyAll();
                }
                synchronized (this) {
                    notifyAll();
                }
            }
        }
    }

    private boolean allEmpty() {
        boolean b = true;
        for (int i = 0; i < 4; i++) if (packets[i] != null) b &= (packets[i].empty());
        return b;
    }

    /**
	 * Inner member class to handle distribution of chat messages
	 */
    class ChatManager implements PacketListener {

        public void packetReceived(int id, Object o) {
            if (!(o instanceof ChatPkt)) return;
            if (id < 0 || id >= 4) return;
            String chat = players[id].getName() + "> ";
            chat += ((ChatPkt) o).getMessage();
            if (!chat.endsWith("\n")) chat += "\n";
            cm.broadcast(new ChatPkt(chat));
        }
    }

    /**
	 * Inner member class to handle players' leaving.
	 */
    class LeaveManager extends Thread {

        private net.sf.nebulacards.main.Queue jobs;

        private boolean shouldStop;

        public LeaveManager() {
            jobs = new net.sf.nebulacards.main.Queue();
            shouldStop = false;
        }

        public void tellLeave(int who) {
            jobs.enqueue(new Integer(who));
            synchronized (LeaveManager.this) {
                LeaveManager.this.notifyAll();
            }
        }

        public void end() {
            shouldStop = true;
            synchronized (LeaveManager.this) {
                LeaveManager.this.notifyAll();
            }
        }

        public void run() {
            while (!shouldStop || !jobs.isEmpty()) {
                try {
                    synchronized (LeaveManager.this) {
                        LeaveManager.this.wait(10000);
                    }
                } catch (InterruptedException e) {
                }
                Integer I = (Integer) jobs.dequeue();
                if (I != null) doLeave(I.intValue());
                synchronized (cm) {
                    synchronized (LeaveManager.this) {
                        if (jobs.isEmpty()) {
                            for (int i = 0; i < 4; i++) {
                                CommThread ct = cm.getThread(i);
                                if (ct != null && !ct.isAlive()) {
                                    tellLeave(i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * Inner member class to handle accepting new players.
	 */
    class JoinManager extends Thread {

        ServerSocket sock = null;

        int port;

        boolean shouldStop = true;

        public JoinManager(int _port) {
            port = _port;
            try {
                sock = new ServerSocket(port);
                sock.setSoTimeout(0);
                shouldStop = false;
            } catch (Exception e) {
                System.err.println("Error establishing socket:");
                e.printStackTrace(System.err);
            }
        }

        public void end() {
            shouldStop = true;
            try {
                sock.close();
            } catch (Exception e) {
            }
        }

        public void run() {
            while (!shouldStop) {
                Socket s;
                try {
                    s = sock.accept();
                    doJoin(s);
                } catch (Exception e) {
                    continue;
                }
            }
            try {
                sock.close();
            } catch (Exception e) {
            }
        }
    }
}
