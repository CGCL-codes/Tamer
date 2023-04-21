package ch.sahits.codegen.core.util;

import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ch.sahits.codegen.ui.Logging;
import ch.sahits.codegen.util.ERunAsType;
import ch.sahits.codegen.util.RunAs;

/**
 * Test the Logging factory when used from a plug-in
 * @author Andi Hotz, Sahits GmbH
 * @since 2.1.0
 */
@RunAs(ERunAsType.JUNIT)
public class LogFactoryUITest {

    @Before
    public void setUp() {
        ch.sahits.codegen.ui.Logging.class.getName();
    }

    /**
	 * Test getLogger()
	 */
    @Test
    public void testGetLogger() {
        ILogger actual = LogFactory.getLogger();
        Assert.assertTrue("The logger must be of instance ch.sahits.codegen.ui.Logging", actual instanceof Logging);
    }
}
