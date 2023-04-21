package net.sf.refactorit.refactorings.conflicts;

import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.common.util.CollectionUtil;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.conflicts.resolution.MoveMemberResolution;
import net.sf.refactorit.source.edit.Editor;
import net.sf.refactorit.source.format.BinFormatter;
import java.util.List;

/**
 *
 * @author vadim
 */
public class MoveDependentConflict extends UpDownMemberConflict {

    private ConflictType conflictType;

    private ConflictResolver resolver;

    public MoveDependentConflict(ConflictResolver resolver, ConflictType conflictType, BinMember upMember, BinMember downMember) {
        this(resolver, conflictType, upMember, CollectionUtil.singletonArrayList(downMember));
    }

    public MoveDependentConflict(ConflictResolver resolver, ConflictType conflictType, BinMember upMember, List downMembers) {
        super(upMember, downMembers);
        this.conflictType = conflictType;
        this.resolver = resolver;
        setResolution(new MoveMemberResolution(upMember, downMembers));
    }

    public int getSeverity() {
        return RefactoringStatus.WARNING;
    }

    public String getDescription() {
        if (conflictType == ConflictType.MOVE_USE_ALSO) {
            return "The following members must be moved also since " + BinFormatter.format(getUpMember()) + " uses them";
        } else if (conflictType == ConflictType.MOVE_USEDBY_ALSO) {
            return "The following members must be moved also since " + BinFormatter.format(getUpMember()) + " is used by them";
        } else {
            return "Unknown conflict";
        }
    }

    public ConflictType getType() {
        return conflictType;
    }

    public Editor[] getEditors() {
        return getResolution().getEditors(resolver);
    }

    public boolean isResolvable() {
        return true;
    }

    public void resolve() {
        getResolution().runResolution(resolver);
    }
}
