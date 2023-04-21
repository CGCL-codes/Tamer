package net.sf.moviekebab.service.implementation.action.cap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.moviekebab.service.action.ActionTag;
import net.sf.moviekebab.service.action.CappedAction;
import net.sf.moviekebab.service.action.TaggedAction;

/**
 * Keeps a map of {@link ActionCap}s referenced by {@link ActionTag}s,
 * and sets {@link TaggedAction}s under the Caps on demand.
 *
 * @author Laurent Caillette
 */
public class ActionCapAttributor {

    private static final Log LOG = LogFactory.getLog(ActionCapAttributor.class);

    private final Map caps = new HashMap();

    public void register(ActionTag tag, ActionCap actionCap) {
        caps.put(tag, actionCap);
    }

    public void unregister(ActionTag tag) {
        caps.remove(tag);
    }

    public boolean isRegistered(ActionTag tag) {
        return caps.containsKey(tag);
    }

    public void cap(CappedAction cappedAction) {
        final ActionTag tag = cappedAction.getTag();
        final ActionCap actionCap = (ActionCap) caps.get(tag);
        if (null == actionCap) {
            throw new NoSuchElementException("No ActionCap registered for " + tag);
        }
        actionCap.setCappedAction(cappedAction);
        LOG.debug("Attributed for " + tag + " this cap: " + actionCap + " on: " + cappedAction);
    }

    public void uncapAll() {
        final Iterator it = caps.values().iterator();
        while (it.hasNext()) {
            final ActionCap actionCap = (ActionCap) it.next();
            actionCap.setCappedAction(null);
        }
        LOG.debug("Uncapped all actions.");
    }

    public TaggedAction getActionCap(ActionTag tag) {
        final ActionCap actionCap = (ActionCap) caps.get(tag);
        if (null == actionCap) {
            throw new NoSuchElementException("No ActionCap registered for " + tag);
        }
        return actionCap;
    }
}
