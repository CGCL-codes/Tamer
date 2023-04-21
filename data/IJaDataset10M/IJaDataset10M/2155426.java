package com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime;

import javax.xml.bind.ValidationEvent;
import org.relaxng.datatype.Datatype;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import com.sun.msv.grammar.IDContextProvider2;
import com.sun.msv.util.LightStack;
import com.sun.msv.util.StartTagInfo;
import com.sun.msv.util.StringRef;
import com.sun.msv.verifier.Acceptor;
import com.sun.msv.verifier.regexp.StringToken;
import com.sun.xml.bind.JAXBAssertionError;
import com.sun.xml.bind.JAXBObject;
import com.sun.xml.bind.RIElement;
import com.sun.xml.bind.marshaller.IdentifiableObject;
import com.sun.xml.bind.serializer.AbortSerializationException;
import com.sun.xml.bind.serializer.Util;
import com.sun.xml.bind.validator.Messages;

/**
 * XMLSerializer that calls the native interface of MSV and performs validation.
 * Used in a pair with a ValidationContext.
 * 
 * @author  Kohsuke Kawaguchi
 */
public class MSVValidator implements XMLSerializer, IDContextProvider2 {

    /** Current acceptor in use. */
    private Acceptor acceptor;

    /** Context object that coordinates the entire validation effort. */
    private final ValidationContext context;

    /** The object which we are validating. */
    private final ValidatableObject target;

    final DefaultJAXBContextImpl jaxbContext;

    /**
     * Acceptor stack. Whenever an element is found, the current acceptor is
     * pushed to the stack and new one is created.
     * 
     * LightStack is a light-weight stack implementation
     */
    private final LightStack stack = new LightStack();

    public NamespaceContext2 getNamespaceContext() {
        return context.getNamespaceContext();
    }

    /**
     * To use this class, call the static validate method.
     */
    private MSVValidator(DefaultJAXBContextImpl _jaxbCtx, ValidationContext _ctxt, ValidatableObject vo) {
        jaxbContext = _jaxbCtx;
        acceptor = vo.createRawValidator().createAcceptor();
        context = _ctxt;
        target = vo;
    }

    /**
     * Validates the specified object and reports any error to the context.
     */
    public static void validate(DefaultJAXBContextImpl jaxbCtx, ValidationContext context, ValidatableObject vo) throws SAXException {
        try {
            new MSVValidator(jaxbCtx, context, vo)._validate();
        } catch (RuntimeException e) {
            context.reportEvent(vo, e);
        }
    }

    /** performs the validation to the object specified in the constructor. */
    private void _validate() throws SAXException {
        context.getNamespaceContext().startElement();
        target.serializeURIs(this);
        endNamespaceDecls();
        target.serializeAttributes(this);
        endAttributes();
        target.serializeBody(this);
        writePendingText();
        context.getNamespaceContext().endElement();
        if (!acceptor.isAcceptState(null)) {
            StringRef ref = new StringRef();
            acceptor.isAcceptState(ref);
            context.reportEvent(target, ref.str);
        }
    }

    public void endNamespaceDecls() throws SAXException {
        context.getNamespaceContext().endNamespaceDecls();
    }

    public void endAttributes() throws SAXException {
        if (!acceptor.onEndAttributes(null, null)) {
            StringRef ref = new StringRef();
            StartTagInfo sti = new StartTagInfo(currentElementUri, currentElementLocalName, currentElementLocalName, emptyAttributes, this);
            acceptor.onEndAttributes(sti, ref);
            context.reportEvent(target, ref.str);
        }
    }

    /** stores text reported by the text method. */
    private StringBuffer buf = new StringBuffer();

    public final void text(String text, String fieldName) throws SAXException {
        if (text == null) {
            reportMissingObjectError(fieldName);
            return;
        }
        if (buf.length() != 0) buf.append(' ');
        buf.append(text);
    }

    public void reportMissingObjectError(String fieldName) throws SAXException {
        reportError(Util.createMissingObjectError(target, fieldName));
    }

    private String attNamespaceUri;

    private String attLocalName;

    private boolean insideAttribute;

    public void startAttribute(String uri, String local) {
        this.attNamespaceUri = uri;
        this.attLocalName = local;
        insideAttribute = true;
    }

    public void endAttribute() throws SAXException {
        insideAttribute = false;
        if (!acceptor.onAttribute2(attNamespaceUri, attLocalName, attLocalName, buf.toString(), this, null, null)) {
            StringRef ref = new StringRef();
            acceptor.onAttribute2(attNamespaceUri, attLocalName, attLocalName, buf.toString(), this, ref, null);
            context.reportEvent(target, ref.str);
        }
        buf = new StringBuffer();
    }

    private void writePendingText() throws SAXException {
        if (!acceptor.onText2(buf.toString(), this, null, null)) {
            StringRef ref = new StringRef();
            acceptor.onText2(buf.toString(), this, ref, null);
            context.reportEvent(target, ref.str);
        }
        if (buf.length() > 1024) buf = new StringBuffer(); else buf.setLength(0);
    }

    private String currentElementUri;

    private String currentElementLocalName;

    public void startElement(String uri, String local) throws SAXException {
        writePendingText();
        context.getNamespaceContext().startElement();
        stack.push(acceptor);
        StartTagInfo sti = new StartTagInfo(uri, local, local, emptyAttributes, this);
        Acceptor child = acceptor.createChildAcceptor(sti, null);
        if (child == null) {
            StringRef ref = new StringRef();
            child = acceptor.createChildAcceptor(sti, ref);
            context.reportEvent(target, ref.str);
        }
        this.currentElementUri = uri;
        this.currentElementLocalName = local;
        acceptor = child;
    }

    public void endElement() throws SAXException {
        writePendingText();
        if (!acceptor.isAcceptState(null)) {
            StringRef ref = new StringRef();
            acceptor.isAcceptState(ref);
            context.reportEvent(target, ref.str);
        }
        Acceptor child = acceptor;
        acceptor = (Acceptor) stack.pop();
        if (!acceptor.stepForward(child, null)) {
            StringRef ref = new StringRef();
            acceptor.stepForward(child, ref);
            context.reportEvent(target, ref.str);
        }
        context.getNamespaceContext().endElement();
    }

    public void childAsAttributes(JAXBObject o, String fieldName) throws SAXException {
    }

    public void childAsURIs(JAXBObject o, String fieldName) throws SAXException {
    }

    /** An empty <code>Attributes</code> object. */
    private static final AttributesImpl emptyAttributes = new AttributesImpl();

    /** namespace URI of dummy elements. TODO: allocate one namespace URI for this. */
    public static final String DUMMY_ELEMENT_NS = "http://java.sun.com/jaxb/xjc/dummy-elements";

    public void childAsBody(JAXBObject o, String fieldName) throws SAXException {
        final ValidatableObject vo = jaxbContext.getGrammarInfo().castToValidatableObject(o);
        if (vo == null) {
            reportMissingObjectError(fieldName);
            return;
        }
        if (insideAttribute) childAsAttributeBody(vo, fieldName); else childAsElementBody(o, vo);
    }

    private void childAsElementBody(Object o, ValidatableObject vo) throws SAXException {
        String intfName = vo.getPrimaryInterface().getName();
        intfName = intfName.replace('$', '.');
        StartTagInfo sti = new StartTagInfo(DUMMY_ELEMENT_NS, intfName, intfName, emptyAttributes, this);
        Acceptor child = acceptor.createChildAcceptor(sti, null);
        if (child == null) {
            StringRef ref = new StringRef();
            child = acceptor.createChildAcceptor(sti, ref);
            context.reportEvent(target, ref.str);
        }
        if (o instanceof RIElement) {
            RIElement rie = (RIElement) o;
            if (!child.onAttribute2(rie.____jaxb_ri____getNamespaceURI(), rie.____jaxb_ri____getLocalName(), rie.____jaxb_ri____getLocalName(), "", null, null, null)) context.reportEvent(target, Messages.format(Messages.INCORRECT_CHILD_FOR_WILDCARD, rie.____jaxb_ri____getNamespaceURI(), rie.____jaxb_ri____getLocalName()));
        }
        child.onEndAttributes(sti, null);
        if (!acceptor.stepForward(child, null)) {
            throw new JAXBAssertionError();
        }
        context.validate(vo);
    }

    private void childAsAttributeBody(ValidatableObject vo, String fieldName) throws SAXException {
        text(" " + vo.getPrimaryInterface().getName(), fieldName);
        context.validate(vo);
    }

    public void reportError(ValidationEvent e) throws AbortSerializationException {
        context.reportEvent(target, e);
    }

    public String onID(IdentifiableObject owner, String value) throws SAXException {
        return context.onID(target, value);
    }

    public String onIDREF(IdentifiableObject value) throws SAXException {
        return context.onIDREF(target, value.____jaxb____getId());
    }

    public String getBaseUri() {
        return null;
    }

    public boolean isUnparsedEntity(String entityName) {
        return true;
    }

    public boolean isNotation(String notation) {
        return true;
    }

    public void onID(Datatype dt, StringToken s) {
    }

    public String resolveNamespacePrefix(String prefix) {
        return context.getNamespaceContext().getNamespaceURI(prefix);
    }
}
