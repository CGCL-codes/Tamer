package net.sf.nxqd;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.nxqd.NxqdManager;
import net.sf.nxqd.common.NxqdUtils;

public abstract class NxqdAbstractTestCase extends TestCase {

    private static Logger logger = Logger.getLogger(NxqdAbstractTestCase.class.getName());

    private static final String defaultHost = "localhost";

    private static final String defaultPort = "8080";

    protected NxqdManager client;

    public NxqdAbstractTestCase(String name) {
        super(name);
    }

    public NxqdManager getNxqdManager() {
        return client;
    }

    public static String getNxqdHost() {
        String host = System.getProperty("nxqd.host", defaultHost);
        if (host == null) {
            host = defaultHost;
        }
        if (host.length() == 0) {
            host = defaultHost;
        }
        return host;
    }

    public static String getNxqdPort() {
        String port = System.getProperty("nxqd.port", defaultPort);
        try {
            Integer.parseInt(port);
        } catch (NumberFormatException nfe) {
            port = defaultPort;
            logger.config("Using default port " + port);
        }
        return port;
    }

    public void setUp() throws Exception {
        String host = getNxqdHost();
        String port = getNxqdPort();
        logger.config("Opening client connection to " + host + ":" + port);
        client = new NxqdManager(host, port);
        client.connect();
        logger.info("Registered with session id " + client.getSessionId());
    }

    public void tearDown() throws Exception {
        client.disconnect();
    }

    public String getUniqueName() {
        return NxqdUtils.generateUniqueId();
    }

    public void cleanUp() throws NxqdException {
        List list = client.listContainers();
        logger.info("Will clean up " + list.size() + " containers");
        for (int i = 0; i < list.size(); i++) {
            logger.info("Cleaning up container " + list.get(i));
            client.deleteContainer(list.get(i).toString());
        }
    }

    public static final String testDocument = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Node0><Node1 class=\"myValue1\">Node1 text </Node1><Node2><Node3>Node3 text</Node3><Node3>Node3 text 2</Node3><Node3>Node3 text 3</Node3><Node4>300</Node4></Node2></Node0>";

    public static final String testNSDocument = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<html xmlns=\"http://www.w3.org/HTML/1998/html4\" xmlns:xdc=\"http://www.xml.com/books\">" + "<head><title>Book Review</title></head>" + "<body>" + "  <xdc:bookreview>" + "   <xdc:title>XML: A Primer</xdc:title>" + "   <table>" + "    <tr align=\"center\">" + "     <td>Author</td><td>Price</td>" + "     <td>Pages</td><td>Date</td></tr>" + "     <tr align=\"left\">" + "     <td><xdc:author>Simon St. Laurent</xdc:author></td>" + "     <td><xdc:price>31.98</xdc:price></td>" + "     <td><xdc:pages>352</xdc:pages></td>" + "     <td><xdc:date>1998/01</xdc:date></td>" + "    </tr>" + "   </table>" + "  </xdc:bookreview>" + " </body>" + "</html>";

    public static final byte[] testBinary = { 1, 45, 23, 1, 6, 3, 32, 5, 23, 2, 4, 32, 54, 34 };
}
