package ca.ucalgary.cpsc.ebe.fitClipse.core.runner;

import java.util.LinkedList;
import org.eclipse.core.resources.IProject;
import ca.ucalgary.cpsc.ebe.fitClipse.core.data.ISuite;
import ca.ucalgary.cpsc.ebe.fitClipse.core.data.ITestFile;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testResults.model.ResultRoot;

public interface IAcceptanceTestManager {

    public abstract void addClassPath(String path);

    public abstract void clearClassPaths();

    public abstract void clearTests();

    public abstract void createTest(ITestFile test);

    public abstract void createTestsInSuite(ISuite suite);

    public abstract IProject getProject();

    public abstract IProject getProjectForWikiNameSpace(String wikiPrjName);

    public abstract String getResultTestRoot();

    public abstract String getSourceTestRoot();

    public abstract LinkedList<IAcceptanceTestRunner> getTests();

    public abstract void loadProjectRuntimeProperties();

    public abstract boolean removeClassPath(String classPath);

    public abstract ResultRoot runTests(ResultRoot root);

    public abstract ResultRoot runTests(ResultRoot root, String runMode);

    public abstract void setProject(IProject project);

    public abstract void setResultTestRoot(String resultTestRoot);

    public abstract void setSourceTestRoot(String sourceTestRoot);

    public abstract void setSuiteQName(String name);

    public abstract int tallyExceptions();

    public abstract int tallyIgnored();

    public abstract int tallyRight();

    public abstract int tallyWrong();
}
