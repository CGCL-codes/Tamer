package de.schlund.pfixcore.editor2.core.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.WeakHashMap;
import de.schlund.pfixcore.editor2.core.dom.SessionInfo;
import de.schlund.pfixcore.workflow.Context;

/**
 * Stores references to all {@link de.schlund.pfixcore.workflow.Context}
 * objects used by the editor. This is used too retrieve a list of all
 * include parts being edited by anyone. The Context objects are internally
 * stored as keys in a WeakHashMap to make sure, they can be garbage
 * collected if the are not used by any other part of Pustefix any longer.
 * This will usually happen, when the HttpSession the context is stored in
 * expires.
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class SessionInfoStore {

    private WeakHashMap<Context, SessionInfo> contextmap = new WeakHashMap<Context, SessionInfo>();

    /**
     * Returns all session info objects that are currently stored within the map.
     * 
     * @return collection of all session info objects
     */
    public synchronized Collection<SessionInfo> getSessionInfos() {
        return new ArrayList<SessionInfo>(contextmap.values());
    }

    /**
     * Registers a context with the specified session info.
     * This method should be triggered during user login as 
     * well as when the edited include part changes.
     * 
     * @param ctx editor's session context
     * @param sessionInfo information about the session associated with the context
     */
    public synchronized void registerContext(Context ctx, SessionInfo sessionInfo) {
        contextmap.put(ctx, sessionInfo);
    }

    /**
     * Unregisters the specified context.
     * This method should be triggered on a logout action to make sure
     * the unused session is not registered any more.
     * 
     * @param ctx the context that should be removed from the map
     */
    public synchronized void unregisterContext(Context ctx) {
        contextmap.remove(ctx);
    }
}
