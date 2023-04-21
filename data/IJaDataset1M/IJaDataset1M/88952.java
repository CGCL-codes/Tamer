package org.one.stone.soup.wiki;

import java.io.IOException;
import java.util.List;
import org.one.stone.soup.authentication.client.AuthenticationException;
import org.one.stone.soup.entity.KeyValuePair;
import org.one.stone.soup.wiki.file.manager.FileManagerInterface;

public abstract class WikiListPage {

    private long timeStamp;

    private FileManagerInterface fileManager;

    private String pageName;

    private List<KeyValuePair> list;

    public WikiListPage(FileManagerInterface fileManager, String pageName) {
        this.fileManager = fileManager;
        this.pageName = pageName;
        loadPagesList();
    }

    public boolean pageHasChanged() throws Exception {
        try {
            if (fileManager.getPageTimeStamp(pageName, fileManager.getSystemLogin()) == timeStamp) {
                return false;
            } else {
                return true;
            }
        } catch (AuthenticationException ae) {
        }
        return true;
    }

    public abstract void loadPagesList();

    public List<KeyValuePair> getList() throws Exception {
        if (pageHasChanged()) {
            try {
                timeStamp = fileManager.getPageTimeStamp(pageName, fileManager.getSystemLogin());
            } catch (AuthenticationException ae) {
                throw new IOException(ae.getMessage());
            }
            loadPagesList();
        }
        return list;
    }

    protected void setList(List<KeyValuePair> list) {
        this.list = list;
    }

    protected FileManagerInterface getFileManager() {
        return fileManager;
    }

    public String getPageName() {
        return pageName;
    }

    protected void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
