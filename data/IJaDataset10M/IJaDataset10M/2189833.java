package alice.simpa;

import java.util.HashMap;
import alice.cartago.*;

/**
 * Class representing a simpA node.
 * 
 * @author aricci
 *
 */
public class SIMPANode {

    private HashMap<String, SIMPAWorkspace> wsps;

    private ISIMPALoggerManager logManager;

    private static SIMPANode instance;

    private boolean standalone;

    private ICartagoContext bootContext;

    class BootAgent extends alice.cartago.util.Agent {

        SIMPANode node;

        String workspaceName;

        BootAgent(SIMPANode node, String workspaceName) {
            super("$bigbangAgent");
            this.node = node;
            this.workspaceName = workspaceName;
        }

        public void run() {
            try {
                ArtifactId agentFactoryId = makeArtifact("agent-factory", "alice.simpa.AgentFactory", new ArtifactConfig("default"));
                ArtifactId agentRegistryId = makeArtifact("agent-registry", "alice.simpa.AgentRegistry", new ArtifactConfig("default"));
                node.createWorkspace("default", agentFactoryId, agentRegistryId);
                if (workspaceName != "default") {
                    createWorkspace(workspaceName);
                    joinWorkspace(workspaceName, "$bigbangAgent");
                    agentFactoryId = makeArtifact("agent-factory", "alice.simpa.AgentFactory", new ArtifactConfig(workspaceName));
                    agentRegistryId = makeArtifact("agent-registry", "alice.simpa.AgentRegistry", new ArtifactConfig(workspaceName));
                    node.createWorkspace(workspaceName, agentFactoryId, agentRegistryId);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    SIMPANode(String workspaceName, String bootAgentName) throws CartagoException {
        this(workspaceName, bootAgentName, true, CartagoNode.DEFAULT_PORT);
    }

    SIMPANode(String workspaceName, String bootAgentName, boolean standalone) throws CartagoException {
        this(workspaceName, bootAgentName, standalone, CartagoNode.DEFAULT_PORT);
    }

    SIMPANode(String workspaceName, String bootAgentName, boolean standalone, int port) throws CartagoException {
        this.standalone = standalone;
        preBanner();
        if (instance != null) {
            throw new CartagoException("Node already created.");
        }
        logManager = new SIMPALoggerManager();
        wsps = new HashMap<String, SIMPAWorkspace>();
        if (standalone) {
            CartagoService.installStandaloneNode();
        } else {
            CartagoService.installNode(port);
        }
        BootAgent agent = new BootAgent(this, workspaceName);
        agent.start();
        try {
            agent.join();
            postBanner();
            SIMPANode.instance = this;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized SIMPAWorkspace createWorkspace(String name, ArtifactId agentFactoryId, ArtifactId agentRegistryId) throws SIMPAException {
        SIMPAWorkspace wsp = wsps.get(name);
        if (wsp == null) {
            wsp = new SIMPAWorkspace(name, agentFactoryId, agentRegistryId);
            wsps.put(name, wsp);
        } else {
            throw new SIMPAException("Workspace already present");
        }
        return wsp;
    }

    private void preBanner() {
        System.out.println("simpA " + getVersion() + " booting...");
    }

    private void postBanner() {
        System.out.println("simpA environment ready.");
    }

    public ICartagoContext getMainContext() {
        return bootContext;
    }

    public synchronized SIMPAWorkspace getWorkspace(String wspName) {
        return wsps.get(wspName);
    }

    /**
	 * Get current version
	 * 
	 * @return version
	 */
    public static String getVersion() {
        return SIMPA_VERSION.getID();
    }

    /**
	 * (Low level feature) Get a CARTAGO interface to the specified workspace 
	 * 
	 * This service makes it possible to join a workspace without being a simpA agent,
	 * but as a low-level (wrt simpA system) CARTAGO agent. 
	 * 
	 * @param agentName agent name as CARTAGO user
	 * @param wspName workspace to join
	 * @return CARTAGO interface to play within the workspace.
	 * 
	 * @throws CartagoException
	 */
    public static ICartagoContext getCartagoInterface(String agentName, String wspName) throws CartagoException {
        if (instance == null) {
            throw new CartagoException("No simpA node created");
        } else {
            return CartagoService.joinWorkspace(wspName, null, null, new alice.cartago.security.UserIdCredential(agentName));
        }
    }

    static SIMPANode getInstance() {
        return instance;
    }
}
