package verinec.importer.parser.iptables;

import java.io.InputStream;
import java.util.logging.Logger;
import org.jdom.Element;
import verinec.importer.parser.ImporterEnvironment;
import verinec.importer.parser.iptables.Iptable;
import verinec.util.FileUtil;
import verinec.util.LogUtil;
import verinec.util.VerinecNamespaces;
import junit.framework.TestCase;

/** Test the Iptable and ImportIptables classes . 
 * 
 * @author david.buchmann at unifr.ch
 */
public class IptableTest extends TestCase {

    /** Test the Iptable class. 
	 * 
	 * @throws Exception If anything goes wrong
	 */
    public void testGenerateChain() throws Exception {
        ImporterEnvironment env = new ImporterEnvironment();
        env.interfacesTable.put("lo", "lo-identifier");
        env.interfacesTable.put("eth0", "eth0-identifier");
        env.interfacesTable.put("eth1", "eth1-identifier");
        env.interfacesTable.put("eth2", "eth2-identifier");
        InputStream is = getClass().getResourceAsStream("/res/iptables_output_large.txt");
        String data = FileUtil.readFileAsString(is);
        Iptable t = new Iptable(data, env);
        Element e = new Element("root", VerinecNamespaces.NS_NODE);
        e.addContent(t.chains);
        LogUtil.logJdom(Logger.global, e);
        System.out.println(env.getIptablesWarnings());
        String ethwarns = env.getEthernetWarnings();
        if (ethwarns.length() > 0) {
            System.out.println("We got ethernet warnings!");
            System.out.println(ethwarns);
        }
        is.close();
    }
}
