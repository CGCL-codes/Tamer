package base.szerver;

import dc.Hubs;
import dc.szerver;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import net.RegTmout;

/**
 *
 * @author xmister
 */
public class NMDC {

    private Request req;

    private DataInputStream in;

    private DataOutputStream out;

    private RegTmout timer;

    private szerver owner;

    private Socket socket;

    private String host;

    private InetAddress bycim;

    public NMDC(Request req, szerver owner, Socket sock, DataInputStream bejovo, DataOutputStream kimeno) throws IOException {
        this.req = req;
        in = bejovo;
        out = kimeno;
        timer = new RegTmout(30, owner, sock);
        this.owner = owner;
        this.socket = sock;
    }

    private synchronized void send(String s) {
        if (out != null) try {
            out.writeBytes(s);
            out.flush();
        } catch (IOException e) {
            owner.removeReq(socket);
            return;
        }
    }

    private void sendLock() {
        send("$Lock KiszamolandoABCABCABC Pk=jexhls|\r\n");
    }

    public Hubs parse(String fullmsg) {
        if (fullmsg.indexOf("$Key") > -1) fullmsg = fullmsg.substring(fullmsg.indexOf("|") + 1);
        StringTokenizer com = new StringTokenizer(fullmsg, "|");
        String[] lines = new String[6];
        int i = 0;
        Hubs newhub = null;
        if (com.countTokens() < 5) {
            owner.removeReq(socket);
            return null;
        } else {
            if (com.countTokens() > 5) com.nextToken();
            while (com.hasMoreTokens()) {
                String cur = com.nextToken();
                if (cur.indexOf("$Key") == -1) {
                    lines[i] = cur;
                    i++;
                }
            }
            if (lines[4] == null) lines[4] = "0";
            if (lines[0] != null) try {
                boolean hiba = false;
                lines[1] = lines[1].replace("dchub://", "");
                boolean banned = owner.bans.containsKey(lines[1].toLowerCase());
                if (banned) {
                    System.out.println("Address banned: " + lines[1]);
                } else {
                    int port = 411;
                    if (lines[1].lastIndexOf(":") > -1) {
                        while (lines[1].lastIndexOf(":") > -1) {
                            host = lines[1].substring(0, lines[1].lastIndexOf(":"));
                            try {
                                port = Integer.valueOf(lines[1].substring(lines[1].lastIndexOf(":") + 1).trim());
                            } catch (Exception e) {
                                hiba = true;
                            }
                            lines[1] = host;
                        }
                        lines[1] = host + ":" + port;
                    } else {
                        host = lines[1];
                    }
                    if (!banned) banned = owner.bans.containsKey(host.toLowerCase());
                    try {
                        bycim = InetAddress.getByName(host);
                        host = bycim.getHostAddress().toString();
                        if (!banned) banned = owner.bans.containsKey(host);
                    } catch (Exception e) {
                        host = "0.0.0.0";
                    }
                    if (host.equals("127.0.0.1") || host.equals("0.0.0.0") || hiba || banned) {
                        if (banned) System.out.println("Hub banned: " + lines[1]); else System.out.println("Wrong address: " + lines[1]);
                    } else {
                        String s = null;
                        try {
                            while (owner.updating) wait(10000);
                            newhub = new Hubs(lines[0], lines[1].trim(), lines[2], lines[3], lines[4], socket.getInetAddress().getHostAddress());
                        } catch (Exception e) {
                            owner.query.log("NMDC Reg error: " + "\n" + lines[0] + "\n" + lines[1] + "\n" + lines[2] + "\n" + lines[3] + "\n" + lines[4]);
                            PrintWriter writer = new PrintWriter(new FileWriter(owner.conf.ConfDir + "/error_" + System.currentTimeMillis() / 1000 + ".txt"));
                            writer.println("[" + owner.query.GetDateTime() + "] " + s);
                            e.printStackTrace(writer);
                            writer.flush();
                            writer.close();
                            owner.query.log(e.toString());
                            System.out.println("Reg error");
                        }
                    }
                }
            } catch (Exception e) {
                owner.query.log("NMDC Reg error(writing)");
                e.printStackTrace();
            }
        }
        return newhub;
    }

    public synchronized Hubs delegate() throws IOException {
        sendLock();
        send(req.getStat() + "\r\n");
        req.shutdownOutputSocket();
        timer.start();
        int cur;
        char currchar;
        String fullmsg = "";
        Hubs newhub = null;
        try {
            while ((cur = in.read()) != -1) {
                currchar = (char) cur;
                fullmsg += currchar;
            }
            timer.stopped = true;
            newhub = parse(fullmsg);
        } catch (IOException e) {
            owner.removeReq(socket);
            return null;
        } catch (Exception e) {
            System.out.println("NMDC delegate error!");
            e.printStackTrace();
        }
        if (in != null) in.close();
        if (out != null) out.close();
        in = null;
        out = null;
        return newhub;
    }

    public void finalize() throws IOException, Throwable {
        if (timer != null) timer.stopped = true;
        if (in != null) in.close();
        if (out != null) out.close();
        in = null;
        out = null;
        timer = null;
        super.finalize();
    }
}
