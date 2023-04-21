package net.raymanoz.config;

import static org.junit.Assert.*;
import net.raymanoz.config.Configuration;
import net.raymanoz.io.File;
import net.raymanoz.io.MockFile;

public class MockConfiguration implements Configuration {

    private boolean getExecuteDirectoryWasCalled;

    private File executeDirectory;

    private String confirmationMessage;

    private boolean getConfirmationWasCalled;

    public void assertGetExecuteDirectoryWasCalled() {
        assertTrue("getExecuteDirectoryWasCalled() was not called", getExecuteDirectoryWasCalled);
    }

    public File getExecuteDirectory() {
        this.getExecuteDirectoryWasCalled = true;
        return executeDirectory;
    }

    public String getExecutionCommand() {
        return "";
    }

    public int getNumberOfDigits() {
        throw new RuntimeException("not yet implemented");
    }

    public File getScriptDirectory() {
        return new MockFile("dir");
    }

    public File getTemplateFile() {
        throw new RuntimeException("not yet implemented");
    }

    public void setExecuteDirectory(File executeDirectory) {
        this.executeDirectory = executeDirectory;
    }

    public Confirmation getConfirmation() {
        getConfirmationWasCalled = true;
        return new MockConfirmation(confirmationMessage);
    }

    public void setConfirmationMessage(String expectedMessage) {
        confirmationMessage = expectedMessage;
    }

    public void assertGetConfirmationWasCalled() {
        assertTrue("getConfirmation() was not called", this.getConfirmationWasCalled);
    }
}
