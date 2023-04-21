package com.technoetic.xplanner.acceptance.web;

import junitx.framework.Assert;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;
import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebImage;
import com.technoetic.xplanner.views.IconConstants;

public class IterationAllTasksPageTestScript extends AbstractPageTestScript {

    public final String firstStoryName = "First Story";

    public final String secondStory = "Second Story";

    public final String ttTaskInFirstStory = "TT Task";

    public final String ssTaskInFirstStory = "SS Task";

    public final String startedTaskInSecondStory = "Started Task";

    public final String nonStartedTaskInSecondStory = "Non Started Task";

    public final String completedTaskInSecondStory = "Completed Task";

    public final String allTasksTableId = "objecttable";

    public final String statusColumnKey = "tasks.tableheading.status";

    public final String taskNameColumnKey = "person.tableheading.task";

    public String startedTaskId;

    public IterationAllTasksPageTestScript(String test) {
        super(test);
    }

    public void setUp() throws Exception {
        super.setUp();
        setUpTestProject();
        setUpTestPerson();
        setUpTestRole("editor");
        tester.login();
        tester.clickLinkWithText(project.getName());
        setUpTestIteration();
    }

    public void tearDown() throws Exception {
        deleteLocalObjects();
        super.tearDown();
    }

    private void deleteLocalObjects() {
        tester.deleteObjects(Task.class, "name", ssTaskInFirstStory);
        tester.deleteObjects(Task.class, "name", ttTaskInFirstStory);
        tester.deleteObjects(Task.class, "name", completedTaskInSecondStory);
        tester.deleteObjects(Task.class, "name", nonStartedTaskInSecondStory);
        tester.deleteObjects(Task.class, "name", startedTaskInSecondStory);
        tester.deleteObjects(UserStory.class, "name", firstStoryName);
        tester.deleteObjects(UserStory.class, "name", secondStory);
    }

    private void setUpTestIteration() throws Exception {
        tester.gotoProjectsPage();
        tester.clickLinkWithText(testProjectName);
        iterationTester.addIteration(testIterationName, tester.dateStringForNDaysAway(15), tester.dateStringForNDaysAway(28), testIterationDescription);
        tester.clickLinkWithText(testIterationName);
        tester.addUserStory(firstStoryName, "First story with higher priority", "23.5", "1");
        tester.assertTextPresent(firstStoryName);
        tester.clickLinkWithText(firstStoryName);
        tester.assertOnStoryPage();
        tester.clickLinkWithKey("action.edit.story");
        tester.setWorkingForm("storyEditor");
        tester.setFormElement("priority", "3");
        tester.submit();
        addTaskToCurrentStory(ttTaskInFirstStory, "First task in first story", "15.5");
        addTaskToCurrentStory(ssTaskInFirstStory, "Second task in first story", "2.3");
        tester.clickLinkWithText(testIterationName);
    }

    public void testAllTasksPage() throws Exception {
        tester.addUserStory(secondStory, testStoryDescription, "10.0", "1");
        tester.assertTextPresent(secondStory);
        tester.clickLinkWithText(secondStory);
        tester.assertOnStoryPage();
        startedTaskId = addTaskToCurrentStory(startedTaskInSecondStory, "A started task", "3.5");
        tester.clickLinkWithText(startedTaskInSecondStory);
        tester.clickLinkWithKey("action.edittime.task");
        tester.setTimeEntry(0, 1, 2, developer.getName());
        iterationTester.assertOnStartIterationPromptPageAndStart();
        tester.assertOnTaskPage();
        tester.clickLinkWithText(secondStory);
        addTaskToCurrentStory(nonStartedTaskInSecondStory, "A non started task", "2.0");
        addTaskToCurrentStory(completedTaskInSecondStory, "completed task", "4.0");
        tester.clickLinkWithText(completedTaskInSecondStory);
        tester.completeCurrentTask();
        tester.clickLinkWithText(testIterationName);
        tester.clickLinkWithKey("iteration.link.all_tasks");
        tester.assertKeyNotPresent("security.notauthorized");
        verifyAllTasksPage();
        deleteLocalTimeEntry(startedTaskId);
    }

    public void testEditTask() {
        tester.clickLinkWithKey("iteration.link.all_tasks");
        tester.clickEditLinkInRowWithText(ttTaskInFirstStory);
        tester.selectOption("acceptorId", tester.getXPlannerLoginId());
        tester.submit();
        tester.assertTextPresent(tester.getXPlannerLoginId());
    }

    private void verifyAllTasksPage() {
        tester.assertKeyPresent("iteration.alltasks.prefix");
        assertCellEquals(startedTaskInSecondStory, 1, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_STARTED_ICON, 1, statusColumnKey);
        assertCellEquals(ssTaskInFirstStory, 2, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_OPEN_ICON, 2, statusColumnKey);
        assertCellEquals(ttTaskInFirstStory, 3, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_OPEN_ICON, 3, statusColumnKey);
        assertCellEquals(nonStartedTaskInSecondStory, 4, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_OPEN_ICON, 4, statusColumnKey);
        assertCellEquals(completedTaskInSecondStory, 5, taskNameColumnKey);
        assertCellContains(IconConstants.STATUS_COMPLETED_ICON, 5, statusColumnKey);
    }

    private void assertCellEquals(String expectedCellText, int rowIndex, String columnKey) {
        tester.assertCellTextForRowIndexAndColumnKeyEquals(XPlannerWebTester.MAIN_TABLE_ID, rowIndex, columnKey, expectedCellText);
    }

    private void assertCellContains(String image, int rowIndex, String columnKey) {
        TableCell cell = tester.getCell(XPlannerWebTester.MAIN_TABLE_ID, columnKey, rowIndex);
        WebImage[] images = cell.getImages();
        for (int i = 0; i < images.length; i++) {
            WebImage webImage = images[i];
            String src = webImage.getSource();
            if (src.endsWith(image)) return;
        }
        Assert.fail("Could not find image " + image + " in " + dumpImageArray(images));
    }

    private String dumpImageArray(WebImage[] images) {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        for (int i = 0; i < images.length; i++) {
            if (i > 0) buf.append(", ");
            buf.append(images[i].getSource());
        }
        buf.append("]");
        String imagesArrayString = buf.toString();
        return imagesArrayString;
    }

    private String addTaskToCurrentStory(String taskToAdd, String description, String estimatedHours) {
        String taskId = tester.addTask(taskToAdd, developerName, description, estimatedHours);
        tester.assertOnStoryPage();
        tester.assertTextPresent(taskToAdd);
        return taskId;
    }
}
