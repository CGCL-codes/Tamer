package aaftt;

import java.util.List;

/**
 * The Interface Test describes an acceptance test.
 */
public interface Test {

    /**
	 * Gets the name of the test or suite. The names through the hierarchy build
	 * the uniqueID.
	 * 
	 * @return the unique id
	 */
    public String getName();

    /**
	 * Gets the name of the test or suite. The names through the hierarchy build
	 * the uniqueID.
	 * 
	 * @param testName
	 *            the new unique id
	 * 
	 * @throws RefactoringException
	 *             the refactoring exception
	 */
    public void setName(String testName) throws RefactoringException;

    /**
	 * Gets the unique id.
	 * 
	 * @return TODO the fully qualified unique Name/ID of the test. The unique
	 *         ID is composed of the names of all elements in the hierarchy.
	 */
    public String getUniqueID();

    /**
	 * Gets the test definition.
	 * 
	 * @return the test definition
	 */
    public String getTestDefinition();

    /**
	 * Sets the test definition.
	 * 
	 * @param testDefinition
	 *            the new test definition
	 * 
	 * @throws RefactoringException
	 *             the refactoring exception
	 */
    public void setTestDefinition(String testDefinition) throws RefactoringException;

    /**
	 * Gets the fixture class name. Represents the file name of the source file
	 * of the class which contains the fixture code.
	 * 
	 * @return the fixture class name
	 */
    public List<FixtureInfo> getFixtureInfo();

    /**
	 * Sets the fixture class name. Represents the file name of the source file
	 * of the class which contains the fixture code.
	 * 
	 * @param fixtureInfo
	 *            the new fixture class name
	 * 
	 * @throws RefactoringException
	 *             the refactoring exception
	 */
    public void setFixtureInfo(List<FixtureInfo> fixtureInfo) throws RefactoringException;

    /**
	 * Gets the suite which contains this test.
	 * 
	 * @return the suite this Test belongs to. <code>null</code>, if Test is
	 *         not stored in a suite.
	 */
    public Suite getSuite();

    /**
	 * Sets the suite which contains this test.
	 * 
	 * @param suite
	 *            the suite
	 * 
	 * @throws RefactoringException
	 *             the refactoring exception
	 */
    public void set(Suite suite) throws RefactoringException;
}
