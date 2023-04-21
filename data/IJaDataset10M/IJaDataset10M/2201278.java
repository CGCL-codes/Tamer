package org.plazmaforge.bsolution.personality.client.swing.actions;

import org.plazmaforge.bsolution.personality.client.swing.GUIPersonalityEnvironment;
import org.plazmaforge.framework.client.swing.actions.ResourceAction;
import org.plazmaforge.framework.resources.Resources;

/**
 * @author Oleh Hapon
 * Date: 17.09.2004
 * Time: 8:01:33
 * $Id: GUIPersonalityAction.java,v 1.2 2010/04/28 06:28:12 ohapon Exp $
 */
public abstract class GUIPersonalityAction extends ResourceAction {

    public GUIPersonalityAction() {
        super(GUIPersonalityEnvironment.getResources());
    }

    public GUIPersonalityAction(Resources resources) {
        super(resources == null ? GUIPersonalityEnvironment.getResources() : resources);
    }
}
