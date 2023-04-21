package net.sf.agentopia.platform.net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sf.agentopia.core.AgentopiaConstants;
import net.sf.agentopia.platform.IAgentopiaAgent;
import net.sf.agentopia.platform.IAgentopiaConnection;
import net.sf.agentopia.platform.IAgentopiaServerRunnable;
import net.sf.agentopia.platform.IMarketPlace;
import net.sf.agentopia.util.ClassedObjectTransmitter;
import net.sf.agentopia.util.Logger;
import net.sf.agentopia.util.OS;
import net.sf.agentopia.util.net.HostId;

/**
 * Objects of the class sustain the connection to another host.
 * <p>
 * Always comes in pairs, an active and a passive sustainer. The active one
 * opens the connection to the target host, the passive one resides on the
 * target host.
 * <p>
 * Both send a HEADER_PING packet around all the time (to keep the connection
 * alive), allowing it to be replaced by an MESSAGE_AGENT_COMING if deserved.
 * <p>
 * The clue about the sustainer is that it hides a bidirectional connection
 * (active/passive) inside just one unidirectional TCP/IP connection.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 2001
 */
public class Sustainer extends Thread implements IAgentopiaServerRunnable, IAgentopiaConnection {

    /** The host to which this communicator communicates. */
    private HostId targetId;

    /** The socket to the target. */
    private Socket clientSocket;

    /** The input channel from the target. */
    private DataInputStream dataIn;

    /** The output channel to the target. */
    private DataOutputStream dataOut;

    /** The shared market place of all sustaining ends on this host. */
    private IMarketPlace marketPlace;

    /** The list of people that wish to be transferred. */
    private List<IAgentopiaAgent> transferQueue = Collections.synchronizedList(new ArrayList<IAgentopiaAgent>(64));

    /** Active or passive end? */
    private boolean isActiveSustainer;

    /** A counter for the ping send back and forth. May overflow. */
    private long ballCounter = 0;

    /**
     * The active side of the Sustainer. Creats a link to the given Host ID.
     * 
     * @param marketPlace The market place.
     * @param targetHost The other host.
     * @throws IOException If I/O failed.
     */
    private Sustainer(IMarketPlace marketPlace, HostId targetHost) throws IOException {
        this.marketPlace = marketPlace;
        this.targetId = targetHost;
        this.clientSocket = new Socket(targetHost.getAddress(), targetHost.getPort());
        this.dataIn = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        this.dataOut = new DataOutputStream(clientSocket.getOutputStream());
        this.isActiveSustainer = true;
        setName("Sustainer (Active)");
    }

    /**
     * The passive side of the Sustainer. Maintains a connection that arrived
     * here.
     * 
     * @param marketPlace The master market place.
     * @param port The master port.
     * @param clientSocket The master socket.
     * @param dataIn The master input stream.
     * @param dataOut The master output stream.
     * @throws IOException If I/O failed.
     */
    private Sustainer(IMarketPlace marketPlace, int port, Socket clientSocket, DataInputStream dataIn, DataOutputStream dataOut) throws IOException {
        this.marketPlace = marketPlace;
        this.targetId = new HostId(port, clientSocket.getInetAddress());
        this.clientSocket = clientSocket;
        this.dataIn = dataIn;
        this.dataOut = dataOut;
        this.isActiveSustainer = false;
        setName("Sustainer (Passive)");
    }

    /**
     * Creates a sustainer (the active part of it).
     * 
     * @param marketPlace The market place that holds the new connection.
     * @param targetHostId The other side.
     * @param sourceHostId This host.
     * @return The active sustainer part.
     * @throws IOException If I/O failed.
     */
    public static final Sustainer createActiveSustainer(IMarketPlace marketPlace, HostId targetHostId, HostId sourceHostId) throws IOException {
        Sustainer comm = new Sustainer(marketPlace, targetHostId);
        try {
            comm.marketPlace.addExit(comm);
        } catch (IOException exc) {
            if (AgentopiaConstants.SUSTAINER_DEBUG) {
                Logger.getLogger().info("A: Adding Sustainer to marketplace was denied. Reason: " + exc);
            }
            comm.shutDown();
            throw exc;
        }
        if (AgentopiaConstants.SUSTAINER_DEBUG) {
            Logger.getLogger().info("A: Opened active Sustainer, now sending HEADER_SUSTAINER and port.");
        }
        comm.writeInt(AgentopiaConstants.HEADER_SUSTAINER);
        comm.writeInt(sourceHostId.getPort());
        if (AgentopiaConstants.SUSTAINER_DEBUG) {
            Logger.getLogger().info("A: Sent HEADER_SUSTAINER and port, now awaiting MESSAGE_QUIT or PACKET_PING.");
        }
        int intFlag = comm.readInt();
        if (intFlag == AgentopiaConstants.MESSAGE_QUIT) {
            comm.shutDown();
            if (AgentopiaConstants.SUSTAINER_DEBUG) {
                Logger.getLogger().info("A: Active Sustainer was rejected per MESSAGE_QUIT.");
            }
            throw new IOException("Sustainer was rejected from the other side.");
        }
        if (AgentopiaConstants.SUSTAINER_DEBUG) {
            Logger.getLogger().info("A: Received PACKET_PING as affirmation, now sending the ball.");
        }
        comm.writeInt(AgentopiaConstants.PACKET_PING);
        comm.setPriority(AgentopiaConstants.SUSTAINER_PRIORITY);
        if (AgentopiaConstants.LOG_AT_ALL) {
            Logger.getLogger().info(comm.getSustainerName() + " established.");
        }
        comm.start();
        return comm;
    }

    /**
     * Creates the passive sustainer.
     * 
     * @param marketPlace The market place that holds the new connection.
     * @param clientSocket The socket to the other side.
     * @param dataIn The inputstream from the other side.
     * @param dataOut The outputstream to the other side.
     * @return The passive sustainer part.
     * @throws IOException If I/O failed.
     */
    public static final Sustainer createPassiveSustainer(IMarketPlace marketPlace, Socket clientSocket, DataInputStream dataIn, DataOutputStream dataOut) throws IOException {
        if (AgentopiaConstants.SUSTAINER_DEBUG) {
            Logger.getLogger().info("P: Received request to open passive Sustainer, now reading port.");
        }
        int port = dataIn.readInt();
        Sustainer comm = new Sustainer(marketPlace, port, clientSocket, dataIn, dataOut);
        if (AgentopiaConstants.SUSTAINER_DEBUG) {
            Logger.getLogger().info("P: Opened passive Sustainer, now checking if another one with the same target already exists.");
        }
        if (marketPlace.isHostKnown(comm.getTargetId())) {
            if (AgentopiaConstants.SUSTAINER_DEBUG) {
                comm.shutDown();
                Logger.getLogger().info("P: Rejected because already known. Sending MESSAGE_QUIT.");
            }
            comm.writeInt(AgentopiaConstants.MESSAGE_QUIT);
            comm.shutDown();
            throw new IOException("Host already known. Rejected.");
        }
        try {
            comm.marketPlace.addExit(comm);
        } catch (IOException exc) {
            if (AgentopiaConstants.SUSTAINER_DEBUG) {
                Logger.getLogger().info("P: Adding Sustainer to marketplace was denied. Reason: " + exc);
            }
            comm.shutDown();
            throw exc;
        }
        if (AgentopiaConstants.SUSTAINER_DEBUG) {
            Logger.getLogger().info("P: Accepted. Sending PACKET_PING as affirmation, waiting for the ball in run().");
        }
        comm.writeInt(AgentopiaConstants.PACKET_PING);
        comm.setPriority(AgentopiaConstants.SUSTAINER_PRIORITY);
        if (AgentopiaConstants.LOG_AT_ALL) {
            Logger.getLogger().info(comm.getSustainerName() + " established.");
        }
        comm.start();
        return comm;
    }

    /**
     * Returns a random delay time (to make the system more chaotic, simulating
     * network lag and so on).
     * 
     * @return The delay in milliseconds, between 100 and 350 ms.
     */
    private long getRandomDelay() {
        return 100 + ((int) (Math.random() * 5)) * 50;
    }

    /**
     * Returns the sustainers name.
     * 
     * @return The name.
     */
    public String getSustainerName() {
        return (isActiveSustainer ? "Active" : "Passive") + " Sustainer from \"" + marketPlace.getHomeId() + "\" to \"" + getTargetId() + "\"";
    }

    /**
     * Returns the host id of the other side.
     * 
     * @return The host id of the other side..
     */
    public HostId getTargetId() {
        return targetId;
    }

    /**
     * Turns the HEADER_PING round and round, occasionally sending an
     * AGENT_COMING instead.
     */
    public void run() {
        int intFlag = AgentopiaConstants.NEUTRAL;
        while (true) {
            try {
                intFlag = readInt();
            } catch (IOException exc) {
                if (AgentopiaConstants.SUSTAINER_DEBUG) {
                    Logger.getLogger().info("Ball not arrived. Cannot read from \"" + getTargetId() + "\". Shutdown.");
                }
                shutDown();
                return;
            }
            switch(intFlag) {
                case AgentopiaConstants.PACKET_PING:
                    ballCounter++;
                    if (AgentopiaConstants.SUSTAINER_DEBUG) {
                        Logger.getLogger().info("Ball arrived from \"" + getTargetId() + "\".");
                    }
                    OS.sleep(getRandomDelay());
                    if (transferQueue.size() <= 0) {
                        try {
                            writeInt(AgentopiaConstants.PACKET_PING);
                        } catch (IOException exc) {
                            if (AgentopiaConstants.SUSTAINER_DEBUG) {
                                Logger.getLogger().info("Ball not sendable. Cannot write to \"" + getTargetId() + "\". Shutdown.");
                            }
                            shutDown();
                            return;
                        }
                    } else {
                        if (AgentopiaConstants.SUSTAINER_DEBUG) {
                            Logger.getLogger().info("Sustainer sending agent to \"" + getTargetId() + "\".");
                        }
                        final IAgentopiaAgent agent = transferQueue.get(0);
                        final ClassedObjectTransmitter transmitter = marketPlace.getClassedObjectTransmitter();
                        try {
                            transferQueue.remove(0);
                            writeInt(AgentopiaConstants.MESSAGE_AGENT_COMING);
                            transmitter.transferAgentToStream(agent, dataOut);
                            Logger.getLogger().info("Agent \"" + agent + "\" left the system (" + marketPlace.getHomeId() + ") per Sustainer.");
                            agent.shutDown();
                        } catch (IOException exc) {
                            Logger.getLogger().info("Agent \"" + agent + "\" failed to leave to \"" + targetId + "\"... and died.");
                        }
                    }
                    break;
                case AgentopiaConstants.MESSAGE_AGENT_COMING:
                    if (AgentopiaConstants.SUSTAINER_DEBUG) {
                        Logger.getLogger().info("Sustainer receiving agent from \"" + getTargetId() + "\".");
                    }
                    final ClassedObjectTransmitter transmitter = marketPlace.getClassedObjectTransmitter();
                    try {
                        final IAgentopiaAgent agent = (IAgentopiaAgent) transmitter.transferAgentFromStream(dataIn);
                        Logger.getLogger().info("Agent \"" + agent + "\" entered the system (" + marketPlace.getHomeId() + ") per Sustainer.");
                        marketPlace.agentArrived(agent);
                    } catch (IOException exc) {
                        Logger.getLogger().info("Loading agent from \"" + targetId + "\" failed.");
                    }
                    try {
                        writeInt(AgentopiaConstants.PACKET_PING);
                    } catch (IOException exc) {
                        if (AgentopiaConstants.SUSTAINER_DEBUG) {
                            Logger.getLogger().info("Ball not sendable. Cannot write to \"" + getTargetId() + "\". Shutdown.");
                        }
                        shutDown();
                        return;
                    }
                    break;
                case AgentopiaConstants.MESSAGE_QUIT:
                    if (AgentopiaConstants.SUSTAINER_DEBUG) {
                        Logger.getLogger().info("Received MESSAGE_QUIT from \"" + getTargetId() + "\".");
                    }
                    shutDown();
                    return;
            }
        }
    }

    /**
     * Reads an int from the other side.
     * 
     * @return An int.
     * @exception IOException If network access failed.
     */
    public int readInt() throws IOException {
        return dataIn.readInt();
    }

    /**
     * Sends an int flag packet to the other side.
     * 
     * @param intFlag The int flag packet.
     * @throws IOException If I/O failed.
     */
    public void writeInt(int intFlag) throws IOException {
        dataOut.writeInt(intFlag);
        dataOut.flush();
    }

    /**
     * Shuts the connection to the other side down.
     */
    public void shutDown() {
        marketPlace.removeExit(this);
        try {
            dataOut.writeInt(AgentopiaConstants.MESSAGE_QUIT);
            dataOut.flush();
        } catch (IOException exc) {
        }
        try {
            this.dataOut.close();
            this.dataIn.close();
            this.clientSocket.close();
            if (AgentopiaConstants.SUSTAINER_DEBUG) {
                Logger.getLogger().info((isActiveSustainer ? "A" : "P") + ": Shutdown (" + getSustainerName() + ") finished, all sockets closed.");
            }
        } catch (IOException exc) {
        }
    }

    /**
     * Enlists given agent for the transfer to the other side.
     * 
     * @param agent The agent to be transferred.
     */
    public void transferMe(IAgentopiaAgent agent) {
        transferQueue.add(agent);
    }

    /**
     * Returns the number of pings being sent back and forth by the sustainers.
     * 
     * @return The ball (ping) counter.
     */
    public long getBallCounter() {
        return ballCounter;
    }

    /**
     * Returns a descriptive string (including target host id).
     * 
     * @see java.lang.Thread#toString()
     */
    public String toString() {
        return getTargetId().toString();
    }
}
