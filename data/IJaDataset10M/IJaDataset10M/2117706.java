package melcoe.xacml.pdp;

import melcoe.xacml.MelcoeXacmlException;
import org.apache.log4j.Logger;

/**
 * @author nishen@melcoe.mq.edu.au
 */
public class MelcoePDPException extends MelcoeXacmlException {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(MelcoePDPException.class.getName());

    public MelcoePDPException() {
        super();
        log.error("No message provided");
    }

    public MelcoePDPException(String msg) {
        super(msg);
        log.error(msg);
    }

    public MelcoePDPException(String msg, Throwable t) {
        super(msg);
        log.error(msg, t);
    }
}
