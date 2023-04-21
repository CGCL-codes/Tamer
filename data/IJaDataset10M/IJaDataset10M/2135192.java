package ingenias.generator.interpreter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ingenias.exception.*;
import ingenias.generator.datatemplate.*;

public class SplitHandler {

    /**
	 *  Constructor for the TemplateHandler object
	 *
	 *@param  xmlFile        Description of Parameter
	 *@exception  Exception  Description of Exception
	 */
    public SplitHandler(String xmlFile) throws FileTagEmpty, TextTagEmpty, java.io.IOException, SAXException {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(xmlFile);
            Document document = parser.getDocument();
            NodeList children = document.getChildNodes();
            for (int k = 0; k < children.getLength(); k++) {
                traverse(children.item(k));
            }
        } catch (SAXException e) {
            throw e;
        } catch (java.io.UTFDataFormatException formatEx) {
            ingenias.editor.Log.getInstance().logERROR("Error interpreting a INGENIAS file");
            ingenias.editor.Log.getInstance().logERROR("The text contained in " + xmlFile + " contains non UTF-8 characters");
            ingenias.generator.util.FormatVerifier.verify(xmlFile);
            throw formatEx;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
	 *  Description of the Method
	 *
	 *@param  node           Description of Parameter
	 *@return                The text value
	 */
    private StringBuffer getText(Node node) {
        StringBuffer text = new StringBuffer();
        int type = node.getNodeType();
        if (type == Node.ELEMENT_NODE) {
            return new StringBuffer();
        } else {
            return new StringBuffer(node.getNodeValue());
        }
    }

    public static String decodeSpecialSymbols(String text) {
        try {
            String s = text;
            s = ingenias.generator.util.Conversor.restoreInvalidChar(text);
            return s;
        } catch (Exception uee) {
            uee.printStackTrace();
        }
        return "";
    }

    /**
	 *  Description of the Method
	 *
	 *@param  node           Description of Parameter
	 *@exception  Exception  Description of Exception
	 */
    private void traverse(Node node) throws FileTagEmpty, TextTagEmpty, java.io.IOException {
        int type = node.getNodeType();
        if (type == Node.ELEMENT_NODE) {
            if (node.getNodeName().equalsIgnoreCase("saveto")) {
                NodeList nl = node.getChildNodes();
                String fid = null;
                StringBuffer text = new StringBuffer();
                boolean overwrite = true;
                int k = 0;
                while ((fid == null || text.length() == 0) && k < nl.getLength()) {
                    if (nl.item(k).getNodeName().equalsIgnoreCase("file") && nl.item(k).getChildNodes().getLength() >= 1) {
                        fid = nl.item(k).getChildNodes().item(0).getNodeValue();
                        if (nl.item(k).getAttributes().getNamedItem("overwrite") != null) overwrite = nl.item(k).getAttributes().getNamedItem("overwrite").getNodeValue().equalsIgnoreCase("yes");
                    }
                    if (nl.item(k).getNodeName().equalsIgnoreCase("text") && nl.item(k).getChildNodes().getLength() >= 1) {
                        for (int j = 0; j < nl.item(k).getChildNodes().getLength(); j++) {
                            text.append(getText(nl.item(k).getChildNodes().item(j)));
                        }
                    }
                    k = k + 1;
                }
                if (fid == null) {
                    throw new FileTagEmpty(" At " + node + ". First tag must be <file>FILENAME</file> and now it is " + nl.item(1).getNodeName());
                }
                if (text.length() == 0) {
                    throw new TextTagEmpty(" At " + node + ". Second tag must be <text>TEXTTOWRITE</text> and now it is " + nl.item(3).getNodeName());
                }
                fid = fid.replace('\n', ' ').trim();
                try {
                    File f = new File(fid);
                    if (!f.exists() || overwrite) {
                        ingenias.editor.Log.getInstance().logSYS("-------------" + f);
                        ingenias.editor.Log.getInstance().logSYS("writing to " + fid);
                        this.createPath(f);
                        FileOutputStream fos = new FileOutputStream(fid);
                        PrintWriter pw = new PrintWriter(fos);
                        pw.print(decodeSpecialSymbols(text.toString()));
                        pw.flush();
                        pw.close();
                    } else ingenias.editor.Log.getInstance().logWARNING("I won't overwrite file " + fid);
                } catch (IOException ioe) {
                    throw new java.io.IOException("At " + node + ". Could not execute a <saveto> tag. It was not possible to write in the file " + fid);
                }
            }
            if (node.getNodeName().equalsIgnoreCase("program")) {
                NodeList children = node.getChildNodes();
                if (children != null) {
                    for (int i = 0; i < children.getLength(); i++) {
                        traverse(children.item(i));
                    }
                }
            }
        }
    }

    private void createPath(File f) throws IOException {
        new File(f.getParent()).mkdirs();
    }

    /**
	 *  The main program for the SplitHandler class
	 *
	 *@param  args           The command line arguments
	 *@exception  Exception  Description of Exception
	 */
    public static void main(String[] args) throws Exception {
        SplitHandler sd = new SplitHandler(args[0]);
    }
}
