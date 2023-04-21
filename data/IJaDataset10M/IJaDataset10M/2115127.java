package fedora.server.utilities.status;

public class ServerState {

    public static final ServerState NEW_SERVER = new ServerState("New Server");

    public static final ServerState NOT_STARTING = new ServerState("Not Starting");

    public static final ServerState STARTING = new ServerState("Starting");

    public static final ServerState STARTED = new ServerState("Started");

    public static final ServerState STARTUP_FAILED = new ServerState("Startup Failed");

    public static final ServerState STOPPING = new ServerState("Stopping");

    public static final ServerState STOPPED = new ServerState("Stopped");

    public static final ServerState STOPPED_WITH_ERR = new ServerState("Stopped with error");

    public static final ServerState[] STATES = new ServerState[] { NEW_SERVER, NOT_STARTING, STARTING, STARTED, STARTUP_FAILED, STOPPING, STOPPED, STOPPED_WITH_ERR };

    private String _name;

    private ServerState(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public String toString() {
        return _name;
    }

    public static ServerState fromString(String name) throws Exception {
        for (int i = 0; i < STATES.length; i++) {
            if (STATES[i].getName().equals(name)) {
                return STATES[i];
            }
        }
        throw new Exception("Unrecognized Server State: " + name);
    }
}
