package org.pfyshnet.com;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.pfyshnet.bc_codec.PfyshNodePrivateKeys;
import org.pfyshnet.core.MyNodeInfo;

public class PfyshServer implements Runnable {

    private MyNodeInfo MyNodeInfo;

    private ServerSocket Server;

    private PfyshComSettings Settings;

    private boolean Run;

    private PfyshCom Com;

    private SecureRandom Random;

    public PfyshServer(PfyshCom com, SecureRandom rand) {
        Run = false;
        Com = com;
        Random = rand;
    }

    public void Stop() {
        Run = false;
    }

    public void setMyNodeInfo(MyNodeInfo data) {
        MyNodeInfo = data;
        if (!(MyNodeInfo.getPrivateKey() instanceof PfyshNodePrivateKeys)) {
            throw new RuntimeException("Expecting keys to be set by now!");
        }
        if (!Run) {
            Settings = (PfyshComSettings) Com.getCore().getComSettings();
            int port = Settings.getPort();
            if (port == 0) {
                String locstr = data.getNode().getConnectionLocation();
                if (locstr != null) {
                    Matcher m = Pattern.compile(";(\\d+)").matcher(locstr);
                    if (m.find()) {
                        port = Integer.valueOf(m.group(1));
                    }
                }
            }
            if (port == 0) {
                boolean found = false;
                int trys = 10000;
                while (!found && trys > 0) {
                    port = 49152 + (int) (16383D * Random.nextDouble());
                    try {
                        Server = new ServerSocket(port);
                        found = true;
                    } catch (Exception e) {
                    }
                    trys--;
                }
                if (!found) {
                    throw new RuntimeException("Unable to find an ephemeral port to open.");
                }
            } else {
                int trys = 20;
                boolean connected = false;
                while (trys > 0 && !connected) {
                    try {
                        Server = new ServerSocket(port);
                        connected = true;
                    } catch (Exception e) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    trys--;
                }
                if (!connected) {
                    throw new RuntimeException("Unable to bind to specified port: " + Settings.getPort());
                }
            }
            if (Settings.getAddress() == null) {
                if (MyNodeInfo.getNode().getConnectionLocation() == null) {
                    MyNodeInfo.getNode().setConnectionLocation(";" + Server.getLocalPort());
                } else {
                    Pattern p = Pattern.compile(";(\\d+)");
                    Matcher m = p.matcher(MyNodeInfo.getNode().getConnectionLocation());
                    if (!m.find()) {
                        MyNodeInfo.getNode().setConnectionLocation(";" + Server.getLocalPort());
                    } else {
                        MyNodeInfo.getNode().setConnectionLocation(m.replaceAll(";" + Server.getLocalPort()));
                    }
                }
            } else {
                MyNodeInfo.getNode().setConnectionLocation(Settings.getAddress() + ";" + Server.getLocalPort());
            }
            Run = true;
            Thread t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        while (Run) {
            try {
                Socket s = Server.accept();
                Com.addIncoming(s);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Server.close();
                } catch (Exception e1) {
                }
                boolean connected = false;
                while (Run && !connected) {
                    try {
                        Server = new ServerSocket(Settings.getPort());
                        connected = true;
                    } catch (Exception e2) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (!connected) {
                    throw new RuntimeException("Unable to bind to specified port: " + Settings.getPort());
                }
            }
        }
    }
}
