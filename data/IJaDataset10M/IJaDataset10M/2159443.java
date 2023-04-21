package net.sf.refactorit.refactorings.conflicts;

import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.common.util.CollectionUtil;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.conflicts.resolution.AddImplementationResolution;
import net.sf.refactorit.refactorings.conflicts.resolution.ConflictResolution;
import net.sf.refactorit.source.edit.Editor;
import net.sf.refactorit.source.format.BinFormatter;
import net.sf.refactorit.ui.DialogManager;
import java.util.List;

/**
 *
 * @author vadim
 */
public class AddImplementationConflict extends UpDownMemberConflict {

    private static final ConflictType conflictType = ConflictType.IMPLEMENTATION_NEEDED;

    private ConflictResolver resolver;

    public AddImplementationConflict(ConflictResolver resolver, BinMember upMember, List implementers, int implementAccess) {
        super(upMember, implementers);
        this.resolver = resolver;
        setResolution(new AddImplementationResolution(resolver, upMember, implementers, implementAccess));
    }

    public ConflictType getType() {
        return AddImplementationConflict.conflictType;
    }

    public String getDescription() {
        return "Implementation of " + BinFormatter.format(getUpMember()) + " must be added" + " into the following types";
    }

    public boolean isResolvable() {
        return true;
    }

    public void resolve() {
        ConflictResolution resolution = DialogManager.getInstance().getResultFromResolutionDialog(CollectionUtil.singletonArrayList(getResolution()));
        if (resolution != null) {
            resolution.runResolution(resolver);
        }
    }

    public int getSeverity() {
        return RefactoringStatus.INFO;
    }

    public Editor[] getEditors() {
        return getResolution().getEditors(resolver);
    }
}
