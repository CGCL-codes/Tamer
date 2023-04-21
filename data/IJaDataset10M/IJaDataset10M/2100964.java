package de.knup.jedi.jayshare;

import de.knup.jedi.jayshare.*;
import de.knup.jedi.jayshare.sax.*;
import de.knup.jedi.jayshare.FileTransfer.*;
import java.io.*;
import java.net.UnknownHostException;
import org.jabber.jabberbeans.*;
import java.net.*;

public class Main {

    static class JTestConnectionListener implements ConnectionListener {

        public void connected(ConnectionEvent ce) {
            System.out.println("$CL$: Connected!");
        }

        public void disconnected(ConnectionEvent ce) {
            System.out.println("$CL$: Disconnected");
        }

        public void connecting(ConnectionEvent ce) {
            System.out.println("$CL$: Connecting...");
        }

        public void connectFailed(ConnectionEvent ce) {
            System.out.println("$CL$: Connect Failed");
        }

        public void connectionChanged(ConnectionEvent ce) {
            System.out.println("$CL$: Connect changed!");
        }
    }

    static class JTestPacketListener implements PacketListener {

        public void receivedPacket(PacketEvent pe) {
            System.out.println(">>> " + pe.getPacket().toString());
        }

        public void sentPacket(PacketEvent pe) {
            System.out.println("<<< " + pe.getPacket().toString());
        }

        public void sendFailed(PacketEvent pe) {
            System.out.println("Failed <<< " + pe.getPacket().toString());
        }
    }

    public static void main(String[] args) {
        if (args.length < 10) {
            System.out.println("usage:\njava Main server confserver username password port userlist.xml dlqueue.xml thishost thisport exportpath");
            return;
        }
        String server = args[0];
        String confserver = args[1];
        String user = args[2];
        String passwd = args[3];
        int port = Integer.valueOf(args[4]).intValue();
        String room = "jayshare";
        UserList list = FileParser.parseFileList(args[5]);
        DownloadQueue queue = FileParser.parseQueue(args[6]);
        String exportHost = args[7];
        String exportProto = "http";
        int exportPort = Integer.parseInt(args[8]);
        String exportRoot = args[9];
        HTTPServer httpServer;
        try {
            httpServer = new HTTPServer(InetAddress.getByName(exportHost), exportPort, exportRoot);
        } catch (UnknownHostException e) {
            System.out.println("Failed: " + e);
            return;
        }
        httpServer.start();
        Status status = new Status(user, server, passwd, confserver, room, user, port);
        status.setUserList(list);
        status.setDownloadQueue(queue);
        status.setExportServer(httpServer);
        status.setExport(exportProto, exportHost, exportPort);
        status.setDownloadDir("/tmp");
        DownloadQueueManager queueMan = new DownloadQueueManager(status);
        Connection conn = new Connection(status);
        conn.addPacketListener(new JTestPacketListener());
        try {
            conn.connect();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e);
            return;
        } catch (IOException e) {
            System.err.println("I/O Error: " + e);
            return;
        } catch (NoUserListException e) {
            System.err.println("No userlist: " + e);
            return;
        }
        queueMan.start();
        return;
    }
}
