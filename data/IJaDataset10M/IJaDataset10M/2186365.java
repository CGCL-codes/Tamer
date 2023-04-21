package joelib2.example;

import joelib2.io.BasicIOType;
import joelib2.io.BasicIOTypeHolder;
import joelib2.io.MoleculeFileHelper;
import joelib2.io.MoleculeFileIO;
import joelib2.io.MoleculeIOException;
import joelib2.molecule.BasicConformerMolecule;
import joelib2.molecule.Molecule;
import joelib2.molecule.types.BasicPairData;
import joelib2.molecule.types.PairData;
import joelib2.util.iterator.PairDataIterator;
import wsi.ra.tool.StopWatch;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.log4j.Category;

/**
 * Example for converting molecules.
 *
 * @.author     wegnerj
 * @.license    GPL
 * @.cvsversion    $Revision: 1.9 $, $Date: 2005/02/17 16:48:29 $
 */
public class CombineExample {

    private static Category logger = Category.getInstance(CombineExample.class.getName());

    /**
     *  Description of the Field
     */
    public PrintStream generatedOutput;

    private boolean checkIfSameTitle = true;

    private String inputFile1;

    private String inputFile2;

    private String inputTypeName1;

    private String inputTypeName2;

    private BasicIOType inType1;

    private BasicIOType inType2;

    private String outputFile;

    private BasicIOType outType;

    /**
     *Constructor for the ConvertSkip object
     */
    public CombineExample() {
    }

    /**
     *  The main program for the ConvertSkip class
     *
     * @param args  The command line arguments
     */
    public static void main(String[] args) {
        CombineExample combine = new CombineExample();
        if (args.length != 6) {
            combine.usage();
            System.exit(0);
        } else {
            if (combine.parseCommandLine(args)) {
                combine.test();
            } else {
                System.exit(1);
            }
        }
    }

    /**
     *  Description of the Method
     *
     * @param args  Description of the Parameter
     * @return      Description of the Return Value
     */
    public boolean parseCommandLine(String[] args) {
        if (args[0].indexOf("-i") == 0) {
            inputTypeName1 = args[0].substring(2);
            inType1 = BasicIOTypeHolder.instance().getIOType(inputTypeName1);
            if (inType1 == null) {
                logger.error("Input type '" + inputTypeName1 + "' not defined.");
                return false;
            }
        }
        inputFile1 = args[1];
        if (args[2].indexOf("-i") == 0) {
            inputTypeName2 = args[2].substring(2);
            inType2 = BasicIOTypeHolder.instance().getIOType(inputTypeName2);
            if (inType2 == null) {
                logger.error("Input type '" + inputTypeName2 + "' not defined.");
                return false;
            }
        }
        inputFile2 = args[3];
        if (args[4].indexOf("-o") == 0) {
            String outTypeS = args[4].substring(2);
            outType = BasicIOTypeHolder.instance().getIOType(outTypeS.toUpperCase());
            if (outType == null) {
                logger.error("Output type '" + outTypeS + "' not defined.");
                return false;
            }
        }
        outputFile = args[5];
        return true;
    }

    public void test() {
        if ((inType1 == null) || (inputFile1 == null) || (inType2 == null) || (inputFile2 == null) || (outType == null) || (outputFile == null)) {
            logger.error("Not correctly initialized.");
            logger.error("input type 1: " + inType1.getName());
            logger.error("input file 1: " + inputFile1);
            logger.error("input type 2: " + inType2.getName());
            logger.error("input file 2: " + inputFile2);
            logger.error("output type: " + outType.getName());
            logger.error("output file: " + outputFile);
            System.exit(1);
        }
        FileInputStream in1 = null;
        FileInputStream in2 = null;
        FileOutputStream out = null;
        MoleculeFileIO loader1 = null;
        MoleculeFileIO loader2 = null;
        MoleculeFileIO writer = null;
        try {
            in1 = new FileInputStream(inputFile1);
            in2 = new FileInputStream(inputFile2);
            out = new FileOutputStream(outputFile);
            loader1 = MoleculeFileHelper.getMolReader(in1, inType1);
            loader2 = MoleculeFileHelper.getMolReader(in2, inType2);
            writer = MoleculeFileHelper.getMolWriter(out, outType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!loader1.readable()) {
            logger.error(inType1.getRepresentation() + " is not readable.");
            logger.error("You're invited to write one !;-)");
            System.exit(1);
        }
        if (!loader2.readable()) {
            logger.error(inType2.getRepresentation() + " is not readable.");
            logger.error("You're invited to write one !;-)");
            System.exit(1);
        }
        if (!writer.writeable()) {
            logger.error(outType.getRepresentation() + " is not writeable.");
            logger.error("You're invited to write one !;-)");
            System.exit(1);
        }
        Molecule mol1 = new BasicConformerMolecule(inType1, outType);
        Molecule mol2 = new BasicConformerMolecule(inType2, outType);
        boolean success = true;
        StopWatch watch = new StopWatch();
        int molCounter = 0;
        logger.info("Start file combination ...");
        for (; ; ) {
            mol1.clear();
            mol2.clear();
            try {
                success = loader1.read(mol1);
                if (!success) {
                    break;
                }
                success = loader2.read(mol2);
                if (!success) {
                    break;
                }
                if (checkIfSameTitle) {
                    if ((mol1.getTitle() != null) && (mol2.getTitle() != null)) {
                        if (!mol1.getTitle().equals(mol2.getTitle())) {
                            logger.warn("Different molecules: " + mol1.getTitle() + " " + mol2.getTitle() + " ?");
                        }
                    }
                }
                PairDataIterator gdit = mol2.genericDataIterator();
                PairData pairData;
                while (gdit.hasNext()) {
                    pairData = gdit.nextPairData();
                    PairData dp = new BasicPairData();
                    dp.setKey(pairData.getKey());
                    dp.setKeyValue(pairData.toString(outType));
                    mol1.addData(dp);
                }
                success = writer.write(mol1);
                if (!success) {
                    break;
                }
                molCounter++;
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(1);
            } catch (MoleculeIOException ex) {
                ex.printStackTrace();
                molCounter++;
                logger.info("Molecule entry (#" + molCounter + ") was skipped: " + mol1.getTitle());
                continue;
            }
            if ((molCounter % 500) == 0) {
                logger.info("... " + molCounter + " molecules successful combined in " + watch.getPassedTime() + " ms.");
            }
        }
        logger.info("... " + molCounter + " molecules successful combined in " + watch.getPassedTime() + " ms.");
    }

    /**
     *  Description of the Method
     */
    public void usage() {
        StringBuffer sb = new StringBuffer();
        String programName = this.getClass().getName();
        sb.append("Usage is :\n");
        sb.append("java -cp . ");
        sb.append(programName);
        sb.append(" -i<inputFormat>");
        sb.append(" <input file 1>");
        sb.append(" -i<inputFormat>");
        sb.append(" <input file 2>");
        sb.append(" -o<outputFormat>");
        sb.append(" <output file>");
        sb.append("\n\nSupported molecule types:");
        sb.append(BasicIOTypeHolder.instance().toString());
        sb.append("\n\nThis is version $Revision: 1.9 $ ($Date: 2005/02/17 16:48:29 $)\n");
        System.out.println(sb.toString());
        System.exit(0);
    }
}
