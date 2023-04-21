package aie.agent;

import aie.common.*;
import aie.server.*;
import java.io.*;
import java.net.*;

/**
 * An agent modeled after the real-world Wolf.
 */
public class Wolf extends Agent {

    private static final byte[] protocol = { 1, 6 };

    public static final byte[] sid = { 0, 1 };

    public static final byte[] ver = { 1, 7 };

    protected static DataInputStream in;

    protected static DataOutputStream out;

    public static void main(String[] args) {
        if (args.length < 4 || args.length > 5) {
            System.out.println("Usage: java ../.. aie.agent.Wolf " + "<username> <password> <ip of server> <port> [-debug]");
        } else {
            boolean debug_enabled = false;
            if (args.length == 5 && args[4].equalsIgnoreCase("-debug")) {
                debug_enabled = true;
            }
            try {
                Socket socket = new Socket(args[2], Integer.parseInt(args[3]));
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                out.write(aie.server.Authenticator.mgcnum);
                out.write(protocol);
                byte[] b;
                b = new byte[aie.server.Authenticator.cmd_accept.length];
                in.read(b);
                if (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_accept)) {
                    System.out.println("Magic number not accepted.");
                    System.exit(1);
                }
                if (debug_enabled) {
                    System.out.println("Magic number accepted.");
                }
                b = new byte[aie.server.Authenticator.srvr_wlcm.length];
                in.read(b);
                while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.srvr_wlcm)) {
                    in.read(b);
                }
                b = new byte[in.readInt()];
                in.read(b);
                System.out.println(new String(b));
                b = new byte[1];
                in.read(b);
                while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_user)) {
                    in.read(b);
                }
                if (debug_enabled) {
                    System.out.println("sending username: " + args[0]);
                }
                out.write(aie.server.Authenticator.cmd_user);
                b = args[0].getBytes();
                out.writeInt(b.length);
                out.write(b);
                b = new byte[1];
                in.read(b);
                while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_pass)) {
                    in.read(b);
                }
                if (debug_enabled) {
                    System.out.println("sending password: " + args[1]);
                }
                out.write(aie.server.Authenticator.cmd_pass);
                b = args[1].getBytes();
                out.writeInt(b.length);
                out.write(b);
                b = new byte[1];
                in.read(b);
                while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.srvr_updchk)) {
                    in.read(b);
                }
                if (debug_enabled) {
                    System.out.println("recieved updchk, sending ready");
                }
                out.writeInt(2);
                out.write(Agent.sid);
                out.write(Agent.ver);
                out.write(sid);
                out.write(ver);
                for (int i = 0; i < in.readInt(); i++) {
                    b = new byte[2];
                    if (aie.server.Authenticator.verify(b, Agent.sid)) {
                        for (long j = 0; j < in.readLong(); j++) {
                        }
                    } else if (aie.server.Authenticator.verify(b, sid)) {
                        for (long j = 0; j < in.readLong(); j++) {
                        }
                    }
                }
                if (debug_enabled) {
                    System.out.println("all updates recieved successfully");
                }
                out.write(aie.server.Authenticator.cmd_ready);
                out.write(aie.server.Authenticator.cmd_ready);
                if (debug_enabled) {
                    System.out.println("connected!");
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public Wolf() {
    }

    public void receiveMessage(Message m) {
    }

    public void setComm(CommSystem c) {
    }
}
