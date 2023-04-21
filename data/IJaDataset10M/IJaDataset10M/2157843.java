package wsi.ra.io;

import wsi.ra.tool.ResourceLoader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.Writer;
import java.util.Hashtable;
import org.apache.log4j.Category;

/**
 *  Contains static methods for reading data from temporary files.
 *
 * @author     wegnerj
 * @license    GPL
 * @cvsversion    $Revision: 1.5 $, $Date: 2004/09/13 07:16:54 $
 */
public class BatchScriptReplacer {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("wsi.ra.io.BatchScriptReplacer");

    private static BatchScriptReplacer scriptReplacer;

    private static char DEFAULT_QUOTING_CHARACTER = '?';

    private char quotingCharacter = DEFAULT_QUOTING_CHARACTER;

    /**
     *  Constructor for the BatchScriptReplacer object
     */
    private BatchScriptReplacer() {
    }

    /**
     *  Sets the quoteCharacter attribute of the BatchScriptReplacer object
     *
     *@param  _quotingCharacter  The new quoteCharacter value
     */
    public void setQuoteCharacter(char _quotingCharacter) {
        quotingCharacter = _quotingCharacter;
    }

    /**
     *  Gets the quoteCharacter attribute of the BatchScriptReplacer object
     *
     *@return    The quoteCharacter value
     */
    public char getQuoteCharacter() {
        return quotingCharacter;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public static synchronized BatchScriptReplacer instance() {
        if (scriptReplacer == null) {
            scriptReplacer = new BatchScriptReplacer();
        }
        return scriptReplacer;
    }

    /**
     *  Description of the Method
     *
     *@param  reader     Description of the Parameter
     *@param  writer     Description of the Parameter
     *@param  variables  Description of the Parameter
     *@return            Description of the Return Value
     */
    public boolean createBatchFile(Reader reader, Writer writer, Hashtable variables) {
        if ((reader == null) || (writer == null) || (variables == null)) {
            return false;
        }
        try {
            BufferedReader in = new BufferedReader(reader);
            StreamTokenizer tin = new StreamTokenizer(in);
            tin.eolIsSignificant(false);
            final int quoteChar = (int) quotingCharacter;
            tin.resetSyntax();
            tin.wordChars(' ', 255);
            tin.whitespaceChars(0, ' ');
            tin.quoteChar(quoteChar);
            tin.eolIsSignificant(true);
            int type;
            String outString = "";
            String eol = System.getProperty("line.separator");
            boolean quoted = false;
            boolean newLine = true;
            String space = " ";
            String noSpace = new String();
            String usedSpace = null;
            String variable;
            while ((type = tin.nextToken()) != StreamTokenizer.TT_EOF) {
                if (quoted || newLine) {
                    usedSpace = noSpace;
                    quoted = false;
                    newLine = false;
                } else {
                    usedSpace = space;
                }
                outString = "";
                switch(type) {
                    case StreamTokenizer.TT_NUMBER:
                        writer.write(usedSpace);
                        writer.write(Double.toString(tin.nval));
                        break;
                    case StreamTokenizer.TT_WORD:
                        writer.write(usedSpace);
                        writer.write(tin.sval);
                        break;
                    case StreamTokenizer.TT_EOL:
                        writer.write(eol);
                        newLine = true;
                        break;
                }
                if (type == quoteChar) {
                    variable = outString = tin.sval;
                    quoted = true;
                    if (outString.lastIndexOf("\n") == -1) {
                        outString = "" + (String) variables.get(variable);
                    }
                    if (outString == null) {
                        logger.error("Could not generate new batch file, the user variable '" + "' " + variable + " is not defined.");
                        return false;
                    }
                    writer.write(outString);
                }
            }
            reader.close();
            writer.close();
        } catch (Exception e) {
            logger.error("Could not generate new batch file...");
            return false;
        }
        return true;
    }

    /**
     *  Description of the Method
     *
     *@param  resourceLocation  Description of the Parameter
     *@param  outputFile        Description of the Parameter
     *@param  variables         Description of the Parameter
     *@return                   Description of the Return Value
     */
    public boolean fromResource(String resourceLocation, String outputFile, Hashtable variables) throws IOException {
        String batchFile = null;
        byte[] batchFileChars = null;
        try {
            batchFileChars = ResourceLoader.instance().getBytesFromResourceLocation(resourceLocation);
        } catch (Exception e) {
            logger.error("Could not find batch file " + resourceLocation + " in resource path...");
            return false;
        }
        if (batchFileChars == null) {
            return false;
        } else {
            batchFile = String.valueOf(batchFileChars);
            StringReader sreader = new StringReader(batchFile);
            logger.debug("Try to create batch file: " + outputFile);
            FileWriter writer = new FileWriter(outputFile);
            createBatchFile(sreader, writer, variables);
        }
        return true;
    }
}
