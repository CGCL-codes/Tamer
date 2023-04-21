package org.eledge.components;

import static org.eledge.Eledge.checkParameters;
import org.apache.log4j.Logger;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.eledge.domain.participants.ParticipantAssignable;

/**
 * @author robertz
 * 
 */
public class AddParticipantControl extends BaseComponent {

    private Logger log = Logger.getLogger(AddParticipantControl.class);

    public void activateEditParticipantPage(IRequestCycle cycle) {
        log.debug("activating edit participant page");
        Object[] params = cycle.getServiceParameters();
        log.debug("checking parameters");
        if (!checkParameters(params, ParticipantAssignable.class)) {
            log.debug("invalid parameters found: " + params);
            return;
        }
        ParticipantAssignable pa = (ParticipantAssignable) params[0];
        EditParticipantPage page = (EditParticipantPage) cycle.getPage("EditParticipantPage");
        log.debug("got edit participant page");
        page.setParticipantAssignable(pa);
        log.debug("set the pa for edit p. page to pa");
        cycle.activate(page);
        log.debug("activated edit p. page");
    }
}
