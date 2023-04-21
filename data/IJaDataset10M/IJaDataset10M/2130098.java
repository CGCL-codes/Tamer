package org.openscience.cdk;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IMonomer;
import org.openscience.cdk.interfaces.AbstractMonomerTest;
import org.openscience.cdk.interfaces.ITestObjectBuilder;

/**
 * TestCase for the Monomer class.
 *
 * @cdk.module test-data
 *
 * @author  Edgar Luttman <edgar@uni-paderborn.de>
 * @cdk.created 2001-08-09
 */
public class MonomerTest extends AbstractMonomerTest {

    @BeforeClass
    public static void setUp() {
        setTestObjectBuilder(new ITestObjectBuilder() {

            public IChemObject newTestObject() {
                return new Monomer();
            }
        });
    }

    @Test
    public void testMonomer() {
        IMonomer oMonomer = new Monomer();
        Assert.assertNotNull(oMonomer);
    }
}
