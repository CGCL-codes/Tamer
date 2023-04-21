package org.mobicents.slee.resource.diameter.base.events;

import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;
import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;

/**
 * Implementation of {@link SessionTerminationRequest}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @see SessionTerminationMessageImpl
 */
public class SessionTerminationRequestImpl extends SessionTerminationMessageImpl implements SessionTerminationRequest {

    public SessionTerminationRequestImpl(Message message) {
        super(message);
    }

    @Override
    public String getLongName() {
        return "Session-Termination-Request";
    }

    @Override
    public String getShortName() {
        return "STR";
    }

    public boolean hasTerminationCause() {
        return hasAvp(Avp.TERMINATION_CAUSE);
    }

    public TerminationCauseType getTerminationCause() {
        return (TerminationCauseType) getAvpAsEnumerated(Avp.TERMINATION_CAUSE, TerminationCauseType.class);
    }

    public void setTerminationCause(TerminationCauseType terminationCause) {
        addAvp(Avp.TERMINATION_CAUSE, terminationCause.getValue());
    }
}
