package org.tagunit.test;

import org.tagunit.TestContextContainer;
import org.tagunit.TestContext;
import org.tagunit.taginfo.TagInfo;

/**
 * Wraps up the automatic tests for a tag handler.
 *
 * @author    Simon Brown
 */
public class BodyContentTestPackage extends AbstractTagTestPackage {

    /** the (expected) body content type of the tag */
    private String bodyContent;

    /**
   * Creates a new test package for the specified tag.
   *
   * @param tagInfo   a TagInfo instance
   */
    public BodyContentTestPackage(TagInfo tagInfo, String bodyContent) {
        super(tagInfo);
        this.bodyContent = bodyContent;
    }

    /**
   * Runs the tests that are part of this test package.
   *
   * @param testContext   the TestContext to which these tests are to be run
   */
    public void executeTest(TestContextContainer testContext) {
        if (!getTagInfo().getBodyContent().equalsIgnoreCase(bodyContent)) {
            testContext.setMessage("Body content type is " + getTagInfo().getBodyContent());
            testContext.setStatus(TestContext.FAIL);
        } else {
            testContext.setStatus(TestContext.PASS);
        }
    }

    /**
   * Gets the name of this test package.
   *
   * @return  the name of this test package as a String
   */
    public String getName() {
        return "Body content is " + this.bodyContent;
    }
}
