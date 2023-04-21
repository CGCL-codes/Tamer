package com.jsystem.j2sikuli;

import java.io.File;
import jsystem.framework.TestProperties;
import jsystem.framework.report.ReporterHelper;
import junit.framework.Assert;
import junit.framework.SystemTestCase4;
import org.junit.Before;
import org.junit.Test;

public class SikuliOperations extends SystemTestCase4 {

    private SikuliClient client;

    private int x, y;

    private File imageFile;

    private String text;

    @Before
    public void before() throws Exception {
        client = (SikuliClient) system.getSystemObject("sikuli");
    }

    @Test
    @TestProperties(paramsInclude = "imageFile")
    public void testSikuliClick() throws SikuliAgentException {
        client.addImage(imageFile);
        client.click(imageFile.getName());
    }

    @Test
    @TestProperties(paramsInclude = { "x", "y" })
    public void testRobotClick() throws SikuliAgentException {
        client.click(x, y);
    }

    @Test
    @TestProperties(paramsInclude = { "x", "y" })
    public void testRobotDoubleClick() throws SikuliAgentException {
        client.doubleClick(x, y);
    }

    @Test
    @TestProperties(paramsInclude = "imageFile")
    public void testExist() throws SikuliAgentException {
        boolean result = client.exist("start.png");
        report.report("File existence", result);
    }

    @Test
    @TestProperties(paramsInclude = "imageFile")
    public void testAddImage() throws SikuliAgentException {
        client.addImage(imageFile);
    }

    @Test
    @TestProperties(paramsInclude = "")
    public void testCaputureScreenshot() throws Exception {
        File screenshot = client.captureScreenshotFile();
        ReporterHelper.copyFileToReporterAndAddLink(report, screenshot, "Screenshot");
    }

    @Test
    @TestProperties(paramsInclude = { "imageFile", "text" })
    public void testSikuliType() throws SikuliAgentException {
        client.addImage(imageFile);
        boolean exists = client.type(imageFile.getName(), text);
        Assert.assertTrue(exists);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
