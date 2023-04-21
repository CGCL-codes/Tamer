package org.openscience.cdk.test.io;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.io.IChemObjectWriter;
import org.openscience.cdk.io.WriterFactory;
import org.openscience.cdk.io.formats.IChemFormat;
import org.openscience.cdk.io.formats.XYZFormat;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.tools.DataFeatures;

/**
 * TestCase for the writing files.
 *
 * @cdk.module test-io
 */
public class WriterFactoryTest extends CDKTestCase {

    private WriterFactory factory;

    public WriterFactoryTest(String name) {
        super(name);
        factory = new WriterFactory();
    }

    public static Test suite() {
        return new TestSuite(WriterFactoryTest.class);
    }

    public void testFormatCount() {
        assertTrue(factory.formatCount() > 0);
    }

    public void testFindChemFormats() {
        IChemFormat[] formats = factory.findChemFormats(DataFeatures.HAS_3D_COORDINATES);
        assertNotNull(formats);
        assertTrue(formats.length > 0);
    }

    public void testCreateWriter_IChemFormat() {
        IChemFormat format = (IChemFormat) XYZFormat.getInstance();
        IChemObjectWriter writer = factory.createWriter(format);
        assertNotNull(writer);
        assertEquals(format.getFormatName(), writer.getFormat().getFormatName());
    }
}
