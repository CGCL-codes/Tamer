package org.openscience.cdk.fingerprint;

import java.util.BitSet;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

/**
 * @cdk.module test-fingerprint
 */
public class KlekotaRothFingerprinterTest extends AbstractFingerprinterTest {

    public IFingerprinter getFingerprinter() {
        return new KlekotaRothFingerprinter();
    }

    @Test
    public void testGetSize() throws Exception {
        IFingerprinter printer = getFingerprinter();
        Assert.assertEquals(4860, printer.getSize());
    }

    @Test
    public void testFingerprint() throws Exception {
        SmilesParser parser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        IFingerprinter printer = getFingerprinter();
        BitSet bs1 = printer.getFingerprint(parser.parseSmiles("C=C-C#N"));
        BitSet bs2 = printer.getFingerprint(parser.parseSmiles("C=CCC(O)CC#N"));
        Assert.assertEquals(4860, printer.getSize());
        Assert.assertTrue(FingerprinterTool.isSubset(bs2, bs1));
    }
}
