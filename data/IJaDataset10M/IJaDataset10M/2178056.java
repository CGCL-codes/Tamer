package org.openscience.cdk.nonotify;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.interfaces.AbstractRingSetTest;
import org.openscience.cdk.interfaces.ITestObjectBuilder;

/**
 * Checks the functionality of the {@link NNRingSet}.
 *
 * @cdk.module test-nonotify
 */
public class NNRingSetTest extends AbstractRingSetTest {

    @BeforeClass
    public static void setUp() {
        setTestObjectBuilder(new ITestObjectBuilder() {

            public IChemObject newTestObject() {
                return new NNRingSet();
            }
        });
    }

    @Test
    public void testNNRingSet() {
        IRingSet rs = new NNRingSet();
        Assert.assertNotNull(rs);
    }

    @Test
    public void testNotifyChanged() {
        NNChemObjectTestHelper.testNotifyChanged(newChemObject());
    }

    @Test
    public void testNotifyChanged_IChemObjectChangeEvent() {
        NNChemObjectTestHelper.testNotifyChanged_IChemObjectChangeEvent(newChemObject());
    }

    @Test
    public void testStateChanged_IChemObjectChangeEvent() {
        NNChemObjectTestHelper.testStateChanged_IChemObjectChangeEvent(newChemObject());
    }

    @Test
    public void testClone_ChemObjectListeners() throws Exception {
        NNChemObjectTestHelper.testClone_ChemObjectListeners(newChemObject());
    }

    @Test
    public void testAddListener_IChemObjectListener() {
        NNChemObjectTestHelper.testAddListener_IChemObjectListener(newChemObject());
    }

    @Test
    public void testGetListenerCount() {
        NNChemObjectTestHelper.testGetListenerCount(newChemObject());
    }

    @Test
    public void testRemoveListener_IChemObjectListener() {
        NNChemObjectTestHelper.testRemoveListener_IChemObjectListener(newChemObject());
    }

    @Test
    public void testSetNotification_true() {
        NNChemObjectTestHelper.testSetNotification_true(newChemObject());
    }
}
