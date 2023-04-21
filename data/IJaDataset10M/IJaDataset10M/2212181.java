package org.gvsig.gpe.xml.stream.kxml;

import java.io.InputStream;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.IXmlStreamReaderFactory;
import org.gvsig.gpe.xml.stream.XmlStreamException;

/**
 * @author Jorge Piera Llodr� (jorge.piera@iver.es)
 */
public class KxmlXmlParserFactory implements IXmlStreamReaderFactory {

    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReaderFactory#canParse(java.lang.String)
     */
    public boolean canParse(String mimeType) {
        return mimeType.startsWith("text/xml");
    }

    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReaderFactory#createParser(java.lang.String,
     *      java.io.InputStream)
     */
    public IXmlStreamReader createParser(String mimeType, final InputStream in) throws XmlStreamException, IllegalArgumentException {
        if (!canParse(mimeType)) {
            throw new IllegalArgumentException("Unsupported mime type for this reader factory: " + mimeType);
        }
        KxmlXmlStreamReader reader = new KxmlXmlStreamReader();
        reader.setInput(in);
        return reader;
    }
}
