package com.kescom.matrix.core.access;

import java.net.URLEncoder;

public class RequestAccessAction implements IRequestAccessAction {

    private boolean allow;

    private boolean deny;

    private String nextUrl;

    private int denyStatus;

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public boolean isDeny() {
        return deny;
    }

    public void setDeny(boolean deny) {
        this.deny = deny;
    }

    public String getNextUrl(String currentUrl) {
        try {
            return nextUrl.replace("$1", URLEncoder.encode(currentUrl, "UTF8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public int getDenyStatus() {
        return denyStatus;
    }

    public void setDenyStatus(int denyStatus) {
        this.denyStatus = denyStatus;
    }
}
