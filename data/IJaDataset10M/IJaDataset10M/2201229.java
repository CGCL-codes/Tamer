package xvrengine.debug;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.LineBreakpoint;

public class XVRBreakpoint extends LineBreakpoint {

    public XVRBreakpoint() {
    }

    public XVRBreakpoint(final IResource resource, final int lineNumber) throws CoreException {
        IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

            public void run(IProgressMonitor monitor) throws CoreException {
                IMarker marker = resource.createMarker("xvrengine.lineBreakpoint.marker");
                setMarker(marker);
                marker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
                marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
                marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
                marker.setAttribute(IMarker.MESSAGE, "Line Breakpoint: " + resource.getName() + " [line: " + lineNumber + "]");
            }
        };
        run(getMarkerRule(resource), runnable);
    }

    @Override
    public String getModelIdentifier() {
        return IXVRConstants.ID_XVR_DEBUG_MODEL;
    }
}
