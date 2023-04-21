package bs;

import java.io.*;
import java.util.*;

/**
 *
 * @author  cf2
 */
public class NetLoadTest {

    private static final String[] fct = new String[] { "b", "kb", "mb", "gb", "tb" };

    private Timer t = new Timer();

    private NetworkStack nws;

    /** Creates a new instance of NetLoadTest */
    public NetLoadTest() throws Exception {
        nws = new NetworkStack((short) 12243, rndname(), true);
        nws.discoverGames();
        synchronized (nws) {
            nws.wait(3000);
        }
        if (nws.getGames().size() == 0) {
            nws.hostGame("loadTest", null);
        } else if (nws.getGames().containsKey("loadTest")) {
            nws.joinGame("loadTest");
        } else {
            System.out.println("Something went wrong here");
            System.exit(0);
        }
        t.scheduleAtFixedRate(new TimerTask() {

            long last;

            public void run() {
                long now = System.currentTimeMillis();
                if (last != 0) {
                    Map<Peer, Long> res = nws.getCounters();
                    long delay = now - last;
                    for (Peer p : res.keySet()) System.out.println(p.name + " : " + rate(res.get(p), delay));
                    nws.resetCounters();
                }
                last = now;
            }

            private String rate(long no, long delay) {
                StringBuffer sb = new StringBuffer();
                int fc = -1;
                double curres = 0;
                do {
                    fc++;
                    curres = (no * 1000 / delay) / Math.pow(1024, fc);
                } while (curres > 1024 && fc < fct.length);
                sb.append(curres).append(" ").append(fct[fc]).append("/s");
                return sb.toString();
            }
        }, 3000, 3000);
        synchronized (this) {
            wait(3000);
        }
        while (true) {
            int rnd;
            Object o = buildPayload(3);
            Peer dp;
            dp = getRandomPeer();
            NetworkMessage.Message msg = NetworkMessage.Message.NM_CHAT_MESSAGE;
            nws.sendMessage(dp, new NetworkMessage(msg, (Serializable) o, dp));
        }
    }

    private Peer getRandomPeer() {
        LinkedList<Peer> peers = new LinkedList<Peer>();
        peers.addAll(Arrays.asList(nws.theGame.peers));
        peers.add(nws.theGame.host);
        peers.remove(nws.myself);
        peers.add(Peer.ALL);
        int rnd = (int) (Math.random() * (peers.size()));
        return peers.get(rnd);
    }

    public Object buildPayload(int maxr) {
        Object res = null;
        int rnd;
        rnd = (int) (Math.random() * 5);
        if (maxr <= 0) rnd += 2;
        switch(rnd) {
            default:
                Integer[] bar = new Integer[64 * rnd];
                res = bar;
        }
        return res;
    }

    private String rndname() {
        StringBuffer sb = new StringBuffer();
        for (int x = 0; x < 10; x++) sb.append((char) ((Math.random() * 26) + 'a'));
        return sb.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        new NetLoadTest();
    }
}
