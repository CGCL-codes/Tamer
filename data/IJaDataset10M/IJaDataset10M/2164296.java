package org.openscience.cdk.debug;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.AbstractFragmentAtomTest;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IFragmentAtom;
import org.openscience.cdk.interfaces.ITestObjectBuilder;

/**
 * Checks the functionality of the {@link DebugFragmentAtom}.
 *
 * @cdk.module test-datadebug
 */
public class DebugFragmentAtomTest extends AbstractFragmentAtomTest {

    @BeforeClass
    public static void setUp() {
        setTestObjectBuilder(new ITestObjectBuilder() {

            public IChemObject newTestObject() {
                return new DebugFragmentAtom();
            }
        });
    }

    @Test
    public void testDebugFragmentAtom() {
        IFragmentAtom a = new DebugFragmentAtom();
        Assert.assertNotNull(a);
    }
}
