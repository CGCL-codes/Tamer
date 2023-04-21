package jc168063;

import assign1.*;
import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    /**
     * This is the function ClientHandler
     * this function opens a socket for the client handler
     * @param connectSock
     * @param clientNum
     */
    private WaltzClient client;

    private BufferedInputStream inBuf;

    private DataInputStream inStream;

    private double[] spielDoubles;

    public ClientHandler(Socket connectSock, WaltzClient clientNum) {
        try {
            client = clientNum;
            Socket cSock = connectSock;
            inStream = new DataInputStream(cSock.getInputStream());
        } catch (IOException ex) {
            client.appendToDisplay("Error Creating Input Stream");
            ex.printStackTrace();
        }
    }

    public void run() {
        createArray();
        client.appendToDisplay("Playing");
        StdAudio.play(spielDoubles);
        client.appendToDisplay("Finished playing");
    }

    public void createArray() {
        try {
            int arraySize = inStream.readInt();
            spielDoubles = new double[arraySize];
            for (int i = 0; i < arraySize; i++) {
                if (i % 100000 == 0) client.appendToDisplay("Received: " + i + " out of " + arraySize + " elements in the double[]");
                if (inStream.available() != 0) spielDoubles[i] = inStream.readDouble();
            }
            client.appendToDisplay("Received All");
        } catch (IOException ex) {
            client.appendToDisplay("Array Error");
        }
    }
}
