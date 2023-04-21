package ca.ucalgary.cpsc.ebe.fitClipse.tests.fit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.CheckConditionsOperation;
import org.eclipse.ltk.core.refactoring.CreateChangeOperation;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ui.PlatformUI;
import org.junit.Before;
import org.junit.Test;
import aaftt.FixtureManager;
import aaftt.RefactoringException;
import aaftt.Suite;
import ca.ucalgary.cpsc.ebe.fitClipse.persistence.XMLPersistenceUtils;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.core.AcceptanceTestRefactoring;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.core.AddColumnInfo;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.core.AddColumnProcessor;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.core.RemoveColumnInfo;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.core.RemoveColumnProcessor;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.parse.IRefactoringTest;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.parse.RefactoringTestFactory;
import ca.ucalgary.cpsc.ebe.fitClipse.refactoring.parse.fixture.IFixtureParser;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.properties.PropertyPersistance;
import ca.ucalgary.cpsc.ebe.fitClipse.util.FitClipseProject;
import fitlibrary.DoFixture;
import fitpde.FitPdeTestCase;

/**
 * The Class RefactoringAddRemoveColumn.
 */
public class RefactoringAddRemoveColumnPDE extends DoFixture {

    /**
	 * Check file.
	 * 
	 * @param file
	 *            the file
	 * 
	 * @return true, if successful
	 */
    private boolean checkFile(File file) {
        if (!file.exists()) {
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Configuration Problem", "The configured files for persistance don't exist.\n" + "Please check your FitClipse configuration at the FitClipse property page.");
            return false;
        } else return true;
    }

    /**
	 * Check that fixture of has not a parameterless method with return type
	 * string called.
	 * 
	 * @return true, if successful
	 */
    @Test
    public void checkThatFixtureOfHasNotAParameterlessMethodWithReturnTypeStringCalled() {
        String of = "";
        String called = "";
        if (properties != null) {
            if (!properties.containsKey("method.checkThatFixtureOfHasNotAParameterlessMethodWithReturnTypeStringCalled")) return; else {
                of = properties.getProperty("of");
                called = properties.getProperty("called");
            }
        } else return;
        assertFalse(checkFixture(of, called));
    }

    /**
	 * Check that fixture of has a parameterless method with return type string
	 * called.
	 * 
	 * @return true, if successful
	 */
    @Test
    public void checkThatFixtureOfHasAParameterlessMethodWithReturnTypeStringCalled() {
        String of = "";
        String called = "";
        if (properties != null) {
            if (!properties.containsKey("method.checkThatFixtureOfHasAParameterlessMethodWithReturnTypeStringCalled")) return; else {
                of = properties.getProperty("of");
                called = properties.getProperty("called");
            }
        } else return;
        assertTrue(checkFixture(of, called));
    }

    /**
	 * Check fixture.
	 * 
	 * @param of
	 *            the of
	 * @param called
	 *            the called
	 * 
	 * @return true, if successful
	 */
    private boolean checkFixture(String of, String called) {
        aaftt.Test t = getTest(of);
        IDocument testDefinition = new Document(t.getTestDefinition());
        try {
            IRefactoringTest refactorTest = RefactoringTestFactory.getTest(testDefinition, t);
            for (IFixtureParser fixtureParser : refactorTest.getFixtureParser()) {
                if (!fixtureParser.checkIfMethodExists(called, ModifierKeyword.PUBLIC_KEYWORD, null, null)) return false;
            }
        } catch (RefactoringException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
	 * Removes the column of.
	 * 
	 * @return true, if successful
	 */
    @Test
    public void removeColumnOf() {
        String column = "";
        String of = "";
        if (properties != null) {
            if (!properties.containsKey("method.removeColumnOf")) return; else {
                column = properties.getProperty("column");
                of = properties.getProperty("of");
            }
        } else return;
        aaftt.Test t = getTest(of);
        IDocument testDefinition = new Document(t.getTestDefinition());
        try {
            IRefactoringTest refactorTest = RefactoringTestFactory.getTest(testDefinition, t);
            RemoveColumnInfo removeColumnInfo = new RemoveColumnInfo(t);
            removeColumnInfo.setSelectedColumn(refactorTest.getColumnByName(column));
            AcceptanceTestRefactoring ref = new AcceptanceTestRefactoring(new RemoveColumnProcessor(removeColumnInfo));
            CreateChangeOperation co = new CreateChangeOperation(new CheckConditionsOperation(ref, CheckConditionsOperation.ALL_CONDITIONS), RefactoringCore.getConditionCheckingFailedSeverity());
            IWorkspace rootContainer = ResourcesPlugin.getWorkspace();
            rootContainer.checkpoint(false);
            final PerformChangeOperation perform = new PerformChangeOperation(co);
            rootContainer.getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
            rootContainer.run(perform, new NullProgressMonitor());
            Iterator<String> iterator = removeColumnInfo.getTestIterator();
            while (iterator.hasNext()) {
                String uniqueID = iterator.next();
                aaftt.Test test = removeColumnInfo.getTests().get(uniqueID);
                test.setTestDefinition(removeColumnInfo.getWikiText(uniqueID).get());
            }
            assertTrue(perform.changeExecuted());
        } catch (RefactoringException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Check column count of is.
	 * 
	 * @return true, if successful
	 */
    @Test
    public void checkColumnCountOfIs() {
        String of = "";
        String is = "";
        if (properties != null) {
            if (!properties.containsKey("method.checkColumnCountOfIs")) return; else {
                of = properties.getProperty("of");
                is = properties.getProperty("is");
            }
        } else return;
        aaftt.Test t = getTest(of);
        IDocument testDefinition = new Document(t.getTestDefinition());
        try {
            IRefactoringTest refactorTest = RefactoringTestFactory.getTest(testDefinition, t);
            assertTrue(refactorTest.getColumns().size() == Integer.valueOf(is));
        } catch (RefactoringException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Gets the test.
	 * 
	 * @param testName
	 *            the test name
	 * 
	 * @return the test
	 */
    private aaftt.Test getTest(String testName) {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("SimpleBank");
        FixtureManager fixManager = FixtureManager.getInstance();
        fixManager.resetFixtureManager();
        File testResultStorageFile = PropertyPersistance.getProperties(project).getTestResultStorageFile();
        if (!checkFile(testResultStorageFile)) return null;
        XMLPersistenceUtils.loadTestResultStorage(testResultStorageFile);
        File testStorageFile = PropertyPersistance.getProperties(project).getTestStorageFile();
        if (!checkFile(testStorageFile)) return null;
        FitClipseProject.setCurrent(project);
        File saveDir = PropertyPersistance.getProperties(project).getTestStorageFile();
        Suite rootSuite = XMLPersistenceUtils.load(saveDir);
        aaftt.Test t = rootSuite.getByUniqueID("TestStorage.SimpleBank." + testName);
        return t;
    }

    /**
	 * Adds the column to after the last column.
	 * 
	 * @return true, if successful
	 */
    @Test
    public void addColumnToAfterTheLastColumn() {
        String column = "";
        String to = "";
        if (properties != null) {
            if (!properties.containsKey("method.addColumnToAfterTheLastColumn")) return; else {
                column = properties.getProperty("column");
                to = properties.getProperty("to");
            }
        } else return;
        aaftt.Test t = getTest(to);
        IDocument testDefinition = new Document(t.getTestDefinition());
        try {
            IRefactoringTest refactorTest = RefactoringTestFactory.getTest(testDefinition, t);
            AddColumnInfo addColumnInfo = new AddColumnInfo(t);
            addColumnInfo.setBefore(false);
            addColumnInfo.setSelectedColumn(refactorTest.getColumns().get(refactorTest.getColumns().size() - 1));
            addColumnInfo.setValue(column);
            AcceptanceTestRefactoring ref = new AcceptanceTestRefactoring(new AddColumnProcessor(addColumnInfo));
            CreateChangeOperation co = new CreateChangeOperation(new CheckConditionsOperation(ref, CheckConditionsOperation.ALL_CONDITIONS), RefactoringCore.getConditionCheckingFailedSeverity());
            IWorkspace rootContainer = ResourcesPlugin.getWorkspace();
            rootContainer.checkpoint(false);
            final PerformChangeOperation perform = new PerformChangeOperation(co);
            rootContainer.getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
            rootContainer.run(perform, new NullProgressMonitor());
            Iterator<String> iterator = addColumnInfo.getTestIterator();
            while (iterator.hasNext()) {
                String uniqueID = iterator.next();
                aaftt.Test test = addColumnInfo.getTests().get(uniqueID);
                test.setTestDefinition(addColumnInfo.getWikiText(uniqueID).get());
            }
            assertTrue(perform.changeExecuted());
        } catch (RefactoringException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    /**
	 * The properties.
	 */
    protected static Properties properties = null;

    /**
	 * The Constant propertiesName.
	 */
    private static final String propertiesName = "test.properties";

    /**
	 * Pre start.
	 */
    @Before
    public void preStart() {
        properties = new Properties();
        try {
            if (FitPdeTestCase.checkIfPropertiesFileExists(propertiesName)) properties = FitPdeTestCase.loadPropertiesFromFile(propertiesName, properties);
        } catch (IOException e) {
            ;
        }
    }
}
