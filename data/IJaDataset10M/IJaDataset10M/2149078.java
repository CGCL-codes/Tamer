package org.openscience.cdk.debug;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.AbstractChemModelTest;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.ITestObjectBuilder;

/**
 * Checks the functionality of the {@link DebugChemModel}.
 *
 * @cdk.module test-datadebug
 */
public class DebugChemModelTest extends AbstractChemModelTest {

    @BeforeClass
    public static void setUp() {
        setTestObjectBuilder(new ITestObjectBuilder() {

            public IChemObject newTestObject() {
                return new DebugChemModel();
            }
        });
    }

    @Test
    public void testDebugChemModel() {
        IChemModel chemModel = new DebugChemModel();
        Assert.assertNotNull(chemModel);
    }
}
