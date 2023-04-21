package com.bluemarsh.jswat.view;

import com.bluemarsh.jswat.Manager;
import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.SourceSource;
import com.bluemarsh.jswat.event.SessionEvent;
import com.bluemarsh.jswat.event.SessionListener;
import com.bluemarsh.jswat.ui.UIAdapter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.SwingUtilities;

/**
 * The ViewManager class is responsible for maintaining the list of open
 * views. It also handles switching between the different display modes
 * for the views.
 *
 * @author  Nathan Fiedler
 */
public class ViewManager implements Manager, Runnable {

    /** The Session we belong to. */
    private Session owningSession;

    /** Table of open view objects, where the keys are the Views and the
     * values are the SourceSources. */
    private Hashtable viewToSourceMap;

    /** Table of open view objects, where the keys are the SourceSources
     * and the values are the Views. */
    private Hashtable sourceToViewMap;

    /**
     * Constructs a ViewManager.
     */
    public ViewManager() {
        viewToSourceMap = new Hashtable();
        sourceToViewMap = new Hashtable();
    }

    /**
     * Called when the Session has activated. This occurs when the
     * debuggee has launched or has been attached to the debugger.
     *
     * @param  sevt  session event.
     */
    public void activated(SessionEvent sevt) {
        refreshViews();
    }

    /**
     * Adds the view to this manager's list of open views.
     *
     * @param  view  view to be added to the display.
     * @param  src   source of the view.
     */
    public void addView(View view, SourceSource src) {
        viewToSourceMap.put(view, src);
        sourceToViewMap.put(src, view);
        if (view instanceof SessionListener) {
            owningSession.addListener((SessionListener) view);
        }
    }

    /**
     * Called when the Session is about to be closed.
     *
     * @param  sevt  session event.
     */
    public void closing(SessionEvent sevt) {
        Enumeration views = viewToSourceMap.keys();
        while (views.hasMoreElements()) {
            View view = (View) views.nextElement();
            if (view instanceof SessionListener) {
                owningSession.removeListener((SessionListener) view);
            }
        }
        owningSession = null;
    }

    /**
     * Called when the Session has deactivated. The debuggee VM is no
     * longer connected to the Session.
     *
     * @param  sevt  session event.
     */
    public void deactivated(SessionEvent sevt) {
    }

    /**
     * Retrieves the view for the given source. If no view has been
     * opened for this source, return null.
     *
     * @param  src  source object.
     * @return  open view for source, or null if none.
     */
    public View getView(SourceSource src) {
        return (View) sourceToViewMap.get(src);
    }

    /**
     * Returns an enumeration over the set of open views.
     *
     * @return  enumeration of open views.
     */
    public Enumeration getViews() {
        return viewToSourceMap.keys();
    }

    /**
     * Called after the Session has added this listener to the Session
     * listener list.
     *
     * @param  session  the Session.
     */
    public void opened(Session session) {
        owningSession = session;
    }

    /**
     * Refresh the open views immediately.
     */
    public void refreshViews() {
        if (SwingUtilities.isEventDispatchThread()) {
            run();
        } else {
            SwingUtilities.invokeLater(this);
        }
    }

    /**
     * Remove the given source view from the list.
     *
     * @param  view  source view object to remove.
     */
    public void removeView(View view) {
        if (view instanceof SessionListener) {
            owningSession.removeListener((SessionListener) view);
        }
        sourceToViewMap.remove(viewToSourceMap.remove(view));
    }

    /**
     * Called when the debuggee is about to be resumed.
     *
     * @param  sevt  session event.
     */
    public void resuming(SessionEvent sevt) {
    }

    /**
     * Refresh the open views.
     */
    public void run() {
        Enumeration views = viewToSourceMap.keys();
        while (views.hasMoreElements()) {
            View view = (View) views.nextElement();
            SourceSource src = (SourceSource) viewToSourceMap.get(view);
            try {
                view.refresh(src, 0);
            } catch (IOException ioe) {
                owningSession.getUIAdapter().showMessage(UIAdapter.MESSAGE_WARNING, ioe.toString());
            }
        }
    }

    /**
     * Called when the debuggee has been suspended.
     *
     * @param  sevt  session event.
     */
    public void suspended(SessionEvent sevt) {
    }
}
