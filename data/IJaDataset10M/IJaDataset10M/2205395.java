package org.eclipse.jdt.internal.core;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Switch and ICompilationUnit to working copy mode
 * and signal the working copy addition through a delta.
 */
public class BecomeWorkingCopyOperation extends JavaModelOperation {

    IProblemRequestor problemRequestor;

    public BecomeWorkingCopyOperation(CompilationUnit workingCopy, IProblemRequestor problemRequestor) {
        super(new IJavaElement[] { workingCopy });
        this.problemRequestor = problemRequestor;
    }

    protected void executeOperation() throws JavaModelException {
        CompilationUnit workingCopy = getWorkingCopy();
        JavaModelManager.getJavaModelManager().getPerWorkingCopyInfo(workingCopy, true, true, this.problemRequestor);
        workingCopy.openWhenClosed(workingCopy.createElementInfo(), this.progressMonitor);
        if (!workingCopy.isPrimary()) {
            JavaElementDelta delta = new JavaElementDelta(getJavaModel());
            delta.added(workingCopy);
            addDelta(delta);
        } else {
            if (workingCopy.getResource().isAccessible()) {
                JavaElementDelta delta = new JavaElementDelta(getJavaModel());
                delta.changed(workingCopy, IJavaElementDelta.F_PRIMARY_WORKING_COPY);
                addDelta(delta);
            } else {
                JavaElementDelta delta = new JavaElementDelta(this.getJavaModel());
                delta.added(workingCopy, IJavaElementDelta.F_PRIMARY_WORKING_COPY);
                addDelta(delta);
            }
        }
        this.resultElements = new IJavaElement[] { workingCopy };
    }

    protected CompilationUnit getWorkingCopy() {
        return (CompilationUnit) getElementToProcess();
    }

    public boolean isReadOnly() {
        return true;
    }
}
