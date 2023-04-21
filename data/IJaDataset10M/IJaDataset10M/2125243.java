package org.openscience.cdk.io;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemModel;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.CDKTestCase;
import org.openscience.cdk.Reaction;
import org.openscience.cdk.debug.DebugChemFile;
import org.openscience.cdk.debug.DebugChemModel;
import org.openscience.cdk.debug.DebugMolecule;
import org.openscience.cdk.debug.DebugReaction;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.io.formats.IResourceFormat;
import org.openscience.cdk.io.listener.IChemObjectIOListener;
import org.openscience.cdk.io.setting.IOSetting;
import org.openscience.cdk.nonotify.NNChemFile;
import org.openscience.cdk.nonotify.NNChemModel;
import org.openscience.cdk.nonotify.NNMolecule;
import org.openscience.cdk.nonotify.NNReaction;

/**
 * TestCase for CDK IO classes.
 *
 * @cdk.module test-io
 */
public abstract class ChemObjectIOTest extends CDKTestCase {

    protected static IChemObjectIO chemObjectIO;

    public static void setChemObjectIO(IChemObjectIO aChemObjectIO) {
        chemObjectIO = aChemObjectIO;
    }

    @Test
    public void testChemObjectIOSet() {
        Assert.assertNotNull("You must use setChemObjectIO() to set the IChemObjectIO object.", chemObjectIO);
    }

    @Test
    public void testGetFormat() {
        IResourceFormat format = chemObjectIO.getFormat();
        Assert.assertNotNull("The IChemObjectIO.getFormat method returned null.", format);
    }

    private static IChemObject[] acceptableNNChemObjects = { new NNChemFile(), new NNChemModel(), new NNMolecule(), new NNReaction() };

    @Test
    public void testAcceptsAtLeastOneNonotifyObject() {
        boolean oneAccepted = false;
        for (IChemObject object : acceptableNNChemObjects) {
            if (chemObjectIO.accepts(object.getClass())) {
                oneAccepted = true;
            }
        }
        Assert.assertTrue("At least one of the following IChemObect's should be accepted: IChemFile, IChemModel, IMolecule, IReaction", oneAccepted);
    }

    private static IChemObject[] acceptableDebugChemObjects = { new DebugChemFile(), new DebugChemModel(), new DebugMolecule(), new DebugReaction() };

    @Test
    public void testAcceptsAtLeastOneDebugObject() {
        boolean oneAccepted = false;
        for (IChemObject object : acceptableDebugChemObjects) {
            if (chemObjectIO.accepts(object.getClass())) {
                oneAccepted = true;
            }
        }
        Assert.assertTrue("At least one of the following IChemObect's should be accepted: IChemFile, IChemModel, IMolecule, IReaction", oneAccepted);
    }

    protected static IChemObject[] acceptableChemObjects = { new ChemFile(), new ChemModel(), new Molecule(), new Reaction() };

    @Test
    public void testAcceptsAtLeastOneChemObject() {
        boolean oneAccepted = false;
        for (IChemObject object : acceptableChemObjects) {
            if (chemObjectIO.accepts(object.getClass())) {
                oneAccepted = true;
            }
        }
        Assert.assertTrue("At least one of the following IChemObect's should be accepted: IChemFile, IChemModel, IMolecule, IReaction", oneAccepted);
    }

    @Test
    public void testClose() throws Exception {
        chemObjectIO.close();
    }

    @Test
    public void testGetIOSetting() {
        IOSetting[] settings = chemObjectIO.getIOSettings();
        for (IOSetting setting : settings) {
            Assert.assertNotNull(setting);
            Assert.assertNotNull(setting.getDefaultSetting());
            Assert.assertNotNull(setting.getName());
            Assert.assertNotNull(setting.getQuestion());
            Assert.assertNotNull(setting.getLevel());
        }
    }

    @Test
    public void testAddChemObjectIOListener() {
        MyListener listener = new MyListener();
        chemObjectIO.addChemObjectIOListener(listener);
    }

    class MyListener implements IChemObjectIOListener {

        private int timesCalled = 0;

        public void processIOSettingQuestion(IOSetting setting) {
            timesCalled++;
        }
    }

    @Test
    public void testRemoveChemObjectIOListener() {
        MyListener listener = new MyListener();
        chemObjectIO.addChemObjectIOListener(listener);
        chemObjectIO.removeChemObjectIOListener(listener);
    }
}
