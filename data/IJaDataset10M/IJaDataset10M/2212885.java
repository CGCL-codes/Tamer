package org.xaware.server.applications;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xaware.server.connector.ejb.XAEjb;
import org.xaware.server.connector.ejb.XAEjbHome;
import org.xaware.shared.util.XAwareConfig;

public class XAEjbBizDoc {

    private static final String version = "3.5";

    private static final String BIZVIEW = "_BIZVIEW";

    private static final String XQUERY = "_XQUERY";

    private static final String OUTPUT = "_OUTPUT";

    private static final String GEN_OUT = "_GEN_OUT";

    private static final String SAXDRIVER = "_SAXDRIVER";

    private static final String POST = "_POST";

    private static final String XADATA = "_XADATA";

    private static final String RESOURCE_PATH = "_RESOURCE_PATH";

    private static final String CONFIG = "_CONFIG";

    private static final String COMPRESS = "_COMPRESS";

    private static final String UID = "_UID";

    private static final String PWD = "_PWD";

    private static final String FACTORY = "_FACTORY";

    private static final String URL = "_URL";

    static String sOutput = "NONE";

    static String sBizName;

    static String sXQuery;

    static String sGenOut;

    static String sXmlDataFile;

    static String sResourcePath;

    static String sConfigFile;

    static String sUid = "";

    static String sPwd = "";

    static String sEjbJndiFactory = "";

    static String sEjbUrl = "";

    static boolean bCompress = false;

    static String saxDriverClass = "";

    /**
     * <p>
     * This provides a static entry point for creating a JDOM <code>{@link Document}</code> object using a SAX 2.0
     * parser (an <code>XMLReader</code> implementation).
     * </p>
     * 
     * @param args
     *            <code>String[]</code>
     *            <ul>
     *            <li>First argument: filename of XML document to parse</li>
     *            <li>Second argument: optional name of SAX Driver class</li>
     *            </ul>
     */
    public static void main(final String[] args) {
        if ((args.length < 1)) {
            String sUsage = "1. _BIZVIEW=<filename>or<XAware Bizview name> \r\n";
            sUsage += "2. _OUTPUT=DISPLAY|NONE|FILE \r\n";
            sUsage += "3. _GEN_OUT=<output filename> \r\n";
            sUsage += "4. _POST or _XADATA=<xml data file> \r\n";
            sUsage += "5. _UID<=user id> \r\n";
            sUsage += "6. _PWD=<password> \r\n";
            sUsage += "7. _FACTORY=<initial context factory> \r\n";
            sUsage += "8. _URL=<appserver url> \r\n";
            sUsage += "9. _RESOURCE_PATH=<resourcepath> \r\n";
            sUsage += "10. <param>=<value> \r\n";
            System.out.println("Usage: java XAEjbBizDoc. Other optional parameters include\r\n" + sUsage);
            return;
        }
        final String sHomeDir = System.getProperty("xaware.home");
        if (sHomeDir == null) {
            System.out.println("System property xaware.home not set. Please set xaware.home to XAware installation root directory");
            return;
        }
        if (new File(sHomeDir).isDirectory() == false) {
            System.out.println(sHomeDir + " is not a valid directory. Please check value of xaware.home system property");
            return;
        }
        System.out.println("Starting XAEjbBizDoc Version:" + version);
        sUid = "";
        sPwd = "";
        String filename = args[0];
        if (args[0].startsWith(BIZVIEW)) {
            filename = args[0].substring(BIZVIEW.length() + 1);
        }
        if (args[0].startsWith(XQUERY)) {
            sXQuery = args[0].substring(XQUERY.length() + 1);
        }
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith(SAXDRIVER)) {
                saxDriverClass = args[i].substring(SAXDRIVER.length() + 1);
                continue;
            }
            if (args[i].startsWith(OUTPUT)) {
                sOutput = args[i].substring(OUTPUT.length() + 1);
                continue;
            }
            if (args[i].startsWith(FACTORY)) {
                sEjbJndiFactory = args[i].substring(FACTORY.length() + 1);
                continue;
            }
            if (args[i].startsWith(URL)) {
                sEjbUrl = args[i].substring(URL.length() + 1);
                continue;
            }
            if (args[i].startsWith(COMPRESS)) {
                final String sCompressString = args[i].substring(COMPRESS.length() + 1);
                if (sCompressString.compareTo("YES") == 0) {
                    bCompress = true;
                }
                continue;
            }
            if (args[i].startsWith(GEN_OUT)) {
                sGenOut = args[i].substring(GEN_OUT.length() + 1);
                continue;
            }
            if (args[i].startsWith(UID)) {
                sUid = args[i].substring(UID.length() + 1);
                continue;
            }
            if (args[i].startsWith(PWD)) {
                sPwd = args[i].substring(PWD.length() + 1);
                continue;
            }
            if (args[i].startsWith(POST)) {
                sXmlDataFile = args[i].substring(POST.length() + 1);
                if (validateFile(sXmlDataFile) == false) {
                    System.err.println("Invalid POST value");
                    sXmlDataFile = null;
                }
                continue;
            }
            if (args[i].startsWith(XADATA)) {
                sXmlDataFile = args[i].substring(XADATA.length() + 1);
                if (validateFile(sXmlDataFile) == false) {
                    System.err.println("Invalid XADATA value");
                    sXmlDataFile = null;
                }
                continue;
            }
            if (args[i].startsWith(RESOURCE_PATH)) {
                sResourcePath = args[i].substring(RESOURCE_PATH.length() + 1);
            }
            if (args[i].startsWith(CONFIG)) {
                sConfigFile = args[i].substring(CONFIG.length() + 1);
                if (validateFile(sConfigFile) == true) {
                    XAwareConfig.SetConfigFile(sConfigFile);
                } else {
                    System.err.println("Invalid CONFIG value");
                }
                continue;
            }
        }
        String responseXML = "";
        try {
            XAEjb xaejb = null;
            XAEjbHome xaHome = null;
            final Properties p = new Properties();
            p.put(javax.naming.Context.PROVIDER_URL, sEjbUrl);
            p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, sEjbJndiFactory);
            if (sUid.length() > 0) {
                p.put(Context.SECURITY_PRINCIPAL, sUid);
                p.put(Context.SECURITY_CREDENTIALS, sPwd);
            }
            final InitialContext ctx = new InitialContext(p);
            xaHome = (XAEjbHome) ctx.lookup("xaejb");
            xaejb = xaHome.create();
            String sInputData = "";
            if (sXmlDataFile != null && sXmlDataFile.length() > 0) {
                SAXBuilder builder = null;
                if (saxDriverClass.length() > 0) {
                    builder = new SAXBuilder(saxDriverClass);
                } else {
                    builder = new SAXBuilder();
                }
                final Document mDoc = builder.build(new File(sXmlDataFile));
                final StringWriter x = new StringWriter();
                final XMLOutputter outputter = new XMLOutputter();
                outputter.output(mDoc, x);
                sInputData = x.toString();
            }
            String sInputParam = "<mydata>";
            for (int i = 1; i < args.length; i++) {
                if (args[i].charAt(0) != '_') {
                    final int iLoc = args[i].indexOf('=');
                    if (iLoc >= 0) {
                        sInputParam += "<" + args[i].substring(0, iLoc) + ">" + args[i].substring(iLoc + 1) + "</" + args[i].substring(0, iLoc) + ">";
                    }
                }
            }
            sInputParam += "</mydata>";
            responseXML = xaejb.executeBizDoc(filename, sInputData, sInputParam);
        } catch (final IOException e) {
            System.out.println("IO exception:" + e.getMessage());
        } catch (final Exception e) {
            System.out.println("Unknown exception:" + e.getMessage());
            e.printStackTrace();
        }
        if ((sOutput.compareTo("STREAM") == 0) || (sOutput.compareTo("NONE") == 0)) {
            return;
        }
        if (sGenOut != null && sGenOut.length() > 0) {
            try {
                final FileWriter fp = new FileWriter(sGenOut, false);
                fp.write(responseXML);
                fp.close();
            } catch (final IOException e) {
                System.out.println("IO exception saving to file:" + e.getMessage());
            }
        }
        if (sOutput != null && sOutput.compareTo("DISPLAY") == 0) {
            System.out.println(responseXML);
        }
    }

    private static boolean validateFile(final String sFileName) {
        FileReader fileRdr = null;
        try {
            fileRdr = new FileReader(sFileName);
        } catch (final FileNotFoundException e) {
            fileRdr = null;
            return false;
        }
        fileRdr = null;
        return true;
    }
}
