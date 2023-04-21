package org.middleheaven.process.web;

/**
 * 
 */
public interface HttpCookieWriter {

    public void writeAll(HttpCookieBag bag);

    public void write(HttpCookie cookie);
}
