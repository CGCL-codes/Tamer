package org.stlab.xd.manager.workspace;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.stlab.xd.core.XDCorePlugin;
import org.stlab.xd.manager.DefaultManager;

public class ResourceSynchListener implements IResourceChangeListener {

    public void resourceChanged(IResourceChangeEvent event) {
        if (event == null) return;
        if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
            return;
        }
        IResourceDelta rootDelta = event.getDelta();
        ResourceSynchDeltaVisitor visitor = new ResourceSynchDeltaVisitor();
        try {
            visitor.visit(rootDelta);
            if (visitor.hasRemovedOWLFiles()) {
                ((DefaultManager) XDCorePlugin.getManager()).removeLocalOntologies(visitor.getRemovedOWLFiles());
            }
            if (visitor.hasAddedOWLFiles()) {
                ((DefaultManager) XDCorePlugin.getManager()).loadLocalOntologies(visitor.getAddedOWLFiles());
            }
            if (visitor.hasChangedOWLFiles()) {
                ((DefaultManager) XDCorePlugin.getManager()).reloadLocalOntologies(visitor.getChangedOWLFiles());
            }
        } catch (CoreException e) {
            e.printStackTrace();
            System.err.println(e.getLocalizedMessage());
            return;
        }
    }

    /**
	 * HR description of event types
	 * 
	 * @param type
	 * @return
	 */
    @SuppressWarnings("deprecation")
    private String getEventTypeString(int type) {
        if (IResourceChangeEvent.POST_AUTO_BUILD == type) {
            return " POST_AUTO_BUILD";
        } else if (IResourceChangeEvent.POST_BUILD == type) {
            return " POST_BUILD";
        } else if (IResourceChangeEvent.POST_CHANGE == type) {
            return " POST_CHANGE";
        } else if (IResourceChangeEvent.PRE_AUTO_BUILD == type) {
            return " PRE_AUTO_BUILD";
        } else if (IResourceChangeEvent.PRE_BUILD == type) {
            return " PRE_BUILD";
        } else if (IResourceChangeEvent.PRE_CLOSE == type) {
            return " PRE_CLOSE";
        } else if (IResourceChangeEvent.PRE_DELETE == type) {
            return " PRE_DELETE";
        }
        return "?";
    }

    /**
	 * STD Outputs information about delta
	 * 
	 * @param delta
	 * @param recursive
	 */
    private void stdOutPrintDelta(IResourceDelta delta, boolean recursive) {
        if (!recursive) return;
        IResourceDelta[] affectedChildren = delta.getAffectedChildren();
        for (IResourceDelta d : affectedChildren) {
            stdOutPrintDelta(d, true);
        }
    }

    /**
	 * HR descriptions of delta kinds
	 * 
	 * @param kind
	 * @return
	 */
    private String getDeltaKindString(int kind) {
        if (IResourceDelta.ADDED == kind) return "-ADDED";
        if (IResourceDelta.ADDED_PHANTOM == kind) return "-ADDED_PHANTOM";
        if (IResourceDelta.ALL_WITH_PHANTOMS == kind) return "-ALL_WITH_PHANTOMS";
        if (IResourceDelta.CHANGED == kind) return "-CHANGED";
        if (IResourceDelta.CONTENT == kind) return "-CONTENT CHANGED";
        if (IResourceDelta.COPIED_FROM == kind) return "-COPIED FROM";
        if (IResourceDelta.NO_CHANGE == kind) return "-NO_CHANGE";
        if (IResourceDelta.DESCRIPTION == kind) return "-DESCRIPTION (Project description has changed)";
        if (IResourceDelta.TYPE == kind) return "-TYPE (The type has changed)";
        if (IResourceDelta.OPEN == kind) return "-OPENED/CLOSED";
        if (IResourceDelta.REMOVED == kind) return "-REMOVED";
        if (IResourceDelta.SYNC == kind) return "-SYNCH status has changed";
        return "?";
    }
}
