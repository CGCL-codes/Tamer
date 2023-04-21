package com.liferay.portalweb.portlet.polls;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="AddQuestionTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddQuestionTest extends BaseTestCase {

    public void testAddQuestion() throws Exception {
        selenium.click(RuntimeVariables.replace("//input[@value='Add Question']"));
        selenium.waitForPageToLoad("30000");
        selenium.typeKeys("_25_title", RuntimeVariables.replace("Test Poll Question"));
        selenium.typeKeys("_25_description", RuntimeVariables.replace("This is a test poll description!"));
        selenium.typeKeys("_25_choiceDescriptiona", RuntimeVariables.replace("Test Choice A"));
        selenium.typeKeys("_25_choiceDescriptionb", RuntimeVariables.replace("Test Choice B"));
        selenium.click(RuntimeVariables.replace("//input[@value='Add Choice']"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_25_choiceDescriptionc")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.typeKeys("_25_choiceDescriptionc", RuntimeVariables.replace("Test Choice C"));
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Test Poll Question")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}
