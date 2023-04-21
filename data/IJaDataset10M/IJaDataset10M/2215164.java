package org.clouddreamwork.fetcher;

import java.net.*;
import java.util.*;
import javax.jdo.annotations.*;
import org.clouddreamwork.minisql.*;
import com.google.appengine.api.datastore.*;

@PersistenceCapable
public class PageInfo {

    @PrimaryKey
    @Persistent
    private String keyName;

    @Persistent
    private String url;

    @Persistent
    private String method;

    @Persistent
    private String parameters;

    @Persistent
    private String cookie;

    @Persistent
    private Text content;

    @Persistent
    private boolean isFinished;

    @Persistent
    private Date addTime;

    @Persistent
    private Date finishTime;

    public PageInfo(String name, String owner) throws MalformedURLException {
        setKeyName(name, owner);
        addTime = new Date();
        isFinished = false;
        finishTime = new Date(0);
        setContent("");
    }

    public PageInfo(String name, String owner, FetchService fetcher) {
        setKeyName(name, owner);
        setFetcher(fetcher);
        addTime = new Date();
        isFinished = false;
        finishTime = new Date(0);
        setContent("");
    }

    public void setKeyName(String name, String owner) {
        keyName = DataModel.generateKeyName(name, owner);
    }

    public String getKeyName() {
        return keyName;
    }

    public void setFetcher(FetchService fetcher) {
        url = fetcher.getUrl();
        method = fetcher.getMethod();
        parameters = fetcher.getParameters();
        cookie = fetcher.getCookie();
    }

    public FetchService getFetcher() throws MalformedURLException {
        FetchService fetcher = new FetchService(url, method);
        fetcher.setParameters(parameters);
        fetcher.setCookie(cookie);
        return fetcher;
    }

    public void setContent(String content) {
        this.content = new Text(content);
    }

    public String getContent() {
        return content.getValue();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Date getAddTime() {
        return addTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void finishCache(String content) {
        setContent(content);
        isFinished = true;
        finishTime = new Date();
    }
}
