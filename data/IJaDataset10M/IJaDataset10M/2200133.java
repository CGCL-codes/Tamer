package net.sf.cruisemonitor;

public class CruiseControl {

    public static void main(String args[]) throws Exception {
        Tray tray = new Tray("url.txt");
        tray.start();
    }
}
