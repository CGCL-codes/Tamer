package com.jsystem.selenium;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import com.thoughtworks.selenium.Selenium;

public class SeleniumWrapper extends WebDriverBackedSelenium implements Selenium {

    private boolean screenShot = false;

    private String screenShotPath = null;

    private String screenShotFolderName = null;

    public SeleniumWrapper(WebDriver driver, String baseUrl, Selenium selenium) {
        super(driver, baseUrl);
        this.driver = driver;
        this.selenium = selenium;
    }

    private Selenium selenium;

    private WebDriver driver;

    @Override
    public void addCustomRequestHeader(String arg0, String arg1) {
        selenium.addCustomRequestHeader(arg0, arg1);
    }

    @Override
    public void addLocationStrategy(String arg0, String arg1) {
        selenium.addLocationStrategy(arg0, arg1);
    }

    @Override
    public void addScript(String arg0, String arg1) {
        selenium.addScript(arg0, arg1);
    }

    @Override
    public void addSelection(String arg0, String arg1) {
        selenium.addSelection(arg0, arg1);
    }

    @Override
    public void allowNativeXpath(String arg0) {
        selenium.allowNativeXpath(arg0);
    }

    @Override
    public void altKeyDown() {
        selenium.altKeyDown();
    }

    @Override
    public void altKeyUp() {
        selenium.altKeyUp();
    }

    @Override
    public void answerOnNextPrompt(String arg0) {
        selenium.answerOnNextPrompt(arg0);
    }

    @Override
    public void assignId(String arg0, String arg1) {
        selenium.assignId(arg0, arg1);
    }

    @Override
    public void attachFile(String arg0, String arg1) {
        selenium.attachFile(arg0, arg1);
    }

    /**
	 * Saves the entire contents of the current window canvas to a PNG file.
	 * Contrast this with the captureScreenshot command, which captures the
	 * contents of the OS viewport (i.e. whatever is currently being displayed
	 * on the monitor), and is implemented in the RC only. Currently this only
	 * works in Firefox when running in chrome mode, and in IE non-HTA using the
	 * EXPERIMENTAL "Snapsie" utility. The Firefox implementation is mostly
	 * borrowed from the Screengrab! Firefox extension. Please see
	 * http://www.screengrab.org and http://snapsie.sourceforge.net/ for
	 * details.
	 * 
	 * @param filename
	 *            the path to the file to persist the screenshot as. No filename
	 *            extension will be appended by default. Directories will not be
	 *            created if they do not exist, and an exception will be thrown,
	 *            possibly by native code.
	 * @param kwargs
	 *            a kwargs string that modifies the way the screenshot is
	 *            captured. Example: "background=#CCFFDD" . Currently valid
	 *            options:
	 *            <dl>
	 *            <dt>background</dt>
	 *            <dd>the background CSS for the HTML document. This may be
	 *            useful to set for capturing screenshots of less-than-ideal
	 *            layouts, for example where absolute positioning causes the
	 *            calculation of the canvas dimension to fail and a black
	 *            background is exposed (possibly obscuring black text).</dd>
	 *            </dl>
	 */
    @Override
    public void captureEntirePageScreenshot(String arg0, String arg1) {
        takeWebDriverScreenshot(arg0);
    }

    /**
	 * Downloads a screenshot of the browser current window canvas to a based 64
	 * encoded PNG file. The <em>entire</em> windows canvas is captured,
	 * including parts rendered outside of the current view port.
	 * 
	 * Currently this only works in Mozilla and when running in chrome mode.
	 * 
	 * @param kwargs
	 *            A kwargs string that modifies the way the screenshot is
	 *            captured. Example: "background=#CCFFDD". This may be useful to
	 *            set for capturing screenshots of less-than-ideal layouts, for
	 *            example where absolute positioning causes the calculation of
	 *            the canvas dimension to fail and a black background is exposed
	 *            (possibly obscuring black text).
	 * @return The base 64 encoded string of the page screenshot (PNG file)
	 */
    @Override
    public String captureEntirePageScreenshotToString(String arg0) {
        return takeWebDriverScreenshotToString();
    }

    @Override
    public String captureNetworkTraffic(String arg0) {
        return selenium.captureNetworkTraffic(arg0);
    }

    /**
	 * Captures a PNG screenshot to the specified file.
	 * 
	 * @param filename
	 *            the absolute path to the file to be written, e.g.
	 *            "c:\blah\screenshot.png"
	 */
    @Override
    public void captureScreenshot(String arg0) {
        takeWebDriverScreenshot(arg0);
    }

    /**
	 * Capture a PNG screenshot. It then returns the file as a base 64 encoded
	 * string.
	 * 
	 * @return The base 64 encoded string of the screen shot (PNG file)
	 */
    @Override
    public String captureScreenshotToString() {
        return takeWebDriverScreenshotToString();
    }

    @Override
    public void check(String arg0) {
        selenium.check(arg0);
    }

    @Override
    public void chooseCancelOnNextConfirmation() {
        selenium.chooseCancelOnNextConfirmation();
    }

    @Override
    public void chooseOkOnNextConfirmation() {
        selenium.chooseOkOnNextConfirmation();
    }

    @Override
    public void click(String arg0) {
        selenium.click(arg0);
    }

    @Override
    public void clickAt(String arg0, String arg1) {
        selenium.clickAt(arg0, arg1);
    }

    @Override
    public void close() {
        selenium.close();
    }

    @Override
    public void contextMenu(String arg0) {
        selenium.contextMenu(arg0);
    }

    @Override
    public void contextMenuAt(String arg0, String arg1) {
        selenium.contextMenuAt(arg0, arg1);
    }

    @Override
    public void controlKeyDown() {
        selenium.controlKeyDown();
    }

    @Override
    public void controlKeyUp() {
        selenium.controlKeyUp();
    }

    @Override
    public void createCookie(String arg0, String arg1) {
        selenium.createCookie(arg0, arg1);
    }

    @Override
    public void deleteAllVisibleCookies() {
        selenium.deleteAllVisibleCookies();
    }

    @Override
    public void deleteCookie(String arg0, String arg1) {
        selenium.deleteCookie(arg0, arg1);
    }

    @Override
    public void deselectPopUp() {
        selenium.deselectPopUp();
    }

    @Override
    public void doubleClick(String arg0) {
        selenium.doubleClick(arg0);
    }

    @Override
    public void doubleClickAt(String arg0, String arg1) {
        selenium.doubleClickAt(arg0, arg1);
    }

    @Override
    public void dragAndDrop(String arg0, String arg1) {
        selenium.dragAndDrop(arg0, arg1);
    }

    @Override
    public void dragAndDropToObject(String arg0, String arg1) {
        selenium.dragAndDropToObject(arg0, arg1);
    }

    @Override
    public void dragdrop(String arg0, String arg1) {
        selenium.dragAndDrop(arg0, arg1);
    }

    @Override
    public void fireEvent(String arg0, String arg1) {
        selenium.fireEvent(arg0, arg1);
    }

    @Override
    public void focus(String arg0) {
        selenium.focus(arg0);
    }

    @Override
    public String getAlert() {
        return selenium.getAlert();
    }

    @Override
    public String[] getAllButtons() {
        return selenium.getAllButtons();
    }

    @Override
    public String[] getAllFields() {
        return selenium.getAllFields();
    }

    @Override
    public String[] getAllLinks() {
        return selenium.getAllLinks();
    }

    @Override
    public String[] getAllWindowIds() {
        return selenium.getAllWindowIds();
    }

    @Override
    public String[] getAllWindowNames() {
        return selenium.getAllWindowNames();
    }

    @Override
    public String[] getAllWindowTitles() {
        return selenium.getAllWindowTitles();
    }

    @Override
    public String getAttribute(String arg0) {
        return selenium.getAttribute(arg0);
    }

    @Override
    public String[] getAttributeFromAllWindows(String arg0) {
        return selenium.getAttributeFromAllWindows(arg0);
    }

    @Override
    public String getBodyText() {
        return selenium.getBodyText();
    }

    @Override
    public String getConfirmation() {
        return selenium.getConfirmation();
    }

    @Override
    public String getCookie() {
        return selenium.getCookie();
    }

    @Override
    public String getCookieByName(String arg0) {
        return selenium.getCookieByName(arg0);
    }

    @Override
    public Number getCssCount(String arg0) {
        return selenium.getCssCount(arg0);
    }

    @Override
    public Number getCursorPosition(String arg0) {
        return selenium.getCursorPosition(arg0);
    }

    @Override
    public Number getElementHeight(String arg0) {
        return selenium.getElementHeight(arg0);
    }

    @Override
    public Number getElementIndex(String arg0) {
        return selenium.getElementIndex(arg0);
    }

    @Override
    public Number getElementPositionLeft(String arg0) {
        return selenium.getElementPositionLeft(arg0);
    }

    @Override
    public Number getElementPositionTop(String arg0) {
        return selenium.getElementPositionTop(arg0);
    }

    @Override
    public Number getElementWidth(String arg0) {
        return selenium.getElementWidth(arg0);
    }

    @Override
    public String getEval(String arg0) {
        return selenium.getEval(arg0);
    }

    @Override
    public String getExpression(String arg0) {
        return selenium.getExpression(arg0);
    }

    @Override
    public String getHtmlSource() {
        return selenium.getHtmlSource();
    }

    @Override
    public String getLocation() {
        return selenium.getLocation();
    }

    @Override
    public String getLog() {
        return selenium.getLog();
    }

    @Override
    public Number getMouseSpeed() {
        return selenium.getMouseSpeed();
    }

    @Override
    public String getPrompt() {
        return selenium.getPrompt();
    }

    @Override
    public String[] getSelectOptions(String arg0) {
        return selenium.getSelectOptions(arg0);
    }

    @Override
    public String getSelectedId(String arg0) {
        return selenium.getSelectedId(arg0);
    }

    @Override
    public String[] getSelectedIds(String arg0) {
        return selenium.getSelectedIds(arg0);
    }

    @Override
    public String getSelectedIndex(String arg0) {
        return selenium.getSelectedIndex(arg0);
    }

    @Override
    public String[] getSelectedIndexes(String arg0) {
        return selenium.getSelectedIndexes(arg0);
    }

    @Override
    public String getSelectedLabel(String arg0) {
        return selenium.getSelectedLabel(arg0);
    }

    @Override
    public String[] getSelectedLabels(String arg0) {
        return selenium.getSelectedLabels(arg0);
    }

    @Override
    public String getSelectedValue(String arg0) {
        return selenium.getSelectedValue(arg0);
    }

    @Override
    public String[] getSelectedValues(String arg0) {
        return selenium.getSelectedValues(arg0);
    }

    @Override
    public String getSpeed() {
        return selenium.getSpeed();
    }

    @Override
    public String getTable(String arg0) {
        return selenium.getTable(arg0);
    }

    @Override
    public String getText(String arg0) {
        return selenium.getText(arg0);
    }

    @Override
    public String getTitle() {
        return selenium.getTitle();
    }

    @Override
    public String getValue(String arg0) {
        return selenium.getValue(arg0);
    }

    @Override
    public boolean getWhetherThisFrameMatchFrameExpression(String arg0, String arg1) {
        return selenium.getWhetherThisFrameMatchFrameExpression(arg0, arg1);
    }

    @Override
    public boolean getWhetherThisWindowMatchWindowExpression(String arg0, String arg1) {
        return selenium.getWhetherThisWindowMatchWindowExpression(arg0, arg1);
    }

    @Override
    public Number getXpathCount(String arg0) {
        return selenium.getXpathCount(arg0);
    }

    @Override
    public void goBack() {
        selenium.goBack();
    }

    @Override
    public void highlight(String arg0) {
        selenium.highlight(arg0);
    }

    @Override
    public void ignoreAttributesWithoutValue(String arg0) {
        selenium.ignoreAttributesWithoutValue(arg0);
    }

    @Override
    public boolean isAlertPresent() {
        return selenium.isAlertPresent();
    }

    @Override
    public boolean isChecked(String arg0) {
        return selenium.isChecked(arg0);
    }

    @Override
    public boolean isConfirmationPresent() {
        return selenium.isConfirmationPresent();
    }

    @Override
    public boolean isCookiePresent(String arg0) {
        return selenium.isCookiePresent(arg0);
    }

    @Override
    public boolean isEditable(String arg0) {
        return selenium.isEditable(arg0);
    }

    @Override
    public boolean isElementPresent(String arg0) {
        return selenium.isElementPresent(arg0);
    }

    @Override
    public boolean isOrdered(String arg0, String arg1) {
        return selenium.isOrdered(arg0, arg1);
    }

    @Override
    public boolean isPromptPresent() {
        return selenium.isPromptPresent();
    }

    @Override
    public boolean isSomethingSelected(String arg0) {
        return selenium.isSomethingSelected(arg0);
    }

    @Override
    public boolean isTextPresent(String arg0) {
        return selenium.isTextPresent(arg0);
    }

    @Override
    public boolean isVisible(String arg0) {
        return selenium.isVisible(arg0);
    }

    @Override
    public void keyDown(String arg0, String arg1) {
        selenium.keyDown(arg0, arg1);
    }

    @Override
    public void keyDownNative(String arg0) {
        selenium.keyDownNative(arg0);
    }

    @Override
    public void keyPress(String arg0, String arg1) {
        selenium.keyPress(arg0, arg1);
    }

    @Override
    public void keyPressNative(String arg0) {
        selenium.keyPressNative(arg0);
    }

    @Override
    public void keyUp(String arg0, String arg1) {
        selenium.keyUp(arg0, arg1);
    }

    @Override
    public void keyUpNative(String arg0) {
        selenium.keyUpNative(arg0);
    }

    @Override
    public void metaKeyDown() {
        selenium.metaKeyDown();
    }

    @Override
    public void metaKeyUp() {
        selenium.metaKeyUp();
    }

    @Override
    public void mouseDown(String arg0) {
        selenium.mouseDown(arg0);
    }

    @Override
    public void mouseDownAt(String arg0, String arg1) {
        selenium.mouseDownAt(arg0, arg1);
    }

    @Override
    public void mouseDownRight(String arg0) {
        selenium.mouseDownRight(arg0);
    }

    @Override
    public void mouseDownRightAt(String arg0, String arg1) {
        selenium.mouseDownRightAt(arg0, arg1);
    }

    @Override
    public void mouseMove(String arg0) {
        selenium.mouseMove(arg0);
    }

    @Override
    public void mouseMoveAt(String arg0, String arg1) {
        selenium.mouseMoveAt(arg0, arg1);
    }

    @Override
    public void mouseOut(String arg0) {
        selenium.mouseOut(arg0);
    }

    @Override
    public void mouseOver(String arg0) {
        selenium.mouseOver(arg0);
    }

    @Override
    public void mouseUp(String arg0) {
        selenium.mouseUp(arg0);
    }

    @Override
    public void mouseUpAt(String arg0, String arg1) {
        selenium.mouseUpAt(arg0, arg1);
    }

    @Override
    public void mouseUpRight(String arg0) {
        selenium.mouseUpRight(arg0);
    }

    @Override
    public void mouseUpRightAt(String arg0, String arg1) {
        selenium.mouseUpRightAt(arg0, arg1);
    }

    @Override
    public void open(String arg0) {
        selenium.open(arg0);
    }

    @Override
    public void open(String arg0, String arg1) {
        selenium.open(arg0, arg1);
    }

    @Override
    public void openWindow(String arg0, String arg1) {
        selenium.openWindow(arg0, arg1);
    }

    @Override
    public void refresh() {
        selenium.refresh();
    }

    @Override
    public void removeAllSelections(String arg0) {
        selenium.removeAllSelections(arg0);
    }

    @Override
    public void removeScript(String arg0) {
        selenium.removeScript(arg0);
    }

    @Override
    public void removeSelection(String arg0, String arg1) {
        selenium.removeSelection(arg0, arg1);
    }

    @Override
    public String retrieveLastRemoteControlLogs() {
        return selenium.retrieveLastRemoteControlLogs();
    }

    @Override
    public void rollup(String arg0, String arg1) {
        selenium.rollup(arg0, arg1);
    }

    @Override
    public void runScript(String arg0) {
        selenium.runScript(arg0);
    }

    @Override
    public void select(String arg0, String arg1) {
        selenium.select(arg0, arg1);
    }

    @Override
    public void selectFrame(String arg0) {
        selenium.selectFrame(arg0);
    }

    @Override
    public void selectPopUp(String arg0) {
        selenium.selectPopUp(arg0);
    }

    @Override
    public void selectWindow(String arg0) {
        selenium.selectWindow(arg0);
    }

    @Override
    public void setBrowserLogLevel(String arg0) {
        selenium.setBrowserLogLevel(arg0);
    }

    @Override
    public void setContext(String arg0) {
        selenium.setContext(arg0);
    }

    @Override
    public void setCursorPosition(String arg0, String arg1) {
        selenium.setCursorPosition(arg0, arg1);
    }

    @Override
    public void setExtensionJs(String arg0) {
        selenium.setExtensionJs(arg0);
    }

    @Override
    public void setMouseSpeed(String arg0) {
        selenium.setMouseSpeed(arg0);
    }

    @Override
    public void setSpeed(String arg0) {
        selenium.setSpeed(arg0);
    }

    @Override
    public void setTimeout(String arg0) {
        selenium.setTimeout(arg0);
    }

    @Override
    public void shiftKeyDown() {
        selenium.shiftKeyDown();
    }

    @Override
    public void shiftKeyUp() {
        selenium.shiftKeyUp();
    }

    @Override
    public void showContextualBanner() {
        selenium.showContextualBanner();
    }

    @Override
    public void showContextualBanner(String arg0, String arg1) {
        selenium.showContextualBanner(arg0, arg1);
    }

    @Override
    public void shutDownSeleniumServer() {
        selenium.shutDownSeleniumServer();
    }

    @Override
    public void start() {
        selenium.start();
    }

    @Override
    public void start(String arg0) {
        selenium.start();
    }

    @Override
    public void start(Object arg0) {
        selenium.start(arg0);
    }

    @Override
    public void stop() {
        selenium.stop();
    }

    @Override
    public void submit(String arg0) {
        selenium.submit(arg0);
    }

    @Override
    public void type(String arg0, String arg1) {
        selenium.type(arg0, arg1);
    }

    @Override
    public void typeKeys(String arg0, String arg1) {
        selenium.typeKeys(arg0, arg1);
    }

    @Override
    public void uncheck(String arg0) {
        selenium.uncheck(arg0);
    }

    @Override
    public void useXpathLibrary(String arg0) {
        selenium.useXpathLibrary(arg0);
    }

    @Override
    public void waitForCondition(String arg0, String arg1) {
        selenium.waitForCondition(arg0, arg1);
    }

    @Override
    public void waitForFrameToLoad(String arg0, String arg1) {
        selenium.waitForFrameToLoad(arg0, arg1);
    }

    @Override
    public void waitForPageToLoad(String arg0) {
        selenium.waitForPageToLoad(arg0);
    }

    @Override
    public void waitForPopUp(String arg0, String arg1) {
        selenium.waitForPopUp(arg0, arg1);
    }

    @Override
    public void windowFocus() {
        selenium.windowFocus();
    }

    @Override
    public void windowMaximize() {
        selenium.windowMaximize();
    }

    public String getScreenShotFolderName() {
        return screenShotFolderName;
    }

    public void setScreenShotFolderName(String screenShotFolderName) {
        this.screenShotFolderName = screenShotFolderName;
    }

    public boolean isScreenShot() {
        return screenShot;
    }

    public void setScreenShot(boolean screenShot) {
        this.screenShot = screenShot;
    }

    public String getScreenShotPath() {
        return screenShotPath;
    }

    public void setScreenShotPath(String screenShotPath) {
        this.screenShotPath = screenShotPath;
    }

    private void takeWebDriverScreenshot(String saveTo) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(saveTo));
            scrFile.delete();
        } catch (IOException e) {
        }
    }

    private String takeWebDriverScreenshotToString() {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return scrFile.toString();
    }
}
