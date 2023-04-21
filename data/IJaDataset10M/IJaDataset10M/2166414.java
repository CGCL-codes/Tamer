package net.sf.istcontract.wsimport.api.message;

import net.sf.istcontract.wsimport.protocol.soap.VersionMismatchException;
import net.sf.istcontract.wsimport.util.exception.JAXWSExceptionBase;

/**
 * This class represents an Exception that needs to be marshalled
 * with a specific protocol wire format. For example, the SOAP's
 * VersionMismatchFault needs to be written with a correct fault code.
 * In that case, decoder could throw {@link VersionMismatchException},
 * and the correspoinding fault {@link Message} from {@link ExceptionHasMessage::getFaultMessage}
 * is sent on the wire.
 *
 * @author Jitendra Kotamraju
 */
public abstract class ExceptionHasMessage extends JAXWSExceptionBase {

    public ExceptionHasMessage(String key, Object... args) {
        super(key, args);
    }

    /**
     * Returns the exception into a fault Message
     *
     * @return Message for this exception
     */
    public abstract Message getFaultMessage();
}
