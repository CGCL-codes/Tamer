package org.bing.engine.controller.helper;

import java.io.IOException;
import java.net.ServerSocket;

public class PortHelper {

    public static boolean isAlive(String port) {
        return isAlive(Integer.parseInt(port));
    }

    public static boolean isAlive(int port) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    ss = null;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(PortHelper.isAlive(1688));
    }
}
