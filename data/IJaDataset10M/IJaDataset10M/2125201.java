package xvrengine.debug;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;

public class XVRLineBreakpointAdapter implements IToggleBreakpointsTarget {

    @Override
    public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
        return getEditor(part) != null;
    }

    @Override
    public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
        return false;
    }

    @Override
    public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
        return false;
    }

    @Override
    public void toggleLineBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
        ITextEditor textEditor = getEditor(part);
        if (textEditor != null) {
            IResource resource = (IResource) textEditor.getEditorInput().getAdapter(IResource.class);
            ITextSelection textSelection = (ITextSelection) selection;
            int lineNumber = textSelection.getStartLine() + 1;
            IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(IXVRConstants.ID_XVR_DEBUG_MODEL);
            for (int i = 0; i < breakpoints.length; i++) {
                IBreakpoint breakpoint = breakpoints[i];
                if (resource.equals(breakpoint.getMarker().getResource())) {
                    if (((ILineBreakpoint) breakpoint).getLineNumber() == (lineNumber)) {
                        breakpoint.delete();
                        return;
                    }
                }
            }
            XVRBreakpoint lineBreakpoint = new XVRBreakpoint(resource, lineNumber);
            DebugPlugin.getDefault().getBreakpointManager().addBreakpoint(lineBreakpoint);
        }
    }

    @Override
    public void toggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
    }

    @Override
    public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
    }

    private ITextEditor getEditor(IWorkbenchPart part) {
        if (part instanceof ITextEditor) {
            ITextEditor editorPart = (ITextEditor) part;
            IResource resource = (IResource) editorPart.getEditorInput().getAdapter(IResource.class);
            if (resource != null) {
                String extension = resource.getFileExtension();
                if (extension != null && extension.equals("s3d")) {
                    return editorPart;
                }
            }
        }
        return null;
    }
}
