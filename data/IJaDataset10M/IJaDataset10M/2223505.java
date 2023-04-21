package org.codecover.componenttest.model.testsessioncontainer.loadsave;

import java.io.File;
import org.codecover.model.*;
import org.codecover.model.exceptions.FileLoadException;
import org.codecover.model.utils.*;

/**
 * 
 * @author Markus Wittlinger
 * @version 1.0 ($Id: CDLS0002.java 1 2007-12-12 17:37:26Z t-scheller $)
 * 
 */
public class CDLS0002 extends junit.framework.TestCase {

    /**
     * Loads a file, that is not a {@link TestSessionContainer} from a location,
     * using both of the provided load methods.
     * <p>
     * If no {@link FileLoadException} is thrown during the loading process, the
     * testcase will fail.
     * 
     */
    public void testCDLS0002() {
        String containerLocation = "build.xml";
        File containerFile = new File(containerLocation);
        Logger logger = new SimpleLogger();
        MASTBuilder builder = new MASTBuilder(logger);
        try {
            TestSessionContainer.load(org.codecover.model.extensions.PluginManager.create(), logger, builder, containerLocation);
            assertNotNull(null);
        } catch (FileLoadException e) {
        }
        try {
            TestSessionContainer.load(org.codecover.model.extensions.PluginManager.create(), logger, builder, containerFile);
            assertNotNull(null);
        } catch (FileLoadException e) {
        }
    }
}
