package ostf.test.client.telnet;

import java.util.HashMap;
import ostf.test.client.TestAction;
import ostf.test.client.TestActionException;
import ostf.test.thread.TestThread;
import ostf.test.unit.ActionConfig;

public class TelnetActionConfig extends ActionConfig {

    public static final String TELNET_SOCKET_TIMEOUT = "telnet.socket.timeout";

    public static final String TELNET_TCP_NODELAY = "telnet.socket.tcpnodelay";

    public static final String TELNET_SOCKET_LINGER = "telnet.socket.linger";

    public static final String TELNET_READ_ENDFORMAT = "telnet.read.endformat";

    protected String command = null;

    protected HashMap<String, String> telnetParams = null;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public synchronized HashMap<String, String> getTelnetParams(TestThread thread) {
        if (telnetParams == null) {
            telnetParams = new HashMap<String, String>();
            String property = (String) thread.getTestProperty(TELNET_SOCKET_TIMEOUT);
            if (property != null) telnetParams.put(TELNET_SOCKET_TIMEOUT, property);
            property = (String) thread.getTestProperty(TELNET_SOCKET_LINGER);
            if (property != null) telnetParams.put(TELNET_SOCKET_LINGER, property);
            property = (String) thread.getTestProperty(TELNET_TCP_NODELAY);
            if (property != null) telnetParams.put(TELNET_TCP_NODELAY, property);
            property = (String) thread.getTestProperty(TELNET_READ_ENDFORMAT);
            if (property != null) telnetParams.put(TELNET_READ_ENDFORMAT, property);
        }
        return telnetParams;
    }

    public TestAction createAction(TestThread thread) throws TestActionException {
        return new TelnetAction(this, thread);
    }
}
