package net.sourceforge.fsync.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import net.sourceforge.fsync.filesystem.compare.ComparisonStrategy;
import net.sourceforge.fsync.filesystem.compare.FolderComparator;
import net.sourceforge.fsync.portmanager.PortManagerFactory;
import net.sourceforge.fsync.properties.ConnectionProperties;

class OutgoingConnection {

    private Session s;

    private ConnectionProperties c;

    public OutgoingConnection(ConnectionProperties c) {
        super();
        this.c = c;
    }

    public final Session getSession() {
        return s;
    }

    public final ConnectionProperties getConnectionProperties() {
        return c;
    }

    public boolean isActive() {
        return (s != null && s.isActive());
    }

    public void createConnection(int port) {
        assert c.isActive() == false;
        Session session = new Session(false, c.getOtherInet());
        session.setPort(port);
        session.setRemoteAddress(c.getOtherInet());
        session.setName(c.getGroupID());
        session.setLocalFolder(c.getLocalFolder());
        session.setMessageChannel(new MessageChannel(session));
        session.setPortManager(PortManagerFactory.createRemote(session));
        session.setFolderComparator(new FolderComparator(session, ComparisonStrategy.NAMES_ONLY));
    }
}

public class OutgoingConnector implements Runnable {

    Collection<OutgoingConnection> allConnections = new ArrayList<OutgoingConnection>();

    private boolean run = true;

    public OutgoingConnector() {
    }

    public void addConnection(ConnectionProperties cp) {
        OutgoingConnection oc = new OutgoingConnection(cp);
        allConnections.add(oc);
    }

    public void run() {
        while (run) {
            boolean established = true;
            int port = -1;
            for (OutgoingConnection oc : allConnections) {
                if (oc.isActive()) {
                    continue;
                }
                try {
                    Socket skt = new Socket(oc.getConnectionProperties().getOtherInet(), oc.getConnectionProperties().getOtherPort());
                    DataOutputStream dos = new DataOutputStream(skt.getOutputStream());
                    dos.write(MagicNumber.CLIENTREQUEST);
                    dos.writeInt(oc.getConnectionProperties().getGroupID());
                    DataInputStream dis = new DataInputStream(skt.getInputStream());
                    for (int i = 0; i < MagicNumber.SERVERRESPONSE.length; ++i) {
                        if (dis.readByte() != MagicNumber.SERVERRESPONSE[i]) established = false;
                    }
                    if (established) {
                        port = dis.readInt();
                    }
                } catch (UnknownHostException e) {
                    established = false;
                    e.printStackTrace();
                } catch (IOException e) {
                    established = false;
                    e.printStackTrace();
                }
                if (established) {
                    oc.createConnection(port);
                }
            }
        }
    }
}
