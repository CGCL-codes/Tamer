package com.google.code.facebookapi;

import java.util.SortedMap;
import java.util.TreeMap;

public class FBWebRequest {

    private FBAppConf appConf;

    private FBWebSession session;

    private boolean valid;

    private SortedMap<String, String> params;

    private SortedMap<String, String> cookies;

    private boolean inCanvas;

    private boolean inIframe;

    private boolean inProfileTab;

    public FBWebRequest(FBAppConf appConf, FBWebSession session, SortedMap<String, String> params, SortedMap<String, String> cookies, boolean valid) {
        this.appConf = appConf;
        this.session = session;
        this.valid = valid;
        this.params = params != null ? params : new TreeMap<String, String>();
        this.cookies = cookies != null ? cookies : new TreeMap<String, String>();
    }

    public SortedMap<String, String> getParams() {
        return params;
    }

    public void setParams(SortedMap<String, String> params) {
        this.params = params;
    }

    public SortedMap<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(SortedMap<String, String> cookies) {
        this.cookies = cookies;
    }

    public boolean isInCanvas() {
        return inCanvas;
    }

    public void setInCanvas(boolean inCanvas) {
        this.inCanvas = inCanvas;
    }

    public boolean isInIframe() {
        return inIframe;
    }

    public void setInIframe(boolean inIframe) {
        this.inIframe = inIframe;
    }

    public boolean isInProfileTab() {
        return inProfileTab;
    }

    public void setInProfileTab(boolean inProfileTab) {
        this.inProfileTab = inProfileTab;
    }

    public FBAppConf getAppConf() {
        return appConf;
    }

    public FBWebSession getSession() {
        return session;
    }

    public boolean isValid() {
        return valid;
    }
}
