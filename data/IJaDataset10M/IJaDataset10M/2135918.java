package consciouscode.bonsai.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
    Class documentation.
*/
public class ActionRegistry {

    public AutoAction getAction(String actionTag) {
        AutoAction found = null;
        if (myAutoActions != null) {
            for (int i = 0, count = myAutoActions.size(); i < count; i++) {
                AutoAction action = myAutoActions.get(i);
                if (actionTag.equals(action.getActionTag())) {
                    found = action;
                    break;
                }
            }
        }
        return found;
    }

    /**
       Get the set of actions in this registry.

       @return an <em>unmodifiable</em> collection of {@link AutoAction}
       instances.
    */
    public Collection<AutoAction> getActions() {
        if (myAutoActions != null) {
            return Collections.unmodifiableList(myAutoActions);
        }
        return Collections.emptyList();
    }

    public void addAction(AutoAction action) {
        prepareToAddActions(1);
        myAutoActions.add(action);
    }

    public void addActions(AutoAction[] actions) {
        if ((actions != null) && (actions.length != 0)) {
            prepareToAddActions(actions.length);
            for (int i = 0; i < actions.length; i++) {
                myAutoActions.add(actions[i]);
            }
        }
    }

    /**
       Copy all of the actions from another registry into this one.
    */
    public void addAll(ActionRegistry otherRegistry) {
        ArrayList<AutoAction> otherActions = otherRegistry.myAutoActions;
        if ((otherActions != null) && !otherActions.isEmpty()) {
            if (myAutoActions == null) {
                myAutoActions = new ArrayList<AutoAction>(otherActions);
            } else {
                myAutoActions.addAll(otherActions);
            }
        }
    }

    private void prepareToAddActions(int count) {
        if (myAutoActions == null) {
            myAutoActions = new ArrayList<AutoAction>(count);
        } else {
            myAutoActions.ensureCapacity(myAutoActions.size() + count);
        }
    }

    /**
       This list is lazily instantiated when actions are added to the registry.
    */
    private ArrayList<AutoAction> myAutoActions;
}
