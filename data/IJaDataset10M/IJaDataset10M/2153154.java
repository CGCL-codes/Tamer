package org.gvsig.gpe.xml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.IXmlStreamReaderFactory;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;
import org.gvsig.gpe.xml.stream.IXmlStreamWriterFactory;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.stream.kxml.KxmlXmlParserFactory;
import org.gvsig.gpe.xml.stream.stax.StaxXmlStreamWriter;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GPEXmlParserFactory {

    private static ClassLoader classLoader = null;

    /**
	 * @param classLoader the classLoader to set
	 */
    public static void setClassLoader(ClassLoader classLoader) {
        GPEXmlParserFactory.classLoader = classLoader;
    }

    private static Iterator availableParserFactories() {
        if (classLoader != null) {
            return sun.misc.Service.providers(IXmlStreamReaderFactory.class, classLoader);
        } else {
            return sun.misc.Service.providers(IXmlStreamReaderFactory.class);
        }
    }

    /**
     * @return the parser
     * @throws XMLStreamException
     */
    public static IXmlStreamReader getParser(final String mimeType, final InputStream in) throws XmlStreamException {
        Iterator parserFactories = availableParserFactories();
        IXmlStreamReaderFactory factory;
        while (parserFactories.hasNext()) {
            factory = (IXmlStreamReaderFactory) parserFactories.next();
            if (factory.canParse(mimeType)) {
                return factory.createParser(mimeType, in);
            }
        }
        return new KxmlXmlParserFactory().createParser(mimeType, in);
    }

    private static Iterator availableWriterFactories() {
        Iterator providers = sun.misc.Service.providers(IXmlStreamWriterFactory.class);
        return providers;
    }

    public static IXmlStreamWriter getWriter(final String mimeType, final OutputStream os) throws XmlStreamException, IllegalArgumentException {
        Iterator writerFactories = availableWriterFactories();
        IXmlStreamWriterFactory factory;
        while (writerFactories.hasNext()) {
            factory = (IXmlStreamWriterFactory) writerFactories.next();
            if (factory.canWrite(mimeType)) {
                return factory.createWriter(mimeType, os);
            }
        }
        return new StaxXmlStreamWriter(os);
    }
}
