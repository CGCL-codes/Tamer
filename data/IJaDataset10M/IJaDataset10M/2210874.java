package com.mjs_svc.possibility.util;

/**
 *
 * @author Matthew
 */
public class Annotations {

    public static @interface loginRequired {
    }

    public static @interface permissionRequired {

        String model();

        String codeName();
    }
}
