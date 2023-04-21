package jgd.jaxb.impl.runtime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.helpers.NotIdentifiableEventImpl;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
import org.xml.sax.SAXException;
import com.sun.xml.bind.ProxyGroup;
import com.sun.xml.bind.serializer.AbortSerializationException;
import com.sun.xml.bind.validator.Messages;

/**
 * Maintains information that needs to be stored across
 * validations of multiple objects.
 * 
 * Specifically, this object is responsible for:
 * 
 * <ol>
 *   <li>detecting a cycle in a content tree by keeping track of
 *       objects that were validated.
 * 
 *   <li>keeping an instance of NamespaceContextImpl, which is
 *       shared by all MSVValidators.
 * 
 *   <li>keeping a reference to {@link ValidationErrorHandler}.
 *       MSVValidators should use this error handler to report any error.
 * </ol>
 */
class ValidationContext {

    final DefaultJAXBContextImpl jaxbContext;

    /**
     * @param validateID
     *      if true, ID/IDREF validation will be performed.
     */
    ValidationContext(DefaultJAXBContextImpl _context, ValidationEventHandler _eventHandler, boolean validateID) {
        this.jaxbContext = _context;
        this.eventHandler = _eventHandler;
        this.validateID = validateID;
    }

    /** Set of all validated objects. Used to detect a cycle. */
    private final HashSet validatedObjects = new HashSet();

    /**
     * Validates the sub-tree rooted at <code>vo</code> and reports
     * any errors/warnings to the error handler.
     */
    public void validate(ValidatableObject vo) throws SAXException {
        if (validatedObjects.add(ProxyGroup.unwrap(vo))) {
            MSVValidator.validate(jaxbContext, this, vo);
        } else {
            reportEvent(vo, Messages.format(Messages.CYCLE_DETECTED));
        }
    }

    /** namespace context. */
    private final NamespaceContextImpl nsContext = new NamespaceContextImpl(null);

    public NamespaceContextImpl getNamespaceContext() {
        return nsContext;
    }

    /** ID/IDREF validation is done only when this flag is true. */
    private final boolean validateID;

    private final HashSet IDs = new HashSet();

    private final HashMap IDREFs = new HashMap();

    public String onID(XMLSerializable owner, String value) throws SAXException {
        if (!validateID) return value;
        if (!IDs.add(value)) {
            reportEvent(jaxbContext.getGrammarInfo().castToValidatableObject(owner), Messages.format(Messages.DUPLICATE_ID, value));
        }
        return value;
    }

    public String onIDREF(XMLSerializable referer, String value) throws SAXException {
        if (!validateID) return value;
        if (IDs.contains(value)) return value;
        IDREFs.put(value, referer);
        return value;
    }

    /** Tests if all IDREFs have corresponding IDs. */
    protected void reconcileIDs() throws SAXException {
        if (!validateID) return;
        for (Iterator itr = IDREFs.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry e = (Map.Entry) itr.next();
            if (IDs.contains(e.getKey())) continue;
            ValidatableObject source = (ValidatableObject) e.getValue();
            reportEvent(source, new NotIdentifiableEventImpl(ValidationEvent.ERROR, Messages.format(Messages.ID_NOT_FOUND, e.getKey()), new ValidationEventLocatorImpl(source)));
        }
        IDREFs.clear();
    }

    private final ValidationEventHandler eventHandler;

    /**
     * Reports an error to the application.
     */
    public void reportEvent(ValidatableObject source, String formattedMessage) throws AbortSerializationException {
        reportEvent(source, new ValidationEventImpl(ValidationEvent.ERROR, formattedMessage, new ValidationEventLocatorImpl(source)));
    }

    /**
     * Reports an error to the client.
     * This version should be used when an exception is thrown from sub-modules.
     */
    public void reportEvent(ValidatableObject source, Exception nestedException) throws AbortSerializationException {
        reportEvent(source, new ValidationEventImpl(ValidationEvent.ERROR, nestedException.toString(), new ValidationEventLocatorImpl(source), nestedException));
    }

    public void reportEvent(ValidatableObject source, ValidationEvent event) throws AbortSerializationException {
        boolean r;
        try {
            r = eventHandler.handleEvent(event);
        } catch (RuntimeException re) {
            r = false;
        }
        if (!r) {
            throw new AbortSerializationException(event.getMessage());
        }
    }
}
