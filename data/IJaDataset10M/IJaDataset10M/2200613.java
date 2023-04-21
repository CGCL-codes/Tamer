package net.sourceforge.jruntimedesigner.actions.layout;

import java.awt.event.ActionEvent;
import net.sourceforge.jruntimedesigner.JRuntimeDesignerController;
import net.sourceforge.jruntimedesigner.actions.AbstractWidgetAction;

/**
 * Action for loading the layout.
 * 
 * @author ikunin
 * @author $Author: ikunin $ (Last change)
 * @version $Revision: 182 $ $Date: 2009-08-21 11:14:20 -0400 (Fri, 21 Aug 2009) $
 * @since 1.0
 */
public class ResetAction extends AbstractWidgetAction {

    public static final String NAME = "Reset";

    public ResetAction(JRuntimeDesignerController controller) {
        super(NAME, controller);
        updateActionState();
    }

    public void doAction(ActionEvent e) throws Exception {
        controller.reset();
    }
}
