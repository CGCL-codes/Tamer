package ca.ucalgary.cpsc.ebe.fitClipse.refactoring.scenario.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipse;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipseProject;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.parse.IRefactoringTest;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.parse.RefactoringTestFactory;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.parse.fixture.IFixtureParser;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.scenario.RenameScenarioActionInfo;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.utils.RefactoringUtil;
import ca.ucalgary.cpsc.ebe.fitClipseCore.core.data.ITestFile;
import ca.ucalgary.cpsc.ebe.fitClipseCore.exceptions.InvalidTestTableException;
import ca.ucalgary.cpsc.ebe.fitClipseCore.exceptions.RefactoringException;
import ca.ucalgary.cpsc.ebe.fitClipseCore.refactoring.scenario.ScenarioActionTextSplitter;

public class RenameScenarioActionRefactoring extends Refactoring {

    private final RenameScenarioActionInfo info;

    private List<IRefactoringTest> refactoringTests = new ArrayList<IRefactoringTest>();

    private RefactoringUtil util;

    public RenameScenarioActionRefactoring(final RenameScenarioActionInfo info) {
        this.info = info;
    }

    @Override
    public RefactoringStatus checkInitialConditions(final IProgressMonitor pm) {
        pm.beginTask("checking initial conditions", 5);
        final RefactoringStatus result = new RefactoringStatus();
        final ITestFile selectedTestFile = info.getSelectedTestFile();
        try {
            RenameScenarioActionInfo conditionInfo = new RenameScenarioActionInfo(selectedTestFile);
            pm.worked(2);
            conditionInfo.setTestOffset(0);
            conditionInfo.setSplitter(new ScenarioActionTextSplitter("", ""));
            pm.worked(1);
            final IRefactoringTest testToRefactorCondition = RefactoringTestFactory.getTest(selectedTestFile, FitClipseProject.newProject());
            testToRefactorCondition.renameScenarioAction(conditionInfo);
            pm.done();
        } catch (final RefactoringException e) {
            result.addFatalError(e.getMessage());
        } catch (InvalidTestTableException e) {
            result.addFatalError(e.getMessage());
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
            pm.worked(3);
            Map<String, IFixtureParser> parserMap = util.getAllRelatedFixturesAndFixtureParsers(info.getTestOffset());
            info.setInvolvedFixtures(parserMap.keySet());
            for (ITestFile testFile : util.getAllRelatedTests()) {
                info.addTest(testFile);
                IRefactoringTest refactoringTest = RefactoringTestFactory.getTest(testFile, FitClipseProject.newProject());
                refactoringTest.setFixtureParsers(parserMap.values());
                refactoringTests.add(refactoringTest);
            }
            pm.worked(2);
        } catch (final RefactoringException e) {
            result.addFatalError(e.getMessage());
        } catch (InvalidTestTableException e) {
            result.addFatalError(e.getMessage());
        } finally {
            pm.done();
        }
        return result;
    }

    @Override
    public Change createChange(final IProgressMonitor pm) {
        CompositeChange rootChange = new CompositeChange("Rename Scenario Action Refactoring");
        try {
            pm.beginTask("Constructing changes", 1);
            rootChange.addAll(createRenameActionsChange());
            pm.worked(1);
        } finally {
            pm.done();
        }
        return rootChange;
    }

    private Change[] createRenameActionsChange() {
        List<Change> result = new LinkedList<Change>();
        if (info.getSplitter() != null) {
            try {
                for (IRefactoringTest refactoringTest : refactoringTests) {
                    result.addAll(refactoringTest.renameScenarioAction(info));
                }
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
        return "Rename Scenario Action Refactoring";
    }
}
