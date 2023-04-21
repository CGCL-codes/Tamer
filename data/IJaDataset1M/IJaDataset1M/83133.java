package trader.nw;

import java.io.*;
import java.net.*;

public class TestNwClient {

    public static void main(String args[]) {
        int port = 5280;
        int count = 0;
        Object obj = null;
        NwClient nwc;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.out.println("TestNwClient " + e);
            }
        }
        nwc = new NwClient("localhost", port);
        try {
            while (true) {
                obj = "message" + count++;
                try {
                    nwc.send(obj);
                } catch (Exception e) {
                    System.out.println("TestNwClient " + e);
                }
                obj = null;
                try {
                    obj = nwc.receive();
                    System.out.println("TestNwClient received " + obj);
                } catch (Exception e) {
                    System.out.println("TestNwClient receive " + e);
                }
                Thread.sleep(4000);
            }
        } catch (Exception e) {
            System.out.println("TestNwClient: " + e);
        }
    }
}
