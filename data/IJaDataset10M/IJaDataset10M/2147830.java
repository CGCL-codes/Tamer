package comm;

import world.World;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import world.Base;
import world.Enemy;
import world.Flag;
import world.Friend;
import world.Obstacle;
import world.Point;
import world.WorldConstant;

/**
 *
 * @author Derrall Heath
 */
public class Messenger {

    private int port;

    private String host;

    private Socket sock;

    private PrintStream output;

    private BufferedReader input;

    public Messenger(String Host, int Port) {
        this.port = Port;
        this.host = Host;
    }

    public World buildWorld() {
        World world = new World();
        world.setFriends(getFriends());
        world.setEnemies(getEnemies());
        world.setObstacles(getObstacles());
        world.setFlags(getFlags());
        world.setBases(getBases());
        world.setConstants(getWorldConstants());
        String myColor = "";
        for (Flag f : world.getFlags()) {
            boolean found = false;
            for (Enemy e : world.getEnemies()) {
                if (e.getColor().equals(f.getOwnerColor())) {
                    found = true;
                }
            }
            if (!found) {
                myColor = f.getOwnerColor();
            }
        }
        for (Friend fr : world.getFriends()) {
            fr.setColor(myColor);
        }
        return world;
    }

    private Map<String, String> getWorldConstants() {
        List<WorldConstant> worldConstants = new ArrayList();
        List<String> SworldConstants = sendQuery("constants");
        Map<String, String> constants = new TreeMap();
        for (String SworldConstant : SworldConstants) {
            String[] AworldConstant = SworldConstant.split(" ");
            constants.put(AworldConstant[1], AworldConstant[2]);
        }
        return constants;
    }

    private List<Base> getBases() {
        List<Base> bases = new ArrayList();
        List<String> Sbases = sendQuery("bases");
        for (String Sbase : Sbases) {
            String[] Abase = Sbase.split(" ");
            Base base = new Base();
            base.setTeamColor(Abase[1]);
            base.setTLPoint(new Point(Double.parseDouble(Abase[2]), Double.parseDouble(Abase[3])));
            base.setBLPoint(new Point(Double.parseDouble(Abase[4]), Double.parseDouble(Abase[5])));
            base.setBRPoint(new Point(Double.parseDouble(Abase[6]), Double.parseDouble(Abase[7])));
            base.setTRPoint(new Point(Double.parseDouble(Abase[8]), Double.parseDouble(Abase[9])));
            bases.add(base);
        }
        return bases;
    }

    private List<Flag> getFlags() {
        List<Flag> flags = new ArrayList();
        List<String> Sflags = sendQuery("flags");
        for (String Sflag : Sflags) {
            String[] Aflag = Sflag.split(" ");
            Flag flag = new Flag();
            flag.setOwnerColor(Aflag[1]);
            flag.setPossensorColor(Aflag[2]);
            flag.setLocation(new Point(Double.parseDouble(Aflag[3]), Double.parseDouble(Aflag[4])));
            flags.add(flag);
        }
        return flags;
    }

    private List<Obstacle> getObstacles() {
        List<Obstacle> obstacles = new ArrayList();
        List<String> Sobstacles = sendQuery("obstacles");
        for (String Sobstacle : Sobstacles) {
            String[] Aobstacle = Sobstacle.split(" ");
            Obstacle obstacle = new Obstacle();
            obstacle.setTLPoint(new Point(Double.parseDouble(Aobstacle[1]), Double.parseDouble(Aobstacle[2])));
            obstacle.setBLPoint(new Point(Double.parseDouble(Aobstacle[3]), Double.parseDouble(Aobstacle[4])));
            obstacle.setBRPoint(new Point(Double.parseDouble(Aobstacle[5]), Double.parseDouble(Aobstacle[6])));
            obstacle.setTRPoint(new Point(Double.parseDouble(Aobstacle[7]), Double.parseDouble(Aobstacle[8])));
            obstacles.add(obstacle);
        }
        return obstacles;
    }

    private List<Enemy> getEnemies() {
        List<Enemy> enemies = new ArrayList();
        List<String> Senemies = sendQuery("othertanks");
        for (String Senemy : Senemies) {
            String[] Aenemy = Senemy.split(" ");
            Enemy enemy = new Enemy();
            enemy.setCallsign(Aenemy[1]);
            enemy.setColor(Aenemy[2]);
            enemy.setStatus(Aenemy[3]);
            enemy.setFlag(Aenemy[4]);
            enemy.setLocation(new Point(Double.parseDouble(Aenemy[5]), Double.parseDouble(Aenemy[6])));
            enemy.setAngle(Double.parseDouble(Aenemy[7]));
            enemies.add(enemy);
        }
        return enemies;
    }

    private List<Friend> getFriends() {
        List<Friend> friends = new ArrayList();
        List<String> Sfriends = sendQuery("mytanks");
        for (String Sfriend : Sfriends) {
            String[] Afriend = Sfriend.split(" ");
            Friend friend = new Friend();
            friend.setBotId(Integer.parseInt(Afriend[1]));
            friend.setCallSign(Afriend[2]);
            friend.setStatus(Afriend[3]);
            friend.setShotsAvailable(Integer.parseInt(Afriend[4]));
            friend.setTimeToReload(Double.parseDouble(Afriend[5]));
            friend.setFlag(Afriend[6]);
            friend.setLocation(new Point(Double.parseDouble(Afriend[7]), Double.parseDouble(Afriend[8])));
            friend.setAngle(Double.parseDouble(Afriend[9]));
            friend.setVx(Double.parseDouble(Afriend[10]));
            friend.setVy(Double.parseDouble(Afriend[11]));
            friend.setAngvel(Double.parseDouble(Afriend[12]));
            friends.add(friend);
        }
        return friends;
    }

    public void establishConnection() {
        try {
            sock = new Socket(host, port);
            output = new PrintStream(sock.getOutputStream());
            input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String resp = getResponseTimeout();
            System.out.println("connection response: \n\t" + resp);
            System.out.println("first hello:\n\t" + sendInitialMessage());
        } catch (SocketException e) {
            System.out.println("Socket error : " + e);
        } catch (UnknownHostException e) {
            System.out.println("Invalid host!");
        } catch (IOException e) {
            System.out.println("I/O error : " + e);
        }
    }

    private String getResponseTimeout() {
        String response = "";
        String line = "";
        int WaitTm = 10;
        int NumTries = 50;
        int t = 0;
        try {
            while (t < NumTries) {
                if (input.ready()) {
                    line = input.readLine();
                    response += line;
                    t = 0;
                } else {
                    t++;
                    Thread.sleep(WaitTm);
                }
            }
        } catch (Exception e) {
        }
        return response;
    }

    private String sendInitialMessage() {
        String msg = "agent 1\nagent 1\n";
        try {
            output.print(msg);
            System.out.println(msg);
        } catch (Exception e) {
            System.out.println("Error in sendCommand: " + e.getMessage());
        }
        return getResponseTimeout();
    }

    public String sendCommand(String cmd) {
        cmd = cmd + "\n";
        try {
            output.print(cmd);
        } catch (Exception e) {
            System.out.println("Error in sendCommand: " + e.getMessage());
        }
        return getCmdResponse();
    }

    private String getCmdResponse() {
        String response = "";
        try {
            String line = "";
            while (!line.equalsIgnoreCase("ok") && !line.startsWith("fail") && !line.startsWith("error")) {
                line = input.readLine();
                response += line + "\n";
                line = line.trim();
            }
        } catch (Exception e) {
            System.out.println("Error in getResponse: " + e.getMessage());
        }
        return response;
    }

    public List<String> sendQuery(String query) {
        query = query + "\n";
        try {
            output.print(query);
        } catch (Exception e) {
            System.out.println("Error in sendServerCommand: " + e.getMessage());
        }
        return getQueryResponse();
    }

    private List<String> getQueryResponse() {
        List response = new ArrayList();
        try {
            String line = "";
            while (!line.equalsIgnoreCase("end") && !line.startsWith("error")) {
                line = input.readLine();
                line = line.trim();
                if (!line.equalsIgnoreCase("end") && !line.equalsIgnoreCase("begin") && !line.startsWith("ack") && !line.startsWith("error")) {
                    response.add(line);
                } else {
                    if (line.startsWith("error")) {
                        System.out.println(line);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in getResponse: " + e.getMessage());
        }
        return response;
    }

    public void endConnection() {
        System.out.println("Entering endConnection");
        try {
            sock.close();
        } catch (Exception e) {
            System.out.println("Could not close connection: " + e.getMessage());
        }
    }
}
