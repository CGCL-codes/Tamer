package org.eclipse.jface.text.quickassist;

import org.eclipse.jface.text.source.ISourceViewer;

/**
 * Context information for quick fix and quick assist processors.
 * <p>
 * This interface can be implemented by clients.</p>
 * 
 * @since 3.2
 */
public interface IQuickAssistInvocationContext {

    /**
	 * Returns the offset where quick assist was invoked.
	 * 
	 * @return the invocation offset or <code>-1</code> if unknown
	 */
    int getOffset();

    /**
	 * Returns the length of the selection at the invocation offset.
	 * 
	 * @return the length of the current selection or <code>-1</code> if none or unknown
	 */
    int getLength();

    /**
	 * Returns the viewer for this context.
	 * 
	 * @return the viewer or <code>null</code> if not available
	 */
    ISourceViewer getSourceViewer();
}
