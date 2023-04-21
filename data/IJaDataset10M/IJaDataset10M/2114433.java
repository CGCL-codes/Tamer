package net.sourceforge.taggerplugin.resource;

import net.sourceforge.taggerplugin.manager.TagAssociationManager;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class RemovedResourceDeltaVisitor implements IResourceDeltaVisitor {

    public boolean visit(IResourceDelta delta) throws CoreException {
        int kind = delta.getKind();
        if (kind == IResourceDelta.REMOVED) {
            final IMarkerDelta[] markers = delta.getMarkerDeltas();
            if (markers != null) {
                for (IMarkerDelta md : markers) {
                    if (md.getKind() == IResourceDelta.REMOVED && md.getType() == ITaggedMarker.MARKER_TYPE) {
                        final String rcid = (String) md.getAttribute(ITaggedMarker.KEY_RESOURCEID);
                        if (rcid != null) {
                            TagAssociationManager.getInstance().deleteAssociations(rcid);
                        }
                    }
                }
            }
        }
        return true;
    }
}
