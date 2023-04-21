package com.google.code.executer.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    private static final int DEFAULT_PORT = 8090;

    public static void main(String[] args) throws IOException {
        String command = args[0];
        String host = args[1];
        int port = DEFAULT_PORT;
        if (args.length > 2) {
            port = Integer.parseInt(args[2]);
        }
        Socket server = new Socket(host, port);
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
            try {
                writer.write(command + "\n");
                writer.flush();
            } finally {
                writer.close();
            }
        } finally {
            server.close();
        }
        System.exit(0);
    }
}
