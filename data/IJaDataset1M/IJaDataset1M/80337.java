package robot.Network;

import java.util.Vector;
import robot.FileWorks.Logger;

/**
 * This class is used to work with communication :
 *  All networking related things are here.
 * 
 * It uses MultiThreadServer and Client class as objects.
 * any command that you wish to call will go throw Tcp/IP via client server model.
 *
 */
public class NetworkRobot {

    /**
	 * A server refernce used to hold the commincation (server side)
	 * @see MultiThreadServer
	 */
    protected MultiThreadServer server = null;

    /**
	 *  Vector <Robot> referenceToVectorOfWM  
	 *  Describes a static Vector inside the WorldManger 
	 *  when a new robot is added to the world it is add to this (referenceToVectorOfWM) Vector.
	 *  so it can be easy to call a direct function 
	 *  see this as a Tracker in Bittorent.
	 *  
	 */
    private static final Vector<NetworkRobot> referenceToVectorOfWM = null;

    /**
	 * Array that holds connected clients to other servers
	 * @see Client
	 */
    private final Vector<Client> vectorOfClients = new Vector<Client>();

    /**
	 * Vector that hold all ids that this server is connected to.
	 */
    private final Vector<Integer> ids = new Vector<Integer>();

    /**
	 * Stores the last connected index 
	 * for example if there are 100 items in the referenceToVectorOfWM
	 * and this one connected to 2
	 * lastConnectedIndex will be 1 (starting from 0)
	 * the default is 0 (on creation).
	 */
    private final int lastConnectedIndex = 0;

    /**
	 * Describes the Id of robot that a connection will be mad to it.
	 */
    private final int idOfConnectedRobot = 0;

    protected String ip = null;

    protected String ipOfWorldManager = null;

    /**
	 * Client that is used to send msgs to WM.
	 */
    private Client connectionToWM = null;

    protected Logger logger = null;

    /**
 * Adds some client to robot Client Vector by this you can dynamiclly add n arraies.
 * This also will update ids vector.
 * @param Client c some connected client
 * @return succes status.
 * @see vectorOfClients
 * @see ids
 */
    public synchronized boolean addClient(final Client c) {
        boolean retVal = false;
        int index = c.getConnectedServerId();
        if ((index = c.getConnectedServerId()) == this.server.getMyId()) return false;
        if (ids.indexOf(index) == -1) {
            retVal = vectorOfClients.add(c);
            retVal = retVal && ids.add(c.getConnectedServerId());
            System.err.println(this.server.id + " Added " + c.getMyId() + " " + c.getConnectedServerId() + " " + c.getPort());
            System.err.println("Now there are " + ids.size() + " in vector");
        }
        return retVal;
    }

    /**
 * Assigns  referenceToVectorOfWM to vectorOfRobots
 * so all the instences of robots could see who are exist:
 *   for example if the @see WorldManger is creates 5 robots on each creation
 *   each robot will be add to special vector @see vectorOfRobots.
 *   so any other robot could work with all other robots  
 * @param vectorOfRobots
 */
    @Override
    protected void finalize() throws Throwable {
        try {
            if (connectionToWM != null) {
                connectionToWM.sendMsg("bye");
            }
        } finally {
            super.finalize();
        }
    }

    protected void init() {
        server = new MultiThreadServer(this);
        final StringBuffer line = new StringBuffer("Addme ");
        line.append(ip).append(" ").append(server.getPort());
        connectionToWM = new Client(ipOfWorldManager, 10000, this, 0);
        connectionToWM.sendMsg(line.toString());
    }

    public NetworkRobot() {
    }

    public NetworkRobot(String _ipOfRobot, String _ipOfWorldManager) {
        ip = _ipOfRobot;
        ipOfWorldManager = _ipOfWorldManager;
        init();
    }

    public void setComunication(String _ipOfRobot, String _ipOfWorldManager) {
        ip = _ipOfRobot;
        ipOfWorldManager = _ipOfWorldManager;
        init();
    }

    public Client clientArrayGet(final int index) {
        return vectorOfClients.get(index);
    }

    /**
 * Create a connection to robot number 
 * @param idOfRobot
 */
    public boolean commToRobot(final int idOfRobot) {
        if (ids.contains(idOfRobot)) {
            return true;
        } else {
            return false;
        }
    }

    /**
  * 
  * send a mesage to given robot by Id 
  *  
  * @param idOfRobot id of the robot that i send the info to him 
  * @param message the actual message
  * @return true or false by succses
  * 
  */
    public boolean telToRobot(final int idOfRobot, final String message) {
        int retVal = -1;
        int i = ids.indexOf(idOfRobot);
        final int size = ids.size();
        System.err.println("Sending :" + message + " to" + idOfRobot);
        if (i != -1) {
            vectorOfClients.get(i).sendMsg(message);
            retVal = vectorOfClients.get(i).getRetVal();
            System.err.println("<" + server.getMyId() + ".Robot.telToRobot()>Sending the msg \"" + message + "\" to robot no:" + idOfRobot + " The answar was " + retVal);
            return true;
        } else {
            System.out.println("The givn Id " + idOfRobot + "  isn't in my vector so please  \nbut the are : ");
            for (i = 0; i < size; i++) {
                System.out.println("<" + ids.get(i) + ">");
            }
        }
        return false;
    }

    /**
 * Sets server id 
 * @param int someId 
 */
    public boolean setId(final int someId) {
        if (server != null) {
            server.setMyId(someId);
            return true;
        }
        return false;
    }

    public void die() {
        ((world.Robot) this).die();
        System.exit(0);
    }

    public boolean getFriends() {
        connectionToWM.sendMsg("giveMeFriends");
        return (connectionToWM.getRetVal() == 1) ? true : false;
    }
}
