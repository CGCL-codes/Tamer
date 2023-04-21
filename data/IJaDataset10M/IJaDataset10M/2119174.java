package org.tamacat.httpd.examples;

import org.tamacat.httpd.filter.acl.AccessUrl;

public class AccessUrlImpl implements AccessUrl {

    boolean isSuccess;

    long createTime = System.currentTimeMillis();

    public AccessUrlImpl() {
    }

    public AccessUrlImpl(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public boolean isCacheExpired(long expire) {
        return System.currentTimeMillis() - createTime > expire;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }
}
