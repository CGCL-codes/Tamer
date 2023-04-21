package edu.udo.cs.wvtool.generic.loader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolIOException;

/**
 * This loader simply uses the defined source as text. It is a sort of hack in order to allow reading text directly from string attributes of Yale.
 * 
 * @author Ingo Mierswa, Michael Wurst
 * @version $Id: SourceAsTextLoader.java,v 1.5 2007/05/22 16:38:58 mjwurst Exp $
 */
public class SourceAsTextLoader implements WVTDocumentLoader {

    private InputStream stream;

    /** Open the document and return an input stream on it. */
    public InputStream loadDocument(WVTDocumentInfo d) {
        byte[] bArray = d.getSourceName().getBytes();
        stream = new ByteArrayInputStream(bArray);
        return stream;
    }

    /** Close the resource from which the given document has been read. */
    public void close(WVTDocumentInfo d) throws WVToolException {
        try {
            stream.close();
        } catch (IOException e) {
            throw new WVToolIOException("Could not close stream", e);
        }
    }
}
