package org.nakedobjects.nos.client.dnd.notifier;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.NakedObjectRuntimeException;
import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.util.DebugInfo;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.ObjectContent;
import org.nakedobjects.nos.client.dnd.View;

public class ViewUpdateNotifier implements DebugInfo {

    private static final Logger LOG = Logger.getLogger(ViewUpdateNotifier.class);

    protected Hashtable views = new Hashtable();

    public void add(final View view) {
        Content content = view.getContent();
        if (content instanceof ObjectContent) {
            Naked object = content.getNaked();
            if (object != null) {
                Vector viewsToNotify;
                if (views.containsKey(object)) {
                    viewsToNotify = (Vector) views.get(object);
                } else {
                    viewsToNotify = new Vector();
                    views.put(object, viewsToNotify);
                }
                if (viewsToNotify.contains(view)) {
                    throw new NakedObjectRuntimeException(view + " already being notified");
                }
                viewsToNotify.addElement(view);
                LOG.debug("added " + view + " to observers for " + object);
            }
        }
    }

    public void debugData(final DebugString buf) {
        Enumeration f = views.keys();
        while (f.hasMoreElements()) {
            Object object = f.nextElement();
            Vector viewsToNotify = (Vector) views.get(object);
            Enumeration e = viewsToNotify.elements();
            buf.append("Views for " + object + " \n");
            while (e.hasMoreElements()) {
                View view = (View) e.nextElement();
                buf.append("        " + view);
                buf.append("\n");
            }
            buf.append("\n");
        }
    }

    public String debugTitle() {
        return "Views for object details (observers)";
    }

    public void remove(final View view) {
        Content content = view.getContent();
        LOG.debug("removing " + content + " for " + view);
        if (content instanceof ObjectContent) {
            Naked object = ((ObjectContent) content).getObject();
            if (object != null && views.containsKey(object)) {
                Vector viewsToNotify;
                viewsToNotify = (Vector) views.get(object);
                viewsToNotify.removeElement(view);
                LOG.debug("removed " + view + " from observers for " + object);
                if (viewsToNotify.size() == 0) {
                    views.remove(object);
                    LOG.debug("removed observer list for " + object);
                }
            } else {
                throw new NakedObjectRuntimeException("Tried to remove a non-existant view " + view + " from observers for " + object);
            }
        }
    }

    public void shutdown() {
        views.clear();
    }

    public void invalidateViewsForChangedObjects() {
        Enumeration objects = NakedObjectsContext.getUpdateNotifer().allChangedObjects();
        while (objects.hasMoreElements()) {
            NakedObject object = (NakedObject) objects.nextElement();
            LOG.debug("invalidate views for " + object);
            Object viewsVector = this.views.get(object);
            if (viewsVector == null) {
                continue;
            }
            Enumeration views = ((Vector) viewsVector).elements();
            while (views.hasMoreElements()) {
                View view = (View) views.nextElement();
                LOG.debug("   - " + view);
                view.invalidateContent();
            }
        }
    }
}
