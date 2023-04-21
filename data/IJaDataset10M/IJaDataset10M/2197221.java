package com.cnoja.jmsncn.utils.system;

import java.util.Properties;
import com.cnoja.jmsncn.utils.text.StringUtils;

public class SystemUtils {

    public static String getSystemType() {
        String sysName = System.getProperty("os.name");
        if (sysName.startsWith("Windows") || sysName.startsWith("Winnt")) {
            return "winnt";
        }
        return null;
    }

    public static Object getSystemVersion() {
        return System.getProperty("os.version");
    }

    public static String getLocaleId() {
        return "0x0409";
    }

    public static String getOSArchitecture() {
        String os = System.getProperty("os.arch");
        if (StringUtils.notNullOrEmpty(os)) {
            if (os.equals("x86")) {
                return "i386";
            }
        }
        return null;
    }
}
