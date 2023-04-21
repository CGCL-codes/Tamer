package net.sf.refactorit.refactorings.movemember;

import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.LocationAware;
import net.sf.refactorit.common.util.StringUtil;
import net.sf.refactorit.loader.Comment;
import net.sf.refactorit.refactorings.AbstractRefactoring;
import net.sf.refactorit.refactorings.Refactoring;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.conflicts.ConflictResolver;
import net.sf.refactorit.source.edit.MoveEditor;
import net.sf.refactorit.transformations.TransformationList;
import net.sf.refactorit.ui.module.RefactorItContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Move Method refactoring implementation.
 *
 * @author Anton Safonov, Vadim Hahhulin
 */
public class MoveMember extends AbstractRefactoring {

    public static String key = "refactoring.movemember";

    private ConflictResolver conflictResolver;

    private ReferenceUpdater referenceUpdater = new ReferenceUpdater();

    public MoveMember(RefactorItContext context, BinCIType nativeType, List targetAsList) {
        super("Move member", context);
        this.conflictResolver = new MoveMemberConflictsResolver(targetAsList, nativeType, null, this.referenceUpdater);
    }

    public RefactoringStatus checkPreconditions() {
        RefactoringStatus status = new RefactoringStatus();
        BinCIType nativeType = conflictResolver.getNativeType();
        List members = conflictResolver.getBinMembersToMove();
        for (int i = 0, max = members.size(); i < max; i++) {
            BinMember member = (BinMember) members.get(i);
            BinTypeRef owner = member.getOwner();
            if (owner.getBinCIType() != nativeType) {
                status.addEntry("Selected members are not from the single class", RefactoringStatus.ERROR);
                return status;
            }
        }
        if (!nativeType.isFromCompilationUnit()) {
            status.addEntry(StringUtil.capitalizeFirstLetter(nativeType.getMemberType()) + " " + nativeType.getQualifiedName() + "\nis outside of the source path and its members cannot be moved!", RefactoringStatus.ERROR);
        }
        if (nativeType.getDeclaredFields().length == 0 && nativeType.getDeclaredMethods().length == 0 && nativeType.getDeclaredTypes().length == 0) {
            status.addEntry(StringUtil.capitalizeFirstLetter(nativeType.getMemberType()) + " " + nativeType.getQualifiedName() + "\ncontains neither fields " + "nor methods nor inner classes.\nNothing to move.", RefactoringStatus.ERROR);
        }
        if (!nativeType.isFromCompilationUnit()) {
            status.addEntry("Could not move members of a class outside of sourcepath: " + nativeType.getQualifiedName(), RefactoringStatus.ERROR);
        }
        return status;
    }

    /**
   * @see Refactoring#checkUserInput
   */
    public RefactoringStatus checkUserInput() {
        final RefactoringStatus status = new RefactoringStatus();
        return status;
    }

    /**
   * @see net.sf.refactorit.refactorings.Refactoring#performChange
   */
    public TransformationList performChange() {
        TransformationList transList = new TransformationList();
        List toMove = conflictResolver.addConflictEditors(transList);
        referenceUpdater.createEditors(transList);
        if (transList.getStatus().isErrorOrFatal()) {
            conflictResolver.clearConflictRepository();
            return transList;
        }
        List comments = new ArrayList();
        for (int i = 0; i < toMove.size(); i++) {
            final LocationAware member = (LocationAware) toMove.get(i);
            if (!MoveEditor.isFromMultipleDeclaration(member)) {
                comments.addAll(Comment.findAllFor(member));
            }
        }
        toMove.addAll(comments);
        Collections.sort(toMove, LocationAware.PositionSorter.getInstance());
        transList.add(new MoveEditor(toMove, conflictResolver.getTargetType()));
        conflictResolver.clearConflictRepository();
        return transList;
    }

    /** For legacy code which wants direct access to conflicts? */
    public ConflictResolver getResolver() {
        return this.conflictResolver;
    }

    public String getDescription() {
        List members = conflictResolver.getBinMembersToMove();
        if (members.size() > 7) {
            return "Move Member - moving many";
        } else {
            StringBuffer buf = new StringBuffer();
            buf.append("Move");
            for (int i = 0; i < members.size(); i++) {
                BinMember member;
                member = (BinMember) members.get(i);
                buf.append(' ' + member.getName());
                if (member instanceof BinMethod) {
                    buf.append("()");
                }
                buf.append(",");
            }
            buf = buf.deleteCharAt(buf.length() - 1);
            buf.append(" to " + conflictResolver.getTargetType().getName());
            return new String(buf);
        }
    }

    public String getKey() {
        return key;
    }
}
