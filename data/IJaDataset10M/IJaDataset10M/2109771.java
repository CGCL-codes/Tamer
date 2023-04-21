package performance;

import junit.framework.TestCase;
import org.arastreju.api.ArastrejuGate;
import org.arastreju.api.ArastrejuGateFactory;
import de.lichtflut.infra.logging.StopWatch;
import de.lichtflut.infra.security.Crypt;

/**
 * Simple performance test case for Arastreju Gate operations .
 * 
 * Created: 29.11.2008
 * 
 * @author Oliver Tigges
 */
public class ArastrejuGateSpeedTest extends TestCase {

    public void testLogin() {
        StopWatch sw = new StopWatch();
        for (int i = 1; i <= 100; i++) {
            sw.reset();
            ArastrejuGateFactory.getInstance().login("root", Crypt.md5Hex("root"));
            sw.displayTime("login " + i + " needed");
        }
    }

    public void testLoginAndLookup() {
        StopWatch sw = new StopWatch();
        for (int i = 1; i <= 10; i++) {
            sw.reset();
            ArastrejuGate gate = ArastrejuGateFactory.getInstance().login("root", Crypt.md5Hex("root"));
            sw.displayTime("login " + i + " needed");
            gate.lookupIdentityManagementService();
            sw.displayTime("lookupIdentityManagementService " + i + " needed");
            gate.lookupModellingService();
            sw.displayTime("lookupModellingService " + i + " needed");
            gate.lookupNamespaceService();
            sw.displayTime("lookupOntologyService " + i + " needed");
            gate.lookupSchemaService();
            sw.displayTime("lookupSchemaService " + i + " needed");
            gate.lookupTerminologyService();
            sw.displayTime("lookupTerminologyService " + i + " needed");
            sw.displayTime("login & lookup" + i + " needed");
        }
    }
}
