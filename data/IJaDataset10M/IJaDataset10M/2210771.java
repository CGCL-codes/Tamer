package org.xmatthew.spy2servers.util;

import org.apache.commons.lang.StringUtils;

/**
 * Assert Utils
 * 
 * @author Matthew Xie
 *
 */
public final class Assert extends org.springframework.util.Assert {

    public static void notBlank(String str) {
        notBlank(str, null);
    }

    public static void notBlank(String str, String errrorMessage) {
        if (StringUtils.isBlank(str)) {
            if (errrorMessage == null) {
                throw new IllegalArgumentException("blank string not allowed");
            } else {
                throw new IllegalArgumentException(errrorMessage);
            }
        }
    }
}
