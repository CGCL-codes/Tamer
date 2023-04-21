package net.sf.xpontus.actions.impl;

import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingConstants;
import com.vlsolutions.swing.docking.DockingDesktop;
import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;

/**
 * Action to show or hide the messages window
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class ViewMessagesWindowActionImpl extends AbstractXPontusActionImpl {

    private static final long serialVersionUID = -2334642905159364804L;

    public static final String BEAN_ALIAS = "action.viewmessageswindow";

    /**
     * @return
     */
    public Dockable getFirstDockedConsole() {
        ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance().getConsole();
        Dockable dc = null;
        Dockable dockable = null;
        int state = -1;
        for (int i = 0; i < console.getDockables().size(); i++) {
            dockable = (Dockable) console.getDockables().get(i);
            state = dockable.getDockKey().getDockableState();
            if (state == DockableState.STATE_DOCKED) {
                dc = dockable;
                break;
            }
        }
        return dc;
    }

    public void execute() {
        DockingDesktop desktop = DefaultXPontusWindowImpl.getInstance().getDesktop();
        ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance().getConsole();
        MessagesWindowDockable dockable = (MessagesWindowDockable) console.getDockableById(MessagesWindowDockable.DOCKABLE_ID);
        int state = dockable.getDockKey().getDockableState();
        Dockable rightDockable = DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentDockable();
        if (rightDockable == null) {
            rightDockable = DefaultXPontusWindowImpl.getInstance().getDefaultPane();
        }
        if (state == DockableState.STATE_CLOSED) {
            setName("Hide Messages Window");
            if (rightDockable.getDockKey().getDockableState() == DockableState.STATE_MAXIMIZED) {
                desktop.restore(rightDockable);
            }
            Dockable firstDockable = getFirstDockedConsole();
            if (firstDockable == null) {
                desktop.split(rightDockable, dockable, DockingConstants.SPLIT_BOTTOM);
            } else {
                desktop.createTab(firstDockable, dockable, 0);
            }
        } else {
            setName("Show Messages Window");
            desktop.close(dockable);
            dockable.getDockKey().setDockableState(DockableState.STATE_CLOSED);
        }
        desktop.revalidate();
        desktop.repaint();
    }
}
