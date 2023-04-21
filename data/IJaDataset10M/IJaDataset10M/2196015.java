package edu.udo.cs.wvtool.generic.inputfilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * Very simple tag ignoring reader. The encoding is determined using information from the document info and not from the xml document itself.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class SimpleTagIgnoringReader implements WVTInputFilter {

    public Reader convertToPlainText(InputStream source, WVTDocumentInfo d) throws WVToolException {
        Reader reader = null;
        try {
            reader = new InputStreamReader(source, d.getContentEncoding());
        } catch (UnsupportedEncodingException e) {
            reader = null;
            WVToolLogger.getGlobalLogger().logMessage("Warning: Encoding " + d.getContentEncoding() + " unknown. Using default.", WVToolLogger.WARNING);
        }
        if (reader == null) {
            reader = new InputStreamReader(source);
        }
        StringBuffer textBuf = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(reader);
            String buf = null;
            while ((buf = in.readLine()) != null) {
                textBuf.append(buf);
            }
            in.close();
        } catch (IOException e) {
            throw new WVToolException("Could not read document " + d.getSourceName());
        }
        int pos1 = 0;
        int pos2 = 0;
        do {
            pos1 = textBuf.indexOf("<");
            pos2 = textBuf.indexOf(">", pos1);
            if ((pos1 >= 0) && (pos2 > pos1)) textBuf.delete(pos1, pos2 + 1);
        } while ((pos1 >= 0) && (pos2 > pos1));
        return new StringReader(textBuf.toString() + " ");
    }
}
