package honeycrm.server.test.medium;

import honeycrm.client.misc.HistoryTokenFactory;
import honeycrm.client.view.ModuleAction;
import java.util.Random;
import junit.framework.TestCase;

public class HistoryTokenFactoryTest extends TestCase {

    private static final Random r = new Random(System.currentTimeMillis());

    public void testToken() {
        for (final String module : new String[] { "account", "product", "offering", "project" }) {
            for (final ModuleAction action : ModuleAction.values()) {
                if (action.equals(ModuleAction.DETAIL)) {
                    final long id = r.nextLong();
                    final String expectedToken = module + " " + action.toString().toLowerCase() + " " + id;
                    assertEquals(expectedToken, HistoryTokenFactory.get(module, action, id));
                } else {
                    final String expectedToken = module + " " + action.toString().toLowerCase();
                    assertEquals(expectedToken, HistoryTokenFactory.get(module, action));
                }
            }
        }
    }
}
