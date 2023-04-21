package org.mobicents.slee.resource.diameter.rx.events;

import net.java.slee.resource.diameter.rx.events.SessionTerminationAnswer;
import org.jdiameter.api.Message;

/**
 * Implementation of {@link SessionTerminationAnswer}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @see DiameterMessageImpl
 */
public class SessionTerminationAnswerImpl extends SessionTerminationMessageImpl implements SessionTerminationAnswer {

    @Override
    public String getLongName() {
        return "Session-Termination-Answer";
    }

    @Override
    public String getShortName() {
        return "STA";
    }

    public SessionTerminationAnswerImpl(Message message) {
        super(message);
    }
}
