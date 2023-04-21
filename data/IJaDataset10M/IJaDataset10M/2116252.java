package ow.ipmulticast.igmpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.util.Set;
import ow.ipmulticast.Group;
import ow.ipmulticast.VirtualInterface;

public class IGMPDaemonTest {

    public static final void main(String[] args) {
        IGMPDaemonConfiguration config = new IGMPDaemonConfiguration();
        IGMPDaemon igmpd = null;
        try {
            igmpd = new IGMPDaemon(config);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        igmpd.start(new ACallback());
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = null;
            try {
                line = in.readLine();
            } catch (IOException e) {
                break;
            }
            if (line.length() > 0) break;
            igmpd.printStatus(System.out);
        }
    }

    private static class ACallback implements GroupChangeCallback {

        public void igmpMessageReceived(Inet4Address src, Inet4Address dest, int type, int code, Inet4Address groupAddress, byte[] data, VirtualInterface vif) {
            System.out.println("IGMP message received.");
        }

        public void included(Set<Group> includedGroupSet, VirtualInterface vif) {
            StringBuilder sb = new StringBuilder();
            sb.append("included:");
            for (Group g : includedGroupSet) {
                sb.append(" ");
                sb.append(g.getGroupAddress());
            }
            System.out.println(sb);
        }

        public void excluded(Set<Group> excludedGroupSet, VirtualInterface vif) {
            StringBuilder sb = new StringBuilder();
            sb.append("excluded:");
            for (Group g : excludedGroupSet) {
                sb.append(" ");
                sb.append(g.getGroupAddress());
            }
            System.out.println(sb);
        }
    }
}
