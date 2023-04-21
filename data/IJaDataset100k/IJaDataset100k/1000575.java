package net.sf.xpontus.utils;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringReader;

/**
 * A simple entity resolver which does absolutely nothing
 * It allows a fast verification of an XML Document without resolving entities
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class NullEntityResolver implements EntityResolver {

    private static final NullEntityResolver INSTANCE = new NullEntityResolver();

    private final InputSource _source;

    private NullEntityResolver() {
        _source = new InputSource();
    }

    /**  @return a new instance of this class  */
    public static NullEntityResolver getInstance() {
        return INSTANCE;
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        _source.setCharacterStream(new StringReader(""));
        return _source;
    }
}
