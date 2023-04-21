package ces.coffice.webmail.qmail.manager;

import java.net.*;
import java.io.*;

public class Server {

    public static String password = "admin";

    public static String username = "admin";

    public static String shellHome = "";

    private int port = 8181;

    public Server() {
        try {
            this.readConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Server(String configFile) {
        try {
            this.readConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void listen() throws Exception {
        ServerSocket s = new ServerSocket(port);
        try {
            while (true) {
                Socket s2 = s.accept();
                try {
                    ResponseSocket socket = new ResponseSocket(s2);
                } catch (IOException ex) {
                    s2.close();
                }
            }
        } catch (Exception ex1) {
            ex1.printStackTrace();
        } finally {
            s.close();
        }
    }

    public static void main(String args[]) throws Exception {
        Server server = new Server();
        server.listen();
    }

    private void readConfig() throws Exception {
        java.util.Properties properties = new java.util.Properties();
        java.io.InputStream in = getClass().getResourceAsStream("/serverconfig");
        if (in == null) {
            Logger.debug("config file not found", "qmail.manager");
            return;
        }
        properties.load(in);
        if (in != null) in.close();
        in = null;
        try {
            if (properties.get("port") != null) port = Integer.parseInt(properties.get("port").toString());
        } catch (NumberFormatException ex) {
            Logger.debug("read port error", "qmail.manager");
        }
        if (properties.get("password") != null) {
            password = (String) properties.get("password");
        }
        if (properties.get("logfile") != null && properties.get("logvalue") != null) {
            try {
                Logger.InitLogger(Integer.parseInt(properties.get("logvalue").toString()), properties.get("logfile").toString());
            } catch (Exception ex1) {
                Logger.debug("read logconfig error", "qmail.manager");
            }
        }
        if (properties.get("allowshell") != null) {
            try {
                ShellExecute.allowShell = (properties.get("allowshell").toString());
            } catch (Exception ex1) {
                Logger.debug("read logconfig error", "qmail.manager");
            }
        }
        if (properties.get("username") != null) {
            try {
                username = (properties.get("username").toString());
            } catch (Exception ex1) {
                Logger.debug("read logconfig error", "qmail.manager");
            }
        }
        if (properties.get("shellHome") != null) {
            try {
                shellHome = (properties.get("shellHome").toString());
            } catch (Exception ex1) {
                Logger.debug("read logconfig error", "qmail.manager");
            }
        }
    }
}
