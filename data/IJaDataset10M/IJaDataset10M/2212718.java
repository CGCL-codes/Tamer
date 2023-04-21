package org.openscience.cdk;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.AbstractChemModelTest;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.ITestObjectBuilder;

/**
 * Checks the functionality of the ChemModel class.
 *
 * @cdk.module test-data
 *
 * @see org.openscience.cdk.ChemModel
 */
public class ChemModelTest extends AbstractChemModelTest {

    @BeforeClass
    public static void setUp() {
        setTestObjectBuilder(new ITestObjectBuilder() {

            public IChemObject newTestObject() {
                return new ChemModel();
            }
        });
    }

    @Test
    public void testChemModel() {
        IChemModel chemModel = new ChemModel();
        Assert.assertNotNull(chemModel);
    }
}
