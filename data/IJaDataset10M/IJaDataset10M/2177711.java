package source.model;

import java.util.LinkedList;
import source.events.action.Action;
import source.view.MainScreen;

public class UnitMission {

    private LinkedList<Action> queue;

    private ObjectID targetUID;

    private boolean isValid = true;

    public UnitMission(ObjectID uid) {
        queue = new LinkedList<Action>();
        targetUID = uid;
    }

    public void addAction(Action a) {
        queue.add(a);
    }

    public void invalidate() {
        isValid = false;
    }

    public boolean isValid() {
        return isValid;
    }

    public Action getNextAction() {
        if (queue.size() > 0) return queue.getFirst(); else {
            isValid = false;
            return null;
        }
    }

    public Action removeAction() {
        if (queue.size() > 0) return queue.removeFirst(); else {
            MainScreen.writeToConsole("UnitMission:  UnitMission is empty");
            isValid = false;
            return null;
        }
    }

    public ObjectID getUnitID() {
        return targetUID;
    }
}
