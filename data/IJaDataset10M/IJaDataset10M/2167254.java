package Examples;

import starlink.ColouredAutomata.ProtocolBridge;

/**
 * Bonjour2SLP.java
 *
 * Test program to load automata and then execute the Starlink engine
 *
 * @author Paul Grace
 * @version Starlink 0.1
 */
public class Bonjour2SLP {

    private static String mDNS = "mDNS.xml";

    private static String SLPClient = "SLPClient.xml";

    private static String Bonjour2SLP = "Bonjour2SLP.xml";

    private static String CAFolder = "ColouredAutomata";

    private static String MAFolder = "MergedAutomata";

    public Bonjour2SLP() {
    }

    public static void main(String[] args) {
        String thisDir = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        args = new String[3];
        args[0] = thisDir + separator + "DSL" + separator + CAFolder + separator + mDNS;
        args[1] = thisDir + separator + "DSL" + separator + CAFolder + separator + SLPClient;
        args[2] = thisDir + separator + "DSL" + separator + MAFolder + separator + Bonjour2SLP;
        ProtocolBridge r = new ProtocolBridge();
        for (String str : args) {
            r.addAutomata(str);
        }
        r.deploy();
        r.execute();
    }
}
