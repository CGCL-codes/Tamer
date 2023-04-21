package com.tamtamy.jatta.strongbox;

import java.util.List;
import com.tamtamy.jttamobile.data.Content;

public class JattaData {

    private static JattaData instance = null;

    private static Object lock = new Object();

    private String username;

    private String password;

    private String token;

    private String response;

    private int responseCode;

    private String selectedContentId;

    private List<Content> contentList;

    private String searchString;

    private String contentOrder;

    private JattaData() {
    }

    public static JattaData getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new JattaData();
            }
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }

    public List<Content> getContentList() {
        return contentList;
    }

    public void setSelectedContentId(String selectedContentId) {
        this.selectedContentId = selectedContentId;
    }

    public String getSelectedContentId() {
        return selectedContentId;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchString() {
        return searchString;
    }

    public String getContentOrder() {
        return contentOrder;
    }

    public void setContentOrder(String contentOrder) {
        this.contentOrder = contentOrder;
    }
}
