package com.luxoft.fitpro.htmleditor.plugin.htmleditor.ui;

import com.luxoft.fitpro.htmleditor.io.IFileSystem;
import com.luxoft.fitpro.htmleditor.io.MockFileSystem;

public class MockFileSelector implements IFileSelector {

    private final MockFileSystem mockFileSystem;

    private String fixtureFile;

    public MockFileSelector(MockFileSystem mockFileSystem) {
        this.mockFileSystem = mockFileSystem;
    }

    public IFileSystem getFileSystemAdapter() {
        return mockFileSystem;
    }

    public void setFixtureFile(String fixtureFile) {
        this.fixtureFile = fixtureFile;
    }

    public String selectFixtureFile(String location) {
        return fixtureFile;
    }

    public String getBaseDirectory() {
        return mockFileSystem.getBaseDirectory(fixtureFile);
    }
}
