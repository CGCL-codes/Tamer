package openrpg2.common.core.engine;

import java.util.logging.Logger;
import javax.swing.JPanel;
import openrpg2.common.core.network.NetworkMessageRelay;
import openrpg2.common.module.NetworkedModule;
import openrpg2.common.core.ORPGConstants;
import openrpg2.common.core.ORPGMessage;
import openrpg2.common.core.group.GroupManager;
import openrpg2.common.core.gui.GUIManager;
import openrpg2.common.core.route.RouteManager;
import openrpg2.common.module.BaseModule;

/**
 * The ModuleManager is the object that oversees and manages all interactions between modules and the rest of the application.
 * It acts as a proxy for the network with respect to the modules and offers inter-module communication services to loaded modules.
 * @author Snowdog
 */
public class ModuleManager implements NetworkedModuleLocator, ModuleCommunicator {

    private NetworkMessageRelay networkHook = null;

    int opMode = ORPGConstants.OPMODE_DEFAULT;

    private boolean initializedFlag = false;

    private MessageRegistry messageInitRegistry = new MessageRegistry();

    private MessageRegistry messageRegistry = new MessageRegistry();

    private NetworkedModule defaultHandler = null;

    private ModuleRegistry moduleRegistry = new ModuleRegistry();

    private ModuleLoader moduleLoader = null;

    private GUIManager guiManager = null;

    private GroupManager groupManager = null;

    private RouteManager routeManager = null;

    private Logger log = Logger.getLogger(this.getClass().getName());

    /** Creates a new instance of ModuleManager */
    public ModuleManager(ModuleLoader modLoader) {
        moduleLoader = modLoader;
    }

    /**
     * ModuleManager in Client mode?
     * @return true if the ModuleManager is in Client mode
     * @see ORPGConstants#OPMODE_CLIENT
     */
    protected boolean isClient() {
        if (opMode == ORPGConstants.OPMODE_CLIENT) {
            return true;
        }
        return false;
    }

    /**
     * ModuleManager in ServerMode?
     * @return true if ModuleManager is in Server Mode.
     * @see ORPGConstants#OPMODE_SERVER
     */
    protected boolean isServer() {
        if (opMode == ORPGConstants.OPMODE_SERVER) {
            return true;
        }
        return false;
    }

    /**
     * Sets the ModuleManagers operational mode. This determines which module set will get loaded among other things.
     * @param mode Sets the operational mode of the ModuleManager
     * @see ORPGConstants#OPMODE_CLIENT
     * @see ORPGConstants#OPMODE_SERVER
     */
    protected void setMode(int mode) {
        switch(mode) {
            case ORPGConstants.OPMODE_CLIENT:
                {
                    opMode = ORPGConstants.OPMODE_CLIENT;
                    break;
                }
            case ORPGConstants.OPMODE_SERVER:
                {
                    opMode = ORPGConstants.OPMODE_SERVER;
                    break;
                }
            default:
                {
                    opMode = ORPGConstants.OPMODE_DEFAULT;
                }
        }
    }

    /**
     * Registers a network callback hook with the ModuleManager
     * @param network Reference to the NetworkMessageRelay object that will supply network services.
     */
    protected void registerNetwork(NetworkMessageRelay network) {
        networkHook = network;
    }

    /**
     * Registers a module loader with the ModuleManager
     * @param loader Reference to the ModuleLoader object that will supply module loading services.
     */
    protected void registerModuleLoader(ModuleLoader loader) {
        moduleLoader = loader;
    }

    /**
     * Registers a GUIManager with the ModuleManager
     * @param m Reference to the GUIManager object that will supply JPanel display service.
     */
    public void registerGUIManager(GUIManager m) {
        guiManager = m;
    }

    /**
     * Registers a GroupManager with the Module Manager
     * @param m Reference to the GroupManager object that will supply client grouping and role services
     */
    public void registerGroupManager(GroupManager m) {
        groupManager = m;
    }

    /**
     * Registers a RouteManager with the ModuleManager
     * @param m Reference to the RouteManager object that will supply message routing serivices.
     */
    public void registerRouteManager(RouteManager m) {
        routeManager = m;
    }

    /**
     * Called to initialize the ModuleManager. Does a sanity check to make sure the
     * Network hook and module loader are present then initializes the ModuleManager 
     * @throws openrpg2.common.core.engine.NoNetworkHookException Exception thrown if network hook doesn't exist (failed sanity check)
     */
    protected void initialize() throws NoNetworkHookException, NoModuleLoaderException, NoSuchModuleException {
        if (networkHook == null) {
            throw new NoNetworkHookException("ModuleManager initialization error: No network hook");
        }
        if (moduleLoader == null) {
            throw new NoModuleLoaderException("ModuleManager initialization error: No module loader");
        }
        init();
        setInitializedFlag(true);
    }

    /**
     * Internal method used to set the initialized flag
     * @param setting true if initialization is completed
     */
    private void setInitializedFlag(boolean setting) {
        initializedFlag = setting;
    }

    /**
     * ModuleManager initialized?
     * @return true if ModuleManger has finished initializing.
     */
    protected boolean isInitialized() {
        return initializedFlag;
    }

    /**
     * Initializes the ModuleManger to service client side modules.
     */
    private void init() throws NoSuchModuleException {
        moduleLoader.setModuleManagerCallback(this);
        moduleLoader.loadGroupModule(groupManager);
        moduleLoader.loadNetworkModule();
        moduleLoader.loadModules(this);
    }

    /**
     * sets the Default Message Handling Module for all unknown messagetypes
     */
    public void registerDefaultModule(NetworkedModule module) {
        defaultHandler = module;
        defaultHandler.register(this);
        defaultHandler.activate();
    }

    /**
      * Registers a module with this ModuleManager. 
      * @see ModuleLoader
      */
    public void registerModule(BaseModule m) {
        m.register(this);
        m.activate();
        moduleRegistry.registerModule(m);
    }

    /**
     * Locates the handler for a given message type
     * @param moduleName Type (ID) of message to be handled.
     * @throws openrpg2.common.core.engine.NoSuchModuleException Thrown if no handler is registered for the given message type.
     * @return NetworkedModule reference
     */
    public NetworkedModule getNetworkedModuleReference(String moduleName) throws NoSuchModuleException {
        NetworkedModule module = null;
        module = messageRegistry.getReference(moduleName);
        if (module == null) {
            module = messageInitRegistry.getReference(moduleName);
        }
        if (module == null) {
            throw new NoSuchModuleException("No handler registered for \"" + moduleName + "\" messages");
        }
        return module;
    }

    /**
     * Gets the default handler for unknown message types if present.
     * @throws openrpg2.common.core.engine.NoSuchModuleException Thrown if no default handler is registered.
     * @return default NetworkedModule reference
     */
    public NetworkedModule getDefaultModuleReference() throws NoSuchModuleException {
        NetworkedModule module = null;
        module = defaultHandler;
        if (module == null) {
            throw new NoSuchModuleException("No default module registered");
        }
        return module;
    }

    /**
     * Registers a specific message type with the given handler reference
     * Method to satisfy ModuleCommunicator interface
     * @param name Type (ID) of message to deregister
     * @param reference Reference to message handler module
     */
    public void registerMessageType(String name, NetworkedModule reference) {
        messageRegistry.register(name, reference);
    }

    /**
     * Registers a specific message type with the given handler reference as being required at initialization phase of a connection.
     * Method to satisfy ModuleCommunicator interface
     * @param name Type (ID) of message to register
     * @param reference Reference to message handler module
     */
    public void registerInitMessageType(String name, NetworkedModule reference) {
        messageInitRegistry.register(name, reference);
    }

    /**
     * Deregisters a message type from being serviced. 
     * Requires reference to prevent modules from deregistering other modules message handlers.
     * Method to satisfy ModuleCommunicator interface
     * @param name Type (ID) of message to deregister
     * @param reference Reference to message handler module
     */
    public void deregisterMessageType(String name, NetworkedModule reference) {
        try {
            NetworkedModule nm = messageRegistry.getReference(name);
            if (nm == reference) {
                messageRegistry.deregister(name);
            }
        } catch (NoSuchModuleException e) {
        }
    }

    /**
     * Deregisters a message type thats required at initialzation phase of a connection from being serviced. 
     * Requires reference to prevent modules from deregistering other modules message handlers.
     * Method to satisfy ModuleCommunicator interface
     * @param name Type (ID) of message to deregister
     * @param reference Reference to message handler module
     */
    public void deregisterInitMessageType(String name, NetworkedModule reference) {
        try {
            NetworkedModule nm = messageInitRegistry.getReference(name);
            if (nm == reference) {
                messageInitRegistry.deregister(name);
            }
        } catch (NoSuchModuleException e) {
        }
    }

    /**
     * Registers a graphical module component (JPanel Object) with the loaded GUIManager
     */
    public void registerGUIComponent(String moduleName, JPanel modulePanel) {
        guiManager.addModuleGUI(moduleName, modulePanel);
    }

    /**
     * Deregisters a graphical module component (JPanel Object) from the loaded GUIManager
     */
    public void deregisterGUIComponent(String moduleName) {
        guiManager.removeModuleGUI(moduleName);
    }

    /**
     * Send an ORPGMessage to the network subsystem
     * Method to satisfy ModuleCommunicator interface
     * @param msg ORPGMessage to send
     * @throws openrpg2.common.core.engine.ModuleCommunicationException Thrown if network is not available.
     */
    public void sendToNetwork(ORPGMessage msg) throws ModuleCommunicationException {
        if (msg.getDestination() == null) {
            log.warning("Message for module " + msg.getHandlerId() + " dropped due to missing address information.");
            return;
        }
        if (!isInitialized()) {
            throw new ModuleCommunicationException("Network Not Available");
        }
        routeManager.sendMessageToNetwork(msg);
    }

    public void deregisterOperationInterest(int operationIdentifier, BaseModule moduleReference) {
    }

    public void registerOperationInterest(int operationIdentifier, BaseModule moduleReference) {
    }

    public Object requestModuleData(String fromModule, String nameOfData) throws NoSuchModuleException, InvalidDataRequestException {
        if (!isInitialized()) {
            throw new NoSuchModuleException("ModuleManager not initialized. No modules available");
        }
        BaseModule m = moduleRegistry.getModule(fromModule);
        if (m == null) {
            throw new NoSuchModuleException("Module " + fromModule + " not registered with ModuleManager");
        }
        return m.getModuleData(nameOfData);
    }

    public Object requestModuleAction(String fromModule, String actionName, Object[] args) throws NoSuchModuleException, InvalidActionRequestException, IllegalArgumentException {
        if (!isInitialized()) {
            throw new NoSuchModuleException("ModuleManager not initialized. No modules available");
        }
        BaseModule m = moduleRegistry.getModule(fromModule);
        if (m == null) {
            throw new NoSuchModuleException("Module " + fromModule + " not registered with ModuleManager");
        }
        return m.performModuleAction(actionName, args);
    }
}
