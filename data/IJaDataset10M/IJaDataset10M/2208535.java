package de.objectinc.samsha.session;

import de.objectinc.samsha.information.InformationEvent;
import de.objectinc.samsha.ui.controller.InformationControlUI;
import de.objectinc.samsha.user.UserProfile;

/**
 * @author Andreas Heinecke
 * @copyright Andreas Heinecke 2006
 * @since 22.04.2006
 * @version $Revision: 1.1 $
 */
public class SpeechSession extends SessionState {

    public SpeechSession(UserProfile profile, InformationControlUI controlUI) {
        super(profile, controlUI);
    }

    public void received(InformationEvent event) {
    }
}
