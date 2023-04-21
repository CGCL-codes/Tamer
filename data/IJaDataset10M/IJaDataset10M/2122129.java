package netMan;

import gateway.shellManager;
import java.net.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: subasui
 * Date: 21.05.2006
 * Time: 19:43:36
 * To change this template use File | Settings | File Templates.
 */
public class netMan {

    protected String HwAddress;

    protected String IpAddress;

    protected String DHCPAddress;

    protected shellManager shellManager;

    protected NetworkInterface networkInterface;

    protected ArrayList networkCards;

    public netMan() {
        shellManager = new shellManager();
        networkCards = readNetCards();
    }

    /**
     * gets the Host IpAddress
     * @return IpAddress
     */
    public String getIpAddress() {
        return IpAddress;
    }

    /**
     * Gets the DHCP server address
     * @return DHCP server address
     */
    public String getDhcpAddres() {
        return DHCPAddress;
    }

    /**
     * returns all the networks cads on the system as array
     * @return networks cads on the system as array
     */
    public ArrayList getNetwokCards() {
        return networkCards;
    }

    public String getHwAddress() {
        return HwAddress;
    }

    public ArrayList readNetCards() {
        ArrayList netCards = new ArrayList();
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                String[] netDetail = { "", "", "" };
                networkInterface = (NetworkInterface) enumeration.nextElement();
                netDetail[0] = networkInterface.getName();
                netDetail[1] = networkInterface.getDisplayName();
                try {
                    Enumeration inetaddreses = networkInterface.getInetAddresses();
                    Inet4Address internetAdd = (Inet4Address) inetaddreses.nextElement();
                    netDetail[2] = internetAdd.getHostAddress();
                } catch (Exception e) {
                    netDetail[2] = "127.0.0.1";
                }
                netCards.add(netDetail);
                netDetail = (String[]) netDetail.clone();
            }
        } catch (SocketException e) {
            System.out.println(e);
        }
        return netCards;
    }

    public void parseIpconfig() {
        int IpLine = 0;
        DHCPAddress = "";
        HwAddress = "";
        boolean test = false;
        String[] ipConfLine;
        String[] ipconfElement;
        String ipconfig = shellManager.sendCommand("ipconfig /all");
        ipConfLine = ipconfig.split("\n");
        for (int i = 0; i < ipConfLine.length; i++) {
            ipconfElement = ipConfLine[i].split(" ");
            for (int j = 0; j < ipconfElement.length; j++) {
                if (ipconfElement[j].trim().equals(IpAddress)) {
                    IpLine = i;
                    test = true;
                }
                if (test) {
                    if (ipconfElement[j].equals("DHCP")) {
                        while (!ipconfElement[j].startsWith("192.168")) {
                            j++;
                        }
                        DHCPAddress = ipconfElement[j].trim();
                        break;
                    }
                }
            }
        }
        for (int k = IpLine; k > 0; k--) {
            ipconfElement = ipConfLine[k].split(" ");
            for (int l = 0; l < ipconfElement.length; l++) {
                if (ipconfElement[l].trim().startsWith("Physical")) {
                    HwAddress = ipconfElement[ipconfElement.length - 1].trim().replace('-', ':');
                    break;
                }
            }
        }
    }
}
