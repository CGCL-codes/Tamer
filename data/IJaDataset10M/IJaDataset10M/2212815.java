package nz.ac.massey.xmldad.bookcatalogue.impl.runtime;

import java.io.IOException;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.helpers.AbstractUnmarshallerImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.sun.xml.bind.DatatypeConverterImpl;
import com.sun.xml.bind.unmarshaller.DOMScanner;
import com.sun.xml.bind.unmarshaller.InterningXMLReader;
import com.sun.xml.bind.validator.DOMLocator;
import com.sun.xml.bind.validator.Locator;
import com.sun.xml.bind.validator.SAXLocator;

/**
 * Default Unmarshall implementation.
 * 
 * <p>
 * This class can be extended by the generated code to provide
 * type-safe unmarshall methods.
 *
 * @author
 *  <a href="mailto:kohsuke.kawaguchi@sun.com">Kohsuke KAWAGUCHI</a>
 */
public class UnmarshallerImpl extends AbstractUnmarshallerImpl {

    /** parent JAXBContext object that created this unmarshaller */
    private DefaultJAXBContextImpl context = null;

    private final GrammarInfo grammarInfo;

    public UnmarshallerImpl(DefaultJAXBContextImpl context, GrammarInfo gi) {
        this.context = context;
        this.grammarInfo = gi;
        DatatypeConverter.setDatatypeConverter(DatatypeConverterImpl.theInstance);
    }

    public void setValidating(boolean validating) throws JAXBException {
        super.setValidating(validating);
        if (validating == true) context.getGrammar();
    }

    public UnmarshallerHandler getUnmarshallerHandler() {
        return new InterningUnmarshallerHandler(createUnmarshallerHandler(new SAXLocator()));
    }

    /**
     * Creates and configures a new unmarshalling pipe line.
     * Depending on the setting, we put a validator as a filter.
     * 
     * @return
     *      A component that implements both UnmarshallerHandler
     *      and ValidationEventHandler. All the parsing errors
     *      should be reported to this error handler for the unmarshalling
     *      process to work correctly.
     * 
     * @param locator
     *      The object that is responsible to obtain the source
     *      location information for {@link ValidationEvent}s.
     */
    private SAXUnmarshallerHandler createUnmarshallerHandler(Locator locator) {
        SAXUnmarshallerHandler unmarshaller = new SAXUnmarshallerHandlerImpl(this, grammarInfo);
        try {
            if (isValidating()) {
                unmarshaller = ValidatingUnmarshaller.create(context.getGrammar(), unmarshaller, locator);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return unmarshaller;
    }

    protected Object unmarshal(XMLReader reader, InputSource source) throws JAXBException {
        SAXLocator locator = new SAXLocator();
        SAXUnmarshallerHandler handler = createUnmarshallerHandler(locator);
        reader = InterningXMLReader.adapt(reader);
        reader.setContentHandler(handler);
        reader.setErrorHandler(new ErrorHandlerAdaptor(handler, locator));
        try {
            reader.parse(source);
        } catch (IOException e) {
            throw new JAXBException(e);
        } catch (SAXException e) {
            throw createUnmarshalException(e);
        }
        Object result = handler.getResult();
        reader.setContentHandler(dummyHandler);
        reader.setErrorHandler(dummyHandler);
        return result;
    }

    public final Object unmarshal(Node node) throws JAXBException {
        try {
            DOMScanner scanner = new DOMScanner();
            UnmarshallerHandler handler = new InterningUnmarshallerHandler(createUnmarshallerHandler(new DOMLocator(scanner)));
            if (node instanceof Element) scanner.parse((Element) node, handler); else if (node instanceof Document) scanner.parse(((Document) node).getDocumentElement(), handler); else throw new IllegalArgumentException();
            return handler.getResult();
        } catch (SAXException e) {
            throw createUnmarshalException(e);
        }
    }

    private static final DefaultHandler dummyHandler = new DefaultHandler();
}
