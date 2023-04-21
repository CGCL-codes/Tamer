package org.tagunit.test;

import javax.servlet.http.HttpServletResponse;
import org.tagunit.TestContextContainer;
import org.tagunit.TestContext;

/**
 * Asserts that a named header exists, and optionally also asserts its value.
 *
 * @author    Simon Brown
 */
public class HeaderTestPackage extends AbstractTestPackage {

    private String name;

    private String value;

    /**
   * Creates a new test package for the specified tag.
   */
    public HeaderTestPackage(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
   * Runs the tests that are part of this test package.
   *
   * @param testContext   the TestContext to which these tests are to be run
   */
    public void executeTest(TestContextContainer testContext) {
        HttpServletResponse response = (HttpServletResponse) getPageContext().getResponse();
        boolean b = response.containsHeader(name);
        if (!b) {
            testContext.setMessage("Header named " + name + " does not exist.");
            testContext.setStatus(TestContext.FAIL);
        }
    }

    /**
   * Gets the name of this test package.
   *
   * @return  the name of this test package as a String
   */
    public String getName() {
        if (value == null) {
            return "Existence of header named " + name;
        } else {
            return "Existence of header named " + name + " (value=" + value + ")";
        }
    }
}
