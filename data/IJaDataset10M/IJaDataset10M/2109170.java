package org.arch4j.ui.webstart;

import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.ChildNode;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class wraps webstart to add parameters to a JNLP file so that the parameters
 * may be passed into the webstart program.
 *
 * @author Allan Wick
 */
public class WebstartWrapper {

    private static boolean setValidation = false;

    private static boolean setNameSpaces = true;

    private static boolean setSchemaSupport = true;

    private static boolean setDeferredDOM = true;

    /**
     * The JNLP filename.
     */
    private String filename;

    /**
     * The paramters to add to the file before starting up.
     */
    private ArrayList parameters = new ArrayList();

    /**
     * Main method used to start the process.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            showUsage();
            System.exit(1);
        }
        WebstartWrapper theWrapper = new WebstartWrapper(args[0]);
        for (int index = 1; index < args.length; index++) {
            theWrapper.addParameter(args[index]);
        }
        try {
            theWrapper.startup();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
        System.exit(0);
    }

    private static void showUsage() {
        System.out.println("Usage: <filename> <parameters>");
    }

    /**
     * Creates a new instance of WebstartWrapper.
     *
     * @param aFilename The name of the file that contains the JNLP code.
     */
    public WebstartWrapper(String aFilename) {
        filename = aFilename;
    }

    /**
     * Add a startup parameter.
     *
     * @param aParamter The parameter to add.
     */
    public void addParameter(String aParameter) {
        parameters.add(aParameter);
    }

    /**
     * Start the web application.
     */
    public void startup() throws IOException {
        String theFilename = buildTempFile();
        Runtime rt = Runtime.getRuntime();
        if (theFilename != null) {
            String[] callAndArgs = { "javaws", theFilename };
            try {
                Process child = rt.exec(callAndArgs);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * Build the new JNLP file.
     *
     * @return The full name of the temporary file.
     */
    private String buildTempFile() throws IOException {
        File theFile = null;
        try {
            DOMParser theParser = new DOMParser();
            theParser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", setDeferredDOM);
            theParser.setFeature("http://xml.org/sax/features/validation", setValidation);
            theParser.setFeature("http://xml.org/sax/features/namespaces", setNameSpaces);
            theParser.setFeature("http://apache.org/xml/features/validation/schema", setSchemaSupport);
            theParser.parse(filename);
            Document theDocument = theParser.getDocument();
            NodeList theElements = theDocument.getElementsByTagName("application-desc");
            Node theNode = theElements.item(0);
            Node theNewChild;
            String theValue;
            for (int index = 0; index < parameters.size(); index++) {
                theValue = (String) parameters.get(index);
                theNewChild = theDocument.createElement("argument");
                theNewChild.appendChild(theDocument.createTextNode(theValue));
                theNode.appendChild(theNewChild);
            }
            theFile = new File("c:/Temp/Temp.jnlp");
            PrintWriter theWriter = new PrintWriter(new FileOutputStream(theFile));
            new XMLSerializer(theWriter, new OutputFormat(theDocument)).serialize(theDocument);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return theFile.getPath();
    }
}
