package com.quikj.server.log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.quikj.server.framework.AceCommandSession;

/**
 * 
 * @author amit
 */
public class LogShutdownCommandHandler implements com.quikj.server.framework.AceCommandHandlerInterface {

    private byte[] selfAddress;

    private byte[] localHostAddress;

    /** Creates a new instance of LogShutdownCommandHandler */
    public LogShutdownCommandHandler() throws UnknownHostException {
        selfAddress = InetAddress.getLocalHost().getAddress();
        localHostAddress = InetAddress.getByName("127.0.0.1").getAddress();
    }

    public void actionPerformed(String command_line, AceCommandSession session) {
        byte[] from = session.getSocket().getInetAddress().getAddress();
        if (compareBytes(from, selfAddress) == false) {
            if (compareBytes(from, localHostAddress) == false) {
                session.sendMessage("You are not allowed to issue shutdown command from your terminal\n");
                return;
            }
        }
        session.sendMessage("Shutdown Initiated. Your session will be closed, shortly.\n");
        ShutdownHandlerThread thread = new ShutdownHandlerThread();
        thread.start();
    }

    private boolean compareBytes(byte[] b1, byte[] b2) {
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) return false;
        }
        return true;
    }

    public String usage() {
        return "shutdown - command to shutdown the Ace Log Server";
    }
}
