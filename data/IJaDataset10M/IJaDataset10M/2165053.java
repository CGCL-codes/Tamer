package net.community.chest.io.sax;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.community.chest.io.xml.BaseIOTransformer;
import net.community.chest.xml.transform.TransformerUtil;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <P>Copyright 2010 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since May 12, 2010 9:28:03 AM
 */
public abstract class AbstractIOTransformer extends BaseIOTransformer {

    protected AbstractIOTransformer() {
        super();
    }

    public static final String SAX_PARSER_FACTORY_NAME_PROP = "x-sax-parser-factory-name";

    public String getSAXParserFactoryName() {
        return getOutputProperty(SAX_PARSER_FACTORY_NAME_PROP);
    }

    public void setSAXParserFactoryName(String name) {
        setOutputProperty(SAX_PARSER_FACTORY_NAME_PROP, name);
    }

    protected SAXParserFactory getSAXParserFactory() {
        final String facName = getSAXParserFactoryName();
        if ((null == facName) || (facName.length() <= 0)) return SAXParserFactory.newInstance();
        final Thread curThread = Thread.currentThread();
        final ClassLoader cl = curThread.getContextClassLoader();
        return SAXParserFactory.newInstance(facName, cl);
    }

    protected SAXParser getSAXParser() throws ParserConfigurationException, SAXException {
        final SAXParserFactory fac = getSAXParserFactory();
        if (null == fac) throw new ParserConfigurationException("No " + SAXParserFactory.class.getSimpleName() + " available");
        return fac.newSAXParser();
    }

    public abstract void transform(InputSource src, Appendable w) throws TransformerException, IOException;

    public void transform(InputSource src, OutputStream out) throws TransformerException, IOException {
        final Writer w = ((null == src) || (null == out)) ? null : new OutputStreamWriter(out);
        transform(src, w);
        if (w != null) w.flush();
    }

    public void transform(StreamSource xmlSource, StreamResult outputTarget) throws TransformerException {
        final InputSource src = TransformerUtil.resolveInputSource(xmlSource);
        final Closeable tgt = TransformerUtil.resolveOutput(outputTarget, getURLOpenTimeout());
        final Appendable a = (tgt instanceof Appendable) ? (Appendable) tgt : null;
        final OutputStream o = ((null == a) && (tgt instanceof OutputStream)) ? (OutputStream) tgt : null;
        try {
            if (a != null) transform(src, a); else if (o != null) transform(src, o); else throw new TransformerException("No output provided");
        } catch (IOException e) {
            throw new TransformerException(e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void transform(Source xmlSource, Result outputTarget) throws TransformerException {
        if ((null == xmlSource) || (!(xmlSource instanceof StreamSource))) throw new TransformerException("Null/non-" + StreamSource.class.getSimpleName() + " XML " + Source.class.getSimpleName());
        if ((null == outputTarget) || (!(outputTarget instanceof StreamResult))) throw new TransformerException("Null/non-" + StreamResult.class.getSimpleName() + " output " + Result.class.getSimpleName());
        transform((StreamSource) xmlSource, (StreamResult) outputTarget);
    }
}
