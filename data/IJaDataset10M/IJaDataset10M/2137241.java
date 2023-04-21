package jhat_server;

import java.io.*;

public class Configuration {

    static String version = "0.1.2";

    static int port = 3333;

    static String hostname = "Jhat-Server";

    static int dataBaseEngine = 0;

    static String logFile = "";

    static String iconfile = "";

    static final int DBE_dummy = 0;

    static final int DBE_sql = 1;

    static void loadConfig(String filename) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = file.readLine()) != null) {
                if (line.indexOf("<hostname>") == 0) {
                    hostname = line.substring(10);
                }
                if (line.indexOf("<port>") == 0) {
                    port = Integer.parseInt(line.substring(6));
                }
                if (line.indexOf("<logfile>") == 0) {
                    logFile = line.substring(9);
                }
                if (line.indexOf("<icons>") == 0) {
                    iconfile = line.substring(7);
                }
                if (line.indexOf("<dbengine>") == 0) {
                    if (line.substring(10).equals("dummy")) dataBaseEngine = DBE_dummy;
                    if (line.substring(10).equals("sql")) dataBaseEngine = DBE_sql;
                }
            }
        } catch (FileNotFoundException e) {
            Log.message("configuration-file not found: " + filename);
        } catch (IOException e) {
            Log.message(e.toString());
        }
    }
}
