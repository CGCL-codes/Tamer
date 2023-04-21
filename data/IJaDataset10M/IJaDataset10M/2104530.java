package org.parallelj.designer.extension.tools;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.swt.widgets.Display;
import org.parallelj.designer.extension.Activator;
import org.parallelj.model.Specification;

/**
 * This class is used to validate the diagram and checks if the contents of the
 * diagram contains errors.
 * 
 */
public class ParallelJDiagramValidator {

    private ParallelJDiagramValidator() {
    }

    /**
	 * Validates the current diagram of the specified Editor.
	 * 
	 * @param specification
	 * @return boolean condition if errors exist
	 */
    public static boolean validate(final Specification specification) {
        final IFile element = WorkspaceSynchronizer.getFile(specification.eResource());
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
            }
        });
        boolean hasErrors = false;
        IMarker[] markers;
        try {
            markers = element.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
            for (int i = 0; i < markers.length && !hasErrors; i++) {
                if (markers[i].getAttribute(IMarker.SEVERITY).equals(IMarker.SEVERITY_ERROR)) {
                    hasErrors = true;
                    break;
                }
            }
            return hasErrors;
        } catch (CoreException e) {
            Activator.logError(e.getMessage());
            return false;
        }
    }
}
