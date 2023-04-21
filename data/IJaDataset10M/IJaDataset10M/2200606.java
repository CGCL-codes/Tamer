package org.dozer.vo.jaxb.employee.impl.runtime;

import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEvent;
import org.iso_relax.verifier.impl.ForkContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import com.sun.msv.grammar.Grammar;
import com.sun.msv.verifier.Verifier;
import com.sun.msv.verifier.VerifierFilter;
import com.sun.msv.verifier.regexp.REDocumentDeclaration;
import com.sun.xml.bind.validator.Locator;

/**
 * Filter implementation of SAXUnmarshallerHandler.
 * 
 * <p>
 * This component internally uses a VerifierFilter to validate SAX events that goes through this component. Discovered
 * error information is just passed down to the next component.
 * 
 * <p>
 * This will enable the implementation to validate all sources of SAX events in the RI - XMLReader, DOMScanner
 * 
 * SAX events will go the VerifierFilter and then to the SAXUnmarshaller...
 * 
 */
public class ValidatingUnmarshaller extends ForkContentHandler implements SAXUnmarshallerHandler {

    /**
   * Creates a new instance of ValidatingUnmarshaller.
   */
    public static ValidatingUnmarshaller create(Grammar grammar, SAXUnmarshallerHandler _core, Locator locator) {
        Verifier v = new Verifier(new REDocumentDeclaration(grammar), new ErrorHandlerAdaptor(_core, locator));
        v.setPanicMode(true);
        return new ValidatingUnmarshaller(new VerifierFilter(v), _core);
    }

    private ValidatingUnmarshaller(VerifierFilter filter, SAXUnmarshallerHandler _core) {
        super(filter, _core);
        this.core = _core;
    }

    public Object getResult() throws JAXBException, IllegalStateException {
        return core.getResult();
    }

    public void handleEvent(ValidationEvent event, boolean canRecover) throws SAXException {
        core.handleEvent(event, canRecover);
    }

    private final SAXUnmarshallerHandler core;

    private final AttributesImpl xsiLessAtts = new AttributesImpl();

    @Override
    public void startElement(String nsUri, String local, String qname, Attributes atts) throws SAXException {
        xsiLessAtts.clear();
        int len = atts.getLength();
        for (int i = 0; i < len; i++) {
            String aUri = atts.getURI(i);
            String aLocal = atts.getLocalName(i);
            if (aUri.equals("http://www.w3.org/2001/XMLSchema-instance") && (aLocal.equals("schemaLocation") || aLocal.equals("noNamespaceSchemaLocation"))) {
                continue;
            }
            xsiLessAtts.addAttribute(aUri, aLocal, atts.getQName(i), atts.getType(i), atts.getValue(i));
        }
        super.startElement(nsUri, local, qname, xsiLessAtts);
    }
}
