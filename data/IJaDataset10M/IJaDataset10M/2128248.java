package eu.planets_project.ifr.core.services.migration.genericwrapper2;

import java.net.URI;
import junit.framework.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import eu.planets_project.ifr.core.services.migration.genericwrapper2.exceptions.MigrationException;
import eu.planets_project.ifr.core.services.migration.genericwrapper2.utils.DocumentLocator;

/**
 * @author Thomas Skou Hansen &lt;tsh@statsbiblioteket.dk&gt;
 * 
 */
public class MigrationPathsTest {

    /**
     * Full file path to the test configuration file used by this test class.
     */
    private static final String TEST_CONFIGURATION_FILE_NAME = "IF/generic/test/resources/GenericWrapperConfigFileExample.xml";

    private final MigrationPaths migrationPathsToTest;

    /**
     * @throws Exception
     */
    public MigrationPathsTest() throws Exception {
        final DocumentLocator documentLocator = new DocumentLocator(TEST_CONFIGURATION_FILE_NAME);
        final Document pathsConfiguration = documentLocator.getDocument();
        final MigrationPathFactory migrationPathsFactory = new DBMigrationPathFactory(pathsConfiguration);
        this.migrationPathsToTest = migrationPathsFactory.getAllMigrationPaths();
    }

    /**
     * Test method for
     * @throws Exception 
     * Verify that we can get migration path instances for all known paths in
     * the configuration file used by this test class.
     */
    @Test
    public void testGetMigrationPath() throws Exception {
        URI inputFormat = new URI("info:test/lowercase");
        URI outputFormat = new URI("info:test/uppercase");
        this.migrationPathsToTest.getMigrationPath(inputFormat, outputFormat);
        genericGetInstanceFailCheck(outputFormat, inputFormat);
        inputFormat = new URI("info:test/foo");
        outputFormat = new URI("info:test/bar");
        this.migrationPathsToTest.getMigrationPath(inputFormat, outputFormat);
        genericGetInstanceFailCheck(outputFormat, inputFormat);
    }

    /**
     * Verify that the individual paths in the <code>CliMigrationPaths</code>
     * instance are correct.
     * 
     * TODO: Finish implementation.
     * 
     * @throws Exception
     */
    @Test
    public void testMigrationPaths() throws Exception {
        final URI inputFormatURI = new URI("info:test/lowercase");
        final URI outputFormatURI = new URI("info:test/uppercase");
        MigrationPath migrationPath = this.migrationPathsToTest.getMigrationPath(inputFormatURI, outputFormatURI);
        Assert.assertEquals("The input format of the obtained migration path is incorrect.", inputFormatURI, migrationPath.getInputFormat());
        Assert.assertEquals("The output format of the obtained migration path is incorrect.", outputFormatURI, migrationPath.getOutputFormat());
    }

    /**
     * Generic test for verifying the correct behaviour of
     * <code>CliMigrationPathsFactory.getInstance()</code> when attempting to
     * get a <code>CliMigrationPath</code> instance for a path that does not
     * exist in the configuration.
     * 
     * @param inputFormat
     *            <code>URI</code> identifying the desired input format of the
     *            path.
     * @param outputFormat
     *            <code>URI</code> identifying the desired output format of
     *            the path.
     */
    private void genericGetInstanceFailCheck(URI inputFormat, URI outputFormat) {
        try {
            this.migrationPathsToTest.getMigrationPath(inputFormat, outputFormat);
            Assert.fail("Did not expect to find a migration path for input URI: " + inputFormat + " and output URI: " + outputFormat);
        } catch (MigrationException me) {
        }
    }
}
