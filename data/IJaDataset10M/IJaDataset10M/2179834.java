package de.sonivis.tool.core.tests.gnu.r;

import junit.framework.TestCase;
import de.sonivis.tool.core.gnu.r.AbstractRException;
import de.sonivis.tool.core.gnu.r.REvalException;
import de.sonivis.tool.core.gnu.r.RManager;
import de.sonivis.tool.core.gnu.r.RNullException;

/**
 * To run this test click "Run as" -> "Run...", mark "JUnit Plug-in Test" and click the new button
 * (top left). In the "Main" tab select "Run a product" and choose
 * "de.sonivis.tool.installer.SONIVISTool". Also you must unmark "Workspace Data" ->
 * "Clear workspace data before launching". Also please check that the VM Variable is set to locate
 * the jri: -Djava.library.path=D:\Programme\R\R-2.4.1\library\rJava\jri
 * -Djava.library.path=/usr/local/lib/R/library/rJava/jri
 * 
 * @author Anne
 * 
 */
public class RExceptionTest extends TestCase {

    private RManager rconnection;

    @Override
    protected final void setUp() throws Exception {
        rconnection = RManager.getInstance();
        rconnection.evalWithoutResult("library(igraph)");
    }

    public final void testNull() {
        final String test = "transitivity(test,vids=400)";
        try {
            rconnection.eval(test);
            fail("Didn't throw RNullException.");
        } catch (AbstractRException ex) {
            if (ex instanceof RNullException) {
                assert (true);
            }
        }
    }

    public final void testMessage() {
        try {
            rconnection.eval("transitivity(test,vids=400)");
            fail("Didn't throw RNullException.");
        } catch (AbstractRException e) {
            final String test = rconnection.getLastException();
            assertNotNull("String is null", test);
        }
    }

    public final void testMissingSign() {
        final String test = "transitivity(graph,vids=400";
        try {
            rconnection.eval(test);
            fail("Didn't throw REvalException.");
        } catch (AbstractRException ex) {
            if (ex instanceof REvalException) {
                assert (true);
            }
        }
    }

    public final void testDontThrowRException() {
        final String test = "transitivity(graph.ring(10),vids=400)";
        try {
            rconnection.eval(test);
            assert (true);
        } catch (AbstractRException ex) {
            fail("Throw RException.");
        }
    }
}
