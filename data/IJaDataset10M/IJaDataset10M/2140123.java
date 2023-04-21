package com.shoru.tom.gui;

import java.io.IOException;

public class getIP implements Runnable {

    private final String osName = System.getProperty("os.name");

    public String Iface;

    public boolean finished = false;

    public void run() {
        try {
            getIPAddress();
        } catch (Exception e) {
        }
    }

    public void getIPAddress() throws IOException, InterruptedException {
        if (osName.toLowerCase().startsWith("windows")) {
            String[] cmd = new String[] { "net stop winhttpautoproxysvc /y", "net stop msexchagesa /y", "net stop dhcp", "net start dhcp" };
            System.out.println("net stop winhttpautoproxysvc /y");
            Runtime.getRuntime().exec(cmd[0]).waitFor();
            System.out.println("net stop msexchagesa /y");
            Runtime.getRuntime().exec(cmd[1]).waitFor();
            System.out.println("net stop dhcp");
            Runtime.getRuntime().exec(cmd[2]).waitFor();
            System.out.println("net start dhcp");
            Runtime.getRuntime().exec(cmd[3]).waitFor();
        } else if (osName.toLowerCase().startsWith("linux")) {
            System.out.println("killall dhclient");
            Runtime.getRuntime().exec("killall dhclient").waitFor();
            System.out.println("dhclient " + Iface);
            Runtime.getRuntime().exec("dhclient " + Iface).waitFor();
        } else if (osName.toLowerCase().startsWith("freebsd")) {
            System.out.println("killall dhclient");
            Runtime.getRuntime().exec("killall dhclient").waitFor();
            System.out.println("dhclient " + Iface);
            Runtime.getRuntime().exec("dhclient " + Iface).waitFor();
        } else if (osName.toLowerCase().startsWith("mac os x")) {
            System.out.println("ipconfig set " + Iface + " DHCP");
            Runtime.getRuntime().exec("ipconfig set " + Iface + " DHCP").waitFor();
        } else {
            System.out.println("Not supported OS...");
            return;
        }
        System.out.println("Ip refreshed...");
        finished = true;
    }
}
