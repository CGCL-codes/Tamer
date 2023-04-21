package hermes.browser;

import hermes.swing.SwingUtils;
import hermes.swing.actions.ActionRegistry;
import hermes.swing.actions.CreateNewJNDIContextAction;
import hermes.swing.actions.CreateNewSessionFromJNDIAction;
import hermes.swing.actions.JNDIUnbindAction;
import hermes.swing.actions.RefreshJNDIAction;
import hermes.swing.actions.RenameJNDIBindingAction;
import com.jidesoft.action.CommandBar;
import com.jidesoft.action.DockableBarContext;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: JNDIToolBar.java,v 1.3 2005/07/07 10:26:13 colincrist Exp $
 */
public class JNDIToolBar extends CommandBar {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3916903448783084099L;

    public JNDIToolBar() {
        super("JNDI");
        setHidable(false);
        getContext().setInitSide(DockableBarContext.DOCK_SIDE_NORTH);
        getContext().setInitMode(DockableBarContext.STATE_HORI_DOCKED);
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(CreateNewSessionFromJNDIAction.class)));
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(CreateNewJNDIContextAction.class)));
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(RenameJNDIBindingAction.class)));
        addSeparator();
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(JNDIUnbindAction.class)));
        addSeparator();
        add(SwingUtils.createToolBarButton(ActionRegistry.getAction(RefreshJNDIAction.class)));
    }
}
