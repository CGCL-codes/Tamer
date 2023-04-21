package com.liferay.portalweb.portlet.language;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="DeletePageTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DeletePageTest extends BaseTestCase {

    public void testDeletePage() throws Exception {
        selenium.click(RuntimeVariables.replace("//div[@id='navigation']/ul/li[1]/a/span"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Manage Pages"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("//div[@id='_88_layoutsTreeOutput']/ul/li[2]/ul/li[3]/a/span"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("//input[@value='Delete']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.getConfirmation().matches("^Are you sure you want to delete the selected page[\\s\\S]$"));
        selenium.click(RuntimeVariables.replace("link=Return to Full Page"));
        selenium.waitForPageToLoad("30000");
    }
}
