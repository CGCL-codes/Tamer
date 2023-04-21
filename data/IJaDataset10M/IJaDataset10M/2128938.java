package rcscene;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import sceneInfo.VisualInfo;

class FuzzySceneAgent extends SceneAgent implements SendCommand {

    public static void main(String a[]) throws SocketException, IOException {
        String hostName = new String("");
        int port = 6000;
        String team = new String("Poland");
        int sceneSel = 2;
        int distCal = 7;
        String scenes = new String("default.scene");
        String objWeights = new String("{1,0,0,0,0,0,0}");
        String actionWeights = new String("{1,1,1,1,1,1,1}");
        ObjectWeights ow;
        ActionWeights aw;
        float[] aWeights = { 1, 1, 1, 1, 1, 1, 1 };
        double x = 10;
        double y = 0;
        int numBest = 15;
        try {
            for (int c = 0; c < a.length; c += 2) {
                if (a[c].compareTo("-host") == 0) {
                    hostName = a[c + 1];
                } else if (a[c].compareTo("-port") == 0) {
                    port = Integer.parseInt(a[c + 1]);
                } else if (a[c].compareTo("-team") == 0) {
                    team = a[c + 1];
                } else if (a[c].compareTo("-scene") == 0) {
                    scenes = a[c + 1];
                } else if (a[c].compareTo("-sceneSel") == 0) {
                    sceneSel = Integer.parseInt(a[c + 1]);
                } else if (a[c].compareTo("-distCal") == 0) {
                    distCal = Integer.parseInt(a[c + 1]);
                } else if (a[c].compareTo("-numBest") == 0) {
                    numBest = Integer.parseInt(a[c + 1]);
                } else if (a[c].compareTo("-actionWeights") == 0) {
                    actionWeights = new String(a[c + 1]);
                } else if (a[c].compareTo("-objectWeights") == 0) {
                    objWeights = new String(a[c + 1]);
                } else if (a[c].compareTo("-Xcoordinate") == 0) {
                    x = Double.parseDouble(a[c + 1]);
                } else if (a[c].compareTo("-Ycoordinate") == 0) {
                    y = Double.parseDouble(a[c + 1]);
                } else {
                    throw new Exception();
                }
            }
            int counter = 0;
            StringTokenizer st = new StringTokenizer(actionWeights, "{}[]()/,");
            while (st.hasMoreTokens()) {
                aWeights[counter++] = Float.parseFloat(st.nextToken());
            }
            counter = 0;
            float[] oWeights = new float[7];
            st = new StringTokenizer(objWeights, "{}[]()/,");
            while (st.hasMoreTokens()) {
                oWeights[counter++] = Float.parseFloat(st.nextToken());
            }
            ow = new ObjectWeights(oWeights);
            aw = new ActionWeights(aWeights);
        } catch (Exception e) {
            System.err.println("");
            System.err.println("USAGE: FuzzySceneAgent [-parameter value]");
            System.err.println("");
            System.err.println("    Parameters        value        defaults");
            System.err.println("   --------------------------------------------------");
            System.err.println("    host              host_name    localhost");
            System.err.println("    port              port_number  6000");
            System.err.println("    team              team_name    Poland");
            System.err.println("    scene             scene_lib    default.scene");
            System.err.println("    sceneSel          int          2 (VoteWeighted)");
            System.err.println("    distCal           int          7 (EuclidianDistHistograms)");
            System.err.println("    numBest           k_value      15");
            System.err.println("    objectWeights     (see below)  (1,0,0,0,0,0,0)");
            System.err.println("    actionWeights     (see below)  (1,1,1,1,1,1,1)");
            System.err.println("");
            System.err.println("object and action weights should be comma-delimited with NO SPACES");
            System.err.println("   objects: ball,goal,flag,line,teammates,opponents,unknownplayers");
            System.err.println("   actions: none,dash,kick,turnneck,turn,catch,move");
            System.err.println("sceneSel: (0)chooseFirst/(1)chooseRandom/(2)voteWeighted/(3)vote&random");
            System.err.println("distCal: (0)NNCartesianObjects/(1)NNCartesianCellObjects/(2)NNCellBallGoal");
            System.err.println("         (3)Random/(4)bipartiteMatching");
            System.err.println("");
            System.err.println("    Example:");
            System.err.println("      SceneAgent -host www.host.com -port 6000 -team Poland");
            return;
        }
        System.out.println("Running with parameters:");
        System.out.println("host: " + hostName);
        System.out.println("port: " + port);
        System.out.println("team: " + team);
        System.out.println("scenes for training: " + scenes);
        System.out.println("sceneSel: " + sceneSelAlgos[sceneSel] + " (" + sceneSel + ")");
        System.out.println("distCal: " + distCalAlgos[distCal] + " (" + distCal + ")");
        System.out.println("numBest: " + numBest);
        System.out.println("object weights: " + ow);
        System.out.println("action weights: " + aw);
        FuzzySceneAgent player;
        player = new FuzzySceneAgent(InetAddress.getByName(hostName), port, team, scenes, sceneSel, distCal, numBest, ow, aw, x, y);
        player.mainLoop();
    }

    /**
	 * @param array to write
	 * @return
	 */
    private static String writeArray(float[] array) {
        String x = new String("[");
        for (int i = 0; i < array.length; i++) {
            x = x.concat(Float.toString(array[i]));
            if (i < array.length - 1) x = x.concat(",");
        }
        return (x + "]");
    }

    public FuzzySceneAgent() {
    }

    public FuzzySceneAgent(InetAddress host, int port, String team, String scenes, int sceneSel, int distCal, int numBest, ObjectWeights objWeights, ActionWeights actionWeights, double x, double y) throws SocketException {
        m_socket = new DatagramSocket();
        m_host = host;
        m_port = port;
        m_team = team;
        m_scenes = scenes;
        m_sceneSel = sceneSel;
        m_distCal = distCal;
        m_numBest = numBest;
        m_objWeights = objWeights;
        m_actionWeights = actionWeights;
        m_x = x;
        m_y = y;
    }

    public void finalize() {
        m_socket.close();
    }

    protected void mainLoop() throws IOException {
        byte[] buffer = new byte[MSG_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);
        init();
        m_socket.receive(packet);
        parseInitCommand(new String(buffer, 0));
        m_port = packet.getPort();
        while (m_timeOver != true) parseSensorInformation(receive());
    }

    public void move(double x, double y) {
        send("(move " + Double.toString(x) + " " + Double.toString(y) + ")");
    }

    public void turn(double moment) {
        send("(turn " + Double.toString(moment) + ")");
    }

    public void turn_neck(double moment) {
        send("(turn_neck " + Double.toString(moment) + ")");
    }

    public void dash(double power) {
        send("(dash " + Double.toString(power) + ")");
    }

    public void kick(double power, double direction) {
        send("(kick " + Double.toString(power) + " " + Double.toString(direction) + ")");
    }

    public void say(String message) {
        send("(say " + message + ")");
    }

    public void changeView(String angle, String quality) {
        send("(change_view " + angle + " " + quality + ")");
    }

    protected void parseInitCommand(String message) throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(message, "() ");
        if (tokenizer.nextToken().compareTo("init") != 0) {
            throw new IOException(message);
        }
        m_brain = new FuzzyBrain(this, m_team, tokenizer.nextToken().charAt(0), Integer.parseInt(tokenizer.nextToken()), tokenizer.nextToken(), m_scenes, m_sceneSel, m_distCal, m_numBest, m_objWeights, m_actionWeights, m_x, m_y);
    }

    protected void init() {
        send("(init " + m_team + " (version 8))");
    }

    protected void parseSensorInformation(String message) {
        if (message.charAt(1) == 's' && message.charAt(3) == 'e') {
            VisualInfo info = new VisualInfo(message, m_team);
            info.parse();
            m_brain.see(info);
        } else if (message.charAt(1) == 'h' && message.charAt(3) == 'a') parseHear(message);
    }

    protected void parseHear(String message) {
        StringTokenizer tokenizer = new StringTokenizer(message, "() ");
        int time;
        String sender;
        tokenizer.nextToken();
        time = Integer.parseInt(tokenizer.nextToken());
        sender = tokenizer.nextToken();
        if (sender.compareTo("referee") == 0) {
            if (tokenizer.nextToken().compareTo("time_over") == 0) {
                m_timeOver = true;
            }
            m_brain.hear(time, message);
        } else if (sender.compareTo("self") != 0) m_brain.hear(time, Integer.parseInt(sender), message);
    }

    protected void send(String message) {
        byte[] buffer = new byte[MSG_SIZE];
        message.getBytes(0, message.length(), buffer, 0);
        DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE, m_host, m_port);
        try {
            m_socket.send(packet);
        } catch (IOException e) {
            System.err.println("socket sending error " + e);
        }
    }

    protected String receive() {
        byte[] buffer = new byte[MSG_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);
        try {
            m_socket.receive(packet);
        } catch (IOException e) {
            System.err.println("socket receiving error " + e);
        }
        return new String(buffer, 0);
    }

    protected DatagramSocket m_socket;

    protected InetAddress m_host;

    protected int m_port;

    protected String m_team;

    protected String m_scenes;

    protected SensorInput m_brain;

    protected volatile boolean m_timeOver = false;

    protected static final int MSG_SIZE = 4096;

    protected static final String[] sceneSelAlgos = { "ChooseFirstValid", "ChooseRandom", "VoteWeighted", "VoteWeightedWithRandom" };

    protected static final String[] distCalAlgos = { "NearestNeighborCartesianObjects", "NearestNeighborCartesianCellObjects", "NearestNeighborCellBallGoalDistance", "RandomDistance", "bipartiteMatching", "NearestNeighborCartesianObjectsNormalized", "NearestNeighborCartesianCellObjectsNormalized" };

    protected int m_sceneSel;

    protected int m_distCal;

    protected int m_numBest;

    protected ObjectWeights m_objWeights;

    protected ActionWeights m_actionWeights;

    protected double m_x;

    protected double m_y;
}
