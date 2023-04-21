package org.perlipse.internal.debug.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.debug.ui.breakpoints.IScriptBreakpointLineValidator;
import org.eclipse.dltk.debug.ui.breakpoints.ScriptBreakpointLineValidatorFactory;
import org.eclipse.dltk.debug.ui.breakpoints.ScriptToggleBreakpointAdapter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.perlipse.core.PerlCoreConstants;

/**
 * toggle breakpoints
 */
public class PerlToggleBreakpointAdapter extends ScriptToggleBreakpointAdapter {

    private static final IScriptBreakpointLineValidator validator = ScriptBreakpointLineValidatorFactory.createNonEmptyNoCommentValidator("#");

    public boolean canToggleBreakpoints(IWorkbenchPart part, ISelection selection) {
        return canToggleLineBreakpoints(part, selection);
    }

    public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
        return false;
    }

    public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
        return false;
    }

    public void toggleBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
        toggleLineBreakpoints(part, selection);
    }

    public void toggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
    }

    public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
    }

    @Override
    protected String getDebugModelId() {
        return PerlCoreConstants.DEBUG_MODEL_ID;
    }

    @Override
    protected IScriptBreakpointLineValidator getValidator() {
        return validator;
    }
}
