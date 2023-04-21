package netdev.net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import netdev.base.Configurable;
import netdev.base.Medium;
import netdev.base.Stack;
import netdev.base.Station;
import netdev.base.VirtualTimeEngine;
import netdev.gui.NetdevClient;
import netdev.net.ProtocolMessage;

public class StationConnectionHandler extends Thread {

    private Station station;

    private Socket clientSock;

    private ObjectInputStream ois;

    private ObjectOutputStream oos;

    private LinkedList<Stack> sendingStacks;

    public StationConnectionHandler(Station st) {
        station = st;
        sendingStacks = new LinkedList<Stack>();
        try {
            clientSock = new Socket(NetdevClient.getServerHost(), NetdevClient.getServerPort());
            oos = new ObjectOutputStream(clientSock.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(clientSock.getInputStream());
            start();
        } catch (Exception e) {
            NetdevClient.log("Unable to connect to : " + NetdevClient.getServerHost() + ":" + NetdevClient.getServerPort() + ", " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        ProtocolMessage p = new ProtocolMessage();
        p.setCommand(ProtocolMessage.STATION_DECLARE);
        p.addArg(new Configurable(st));
        send(p);
    }

    /**
	 * This method is used by stacks to send byte over medium
	 * @param medium
	 * @param stack
	 * @param b
	 * @return
	 */
    public boolean sendByte(Medium medium, Stack stack, Byte b) {
        synchronized (this) {
            sendingStacks.add(stack);
        }
        ProtocolMessage p = new ProtocolMessage();
        p.setCommand(ProtocolMessage.STATION_DATA_FROM_STATION);
        p.addArg(new Configurable(medium));
        p.addArg(b);
        send(p);
        long kbps = Long.valueOf(medium.getProperty("bandwidth"));
        long bytetimeinterval = (8 / kbps) * Math.round(VirtualTimeEngine.getVirtualTimeUnit());
        NetdevClient.getTimeManager().waitFor(bytetimeinterval);
        synchronized (this) {
            sendingStacks.remove(stack);
        }
        return true;
    }

    /**
	 * This method is used by the stack constructors to declare their link with the medium
	 */
    public void addStack(Stack st) {
        ProtocolMessage p = new ProtocolMessage();
        p.setCommand(ProtocolMessage.STATION_REG_STACK);
        p.addArg(new Configurable(st.getMedium()));
        send(p);
    }

    /**
	 * This method is used by the stack destructors to declare their unlink with the medium
	 */
    public void removeStack(Stack st) {
        ProtocolMessage p = new ProtocolMessage();
        p.setCommand(ProtocolMessage.STATION_UNREG_STACK);
        p.addArg(new Configurable(st.getMedium()));
        send(p);
    }

    /**
	 * Method used to send protocol object to the server
	 */
    private void send(ProtocolMessage p) {
        try {
            oos.writeObject(p);
            oos.flush();
            NetdevClient.log("STATION Sent : " + p.getCommand());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleProtocolMessage(ProtocolMessage p) {
        String cmd = p.getCommand();
        NetdevClient.log("Received at: " + NetdevClient.getTimeManager().getVirtualTime() + ", command: " + cmd);
        if (cmd.equals(ProtocolMessage.STATION_DATA_TO_STATION)) {
            Iterator<Object> it = p.enumerateArgs();
            Medium m = new Medium();
            m.copyProperties((Configurable) it.next());
            String mid = m.getProperty("id");
            Byte b = (Byte) it.next();
            Iterator<Stack> it2 = station.enumerateStacks().iterator();
            while (it2.hasNext()) {
                Stack st = it2.next();
                if (st.getMedium().getProperty("id").equals(mid)) {
                    st.receiveByte(b);
                }
            }
        }
    }

    /**
	 * Thread main entrance to proceed message from the server
	 */
    public void run() {
        ProtocolMessage current;
        for (; ; ) {
            try {
                current = (ProtocolMessage) ois.readObject();
                handleProtocolMessage(current);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
