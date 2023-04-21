package org.granite.util;

import java.util.UUID;

/**
 * @author Franck WOLFF
 */
public abstract class UUIDUtil {

    public static String randomUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
