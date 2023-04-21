package org.plazmaforge.framework.client.swing.desktop.actions;

import javax.swing.*;
import org.plazmaforge.framework.client.swing.actions.ResourceAction;
import org.plazmaforge.framework.resources.Resources;

/**
 * @author Oleh Hapon
 * Date: 17.02.2004
 * Time: 9:15:03
 * $Id: DesktopAction.java,v 1.2 2010/04/28 06:36:12 ohapon Exp $
 */
public abstract class DesktopAction extends ResourceAction {

    protected JDesktopPane desktop;

    public DesktopAction(JDesktopPane desktop, Resources res) {
        super(res);
        this.desktop = desktop;
    }

    public DesktopAction(Resources rsrc) {
        super(rsrc);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public void setDesktop(JDesktopPane desktop) {
        this.desktop = desktop;
    }
}
