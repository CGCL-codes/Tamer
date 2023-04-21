package sf2.view.test;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import sf2.core.Event;
import sf2.core.KeyWrap;
import sf2.core.StageExecption;
import sf2.io.impl.emu.EmuMessageClient;
import sf2.io.impl.emu.EmuMessageServer;
import sf2.log.Logging;
import sf2.view.impl.twophase.TwoPhaseExecutor;
import sf2.view.impl.twophase.TwoPhaseLock;

public final class TwoPhaseTest {

    private static final String LOG_NAME = "2PCTest";

    private static final String SERVICE_NAME = "2PCTest";

    private static final int SERVICE_KEY = SERVICE_NAME.hashCode();

    private static final int NUM_NODES = 3;

    private static byte[] key;

    static {
        key = KeyWrap.hash("2PCTest".getBytes());
    }

    private Logging logging = Logging.getInstance();

    private Random rand = new Random();

    private BlockingQueue<Event> queue = new LinkedBlockingQueue<Event>();

    private EmulatedMachine[] machines = new EmulatedMachine[NUM_NODES];

    private Timer timer = new Timer();

    public TwoPhaseTest() {
        for (int i = 0; i < NUM_NODES; i++) {
            machines[i] = new EmulatedMachine();
            machines[i].start();
        }
        timer.schedule(new TestTimerTask(), 3 * 1000, 5 * 1000);
    }

    public void run() {
        try {
            while (true) {
                Event event = queue.take();
                logging.debug(LOG_NAME, "GOT event " + event);
            }
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        TwoPhaseTest test = new TwoPhaseTest();
        test.run();
    }

    class EmulatedMachine {

        private EmuMessageClient client;

        private EmuMessageServer server;

        private TwoPhaseLock tpc;

        public EmulatedMachine() {
            try {
                server = new EmuMessageServer();
                client = new EmuMessageClient();
                server.configure(false);
                client.configure(false, server);
                tpc = new TwoPhaseLock(client, server);
            } catch (StageExecption e) {
                e.printStackTrace();
            }
        }

        public void start() {
            Set<InetAddress> members = new HashSet<InetAddress>();
            try {
                for (int i = 0; i < NUM_NODES; i++) {
                    members.add(InetAddress.getByName("192.168.20." + (i + 1)));
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            tpc.start(key);
            tpc.addListener(key, queue);
            tpc.setExecutor(key, new TestExecutor());
            tpc.forceViewChange(key, members);
            server.start();
        }

        public void request() {
            tpc.request(key, new TestRequest(rand.nextInt(100)), queue);
        }
    }

    class TestExecutor implements TwoPhaseExecutor {

        public boolean canCommit(Object req, List<Object> log) {
            return false;
        }

        public void execute(Object req) {
        }
    }

    class TestTimerTask extends TimerTask {

        public void run() {
            logging.debug(LOG_NAME, "request()");
            machines[0].request();
        }
    }
}

class TestRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int val;

    public TestRequest(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
