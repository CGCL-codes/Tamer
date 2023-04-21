package freemind.controller.actions.generated.instance.impl.runtime;

import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.ValidationEvent;
import org.xml.sax.SAXException;

/**
 * Unified event handler that processes
 * both the SAX events and error events.
 */
public interface SAXUnmarshallerHandler extends UnmarshallerHandler {

    /**
     * Reports an error to the user, and asks if s/he wants
     * to recover. If the canRecover flag is false, regardless
     * of the client instruction, an exception will be thrown.
     * 
     * Only if the flag is true and the user wants to recover from an error,
     * the method returns normally.
     * 
     * The thrown exception will be catched by the unmarshaller.
     */
    void handleEvent(ValidationEvent event, boolean canRecover) throws SAXException;
}
