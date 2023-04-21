package tr.view.collect;

import org.openide.modules.ModuleInstall;
import org.openide.util.actions.SystemAction;
import tr.appl.InitialAction;
import tr.appl.InitialActionLookup;

/**
 * Collect thoughts module installer.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class Installer extends ModuleInstall {

    /** Adds the collect thoughts action to the lookup of initial actions. */
    @Override
    public void restored() {
        InitialAction action = (InitialAction) SystemAction.get(CollectThoughtsAction.class);
        InitialActionLookup.instance().add(action);
    }
}
