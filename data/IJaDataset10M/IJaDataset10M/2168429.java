package org.openscience.cdk.test.validate;

import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.test.CDKTestCase;
import org.openscience.cdk.validate.Geometry3DValidator;
import org.openscience.cdk.validate.ValidationReport;
import org.openscience.cdk.validate.ValidatorEngine;

/**
 * @cdk.module test-extra
 */
public class Geometry3DValidatorTest extends CDKTestCase {

    public Geometry3DValidatorTest(String name) {
        super(name);
    }

    public void setUp() {
    }

    ;

    public static Test suite() {
        return new TestSuite(Geometry3DValidatorTest.class);
    }

    public void testEthane() throws Exception {
        String filename = "data/Heptan-TestFF-output.mol";
        InputStream ins = this.getClass().getClassLoader().getResourceAsStream(filename);
        MDLReader reader = new MDLReader(ins);
        ChemFile chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
        ValidatorEngine engine = new ValidatorEngine();
        engine.addValidator(new Geometry3DValidator());
        ValidationReport report = engine.validateChemFile(chemFile);
        assertEquals(0, report.getErrorCount());
        assertEquals(0, report.getWarningCount());
    }
}
