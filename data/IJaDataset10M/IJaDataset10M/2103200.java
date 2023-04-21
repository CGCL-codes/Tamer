package be.vds.jtbdive.core.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class SystemConstants {

    public static final String UNKNOWN_OS_TYPE = "Unknown";

    public static final String WINDOWS_OS_TYPE = "Windows";

    public static final String LINUX_OS_TYPE = "Linux";

    private static List<String> WINDOWS_OS_NAMES;

    static {
        WINDOWS_OS_NAMES = new ArrayList<String>();
        WINDOWS_OS_NAMES.add("Windows XP");
    }

    public static List<String> getWindowsOsNames() {
        return WINDOWS_OS_NAMES;
    }

    private static List<String> LINUX_OS_NAMES;

    static {
        LINUX_OS_NAMES = new ArrayList<String>();
        LINUX_OS_NAMES.add("Linux");
    }

    public static List<String> getLinuxOsNames() {
        return LINUX_OS_NAMES;
    }

    public static List<String> getOsNames() {
        List<String> list = new ArrayList<String>();
        list.addAll(LINUX_OS_NAMES);
        list.addAll(WINDOWS_OS_NAMES);
        return list;
    }

    public static List<String> getOsTypes() {
        List<String> list = new ArrayList<String>();
        list.add(LINUX_OS_TYPE);
        list.add(WINDOWS_OS_TYPE);
        return list;
    }
}
