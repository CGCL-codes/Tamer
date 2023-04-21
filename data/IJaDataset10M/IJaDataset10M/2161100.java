package server;

import java.net.*;
import java.io.*;

public class IServer {

    public static OHeap oHeap = new OHeap();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }
        System.out.println("Server Start");
        while (listening) new ObjectServer(serverSocket.accept()).start();
        serverSocket.close();
    }

    public static String genRandomString() {
        double randomNumber;
        double randomNumberSetup;
        char randomCharacter;
        StringBuffer returnBuffer = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            randomNumber = Math.random();
            randomNumberSetup = (randomNumber * 26 + 'a');
            randomCharacter = (char) randomNumberSetup;
            returnBuffer.append(randomCharacter);
        }
        return returnBuffer.toString();
    }
}
