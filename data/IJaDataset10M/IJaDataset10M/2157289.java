package ti.oscript.eclipse.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProviderExtension2;
import org.eclipse.ui.texteditor.IDocumentProviderExtension3;
import org.eclipse.ui.texteditor.IDocumentProviderExtension5;
import oscript.syntaxtree.ProgramFile;

/**
 * @see ICompilationUnitDocumentProvider
 * @author alex.k@ti.com
 */
public interface IOScriptFileDocumentProvider extends IDocumentProvider, IDocumentProviderExtension2, IDocumentProviderExtension3, IDocumentProviderExtension5 {

    /**
	 * Shuts down this provider.
	 */
    void shutdown();

    /**
	 * Returns the working copy for the given element.
	 *
	 * @param element the element
	 * @return the working copy for the given element
	 */
    ProgramFile getWorkingCopy(Object element);

    /**
	 * Saves the content of the given document to the given element. This method has
	 * only an effect if it is called when directly or indirectly inside <code>saveDocument</code>.
	 *
	 * @param monitor the progress monitor
	 * @param element the element to which to save
	 * @param document the document to save
	 * @param overwrite <code>true</code> if the save should be enforced
	 */
    void saveDocumentContent(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException;

    /**
	 * Creates a line tracker for the given element. It is of the same kind as the one that would be
	 * used for a newly created document for the given element.
	 *
	 * @param element the element
	 * @return a line tracker for the given element
	 */
    ILineTracker createLineTracker(Object element);

    /**
	 * Sets the document provider's save policy.
	 *
	 * @param savePolicy the save policy
	 */
    void setSavePolicy(IOScriptSavePolicy savePolicy);

    /**
	 * Adds a listener that reports changes from all compilation unit annotation models.
	 *
	 * @param listener the listener
	 */
    void addGlobalAnnotationModelListener(IAnnotationModelListener listener);

    /**
	 * Removes the listener.
	 *
	 * @param listener the listener
	 */
    void removeGlobalAnnotationModelListener(IAnnotationModelListener listener);
}
