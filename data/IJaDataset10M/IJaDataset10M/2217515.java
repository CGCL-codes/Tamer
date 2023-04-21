package be.vds.jtbdive.view.docking.stateactiondockable;

import be.vds.jtbdive.view.docking.dockable.ConsoleDockable;
import com.javadocking.dockable.action.DefaultDockableStateActionFactory;

public class ConsoleStateActionDockable extends BorderDefaultStateActionDockable {

    public ConsoleStateActionDockable(ConsoleDockable consoleDockable) {
        super(consoleDockable, new DefaultDockableStateActionFactory(), STATES);
    }
}
