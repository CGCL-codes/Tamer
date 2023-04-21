package org.apache.exi.grammars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.exi.core.byteToHex;
import org.apache.exi.core.headerOptions.HeaderPreserveRules;
import org.apache.exi.io.EXI_ByteAlignedInput;
import org.apache.exi.io.EXI_ByteAlignedOutput;
import org.apache.exi.io.EXI_OutputStreamIface;
import org.apache.exi.io.EXI_abstractInput;

/**
 *
 * @author Sheldon L. Snyder<br>
 * @version 1.0.0<br>
 */
public class testEXIoutput {

    static GrammarRunner grammars;

    static boolean CM = false;

    static boolean PI = false;

    static boolean DE = false;

    static boolean NS = false;

    static boolean verboseGrammar = false;

    static boolean makeTXT = false;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        CM = false;
        PI = false;
        DE = false;
        NS = true;
        verboseGrammar = true;
        makeTXT = true;
        boolean doSeimens = false;
        boolean printTables = false;
        HeaderPreserveRules.PRESERVE_CM.setPreserved(CM);
        HeaderPreserveRules.PRESERVE_PI.setPreserved(PI);
        HeaderPreserveRules.PRESERVE_DTD_ENTITY.setPreserved(DE);
        HeaderPreserveRules.PRESERVE_NAMESPACE_PREFIX.setPreserved(NS);
        String SeimensDecodeMyEXI_XML = "sampleOutput/_mySeimensDecoded.xml";
        String saveMyEXI = "sampleOutput/_myBYTEexi.exi";
        String seimensEncodeEXI = "sampleOutput/_seimensEXI.exi";
        String myXMLoutput = "sampleOutput/_myXMLDecoded.xml";
        String inputXML = "sampleXML/notebook.xml";
        try {
            writeMyEXI(inputXML, saveMyEXI);
            if (printTables) {
                printTables();
            }
            writeMyXML(myXMLoutput, saveMyEXI);
        } catch (Exception e) {
            System.out.println("***RUNNER ERROR*****\n" + e);
        }
        System.out.println("\n\n");
    }

    public static void printTables() {
        try {
            System.out.println("\n****THE RESULTING STRING TABLES****");
            grammars.NStables.prettyPrint();
            System.out.println("Done printing tables!!!!");
        } catch (Exception e) {
            System.out.println("ERROR STRING TABLE PRINT\n" + e);
        }
    }

    public static void makeByteTXT(String inputEXI) {
        try {
            System.out.println("\tMaking ByteTxt of " + inputEXI + " ****");
            byteToHex.makeByteToHex(inputEXI);
            System.out.println("\tDone making txt!!!!");
        } catch (Exception e) {
            System.out.println("ERROR BYTE TEXT CONVERT\n" + e);
        }
    }

    public static void writeMyXML(String outXML, String inputEXI) {
        System.out.println("\n****DECODING WITH MINE to XML (" + inputEXI + " -> " + outXML + ")****");
        try {
            EXI_abstractInput xmlOUT = new EXI_ByteAlignedInput(new FileInputStream(new File(inputEXI)), inputEXI);
            grammars = new GrammarRunner(outXML, xmlOUT, verboseGrammar);
            xmlOUT.cleanAndClose();
            System.out.println("\tDone my XML write!!!");
        } catch (Exception e) {
            System.out.println(e + "\nERROR WRITING XML");
        }
    }

    public static void writeMyEXI(String inputXML, String outputEXI) {
        System.out.println("\n****ENCODING WITH MINE to EXI (" + inputXML + ")****");
        try {
            EXI_OutputStreamIface exiOUT = new EXI_ByteAlignedOutput(new FileOutputStream(new File(outputEXI)), outputEXI);
            grammars = new GrammarRunner(inputXML, exiOUT, verboseGrammar);
            exiOUT.cleanAndClose();
            if (makeTXT) makeByteTXT(outputEXI);
            System.out.println("\tDone my EXI write!!!");
        } catch (Exception e) {
            System.out.println("ERROR WRITING MY EXI\n" + e);
        }
    }
}
