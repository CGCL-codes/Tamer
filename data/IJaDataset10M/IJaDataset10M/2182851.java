package com.ibm.celldt.debug.be.ui.views.spu.signals;

import org.eclipse.cdt.debug.core.model.ICDebugTarget;
import org.eclipse.cdt.debug.internal.ui.views.IDebugExceptionHandler;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import com.ibm.celldt.debug.be.DebugBEActivator;

/**
 * Provides content for the SPU signals view.
 *
 * @author Ricardo M. Matinata
 * @since 1.3
 */
public class SPUSignalsViewContentProvider implements IStructuredContentProvider {

    /**
	 * Handler for exceptions as content is retrieved
	 */
    private IDebugExceptionHandler fExceptionHandler = null;

    public SPUSignalsViewContentProvider() {
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof ICDebugTarget) {
            ICDebugTarget target = (ICDebugTarget) inputElement;
            if (target != null) {
                Object[] signals = DebugBEActivator.getDefault().getSPUProcessor().processSPUSignal(target);
                if (signals != null) return signals;
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
