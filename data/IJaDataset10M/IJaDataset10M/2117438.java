package magenta;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * class ComManager -
 *  the communications centre for a Magenta agent.
 *
 *  <p>A ComManager is instantiated by an Agent object (or a derived
 *  class of Agent).  It binds to a TCP/IP port using sockets and
 *  <b>listens</b> for incoming messages.  When a message is received a
 *  ComManagerThread is spawned to handle the request while the
 *  main port is freed to listen for new requests.  The ComManager
 *  also contains the code to <b>parse the first token</b> of incoming
 *  messages and demultiplex it to the appropriate internal
 *  method.
 *
 *  <p>ComManager can <b>send</b> messages to remote agents.
 *  It contains the code to establish a TCP/IP connection with a 
 *  remote agent on a specified port, send a message, receive the reply and
 *  return it to the caller.
 *
 * @author aloke mukherjee
 * @version
 * 2001.03.11 alokem creation
 */
public class ComManager extends Thread {

    /** the agent which contains us */
    private Agent myAgent;

    /**
   * ComManager constructor -
   *  Call parent class constructor, init member variables.
   *
   * @param a  agent which contains this ComManager
   *
   * @version
   * 2001.02.26 alokem creation
   */
    public ComManager(Agent a) {
        super();
        myAgent = a;
    }

    /** 
   * ComManager::listen -
   *  handle messages from remote agents.  When a message
   *  arrives a ComManagerThread is instantiated and started to
   *  handle the request freeing the ComManager to listen for new
   *  requests.
   *
   *  <p>If the agent is unable to listen on the Agent's port
   *  the Agent's hostInfo is set to null and the ComManager exits.
   *
   * @version
   * 2001.03.11 alokem creation
   */
    private void listen() throws IOException {
        ServerSocket serverSocket = null;
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(myAgent.getPort());
        } catch (IOException e) {
            myAgent.log("Could not listen on port: " + myAgent.getPort());
            myAgent.setHostInfo(null);
            return;
        }
        while (listening) new ComManagerThread(serverSocket.accept(), this).start();
        serverSocket.close();
    }

    /**
   * ComManager::send -
   *  send a message to a remote agent and block waiting for the reply.
   * 
   * @param agent   handle used to communicate with remote agent.
   * @param message message to be passed to the remote agent.
   *
   * @return
   * the result status and return value from the message.
   *
   * @version
   * 2001.02.26 alokem creation from Sun's KnockKnockClient 
   *   (sockets tutorial)
   * 2001.04.25 alokem changed to take AgentProxy as an argument
   * 2001.06.11 alokem move bulk of function to sendHelper to accomodate
   *  reply and no-reply cases
   */
    public String send(AgentProxy agent, String message) throws IOException {
        return sendHelper(agent, message, true);
    }

    /**
   * ComManager::sendNoReply -
   *  send a message to a remote agent and return immediately.
   * 
   * @param agent   handle used to communicate with remote agent.
   * @param message message to be passed to the remote agent.
   *
   * @version
   * 2001.06.11 alokem creation
   */
    public void sendNoReply(AgentProxy agent, String message) throws IOException {
        sendHelper(agent, message, false);
    }

    /**
   * ComManager::sendHelper -
   *  send a message to a remote agent.
   *
   * @param agent     handle used to communicate with remote agent.
   * @param message   message to be passed to the remote agent.
   * @param expectReply whether to block waiting for a reply
   *
   * @return 
   * the result status and return value from the message (this is
   * meaningless when expectReply is false)
   *
   * @version
   * 2001.06.11 creation from original send
   */
    private String sendHelper(AgentProxy agent, String message, boolean expectReply) throws IOException {
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String hostName;
        int port;
        String reply = "";
        StringTokenizer tokenizer = new StringTokenizer(agent.getHostInfo(), ":");
        if (tokenizer.hasMoreElements()) {
            hostName = tokenizer.nextToken();
        } else {
            return new String("error: agentproxy does not have valid hostname");
        }
        if (tokenizer.hasMoreElements()) {
            port = Integer.parseInt(tokenizer.nextToken());
        } else {
            return new String("error: agentproxy does not have valid port");
        }
        try {
            clientSocket = new Socket(hostName, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            if (expectReply) {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            }
        } catch (UnknownHostException e) {
            return new String("error: don't know about host: " + hostName);
        } catch (IOException e) {
            return new String("error: couldn't get I/O for the connection to: " + hostName);
        }
        out.println(message);
        if (expectReply) {
            reply = in.readLine();
        }
        out.close();
        if (expectReply) {
            in.close();
        }
        clientSocket.close();
        return reply;
    }

    /**
   * ComManager::processMessage -
   *  dispatch an incoming request to appropriate handler.
   *  Usually the handler is the ObjectManager which takes care
   *  of creating, deleting and setting the attributes of 
   *  the objects it contains.
   * 
   *  <p>The ObjectManager will then direct the request to the
   *  appropriate object.  This will return a String containing
   *  the status and result of the query which is returned to
   *  the caller and passed back to the remote agent which initiated
   *  the query.
   * 
   * @param message  remote request to this agent.
   *
   * @return 
   * String containing the response to query
   *
   * @version
   * 2001.01.30 alokem creation
   */
    protected String processMessage(String message) {
        String retval = "failure";
        if (message != null) {
            StringTokenizer msgTokenizer = new StringTokenizer(message, ObjectManager.requestDelimiter);
            if (msgTokenizer.hasMoreElements()) {
                String command = msgTokenizer.nextToken();
                String parameters = null;
                if (msgTokenizer.hasMoreElements()) {
                    parameters = msgTokenizer.nextToken("");
                }
                retval = dispatchRequest(command, parameters);
            }
        }
        return retval;
    }

    /**
   * ComManager::dispatchRequest -
   *  figure out which ObjectManager operation is required. 
   *
   * @param cmd     token containing command such as create,
   *                set, get, etc.
   * @param message the parameters for the command, for example
   *                the type of object to create, or the attribute to 
   *                get.
   *
   * @return 
   * whether the command succeeded or failed and if successful
   * the return value.
   *
   * @version
   * 2001.03.11 alokem creation
   */
    private String dispatchRequest(String cmd, String message) {
        ObjectManager om = myAgent.getObjectManager();
        if (cmd.compareTo("create") == 0) {
            return new String("create " + om.create(message));
        } else if (cmd.compareTo("get") == 0) {
            return new String("get " + om.get(message));
        } else if (cmd.compareTo("set") == 0) {
            return new String("set " + om.set(message));
        } else if (cmd.compareTo("delete") == 0) {
            return new String("delete " + om.delete(message));
        } else if (cmd.compareTo("print") == 0) {
            return om.toString();
        } else if (cmd.compareTo("eventreport") == 0) {
            myAgent.handleEvent(message);
            return new String("eventreport");
        } else if (cmd.compareTo("quit") == 0) {
            System.exit(0);
        }
        return new String("error: [" + cmd + "] is an invalid command");
    }

    /**
   * ComManager::run - 
   *  start listening for incoming requests.
   *
   * @version
   * 2001.03.11 alokem creation
   */
    public void run() {
        try {
            this.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
