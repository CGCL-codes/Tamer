package titancommon.execution;

import java.util.Observable;
import java.util.Observer;
import titancommon.Performance;
import titancommon.TitanCommand;
import titan.TitanCommunicate;
import titancommon.compiler.Compiler;
import titancommon.compiler.GreedyCompiler;
import titancommon.messages.MessageDispatcher;
import titan.messages.SerialMsg;
import titancommon.services.ServiceDirectory;

public class NetworkManager implements Observer {

    private Compiler m_Compiler;

    private TaskNetwork m_TaskNetwork;

    private TaskNetwork m_RunningNetwork;

    private TitanCommunicate m_Comm;

    private MessageDispatcher m_MessageDispatcher;

    private ServiceDirectory m_ServiceDirectory;

    private int m_configID;

    public boolean m_bDebugOutput = true;

    private RecoverNode[] m_nodeObservers;

    /**
    * Instantiates a Network Manager.
    * @param md   Service Directory storing the information about the network
    * @param mif  Connection to the sensor network
    */
    public NetworkManager(MessageDispatcher md, ServiceDirectory sd, TitanCommunicate comm) {
        m_MessageDispatcher = md;
        m_ServiceDirectory = sd;
        m_Compiler = new GreedyCompiler(sd);
        m_Comm = comm;
    }

    /**
    * Instantiates a Network Manager.
    * @param sd   Service Directory storing the information about the network
    * @param mif  Connection to the sensor network
    * @param bDebugOutput Turns on/off debug output
    */
    public NetworkManager(MessageDispatcher md, ServiceDirectory sd, TitanCommunicate comm, boolean bDebugOutput) {
        this(md, sd, comm);
        m_bDebugOutput = bDebugOutput;
    }

    /**
    * Starts the execution of the given task network on the network. The description 
    * of the task network should not be spread over multiple nodes. This will be done 
    * organized by the Network Manager.
    * 
    * @param configID Configuration ID to identify the task network. Should be unique at the time running
    * @param tn       Task network description (single node) to be run on the network
    * @return         Whether the network could successfully be restarted
    */
    public boolean start(int configID, TaskNetwork tn) throws Exception {
        if (m_TaskNetwork != null) {
            return false;
        }
        if (configID > 15) {
            System.err.println("WARNING from NetworkManager: ConfigID > 7 (only 3 bits allowed)  Resetting to " + (configID % 16));
            configID %= 7;
        }
        if (m_bDebugOutput) {
            System.out.println("Starting network manager for configuration " + configID);
        }
        m_configID = configID;
        m_TaskNetwork = tn;
        Performance.printEvent("NetworkManager: Starting compilation");
        m_RunningNetwork = m_Compiler.compileNetwork(m_TaskNetwork);
        if (m_RunningNetwork == null) {
            System.out.println("NetworkManager.start: failed to compile network");
            return false;
        }
        m_RunningNetwork.setConfigID((short) m_configID);
        if (m_bDebugOutput) {
            m_RunningNetwork.printTaskNetwork();
        }
        m_nodeObservers = new RecoverNode[m_RunningNetwork.m_Nodes.size()];
        for (int i = 0; i < m_nodeObservers.length; i++) {
            m_nodeObservers[i] = new RecoverNode(m_Comm, m_RunningNetwork, m_MessageDispatcher, ((TaskNetwork.NodeConfiguration) m_RunningNetwork.m_Nodes.get(i)).address);
        }
        Performance.printEvent("NetworkManager: Done compiling");
        if (m_RunningNetwork.configureNetwork(m_Comm)) {
            m_ServiceDirectory.setActiveSearch(false);
            m_ServiceDirectory.addObserver(this);
            return true;
        } else {
            return false;
        }
    }

    /**
    * Receives updates about changes in the service directory. This might affect 
    * the execution on the nodes and need rearrangements. 
    */
    public void update(Observable obs, Object param) {
    }

    /**
    * Stops the execution of the task network and deregister from getting messages.
    * @return whether successful
    */
    public boolean stop(Observable msgDispatcher) {
        msgDispatcher.deleteObserver(this);
        m_ServiceDirectory.deleteObserver(this);
        m_TaskNetwork = null;
        m_RunningNetwork.clearConfig(m_Comm);
        m_RunningNetwork = null;
        for (int i = 0; i < m_nodeObservers.length; i++) {
            m_nodeObservers[i].stop();
        }
        m_nodeObservers = null;
        return true;
    }

    public boolean clearAll() {
        short[] fwdconfig = { (short) ((TitanCommand.TC_VERSION << 4) | TitanCommand.TITANCOMM_CONFIG), 0, 0, 0, 0, 0, 0 };
        SerialMsg msg = new SerialMsg(fwdconfig.length + SerialMsg.DEFAULT_MESSAGE_SIZE);
        msg.set_length((short) fwdconfig.length);
        msg.set_address(65535);
        msg.set_data(fwdconfig);
        if (m_bDebugOutput) {
            System.out.print("Sending broadcast clear config message...");
        }
        boolean res = m_Comm.send(0, msg);
        if (m_bDebugOutput) {
            System.out.println("ok");
        }
        return res;
    }

    /** Switches the task network currently running. Returns the template for the old task network 
    * @throws Exception */
    public TaskNetwork switchTaskNetwork(TaskNetwork tnNew) throws Exception {
        TaskNetwork tnOld = m_TaskNetwork;
        m_RunningNetwork.clearConfig(m_Comm);
        m_RunningNetwork = null;
        m_TaskNetwork = tnNew;
        m_RunningNetwork = m_Compiler.compileNetwork(m_TaskNetwork);
        m_RunningNetwork.configureNetwork(m_Comm);
        return tnOld;
    }

    public int getNodeAddrByPort(int port) {
        if (m_RunningNetwork == null) {
            return -1;
        }
        return m_RunningNetwork.getNodeAddrByPort(m_RunningNetwork.m_iMasterAddr, port);
    }

    public int getConfigID() {
        return m_configID;
    }
}
