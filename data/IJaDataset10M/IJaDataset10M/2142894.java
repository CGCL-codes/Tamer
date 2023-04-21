package jomm.utils;

import org.dom4j.Document;
import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

/**
 * @author Jorge Machado
 * @date 26/Nov/2009
 * @time 15:42:54
 * @email machadofisher@gmail.com
 */
public class XsltMain {

    private static final Logger logger = Logger.getLogger(XsltMain.class);

    public static void main(String[] args) throws Exception {
        Op operation = null;
        String xmlFile = null;
        String xslFile = null;
        String outputFile = null;
        String xmlEnc = null;
        String outputEnc = null;
        OutputType outputType = OutputType.other;
        if (args.length == 1 && args[0].equals("--help")) XsltMain.printUsage();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-op")) {
                operation = Op.parse(args[i + 1]);
                i++;
            } else if (args[i].equals("-xml")) {
                xmlFile = args[i + 1];
                i++;
            } else if (args[i].equals("-xsl")) {
                xslFile = args[i + 1].replaceAll("\\\\", "/");
                i++;
            } else if (args[i].equals("-xmlEnc")) {
                xmlEnc = args[i + 1];
                i++;
            } else if (args[i].equals("-o")) {
                outputFile = args[i + 1];
                i++;
            } else if (args[i].equals("-oEnc")) {
                outputEnc = args[i + 1];
                i++;
            } else if (args[i].equals("-oType")) {
                outputType = OutputType.parse(args[i + 1]);
                i++;
            }
        }
        if (operation == Op.transform) {
            if (xmlFile == null || xslFile == null) XsltMain.printUsage(); else {
                Document xmlDoc;
                if (xmlEnc != null) xmlDoc = XmlUtils.parse(new FileInputStream(xmlFile), xmlEnc); else xmlDoc = XmlUtils.parse(new FileInputStream(xmlFile));
                if (outputType == OutputType.xml && outputEnc != null && outputFile != null) {
                    Document doc;
                    if (xslFile.startsWith("classpath://")) {
                        doc = XmlUtils.styleDocument(xmlDoc, xslFile.substring("classpath://".length()), true, null);
                    } else doc = XmlUtils.styleDocument(xmlDoc, xslFile, false, null);
                    XmlUtils.write(doc, new FileWriter(outputFile), outputEnc);
                } else if (outputType == OutputType.other && outputEnc == null && outputFile == null) {
                    if (xslFile.startsWith("classpath://")) XmlUtils.styleDocument(xmlDoc, xslFile.substring("classpath://".length()), true, null, System.out); else XmlUtils.styleDocument(xmlDoc, xslFile, false, null, System.out);
                } else if (outputType == OutputType.other && outputFile != null) {
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    if (xslFile.startsWith("classpath://")) XmlUtils.styleDocument(xmlDoc, xslFile.substring("classpath://".length()), true, null, fos); else XmlUtils.styleDocument(xmlDoc, xslFile, false, null, fos);
                    fos.close();
                } else {
                    System.out.println("Not implemented yet");
                }
            }
        }
    }

    private static void printUsage() {
        System.out.println("java -jar jmachado-utils.jar -type <operation> [-OPTION value]*");
        System.out.println("Operations: {transform}");
        System.out.println("Options:");
        System.out.println("-xml: XML inputFile");
        System.out.println("-xmlEnc: XML encoding");
        System.out.println("-xsl: XSL inputFile");
        System.out.println("-o: output file");
        System.out.println("-oEnc: output file encoding");
    }

    public static enum Op {

        transform("transform");

        String op;

        private Op(String op) {
            this.op = op;
        }

        public static Op parse(String op) {
            for (Op operation : XsltMain.Op.values()) {
                if (op.equals(operation.op)) return operation;
            }
            return null;
        }
    }

    public static enum OutputType {

        xml("xml"), html("html"), other("other");

        String type;

        private OutputType(String type) {
            this.type = type;
        }

        public static OutputType parse(String op) {
            for (OutputType operation : XsltMain.OutputType.values()) {
                if (op.equals(operation.type)) return operation;
            }
            return null;
        }
    }
}
