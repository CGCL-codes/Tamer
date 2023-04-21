package uk.org.ogsadai.activity.event;

import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.authorization.DistinguishedNameProvider;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.common.msgs.DAILogger;

/**
 * Demonstration activity input listener that logs all expressions sent to
 * SQLQuery activities.
 *
 * @author The OGSA-DAI Project Team
 */
public class DemoActivityInputListener implements ActivityInputListener {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008.";

    /** Logger object for logging in this class. */
    private static final DAILogger LOG = DAILogger.getLogger(DemoActivityInputListener.class);

    /**
     * {@inheritDoc}
     */
    public boolean isInputOfInterest(InputDetails inputDetails) {
        if (inputDetails.getActivityName().getLocalPart().equals("SQLQuery") && inputDetails.getInputName().equals("expression")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void blockReadEvent(InputDetails inputDetails, Object block) throws ActivityUserException, ActivityProcessingException {
        SecurityContext securityContext = inputDetails.getSecurityContext();
        String dn = "UnknownUser";
        if ((securityContext != null) && (securityContext instanceof DistinguishedNameProvider)) {
            DistinguishedNameProvider dnProvider = (DistinguishedNameProvider) securityContext;
            dn = dnProvider.getDN();
        }
        LOG.debug(dn + " read block: " + block);
    }

    /**
     * {@inheritDoc}
     */
    public void inputClosedByConsumerEvent(InputDetails inputDetails) {
    }

    /**
     * {@inheritDoc}
     */
    public void inputCompletedEvent(InputDetails inputDetails) {
    }

    /**
     * {@inheritDoc}
     */
    public void inputErrorEvent(InputDetails inputDetails) {
    }

    /**
     * {@inheritDoc}
     */
    public void inputTerminatedEvent(InputDetails inputDetails) {
    }
}
