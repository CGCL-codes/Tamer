package ch.olsen.test.scproject2.test;

import java.net.URL;
import ch.olsen.products.util.Application;
import ch.olsen.products.util.logging.Logger;
import ch.olsen.servicecontainer.domain.ExternalDomain;
import ch.olsen.servicecontainer.naming.OscURI;
import ch.olsen.servicecontainer.node.SCNode;
import ch.olsen.test.scproject2.InterestRateNotifier;
import ch.olsen.test.scproject2.InterestRateNotifierService;

public class InterestRateNotifierTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Logger log = Application.getLogger("test");
        try {
            InterestRateNotifier irn = (InterestRateNotifier) SCNode.deployNewService("//localhost:10099/SC1/", "admin", "postit", "Administrator", "interestRateNotifier", new URL[] { new URL("file:///home/vito/work/eclipseJ3.0/sc-project2/sc2.jar") }, InterestRateNotifierService.class.getCanonicalName(), InterestRateNotifier.class.getCanonicalName(), log);
            System.out.println(irn.test());
        } catch (Exception e) {
            System.err.println("Could not test Interest Rate notifier: " + e.getMessage());
            e.printStackTrace();
            try {
                InterestRateNotifier irn = (InterestRateNotifier) ExternalDomain.lookupService(new OscURI("osc://localhost:10099/SCaaaa/interestRateNotifier/"), log);
                System.out.println(irn.test());
            } catch (Exception e1) {
                System.err.println("Could not lookup Interest Rate notifier: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
