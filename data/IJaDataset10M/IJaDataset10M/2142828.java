package com.ideo.sweetdevria.selenium.scenarios;

import com.ideo.sweetdevria.selenium.common.CommonSeleniumTestCase;

/**
 * @author Yannis Julienne
 *
 */
public class TreeViewOverviewTestScenario extends CommonSeleniumTestCase {

    public static String APPLI_CONTEXT_ROOT = "sweetdev-ria-gettingStarted/";

    public static String page = "layout.jsp?url=%2Ftree%2Ftree_overview.jsp";

    private String url;

    public TreeViewOverviewTestScenario(String host, String browserName, int port, String url) {
        super(host, browserName, port, url, page);
        this.url = url + page;
    }

    /**
     * test the location
     */
    public void testLocation() {
        assertEquals(url, browser.getLocation());
    }

    /**
     * test if title is present
     */
    public void testTitle() {
        verifyEquals("SweetDEV RIA show case, play with Ajax tags SweetDEV RIA", browser.getTitle());
        assertEquals("The title should be : Insert the tree component", "Insert the tree component", browser.getText("//div[@class='title']"));
    }

    /**
     * visibility testing
     */
    public void testVisibility() {
        assertTrue("The Tree is not visible", isVisible("treeover_box"));
        assertTrue("The root expand link is not visible", isVisible("treeover_root_lastIndent"));
        assertTrue("The root icon link is not visible", isVisible("treeover_root_icon"));
        assertTrue("The root label link is not visible", isVisible("treeover_root_label"));
    }

    /**
     * testing node moving
     */
    public void testMoveNode() {
        browser.click("treeover_root_lastIndent");
        browser.click("treeover_folder0_icon");
        browser.click("treeover_folder1_label");
        verifyTrue(browser.isVisible("treeover_file1_div"));
        verifyEquals("treeover_file1_li", browser.getAttribute("//ul[@id='treeover_folder1_children']//li[1]@id"));
        browser.dragAndDropToObject("treeover_file1_icon", "treeover_folder2_div");
        pause(500);
        fail("Selenium's dragAndDrop does'nt work properly");
        assertTrue("The Folder2 expand link does not have a correct class attribute", browser.getAttribute("treeover_folder2_lastIndent@class").contains("ideo-tre-minusLast"));
        assertEquals("The Folder2 should have 2 children", 2, browser.getXpathCount("//ul[@id='treeover_folder2_children']/li"));
        assertTrue("File1 should be a child of Folder2", "treeover_file1_li".equals(browser.getAttribute("//ul[@id='treeover_folder2_children']/li[1]@id")) || "treeover_file1_li".equals(browser.getAttribute("//ul[@id='treeover_folder2_children']/li[2]@id")));
        browser.dragAndDrop("treeover_file3_label", "0,-40");
        pause(500);
        assertEquals("File3 should be the second child of Folder1", "treeover_file3_li", browser.getAttribute("//ul[@id='treeover_folder1_children']//li[2]@id"));
        browser.click("treeover_folder2_lastIndent");
        browser.click("treeover_folder1_lastIndent");
        browser.click("treeover_folder0_lastIndent");
        browser.click("treeover_root_lastIndent");
    }

    /**
     * testing keyboard move
     */
    public void testKeyboardMove() {
        browser.click("treeover_root_lastIndent");
        browser.click("treeover_folder0_icon");
        browser.click("treeover_folder1_label");
        browser.click("treeover_folder2_label");
        browser.click("treeover_file2_div");
        verifyTrue(browser.getAttribute("treeover_file2_div@class").contains("ideo-tre-selected"));
        verifyTrue(browser.getAttribute("treeover_folder2_lastIndent@class").contains("ideo-tre-minusLast"));
        browser.keyDown("treeover_file2_label", "\\40");
        browser.keyUp("treeover_file2_label", "\\40");
        pause(500);
        assertTrue("Folder2 should be selected", browser.getAttribute("treeover_file3_div@class").contains("ideo-tre-selected"));
        browser.keyDown("treeover_file3_label", "\\40");
        browser.keyUp("treeover_file3_label", "\\40");
        pause(500);
        assertTrue("File3 should be selected", browser.getAttribute("treeover_folder2_div@class").contains("ideo-tre-selected"));
        browser.keyDown("treeover_file2_label", "\\37");
        browser.keyUp("treeover_file2_label", "\\37");
        pause(500);
        assertTrue("The Folder2 expand link does not have a correct class attribute", browser.getAttribute("treeover_folder2_lastIndent@class").contains("ideo-tre-plusLast"));
        browser.keyDown("treeover_file2_label", "\\39");
        browser.keyUp("treeover_file2_label", "\\39");
        pause(500);
        assertTrue("The Folder2 expand link does not have a correct class attribute", browser.getAttribute("treeover_folder2_lastIndent@class").contains("ideo-tre-minusLast"));
        browser.keyDown("treeover_file2_label", "\\38");
        browser.keyUp("treeover_file2_label", "\\38");
        pause(500);
        assertTrue("The file3 should be selected", browser.getAttribute("treeover_file3_div@class").contains("ideo-tre-selected"));
        browser.click("treeover_folder2_lastIndent");
        browser.click("treeover_folder1_lastIndent");
        browser.click("treeover_folder0_lastIndent");
        browser.click("treeover_root_lastIndent");
    }

    /**
     * testing click on root collapse link
     */
    public void testClickCollapseRoot() {
        browser.click("treeover_root_lastIndent");
        browser.click("treeover_folder0_icon");
        browser.click("treeover_folder1_label");
        browser.click("treeover_folder2_label");
        verifyTrue(isVisible("treeover_root_lastIndent"));
        verifyTrue(isVisible("treeover_folder0_lastIndent"));
        verifyTrue(isVisible("treeover_folder1_lastIndent"));
        verifyTrue(isVisible("treeover_folder2_lastIndent"));
        verifyTrue(browser.getAttribute("treeover_root_lastIndent@class").contains("ideo-tre-minusLast"));
        verifyTrue(browser.getAttribute("treeover_folder0_lastIndent@class").contains("ideo-tre-minusLast"));
        verifyTrue(browser.getAttribute("treeover_folder1_lastIndent@class").contains("ideo-tre-minus"));
        verifyTrue(browser.getAttribute("treeover_folder2_lastIndent@class").contains("ideo-tre-minusLast"));
        browser.click("treeover_root_lastIndent");
        pause(500);
        assertTrue("The root expand link has not the correct class attribute", browser.getAttribute("treeover_root_lastIndent@class").contains("ideo-tre-plusLast"));
        assertFalse("The Folder0 line should not be visible", browser.isVisible("treeover_folder0_div"));
        assertFalse("The Folder1 line should not be visible", browser.isVisible("treeover_folder1_div"));
        assertFalse("The Folder2 line should not be visible", browser.isVisible("treeover_folder2_div"));
        browser.click("treeover_root_lastIndent");
        browser.click("treeover_folder2_lastIndent");
        browser.click("treeover_folder1_lastIndent");
        browser.click("treeover_folder0_lastIndent");
        browser.click("treeover_root_lastIndent");
    }
}
