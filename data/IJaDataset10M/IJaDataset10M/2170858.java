package edu.ucla.cs.rpc.multicast.test;

import java.net.*;
import java.io.*;
import edu.ucla.cs.rpc.multicast.util.token.*;

/**
 * Testing file for the {@link NetworkTokenManager} class. Creates an array of
 * NetworkTokenManagers and starts them in seperate threads. The ring ends after
 * a timeout value.
 * 
 * @author Philip Russell Chase Covello
 * 
 * @see NetworkTokenManager
 * 
 */
public class TokenTester {

    public static void main(String[] args) {
        try {
            int testSize = 10;
            NetworkTokenManager[] managers = new NetworkTokenManager[testSize];
            for (int i = 0; i < managers.length; i++) {
                managers[i] = new NetworkTokenManager(new InetSocketAddress("localhost", 0), i);
            }
            for (int i = 0; i < managers.length - 1; i++) {
                managers[i].setNextAddress(managers[i + 1].getAddress());
            }
            managers[managers.length - 1].setNextAddress(managers[0].getAddress());
            Thread[] threads = new Thread[testSize];
            managers[0].setToken(new Token());
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(managers[i]);
                threads[i].start();
            }
            int numSecond = 20;
            Thread.sleep(numSecond * 1000);
            for (int i = 0; i < managers.length; i++) {
                managers[i].shutdown();
            }
            for (int i = 0; i < managers.length; i++) {
                if (managers[i].hasToken()) {
                    System.out.println("Manager #" + i + " has the token");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
