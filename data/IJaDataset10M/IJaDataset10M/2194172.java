package PRISM.VRW;

import java.io.*;
import java.net.*;
import java.util.*;
import PRISM.VRW.VRWClock;

/**
 * 
 * @author Mauro Dragone
 */
public class VRWTimeOutThread extends Thread {

    VRWClient m_VRWClient = null;

    public VRWTimeOutThread(VRWClient client) throws IOException {
        super("VRWTimeOutThread");
        m_VRWClient = client;
    }

    public void run() {
        while (true) {
            for (VRWRobot obj : m_VRWClient.m_mapRobots.values()) {
                long tmCurrent = VRWClock.getVRWClock().currentTimeMillis();
                if (obj.tmlastUpdateKnown > 0) {
                    if ((tmCurrent - obj.tmlastUpdateKnown) > 3000) {
                        System.out.println("Time out for " + obj.strAgentName);
                        m_VRWClient.m_mapRobots.remove(obj.strAgentName);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (Throwable t) {
                }
                ;
            }
        }
    }
}
