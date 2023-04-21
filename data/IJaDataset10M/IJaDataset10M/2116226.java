package com.madinatek.designproject.client;

import java.io.*;
import ch.ethz.ssh2.*;

public class FileTransfer {

    public static void SendFile(String hostname, String fileName, String dest) throws Exception {
        String username = "abdulka";
        String password = "992491362";
        Connection conn = new Connection(hostname);
        conn.connect();
        boolean isAuthenticated = conn.authenticateWithPassword(username, password);
        if (!isAuthenticated) {
            System.err.println("Cannot authenticate");
            System.exit(-1);
        }
        SCPClient scp = new SCPClient(conn);
        scp.put(fileName, dest);
        conn.close();
    }
}
