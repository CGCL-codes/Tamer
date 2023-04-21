package net.sourceforge.imman.importer;

import net.sourceforge.imman.importer.test.Log4JInitializer;
import static org.junit.Assert.*;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.Before;

/**
 * Class that does JUnit testing for {@link CommandLineChecker}
 * 
 * @author fracky
 */
public class CommandLineCheckerTest {

    private static final Logger logger = Logger.getLogger(CommandLineChecker.class);

    @Before
    public void setUp() {
        Log4JInitializer.initialize();
    }

    @Test
    public void testCheckParameterInfile() {
        logger.info("*** testCheckParameterInfile()");
        CommandLineChecker c = new CommandLineChecker();
        try {
            c.checkParameterInfile(null);
            fail("Expecting a NPE to be thrown when giving null as parameter value.");
        } catch (NullPointerException e) {
        }
        String nonExistingFile = "foo.txt";
        try {
            c.checkParameterInfile(nonExistingFile);
            fail("Excpecting an IllegalArgumentException if a non existing file is given as parameter value");
        } catch (IllegalArgumentException e) {
        }
        String directory = "/tmp";
        try {
            c.checkParameterInfile(directory);
            fail("Expecting an IllegalArgumentException if parameter points to a directory");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testCheckParameterIndexDirectory() {
        logger.info("*** testCheckParameterIndexDirectory()");
        CommandLineChecker c = new CommandLineChecker();
        try {
            c.checkParameterIndexDirectory(null);
        } catch (NullPointerException e) {
        }
        String nonExistingDirectory = "/bar";
        try {
            c.checkParameterIndexDirectory(nonExistingDirectory);
            fail("A non existing directory should yield an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
        }
        String validDirectory = "/tmp";
        try {
            c.checkParameterIndexDirectory(validDirectory);
        } catch (IllegalArgumentException e) {
            fail("Valid directory was given, but an IllegalArgumentException was thrown");
        }
    }
}
