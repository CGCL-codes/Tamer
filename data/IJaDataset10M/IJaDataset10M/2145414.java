package visualbiology.sbmlEditor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IAction;
import org.eclipse.gef.Disposable;

/**
 * A container for editor actions. You must register the actions before they
 * will be available to the editor.
 */
public class ActionRegistry {

    private Map<String, IAction> map = new HashMap<String, IAction>(15);

    public void dispose() {
        Iterator<IAction> actions = getActions();
        while (actions.hasNext()) {
            IAction action = (IAction) actions.next();
            if (action instanceof Disposable) ((Disposable) action).dispose();
        }
    }

    public IAction getAction(Object key) {
        return (IAction) map.get(key);
    }

    public Iterator<IAction> getActions() {
        return map.values().iterator();
    }

    public void registerAction(IAction action) {
        Assert.isNotNull(action.getId(), "action must have an ID in " + getClass().getName() + " :registerAction(IAction)");
        registerAction(action.getId(), action);
    }

    private void registerAction(String id, IAction action) {
        map.put(id, action);
    }

    public void removeAction(IAction action) {
        map.remove(action.getId());
    }
}
