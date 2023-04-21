package com.ibm.celldt.debug.be.ui.views.spu.mailbox;

import org.eclipse.cdt.debug.core.model.ICDebugTarget;
import org.eclipse.cdt.debug.internal.ui.views.IDebugExceptionHandler;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import com.ibm.celldt.debug.be.DebugBEActivator;

/**
 * Provides content for the SPU Mailbox view.
 *
 * @author Ricardo M. Matinata
 * @since 1.3
 */
public class SPUMailboxViewContentProvider implements IStructuredContentProvider {

    /**
	 * Handler for exceptions as content is retrieved
	 */
    private IDebugExceptionHandler fExceptionHandler = null;

    public SPUMailboxViewContentProvider() {
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof ICDebugTarget) {
            ICDebugTarget target = (ICDebugTarget) inputElement;
            if (target != null) {
                Object[] events = DebugBEActivator.getDefault().getSPUProcessor().processSPUMailbox(target);
                if (events != null) return events;
            }
        }
        return new Object[0];
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    /**
	 * Sets an exception handler for this content provider.
	 * 
	 * @param handler debug exception handler or <code>null</code>
	 */
    protected void setExceptionHandler(IDebugExceptionHandler handler) {
        fExceptionHandler = handler;
    }

    /**
	 * Returns the exception handler for this content provider.
	 * 
	 * @return debug exception handler or <code>null</code>
	 */
    protected IDebugExceptionHandler getExceptionHandler() {
        return fExceptionHandler;
    }
}
