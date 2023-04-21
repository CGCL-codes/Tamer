package hambo.mobiledirectory.cronjobs;

import java.util.StringTokenizer;
import java.sql.*;
import java.io.*;

public class ServerDump {

    private Connection con;

    private DatabaseTool db;

    private String path;

    private String dbType = "SYBASE";

    /**
     * Private constructor: this should only be called from main()
     * most importantly, opens the db connection
     **/
    private ServerDump(String conffile, String path) {
        this.path = path;
        String server = "";
        String port = "";
        String dbname = "";
        String login = "";
        String password = "";
        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new FileReader(conffile));
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    StringTokenizer st = new StringTokenizer(line, "|");
                    String cmd = "";
                    if (st.hasMoreTokens()) cmd = st.nextToken();
                    if (cmd.trim().equals("SERVER")) server = st.nextToken(); else if (cmd.trim().equals("PORT")) port = st.nextToken(); else if (cmd.trim().equals("PASSWORD")) password = st.nextToken(); else if (cmd.trim().equals("LOGIN")) login = st.nextToken(); else if (cmd.trim().equals("DB")) dbname = st.nextToken(); else if (cmd.trim().equals("DBTYPE")) dbType = st.nextToken();
                }
            }
            reader.close();
        } catch (Exception exc) {
            System.err.println("Error file " + conffile + " not found!");
        }
        db = new DatabaseTool();
        con = db.connect(server, port, dbname, login, password);
    }

    private void dump() {
        try {
            FullDumper fullDumper = new FullDumper(db, con, path + "/", dbType);
            fullDumper.dumpAll();
            PatchDumper.dump(path + "/", dbType);
        } catch (Exception e) {
        }
    }

    /**
     * Should be called with:
     *
     *  1- configuration file (the same cron.conf file than the main
     *  cronjob)
     *
     *  2- path for the dumps
     **/
    public static void main(String argh[]) {
        ServerDump sd = new ServerDump(argh[0], argh[1]);
        sd.dump();
    }
}
