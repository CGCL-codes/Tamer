package vehikel.configuration;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import vehikel.IUserInteraction;
import vehikel.configuration.ConfigurationTester;
import vehikel.datamodel.IMessageArray;
import vehikel.testing.TestsRunner;

/**
 * @author Thomas Jourdan
 *
 */
public class ConfigurationTesterTest implements IUserInteraction {

    /**
	 * Test method for {@link vehikel.configuration.ConfigurationTester#runConfigurationTests()}.
	 */
    @Test
    public void testRunConfigurationTests() {
        IMessageArray model = new ConfigurationTester().runConfigurationTests(this);
        assertEquals(TestsRunner.skipIoTests ? 1 : 0, model.getMessagesAsArray().length);
    }

    public int informAndWait(String message) {
        System.out.println(message);
        return 0;
    }
}
