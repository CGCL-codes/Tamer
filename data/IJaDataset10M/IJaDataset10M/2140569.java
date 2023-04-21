package Examples;

import starlink.ColouredAutomata.ProtocolBridge;

/**
 * XmlReader.java
 *
 * Test program to load automata and then execute the Starlink engine
 *
 * @author David Bromberg, Labri, University of Bordeaux
 * @modified by Paul Grace, Lancaster University
 * @version Starlink 0.1
 */
public class SLP2Bonjour {

    private static String mDNSClient = "mDNSClient.xml";

    private static String SLP = "SLP.xml";

    private static String SLP2Bonjour = "SLP2Bonjour.xml";

    private static String CAFolder = "ColouredAutomata";

    private static String MAFolder = "MergedAutomata";

    public SLP2Bonjour() {
    }

    public static void main(String[] args) {
        String thisDir = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        args = new String[3];
        args[0] = thisDir + separator + "DSL" + separator + CAFolder + separator + SLP;
        args[1] = thisDir + separator + "DSL" + separator + CAFolder + separator + mDNSClient;
        args[2] = thisDir + separator + "DSL" + separator + MAFolder + separator + SLP2Bonjour;
        ProtocolBridge r = new ProtocolBridge();
        for (String str : args) {
            r.addAutomata(str);
        }
        r.deploy();
        r.execute();
    }
}
