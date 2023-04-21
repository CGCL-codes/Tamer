package org.plazmaforge.bsolution.product.client.swing.actions;

import org.plazmaforge.bsolution.product.client.swing.GUIProductEnvironment;
import org.plazmaforge.framework.client.swing.actions.ResourceAction;
import org.plazmaforge.framework.resources.Resources;

/**
 * @author Oleh Hapon
 * Date: 20.03.2004
 * Time: 17:12:14
 * $Id: GUIProductAction.java,v 1.2 2010/04/28 06:28:14 ohapon Exp $
 */
public abstract class GUIProductAction extends ResourceAction {

    public GUIProductAction() {
        super(GUIProductEnvironment.getResources());
    }

    public GUIProductAction(Resources resources) {
        super(resources == null ? GUIProductEnvironment.getResources() : resources);
    }
}
