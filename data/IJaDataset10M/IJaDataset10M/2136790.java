package edu.udo.cs.wvtool.generic.inputfilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolIOException;
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * Read XML Input ignoring the tags. The encoding is determined using information from the document info and not from the xml document itself.
 * 
 * @author Michael Wurst
 * @version $Id: XMLInputFilter.java,v 1.4 2007/05/20 18:06:04 mjwurst Exp $
 * 
 */
public class XMLInputFilter implements WVTInputFilter {

    /**
     * @see edu.udo.cs.wvtool.generic.inputfilter.WVTInputFilter#convertToPlainText(InputStream, WVTDocumentInfo)
     */
    public Reader convertToPlainText(InputStream source, WVTDocumentInfo d) throws WVToolIOException {
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
        try {
            return new TagIgnoringReader(reader);
        } catch (IOException e) {
            WVToolLogger.getGlobalLogger().logException("Could not read stream", e);
            throw new WVToolIOException("TagIgnoringReader could not read the source:", e);
        }
    }
}
