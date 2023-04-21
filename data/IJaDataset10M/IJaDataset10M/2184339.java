package pop3;

import java.net.*;
import java.io.*;
import java.util.*;
import Mail;
import GeneralSubs;

public class Start extends Thread {

    Pop3Users AllUsers;

    int intPort;

    public Start(int intPassedPort, Pop3Users PassedAllUsers) {
        intPort = intPassedPort;
        AllUsers = PassedAllUsers;
    }

    public void run() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(intPort);
        } catch (IOException e) {
            Mail.ErrorLog.println("Could not listen on port: " + intPort);
            System.exit(-1);
        }
        Mail.ErrorLog.println("pop3 server is ready on port " + intPort);
        while (true) {
            try {
                Mail.ErrorLog.println("pop3 is waiting for a connection on port " + intPort);
                clientSocket = serverSocket.accept();
                (new Process(clientSocket, AllUsers)).start();
            } catch (IOException e) {
                Mail.ErrorLog.println("Accept failed on: " + intPort);
            }
        }
    }
}
