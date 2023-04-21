package net.openchrom.chromatogram.msd.converter.supplier.openchrom.internal.converter;

import java.io.File;
import net.openchrom.chromatogram.msd.converter.supplier.openchrom.TestPathHelper;
import net.openchrom.chromatogram.msd.converter.supplier.openchrom.internal.converter.SpecificationValidator;
import junit.framework.TestCase;

public class SpecificationValidator_1_Test extends TestCase {

    private File file;

    private String spec;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        spec = TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_SPEC);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testValidateAgilentSpecification_1() {
        file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_1));
        file = SpecificationValidator.validateOpenChromSpecification(file);
        assertEquals("File", spec, file.getAbsolutePath());
    }

    public void testValidateAgilentSpecification_3() {
        file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_3));
        file = SpecificationValidator.validateOpenChromSpecification(file);
        assertEquals("File", spec, file.getAbsolutePath());
    }

    public void testValidateAgilentSpecification_4() {
        file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_4));
        file = SpecificationValidator.validateOpenChromSpecification(file);
        assertEquals("File", spec, file.getAbsolutePath());
    }
}
