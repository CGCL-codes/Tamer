package ca.ucalgary.cpsc.ebe.fitClipse.refactoring.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.SharableParticipants;

/**
 * The Class RenameProcessorsProcessor.
 */
public class AddRemoveActionProcessor extends RefactoringProcessor {

    /** The info. */
    private final AddRemoveActionInfo info;

    /** The delegate. */
    private final AddRemoveActionDelegate delegate;

    /**
	 * Instantiates a new rename processors processor.
	 * 
	 * @param info the info
	 */
    public AddRemoveActionProcessor(final AddRemoveActionInfo info) {
        this.info = info;
        this.delegate = new AddRemoveActionDelegate(info);
    }

    public RefactoringStatus checkFinalConditions(final IProgressMonitor pm, final CheckConditionsContext context) {
        return this.delegate.checkFinalConditions(pm, context);
    }

    public RefactoringStatus checkInitialConditions(final IProgressMonitor pm) {
        return this.delegate.checkInitialConditions();
    }

    public Change createChange(final IProgressMonitor pm) {
        CompositeChange result = new CompositeChange(getProcessorName());
        this.delegate.createChange(pm, result);
        return result;
    }

    public Object[] getElements() {
        return null;
    }

    public String getIdentifier() {
        return getClass().getName();
    }

    public String getProcessorName() {
        return "Add/Remove Actions";
    }

    public boolean isApplicable() throws CoreException {
        return true;
    }

    public RefactoringParticipant[] loadParticipants(final RefactoringStatus status, final SharableParticipants sharedParticipants) {
        return new RefactoringParticipant[0];
    }
}
