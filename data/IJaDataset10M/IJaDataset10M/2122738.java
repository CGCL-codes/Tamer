package de.radis.test;

import de.radis.io.xml.XmlHandler;

public class XMLTest {

    private static XmlHandler xml;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        xml = new XmlHandler();
        write();
        read();
    }

    private static void write() {
        xml.getConfiguration().put("root", "C:/");
        xml.getConfiguration().put("hotkey", "Alt+A");
        xml.getConfiguration().put("lnkDirs", "false");
        xml.getConfiguration().put("radius", "100");
        xml.writeConfig();
        System.out.println("Writing successful");
    }

    private static void read() {
        System.out.println("Reading successful");
        System.out.println("Values:");
        System.out.println(xml.getRoot());
        System.out.println(xml.getHotkeyOne());
        System.out.println(xml.getHotkeyTwo());
        System.out.println(xml.getTreatLinksAsDirs());
        System.out.println(xml.getRadius());
    }
}
