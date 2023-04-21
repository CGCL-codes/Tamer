package org.openhealthdata.validation.result;

import java.util.List;

/**
 * Interface for a Managed Validation Result
 * @author swaldren
 *
 */
public interface ValidationResultManager {

    /**
         * Return the Validation Result that is being managed
         *
         * @return The managed validation result
         */
    public ValidationResult getResult();

    /**
         * Set the Validation Result to be managed
         *
         * @param result The managed validation result
         */
    public void setResult(ValidationResult result);

    /**
         * Set the name of the file being tested.  This is needed for non-file
         * based test objects (such as DOM, String, JAXBObject)
         * @param filename
         */
    public void setFileTested(String filename);

    /**
         * Returns the name of the file being tested.  This does not need to be
         * a URI or findable in the filesystem
         * @return
         */
    public String getFileTested();

    /**
         * A validation result may have multiple profiles to be tested against.
         * Each profile will have a unique identifier to link test results back too.
         * @param schemaName  The name of the syntax schema (i.e. XSD) used
         * @param schemaVersion The version of the syntax schema
         * @param profileName  A display name for the profile
         * @param profileVersion The version of the profile
         * @param profileID  A unique identifer for this profile
         */
    public void addValidationUsed(String schemaName, String schemaVersion, String profileName, String profileVersion, String profileID);

    /**
         * Add a profile to validation used
         *
         * @param profileName
         * @param profileVersion
         * @param profileID
         */
    public void addProfile(String profileName, String profileVersion, String profileID);

    /**
         * Add a profile to validation used
         * @param p
         */
    public void addProfile(ValidationResult.ValidationUsed.Profile p);

    /**
         * Each test has a unique identifier.  This method retrieves the test
         * based on the supplied unique identifier
         * @param uid The unique identifier for a test
         * @return a found test or <code>null</code> if none found
         */
    public TestResultType getTest(String uid);

    /**
	 * Adds a rule to the list of rules to be used for testing.
	 * This is used to list all tested rules in the validation result
	 * @param id The unique identifier for the rule
	 * @param name The name of the rule
	 * @param title The title for the rule
	 * @param packge The package the rule resides in
	 * @param description A description of what the rule tests against
	 * @param profile The profile to which the rule belongs
	 */
    public void addRule(String id, String name, String title, String packge, String description, String profile, String source, String author);

    /**
         * Added a test to the validation result
         * @param test
         */
    public void addTest(TestResultType test);

    /**
         * Creates and adds a test to the validation result
         *
         * @param testUID  The unique identifier for the test
         * @param name  The display name for the test
         * @param description A short description of the test
         * @param status The status as an enum (see static final strings in <code>TestResultType</code>
         * @return the created test
         */
    public TestResultType addTest(String testUID, String name, String description, String status);

    /**
         * Creates and adds a test to the validation result
         *
         * @param testUID  The unique identifier for the test
         * @param name  The display name for the test
         * @param description A short description of the test
         * @param status The status as an enum (see static final strings in <code>TestResultType</code>
         * @param profiles the unique identifiers for profiles linked to the test
         * @return
         */
    public TestResultType addTest(String testUID, String name, String description, String status, List<String> profiles);

    /**
         * Creates and adds a test to the validation result.  Should also check
         * for existence of profile and add stub profile if not found
         *
         * @param testUID  The unique identifier for the test
         * @param name  The display name for the test
         * @param description A short description of the test
         * @param status The status as an enum (see static final strings in <code>TestResultType</code>
         * @param profile the unique identifier for profile linked to the test
         * @return
         */
    public TestResultType addTest(String testUID, String name, String description, String status, String profile);

    /**
         * Returns a list of tests based on their status
         * @param status  The status as an enum (see static final strings in <code>TestResultType</code>
         * @return
         */
    public List<TestResultType> getTestByStatus(String status);

    /**
         * Adds an error to a test as identified by the test unique identifier.
         * It should create a stub test if no test found by unique identifier.
         * @param testUID The unique identifier of the test
         * @param message The error message
         * @param serverity the serverity of the error as an enum (see static final string in <code>ErrorType</code>
         * @param xpath An Xpath expression of the location of the error.
         */
    public void addError(String testUID, String message, String serverity, String xpath);

    /**
         * Adds an error to a test as identified by the test unique identifier
         * @param testUID the unique identifier of the test
         * @param error the error to add
         */
    public void addError(String testUID, ErrorType error);

    /**
         * Adds multiple errors to a test as identified by the test unique identifier
         * @param testUID the unique identifier of the test
         * @param error the list of errors to add
         */
    public void addError(String testUID, List<ErrorType> error);

    /**
         * Adds an error to a test as identified by the test uniqu identifier.
         * @param testUID the unique identifier of the test
         * @param message the error message
         * @param serverity the serverity of the error as an enum (see static final string in <code>ErrorType</code>
         * @param lineNumber the line number in the file where the error was found
         * @param columnNumber the column number in the line where the error was found
         */
    public void addError(String testUID, String message, String serverity, int lineNumber, int columnNumber);
}
