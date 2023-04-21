package com.untilov.gb.http.cookies;

import java.util.Vector;

public interface CookiesManagerIF {

    public Vector getCookies();

    boolean isCookiesValid();

    void processCookies(Object connection) throws Exception;
}
