package toxTree.test;

import java.io.File;
import java.io.FileReader;
import junit.framework.Assert;
import org.junit.Test;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import toxTree.data.MoleculesFile;
import toxTree.io.Tools;
import ambit2.core.io.MyIteratingMDLReader;

public class MoleculesFileTest {

    @Test
    public void test() throws Exception {
        IChemObjectBuilder b = SilentChemObjectBuilder.getInstance();
        File file = Tools.getFileFromResource("substituents.sdf");
        MoleculesFile mf = new MoleculesFile(file, b);
        File file1 = Tools.getFileFromResource("substituents.sdf");
        MyIteratingMDLReader reader = new MyIteratingMDLReader(new FileReader(file1), b);
        int record = 0;
        int found = 0;
        while (reader.hasNext()) {
            Object o = reader.next();
            if (o instanceof IAtomContainer) {
                AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms((IAtomContainer) o);
                boolean aromatic = CDKHueckelAromaticityDetector.detectAromaticity((IAtomContainer) o);
                long now = System.currentTimeMillis();
                System.out.print(record);
                System.out.print('\t');
                System.out.print(((IAtomContainer) o).getProperty("#"));
                System.out.print('\t');
                System.out.print(((IAtomContainer) o).getProperty("SMILES"));
                System.out.print("\tAromatic ");
                System.out.print(aromatic);
                System.out.print('\t');
                int index = mf.indexOf("SMILES", ((IAtomContainer) o).getProperty("SMILES"));
                if (index > -1) {
                    System.out.print("found ");
                    System.out.print(System.currentTimeMillis() - now);
                    System.out.print(" ms\tMR\t");
                    System.out.print(mf.getAtomContainer(index).getProperty("MR"));
                    System.out.print(" ms\tB5STM\t");
                    System.out.println(mf.getAtomContainer(index).getProperty("B5STM"));
                    found++;
                } else System.out.println("not found");
                record++;
            }
        }
        Assert.assertEquals(record, found);
    }
}
