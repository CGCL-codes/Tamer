package com.volantis.mcs.migrate.integration.framework;

import com.volantis.mcs.migrate.api.framework.FrameworkFactory;
import com.volantis.mcs.migrate.api.framework.OutputCreator;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.framework.ResourceMigratorBuilder;
import com.volantis.mcs.migrate.api.framework.Version;
import com.volantis.mcs.migrate.api.framework.XSLStreamMigratorBuilder;
import com.volantis.mcs.migrate.impl.framework.identification.DefaultInputMetadata;
import com.volantis.mcs.migrate.impl.notification.reporter.SimpleCLINotificationReporter;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An integration test case for the migration framework which uses
 * {@link FrameworkFactory} to do all the object creation.
 */
public class FrameworkFactoryIntegrationTestCase extends TestCaseAbstract {

    /**
     * Test a very simple configuration of a single type with a single custom
     * step.
     *
     * @throws ResourceMigrationException
     * @throws IOException
     */
    public void testSimpleCustomStep() throws ResourceMigrationException, IOException {
        String filename = "blah.ext";
        final String inputData = "the old input";
        final String outputData = "the new output";
        FrameworkFactory factory = FrameworkFactory.getDefaultInstance();
        Version version1 = factory.createVersion("version 1");
        Version version2 = factory.createVersion("version 2");
        SimpleCLINotificationReporter notificationReporter = new SimpleCLINotificationReporter();
        ResourceMigratorBuilder builder = factory.createResourceMigratorBuilder(notificationReporter);
        builder.setTarget(version2);
        builder.startType("type");
        builder.setRegexpPathRecogniser(filename);
        builder.addRegexpContentRecogniser(version1, inputData);
        builder.addStep(version1, version2, new TestStreamMigrator(inputData, outputData));
        builder.endType();
        ResourceMigrator resourceMigrator = builder.getCompletedResourceMigrator();
        ByteArrayInputStream bais = new ByteArrayInputStream(inputData.getBytes());
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DefaultInputMetadata meta = new DefaultInputMetadata(filename, true);
        resourceMigrator.migrate(meta, bais, new OutputCreator() {

            public OutputStream createOutputStream() {
                return baos;
            }
        });
        assertEquals("", outputData, new String(baos.toByteArray()));
    }

    /**
     * Test a very simple configuration of a single type with a single XSL
     * step.
     *
     * todo: later: finish this test
     */
    public void testSimpleXSL() throws ResourceMigrationException {
        FrameworkFactory factory = FrameworkFactory.getDefaultInstance();
        Version v30 = factory.createVersion("3.0");
        Version v35 = factory.createVersion("3.4");
        SimpleCLINotificationReporter notificationReporter = new SimpleCLINotificationReporter();
        ResourceMigratorBuilder builder = factory.createResourceMigratorBuilder(notificationReporter);
        builder.setTarget(v35);
        builder.startType("lpdm");
        builder.setRegexpPathRecogniser(".*mthm");
        builder.addRegexpContentRecogniser(v30, PolicySchemas.MARLIN_LPDM_V3_0.getLocationURL());
        builder.addRegexpContentRecogniser(v35, PolicySchemas.MARLIN_LPDM_2005_09.getLocationURL());
        XSLStreamMigratorBuilder xslBuilder = builder.createXSLStreamMigratorBuilder();
        xslBuilder.setXSL("../../config/lpdm/xsl/lpdm-v30-to-200509.xsl");
        builder.addStep(v30, v35, xslBuilder.getCompletedMigrator());
        builder.endType();
        builder.getCompletedResourceMigrator();
    }
}
