package testdata;

import java.util.*;

/**
 * The class TestCase represents a testcase for a webapplication,
 * it includes several WebTestCases which will be executed sequentially
 */
@SuppressWarnings("nls")
public class TestCase {

    /**
	 * list of WebTestCases
	 */
    private List<WebTestCase> webTestCases;

    /**
	 * the short description of the testcase
	 */
    private String description;

    /**
	 * Constructs a new TestCase
	 * 
	 * @param description the short description of the testcase
	 * @param webTests list of WebTestCases
	 */
    public TestCase(String description, List<WebTestCase> webTests) {
        this.description = description;
        this.webTestCases = webTests;
    }

    /**
	 * @return the list of WebTestCases
	 */
    public List<WebTestCase> getWebTestCases() {
        return webTestCases;
    }

    /**
	 * @return the short description of the testcase
	 */
    public String getDescription() {
        return description;
    }
}
