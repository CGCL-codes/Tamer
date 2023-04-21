package com.liferay.portalweb.portlet.enterpriseadmin;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="AddUserTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddUserTest extends BaseTestCase {

    public void testAddUser() throws Exception {
        selenium.click(RuntimeVariables.replace("link=Users"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("//input[@value='Add User']"));
        selenium.waitForPageToLoad("30000");
        selenium.typeKeys("_79_screenName", RuntimeVariables.replace("selenium01"));
        selenium.type("_79_screenName", RuntimeVariables.replace("selenium01"));
        selenium.type("_79_emailAddress", RuntimeVariables.replace("test01@selenium.com"));
        selenium.typeKeys("_79_prefixId", RuntimeVariables.replace("label=Mr."));
        selenium.type("_79_prefixId", RuntimeVariables.replace("label=Mr."));
        selenium.typeKeys("_79_firstName", RuntimeVariables.replace("selen01"));
        selenium.type("_79_firstName", RuntimeVariables.replace("selen01"));
        selenium.type("_79_middleName", RuntimeVariables.replace("lenn"));
        selenium.typeKeys("_79_lastName", RuntimeVariables.replace("nium01"));
        selenium.type("_79_lastName", RuntimeVariables.replace("nium01"));
        selenium.select("_79_suffixId", RuntimeVariables.replace("label=PhD."));
        selenium.select("_79_birthdayMonth", RuntimeVariables.replace("label=April"));
        selenium.select("_79_birthdayDay", RuntimeVariables.replace("label=10"));
        selenium.select("_79_birthdayYear", RuntimeVariables.replace("label=1986"));
        selenium.select("_79_male", RuntimeVariables.replace("label=Male"));
        selenium.typeKeys("_79_jobTitle", RuntimeVariables.replace("Selenium Test 01"));
        selenium.type("_79_jobTitle", RuntimeVariables.replace("Selenium Test 01"));
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("//input[@value='Add']"));
        selenium.waitForPageToLoad("30000");
        selenium.type("_79_address", RuntimeVariables.replace("test01@selenium.com"));
        selenium.select("_79_typeId", RuntimeVariables.replace("label=E-mail"));
        selenium.click("_79_primaryCheckbox");
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
        selenium.type("_79_comments", RuntimeVariables.replace("This is a test comment!"));
        selenium.click("link=Password");
        selenium.type("_79_password1", RuntimeVariables.replace("test"));
        selenium.type("_79_password2", RuntimeVariables.replace("test"));
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Return to Full Page"));
        selenium.waitForPageToLoad("30000");
    }
}
