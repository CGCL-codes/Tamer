package org.objectstyle.wolips.refactoring;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.MoveParticipant;
import org.objectstyle.wolips.datasets.adaptable.Project;

/**
 * Changes the package name of a WOLips project's Principal Class when it gets moved.  
 * 
 * @author mschrag
 */
public class PrincipalClassMoveParticipant extends MoveParticipant {

    private Project myProject;

    private String myName;

    private IJavaElement myJavaDestination;

    public PrincipalClassMoveParticipant() {
    }

    protected boolean initialize(Object _element) {
        myProject = PrincipalClassMoveParticipant.getInitializedProject(_element);
        boolean initialized = false;
        if (myProject != null) {
            Object destination = getArguments().getDestination();
            if (destination instanceof IPackageFragment || destination instanceof IType) {
                myName = ((IType) _element).getElementName();
                myJavaDestination = (IJavaElement) destination;
                initialized = true;
            }
        }
        return initialized;
    }

    public String getName() {
        return "Move Principal Class";
    }

    public RefactoringStatus checkConditions(IProgressMonitor _pm, CheckConditionsContext _context) throws OperationCanceledException {
        RefactoringStatus refactoringStatus = new RefactoringStatus();
        return refactoringStatus;
    }

    public Change createChange(IProgressMonitor _pm) throws CoreException, OperationCanceledException {
        Change change = null;
        if (myProject != null) {
            String newName = myJavaDestination.getElementName();
            String newFullyQualifiedName;
            if (myJavaDestination instanceof IType) {
                newFullyQualifiedName = ((IType) myJavaDestination).getFullyQualifiedName() + '$' + myName;
            } else if (myJavaDestination instanceof IPackageFragment) {
                IPackageFragment destinationPackage = (IPackageFragment) myJavaDestination;
                if (destinationPackage.isDefaultPackage()) {
                    newFullyQualifiedName = myName;
                } else {
                    newFullyQualifiedName = myJavaDestination.getElementName() + '.' + myName;
                }
            } else {
                newFullyQualifiedName = null;
            }
            if (newFullyQualifiedName != null) {
                change = new PrincipalClassChange(myProject, newFullyQualifiedName);
            }
        }
        return change;
    }

    public static Project getInitializedProject(Object _element) {
        Project initializedProject = null;
        try {
            if (_element instanceof IType) {
                IType sourceType = (IType) _element;
                Project project = (Project) sourceType.getJavaProject().getProject().getAdapter(Project.class);
                String principalClass = project.getPrincipalClass(true);
                String fullyQualifiedName = sourceType.getFullyQualifiedName();
                if (principalClass != null && principalClass.equals(fullyQualifiedName)) {
                    initializedProject = project;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return initializedProject;
    }
}
