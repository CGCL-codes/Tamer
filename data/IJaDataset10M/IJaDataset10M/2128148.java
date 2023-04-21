package net.sf.orcc.ui.refactoring;

import net.sf.orcc.util.OrccUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.MoveParticipant;

/**
 * This class defines a MoveParticipant for XDF networks.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class XdfMoveParticipant extends MoveParticipant {

    private IFile networkFile;

    private XdfReferencesUpdater referenceUpdater;

    @Override
    public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {
        referenceUpdater = new XdfReferencesUpdater(networkFile);
        referenceUpdater.checkConditions(context);
        return new RefactoringStatus();
    }

    @Override
    public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
        Object dest = getArguments().getDestination();
        if (dest instanceof IFolder) {
            IFolder destFolder = (IFolder) dest;
            IFile destFile = destFolder.getFile(networkFile.getName());
            String newQualifiedName = OrccUtil.getQualifiedName(destFile);
            return referenceUpdater.createChange(newQualifiedName);
        }
        return null;
    }

    @Override
    public String getName() {
        return "XDF move participant";
    }

    @Override
    protected boolean initialize(Object element) {
        networkFile = (IFile) element;
        return true;
    }
}
