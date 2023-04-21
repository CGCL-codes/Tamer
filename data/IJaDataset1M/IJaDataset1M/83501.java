package gd.xml.tiny;

import gd.xml.*;
import java.io.*;
import java.net.*;

public class TinyParser {

    /** Parses an input stream as XML.
     *  @param  is  the input stream from which the XML should be read
     *  @return the root node of a parse tree generated from the input stream */
    public static ParsedXML parseXML(InputStream is) throws ParseException {
        XMLParser xp = new XMLParser();
        TinyResponder tr = new TinyResponder(is);
        xp.parseXML(tr);
        return tr.getRootNode();
    }

    /** Parses XML encoded data into a tree.
     *  The XML is read from a <code>URL</code>
     *  @param  url the <code>URL</code> from which the XML is obtained
     *  @return the root node of the parse tree
     */
    public static ParsedXML parseXML(URL url) throws ParseException {
        try {
            InputStream is = url.openStream();
            ParsedXML px = parseXML(is);
            is.close();
            return px;
        } catch (IOException e) {
            throw new ParseException("could not read from URL" + url.toString());
        }
    }

    /** Parses XML encoded data into a tree.
     *  The XML is read from a file with the name specified.
     *  @param  fname the name of the file from which the XML is read
     *  @return the root node of the parse tree
     */
    public static ParsedXML parseXML(String fname) throws ParseException {
        try {
            InputStream is = new FileInputStream(fname);
            ParsedXML px = parseXML(is);
            is.close();
            return px;
        } catch (IOException e) {
            throw new ParseException("could not read from file " + fname);
        }
    }
}
