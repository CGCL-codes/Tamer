package org.systemsbiology.apps.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Inject a namespace into an XML file
 * 
 * @author Mark Christiansen
 *
 */
public class NameSpaceInjector {

    private static Logger logger = Logger.getLogger(NameSpaceInjector.class.getName());

    private static String consensusXMLNamespaceCheck = "xsi:noNamespaceSchemaLocation=\"http://open-ms.sourceforge.net/schemas/ConsensusXML_1_4.xsd\"";

    private static String featureXMLNamespaceCheck = "xsi:noNamespaceSchemaLocation=\"http://open-ms.sourceforge.net/schemas/FeatureXML_1_4.xsd\"";

    private static String consensusXMLNamespace = " xmlns=\"http://systemsbiology.org/openms/consensusxml\">";

    private static String featureXMLNamespace = " xmlns=\"http://systemsbiology.org/openms/featurexml\">";

    /**
     * Launch the name space injector for an xml file
     * @param args parameter arguments
     */
    public static void main(String[] args) {
        int iarg = 0;
        String temp_str[];
        String option;
        String inputf = null;
        String outputf = null;
        String logf = null;
        String batchf = null;
        while (iarg < args.length) {
            option = args[iarg];
            if (option.indexOf("-inputfile") != -1) {
                temp_str = option.split("=");
                inputf = temp_str[1];
            } else if (option.indexOf("-outputfile") != -1) {
                temp_str = option.split("=");
                outputf = temp_str[1];
            } else if (option.indexOf("-logfile") != -1) {
                temp_str = option.split("=");
                logf = temp_str[1];
            } else {
                printUsage();
            }
            iarg++;
        }
        if (inputf == null && batchf == null) {
            printUsage();
        }
        if (logf == null) {
            setDefaultLoggerHandler();
        } else {
            try {
                logger.addHandler(new FileHandler(logf));
            } catch (SecurityException e) {
                setDefaultLoggerHandler();
                e.printStackTrace();
            } catch (IOException e) {
                setDefaultLoggerHandler();
                e.printStackTrace();
            }
        }
        if (batchf != null) {
        } else {
            if (outputf == null) {
                printUsage();
            }
            try {
                injectNameSpace(inputf, outputf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inject name space into an xml file to later be converted into apml
     * @param inputFileName input file without name space
     * @param outputFileName output file with name space
     * @throws Exception file not found exception
     */
    public static void injectNameSpace(String inputFileName, String outputFileName) throws Exception {
        File xmlFile = new File(inputFileName);
        if (xmlFile.isDirectory()) throw new FileNotFoundException("File is a directory");
        File outFile = new File(outputFileName);
        FileInputStream fis = new FileInputStream(xmlFile);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        FileOutputStream fos = new FileOutputStream(outFile);
        PrintWriter out = new PrintWriter(fos);
        String thisLine = "";
        int i = 1;
        while ((thisLine = in.readLine()) != null) {
            if (thisLine.contains(consensusXMLNamespaceCheck)) {
                out.println(thisLine.replace(">", consensusXMLNamespace));
            } else if (thisLine.contains(featureXMLNamespaceCheck)) {
                out.println(thisLine.replace(">", featureXMLNamespace));
            } else {
                out.println(thisLine);
            }
            i++;
        }
        out.flush();
        out.close();
        in.close();
    }

    private static void setDefaultLoggerHandler() {
        logger.addHandler(new ConsoleHandler());
    }

    private static void printUsage() {
        System.err.println("Usage: " + NameSpaceInjector.class.getName());
        System.err.println("\t-inputfile=<xml_file_name>");
        System.err.println("\t-outputfile=<xml_file_name>");
        System.err.println("\t-logfile=<log_file_name> (optional)");
        System.err.println("\n");
        System.err.println("Example: " + NameSpaceInjector.class.getName() + " inputfile=myinput.xml outputfile=myoutput.xml");
        System.exit(1);
    }
}
