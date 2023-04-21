package net.sipvip.client;

import java.util.Date;

public class GwtCommonUtils {

    public static Date CookiesDate() {
        final long DURATION = 1000 * 60 * 60 * 24 * 364;
        Date expires = new Date(System.currentTimeMillis() + (DURATION * 25));
        return expires;
    }
}
