package ca.ucalgary.cpsc.ebe.fitClipse.refactoring.core;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipse;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipseProject;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.parse.IRefactoringTest;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.parse.RefactoringTestFactory;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.utils.RefactoringUtil;
import ca.ucalgary.cpsc.ebe.fitClipseCore.core.data.ITestFile;
import ca.ucalgary.cpsc.ebe.fitClipseCore.core.data.impl.Column;
import ca.ucalgary.cpsc.ebe.fitClipseCore.exceptions.InvalidTestTableException;
import ca.ucalgary.cpsc.ebe.fitClipseCore.exceptions.RefactoringException;
import ca.ucalgary.cpsc.ebe.fitClipseCore.refactoring.RemoveColumnInfo;

public class RemoveColumnRefactoring extends Refactoring {

    private final RemoveColumnInfo info;

    private IRefactoringTest refactoringTest;

    RefactoringUtil util;

    public RemoveColumnRefactoring(final RemoveColumnInfo info) {
        this.info = info;
    }

    @Override
    public RefactoringStatus checkInitialConditions(final IProgressMonitor pm) {
        pm.beginTask("checking initial conditions", 5);
        final RefactoringStatus result = new RefactoringStatus();
        final ITestFile selectedTestFile = info.getSelectedTestFile();
        try {
            RemoveColumnInfo conditionInfo = new RemoveColumnInfo(selectedTestFile);
            pm.worked(1);
            conditionInfo.setDeleteColumn(new Column());
            pm.worked(1);
            IRefactoringTest testToRefactor = RefactoringTestFactory.getTest(selectedTestFile, FitClipseProject.newProject());
            testToRefactor.removeColumn(conditionInfo);
            pm.worked(3);
        } catch (final RefactoringException e) {
            result.addFatalError(e.getMessage());
        } catch (final InvalidTestTableException e) {
            result.addFatalError(e.getMessage());
        } finally {
            pm.done();
        }
        return result;
    }

    @Override
    public RefactoringStatus checkFinalConditions(final IProgressMonitor pm) {
        pm.beginTask("checking final conditions", 5);
        final RefactoringStatus result = new RefactoringStatus();
        final ITestFile selectedTestFile = info.getSelectedTestFile();
        try {
            util = new RefactoringUtil(selectedTestFile, FitClipseProject.newProject());
            pm.worked(4);
            refactoringTest = RefactoringTestFactory.getTest(selectedTestFile, FitClipseProject.newProject());
            refactoringTest.setFixtureParsers(util.getFixtureParsersForRemoveRefactoring(info));
            pm.worked(1);
        } catch (final RefactoringException e) {
            result.addFatalError(e.getMessage());
        } catch (final InvalidTestTableException e) {
            result.addFatalError(e.getMessage());
        } finally {
            pm.done();
        }
        return result;
    }

    @Override
    public Change createChange(final IProgressMonitor pm) {
        CompositeChange rootChange = new CompositeChange("Remove Column Refactoring");
        try {
            pm.beginTask("Constructing changes", 1);
            rootChange.addAll(createRemoveColumnChange());
            pm.worked(1);
        } finally {
            pm.done();
        }
        return rootChange;
    }

    private Change[] createRemoveColumnChange() {
        List<Change> result = new LinkedList<Change>();
        if (info.getDeleteColumn() != null) {
            try {
                result.addAll(refactoringTest.removeColumn(info));
            } catch (final RefactoringException e) {
                FitClipse.showDialog(e.getMessage());
            } catch (final InvalidTestTableException e) {
                e.printStackTrace();
            }
        }
        RefactoringUtil.removeDuplicateAndEmptyChanges(result, info);
        return result.toArray(new Change[0]);
    }

    @Override
    public String getName() {
        return "Remove Column Refactoring";
    }
}
