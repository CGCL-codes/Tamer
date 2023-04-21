package org.asky78.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import lombok.Cleanup;

public class ChatClient {

    public static void main(String[] args) {
        ChatClient clientSocket = new ChatClient();
    }

    public ChatClient() {
        try {
            Socket socket = new Socket("localhost", 5432);
            Debuger.debug("클라이언트에서 접속 시도");
            @Cleanup OutputStream out = socket.getOutputStream();
            @Cleanup BufferedReader input = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            @Cleanup BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            String line = "";
            while ((line = input.readLine()) != null) {
                writer.write(line);
                writer.flush();
                Debuger.debug(line + "보냄");
            }
        } catch (UnknownHostException e) {
            Debuger.debug(e.getMessage());
        } catch (IOException e) {
            Debuger.debug(e.getMessage());
        }
    }
}
