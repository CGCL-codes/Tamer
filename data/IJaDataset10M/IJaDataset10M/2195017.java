package router;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import gateway.observer;

public class mainclass {

    public static int mode;

    public static String InterfaceName = "";

    public static String BroadcastAddress = "";

    public static String NetwokAddress = "";

    public static String NetwokPart = "";

    public static acces c = new acces();

    public static RouteManager route = null;

    public static String IP = "";

    protected NetworkInterface networkInterface;

    observer observer = new observer();

    /**
    *
    * @param opMode
    * @param IntName
    * @param BdcastAddress
    * @param NetAddress
    * @param NetPart
    */
    public mainclass(int opMode, String IntName, String BdcastAddress, String NetAddress, String NetPart) {
        InterfaceName = IntName;
        BroadcastAddress = BdcastAddress;
        NetwokAddress = NetAddress;
        NetwokPart = NetPart;
        mode = opMode;
        try {
            int index = 0;
            Enumeration enumeration = networkInterface.getByName(mainclass.InterfaceName).getInetAddresses();
            while (enumeration.hasMoreElements()) {
                if (index == 1) IP = enumeration.nextElement().toString().replaceFirst("/", ""); else enumeration.nextElement();
                index++;
            }
        } catch (IOException e) {
            System.out.println(e);
            observer.raiseExcetion(e);
        }
        route = new RouteManager(IP);
    }

    /**
   *
   */
    public void startRouting() {
        try {
            new MulticastServerThread(c, route).start();
            new MulticastClient(c, route).start();
        } catch (IOException e) {
            observer.raiseExcetion(e);
            System.out.print(e.toString());
        }
    }
}
