package com.aptana.ide.debug.model;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.LineBreakpoint;
import com.aptana.ide.debug.ui.IDebugConstants;

/**
 * Ant line breakpoint
 */
public class JSLineBreakpoint extends LineBreakpoint {

    /**
	 * Default constructor is required for the breakpoint manager to re-create persisted breakpoints. After
	 * instantiating a breakpoint, the <code>setMarker(...)</code> method is called to restore this breakpoint's
	 * attributes.
	 */
    public JSLineBreakpoint() {
    }

    /**
	 * Constructs a line breakpoint on the given resource at the given line number. The line number is 1-based (i.e. the
	 * first line of a file is line number 1).
	 * 
	 * @param resource
	 *            file on which to set the breakpoint
	 * @param lineNumber
	 *            1-based line number of the breakpoint
	 * @throws CoreException
	 *             if unable to create the breakpoint
	 */
    public JSLineBreakpoint(IResource resource, int lineNumber) throws CoreException {
        this(resource, lineNumber, new HashMap(), true);
    }

    /**
	 * Constructs a line breakpoint on the given resource at the given line number. The line number is 1-based (i.e. the
	 * first line of a file is line number 1).
	 * 
	 * @param resource
	 *            file on which to set the breakpoint
	 * @param lineNumber
	 *            1-based line number of the breakpoint
	 * @param attributes
	 *            the marker attributes to set
	 * @param register
	 *            whether to add this breakpoint to the breakpoint manager
	 * @throws CoreException
	 *             if unable to create the breakpoint
	 */
    public JSLineBreakpoint(final IResource resource, final int lineNumber, final Map attributes, final boolean register) throws CoreException {
        IWorkspaceRunnable wr = new IWorkspaceRunnable() {

            public void run(IProgressMonitor monitor) throws CoreException {
                IMarker marker = resource.createMarker(IDebugConstants.ID_JS_LINE_BREAKPOINT_MARKER);
                setMarker(marker);
                attributes.put(IBreakpoint.ENABLED, Boolean.TRUE);
                attributes.put(IMarker.LINE_NUMBER, new Integer(lineNumber));
                attributes.put(IBreakpoint.ID, IDebugConstants.ID_JS_DEBUG_MODEL);
                attributes.put(IMarker.MESSAGE, MessageFormat.format("JS breakpoint [line: {0}]", new String[] { Integer.toString(lineNumber) }));
                ensureMarker().setAttributes(attributes);
                register(register);
            }
        };
        run(getMarkerRule(resource), wr);
    }

    /**
	 * @see org.eclipse.debug.core.model.IBreakpoint#getModelIdentifier()
	 */
    public String getModelIdentifier() {
        return IDebugConstants.ID_JS_DEBUG_MODEL;
    }

    /**
	 * @return whether this breakpoint is a run to line breakpoint
	 */
    public boolean isRunToLine() {
        try {
            return ensureMarker().getAttribute(IDebugConstants.JS_RUN_TO_LINE, false);
        } catch (DebugException e) {
            return false;
        }
    }

    /**
	 * Add this breakpoint to the breakpoint manager, or sets it as unregistered.
	 */
    private void register(boolean register) throws CoreException {
        if (register) {
            DebugPlugin.getDefault().getBreakpointManager().addBreakpoint(this);
        } else {
            setRegistered(false);
        }
    }
}
