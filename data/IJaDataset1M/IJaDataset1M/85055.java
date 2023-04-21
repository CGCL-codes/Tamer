package net.nexttext.behaviour.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.behaviour.Action;
import net.nexttext.property.Property;

public class Multiplexer extends AbstractAction {

    protected List<Action> actions;

    protected HashMap<Action, HashSet<TextObject>> doneWithObject;

    /**
     * @param actions a List containing Action objects.
     */
    public Multiplexer(List<Action> actions) {
        this.actions = actions;
        doneWithObject = new HashMap<Action, HashSet<TextObject>>();
        Action action;
        for (ListIterator<Action> i = actions.listIterator(); i.hasNext(); ) {
            action = i.next();
            doneWithObject.put(action, new HashSet<TextObject>());
        }
    }

    /**
     * Create a new Multiplexer with no actions.
     */
    public Multiplexer() {
        actions = new ArrayList<Action>();
        doneWithObject = new HashMap<Action, HashSet<TextObject>>();
    }

    /**
     * Add an action to the Multiplexer.
     */
    public void add(Action action) {
        actions.add(action);
        doneWithObject.put(action, new HashSet<TextObject>());
    }

    /**
     * Apply all the actions to the TextObject.
     *
     * <p>The results of the called actions are combined using the method
     * described in ActionResult.  </p>
     */
    public ActionResult behave(TextObject to) {
        ActionResult res = new ActionResult();
        for (Iterator<Action> i = actions.iterator(); i.hasNext(); ) {
            Action current = i.next();
            ActionResult tres = null;
            if (doneWithObject.get(current).contains(to)) tres = new ActionResult(true, true, false); else tres = current.behave(to);
            if (tres.complete) {
                doneWithObject.get(current).add(to);
            }
            res.combine(tres);
        }
        res.endCombine();
        if (res.complete) {
            for (Iterator<Action> i = actions.iterator(); i.hasNext(); ) {
                Action current = i.next();
                doneWithObject.get(current).remove(to);
            }
            complete(to);
        }
        return res;
    }

    /**
     * Reset the multiplexer.
     */
    public void reset(TextObject to) {
        for (Iterator<Action> i = actions.iterator(); i.hasNext(); ) {
            Action current = i.next();
            doneWithObject.get(current).remove(to);
        }
    }

    /**
     * End the multiplexer for this object.
     */
    public void complete(TextObject to) {
        super.complete(to);
        for (Iterator<Action> i = actions.iterator(); i.hasNext(); ) {
            i.next().complete(to);
        }
    }

    /**
     * The required properties are the union of all properties in the action
     * chain.
     */
    public Map<String, Property> getRequiredProperties() {
        HashMap<String, Property> rP = new HashMap<String, Property>();
        for (Iterator<Action> i = actions.iterator(); i.hasNext(); ) {
            rP.putAll(i.next().getRequiredProperties());
        }
        return rP;
    }
}
