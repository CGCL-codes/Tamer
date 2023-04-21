package test.old;

import test.ui.XprocessWebTest;

public class XprocessTest extends XprocessWebTest {

    public void update() {
        selenium.click("reschedule");
        selenium.waitForPageToLoad("30000");
    }

    public void login(String xprocessUserName, String xprocessPassword) {
        openApp();
        selenium.type("document.forms[0].userName", xprocessUserName);
        selenium.type("document.forms[0].password", xprocessPassword);
        selenium.click("loginButton");
        selenium.waitForPageToLoad("30000");
    }

    public void loginAndUpdate(String userName, String password) {
        login(userName, password);
        update();
    }

    public void testSubmitTime() throws Exception {
        setupProjectResourceUser1();
        setupT1InSimpleProject();
        loginAndUpdate("user1", "user1");
        verifyFalse(linkIsEnabled("saveButton"));
        verifyFalse(linkIsEnabled("closeTaskButton"));
        verifyFalse(linkIsEnabled("cancelButton"));
        verifyFalse(linkIsEnabled("reopenTaskButton"));
        verifyFalse(linkIsEnabled("setActiveButton"));
        verifyFalse(linkIsEnabled("setInactiveButton"));
        selenium.type("fridayDateField", "1");
        selenium.keyUp("fridayDateField", "33");
        verifyTrue(linkIsEnabled("saveButton"));
        verifyTrue(linkIsEnabled("cancelButton"));
        selenium.click("saveButton");
        selenium.waitForPageToLoad("30000");
        verifyFalse(linkIsEnabled("saveButton"));
        verifyFalse(linkIsEnabled("closeTaskButton"));
        verifyFalse(linkIsEnabled("cancelButton"));
        verifyFalse(linkIsEnabled("reopenTaskButton"));
        verifyFalse(linkIsEnabled("setActiveButton"));
        verifyFalse(linkIsEnabled("setInactiveButton"));
        verifyEquals("1.0", selenium.getValue("fridayDateField"));
        selenium.type("fridayDateField", "0");
        selenium.keyUp("fridayDateField", "33");
        selenium.click("saveButton");
        selenium.waitForPageToLoad("30000");
        verifyEquals("0.0", selenium.getValue("fridayDateField"));
        selenium.click("logoutButton");
        selenium.waitForPageToLoad("30000");
        checkForVerificationErrors();
    }

    public void testFilterActive() throws Exception {
        setupTestFixture();
        loginAndUpdate("user1", "user1");
        verifyTrue(selenium.isElementPresent("selectboxCheckBox"));
        selenium.select("ActiveStatusChooser", "label=Active");
        selenium.click("filterPageButton");
        selenium.waitForPageToLoad("30000");
        verifyFalse(selenium.isElementPresent("selectboxCheckBox"));
        selenium.select("ActiveStatusChooser", "label=Inactive");
        selenium.click("filterPageButton");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("selectboxCheckBox"));
        selenium.click("logoutButton");
        selenium.waitForPageToLoad("30000");
        checkForVerificationErrors();
    }

    public void testFilterClosed() throws Exception {
        setupProjectResourceUser1();
        setupT1InSimpleProject();
        loginAndUpdate("user1", "user1");
        verifyTrue(selenium.isElementPresent("selectboxCheckBox"));
        selenium.select("ClosedStatusChooser", "label=Closed");
        selenium.click("filterPageButton");
        selenium.waitForPageToLoad("30000");
        verifyFalse(selenium.isElementPresent("selectboxCheckBox"));
        selenium.select("ClosedStatusChooser", "label=Open");
        selenium.click("filterPageButton");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("selectboxCheckBox"));
        selenium.click("logoutButton");
        selenium.waitForPageToLoad("30000");
        checkForVerificationErrors();
    }

    public void testFilterDate() throws Exception {
        setupProjectResourceUser1();
        setupT1InSimpleProject();
        loginAndUpdate("user1", "user1");
        selenium.click("//img[@alt='Click on icon to choose a date/time value.']");
        selenium.select("//td[3]/select", "label=1988");
        selenium.click("//div[2]/table/tbody/tr[2]/td[1]");
        selenium.click("filterPageButton");
        selenium.waitForPageToLoad("30000");
        verifyFalse(selenium.isElementPresent("selectboxCheckBox"));
        selenium.click("//img[@alt='Click on icon to choose a date/time value.']");
        selenium.click("//td[2]/button");
        selenium.click("filterPageButton");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("selectboxCheckBox"));
        selenium.click("logoutButton");
        selenium.waitForPageToLoad("30000");
        checkForVerificationErrors();
    }

    public void testTaskPane() throws Exception {
        setupProjectResourceUser1();
        setupT1InSimpleProject();
        loginAndUpdate("user1", "user1");
        verifyTrue(selenium.isTextPresent("Please click on a task name in your task list to view its details here."));
        selenium.click("taskNameLink");
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("Task Name:"));
        selenium.click("logoutButton");
        selenium.waitForPageToLoad("30000");
        checkForVerificationErrors();
    }

    public void testMakeActive() throws Exception {
        setupProjectResourceUser1();
        setupT1InSimpleProject();
        loginAndUpdate("user1", "user1");
        assertFalse(linkIsEnabled("saveButton"));
        assertFalse(linkIsEnabled("closeTaskButton"));
        assertFalse(linkIsEnabled("cancelButton"));
        assertFalse(linkIsEnabled("reopenTaskButton"));
        assertFalse(linkIsEnabled("setActiveButton"));
        assertFalse(linkIsEnabled("setInactiveButton"));
        selenium.click("selectboxCheckBox");
        assertTrue(linkIsEnabled("setActiveButton"));
        assertTrue(linkIsEnabled("closeTaskButton"));
        selenium.click("setActiveButton");
        Thread.sleep(200);
        selenium.click("closeTaskButton");
        Thread.sleep(5000);
        assertFalse(linkIsEnabled("saveButton"));
        assertFalse(linkIsEnabled("closeTaskButton"));
        assertFalse(linkIsEnabled("cancelButton"));
        assertFalse(linkIsEnabled("reopenTaskButton"));
        assertFalse(linkIsEnabled("setActiveButton"));
        assertFalse(linkIsEnabled("setInactiveButton"));
        assertEquals(getStatusImage(1), "regexp:active_task.jpg");
        selenium.click("selectboxCheckBox");
        selenium.click("setInactiveButton");
        Thread.sleep(200);
        selenium.click("closeTaskButton");
        Thread.sleep(5000);
        selenium.click("logoutButton");
        selenium.waitForPageToLoad("30000");
    }

    public void testTaskDescription() throws Exception {
        setupProjectResourceUser1();
        setupT1InSimpleProject();
        setupT2InSimpleProject();
        loginAndUpdate("user1", "user1");
        selenium.click("taskNameLink");
        selenium.waitForPageToLoad("30000");
        selenium.type("description", "Guy");
        selenium.keyPress("description", "33");
        assertTrue(linkIsEnabled("saveTaskButton"));
        selenium.click("saveTaskButton");
        selenium.waitForPageToLoad("30000");
        verifyEquals("Guy", selenium.getText("description"));
        selenium.click("taskNameLink_0");
        selenium.waitForPageToLoad("30000");
        selenium.click("taskNameLink");
        selenium.waitForPageToLoad("30000");
        verifyEquals("Guy", selenium.getText("description"));
        selenium.type("description", "");
        selenium.keyPress("description", "33");
        assertTrue(linkIsEnabled("saveTaskButton"));
        selenium.click("saveTaskButton");
        selenium.waitForPageToLoad("30000");
        verifyEquals("", selenium.getText("description"));
        selenium.click("logoutButton");
        selenium.waitForPageToLoad("30000");
        checkForVerificationErrors();
    }

    public void testCloseTask() throws Exception {
        setupProjectResourceUser1();
        setupT1InSimpleProject();
        loginAndUpdate("user1", "user1");
        verifyFalse(linkIsEnabled("saveButton"));
        verifyFalse(linkIsEnabled("closeTaskButton"));
        verifyFalse(linkIsEnabled("cancelButton"));
        verifyFalse(linkIsEnabled("reopenTaskButton"));
        verifyFalse(linkIsEnabled("setActiveButton"));
        verifyFalse(linkIsEnabled("setInactiveButton"));
        selenium.click("selectboxCheckBox");
        verifyTrue(linkIsEnabled("setActiveButton"));
        verifyTrue(linkIsEnabled("closeTaskButton"));
        selenium.click("closeTaskButton");
        selenium.waitForPageToLoad("30000");
        verifyFalse(linkIsEnabled("saveButton"));
        verifyFalse(linkIsEnabled("closeTaskButton"));
        verifyFalse(linkIsEnabled("cancelButton"));
        verifyFalse(linkIsEnabled("reopenTaskButton"));
        verifyFalse(linkIsEnabled("setActiveButton"));
        verifyFalse(linkIsEnabled("setInactiveButton"));
        selenium.select("ClosedStatusChooser", "label=All");
        selenium.click("filterPageButton");
        selenium.waitForPageToLoad("30000");
        verifyTrue(getStatusImage(1).contains("closed_status.jpg"));
        selenium.click("selectboxCheckBox");
        selenium.click("reopenTaskButton");
        selenium.waitForPageToLoad("30000");
        selenium.click("logoutButton");
        selenium.waitForPageToLoad("30000");
        checkForVerificationErrors();
    }

    public void testScheduler() {
        setupProjectResourceUser1();
        setupTaskInSimpleProject("Big Task", 2400);
        loginAndUpdate("user1", "user1");
        verifyEquals("8.0", selenium.getValue("fridayDateField"));
        selenium.click("logoutButton");
        selenium.waitForPageToLoad("30000");
        checkForVerificationErrors();
    }
}
