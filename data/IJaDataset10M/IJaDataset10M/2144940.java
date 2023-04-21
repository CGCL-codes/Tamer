package joelib2.example;

import joelib2.io.BasicIOType;
import joelib2.io.BasicIOTypeHolder;
import joelib2.io.BasicMoleculeWriter;
import joelib2.molecule.BasicConformerMolecule;
import joelib2.molecule.Molecule;
import joelib2.smiles.SMILESParser;
import java.io.FileOutputStream;
import org.apache.log4j.Category;

/**
 * Example for generating a molecule using SMILES.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.8 $, $Date: 2005/02/17 16:48:29 $
 */
public class SMILESExample {

    private static Category logger = Category.getInstance(SMILESExample.class.getName());

    public SMILESExample() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
    }

    /**
     *  The main program for the TestSmarts class
     *
     * @param  args  The command line arguments
     */
    public static void main(String[] args) {
        SMILESExample joeMolTest = new SMILESExample();
        if (args.length != 2) {
            joeMolTest.usage();
            System.exit(0);
        } else {
            Molecule mol = joeMolTest.test(args[0], BasicIOTypeHolder.instance().getIOType("SDF"), BasicIOTypeHolder.instance().getIOType("SDF"));
            joeMolTest.write(mol, args[1], BasicIOTypeHolder.instance().getIOType("SDF"));
        }
        System.exit(0);
    }

    /**
     *  A unit test for JUnit
     *
     * @param  molURL   Description of the Parameter
     * @param  inType   Description of the Parameter
     * @param  outType  Description of the Parameter
     */
    public Molecule test(String smiles, BasicIOType inType, BasicIOType outType) {
        Molecule mol = new BasicConformerMolecule(inType, outType);
        System.out.println("Generate molecule from \"" + smiles + "\"");
        if (!SMILESParser.smiles2molecule(mol, smiles, "Name:" + smiles)) {
            System.err.println("Molecule could not be generated from \"" + smiles + "\".");
        }
        System.out.println(mol);
        mol.addHydrogens();
        System.out.println("Add hydrogens:");
        System.out.println(mol);
        return mol;
    }

    /**
     *  Description of the Method
     */
    public void usage() {
        StringBuffer sb = new StringBuffer();
        String programName = this.getClass().getName();
        sb.append("\nUsage is : ");
        sb.append("java -cp . ");
        sb.append(programName);
        sb.append("<SMILES pattern> <SDF file>");
        sb.append("\n\nThis is version $Revision: 1.8 $ ($Date: 2005/02/17 16:48:29 $)\n");
        System.out.println(sb.toString());
        System.exit(0);
    }

    public void write(Molecule mol, String molFile, BasicIOType outType) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(molFile);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        try {
            BasicMoleculeWriter writer = new BasicMoleculeWriter(fos, outType);
            if (!writer.writeNext(mol)) {
                System.err.println("Error writing SMILES.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
