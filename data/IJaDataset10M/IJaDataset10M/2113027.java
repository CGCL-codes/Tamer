package org.snipecode.reg.test;

import org.snipecode.reg.RegUtil;
import junit.framework.TestCase;

public class RegUtilTestCase extends TestCase {

    public static void testWriteRead() {
        int handle = RegUtil.RegCreateKeyEx(RegUtil.HKEY_LOCAL_MACHINE, "SOFTWARE\\Java\\regutil")[RegUtil.NATIVE_HANDLE];
        RegUtil.RegCloseKey(handle);
        handle = RegUtil.RegOpenKey(RegUtil.HKEY_LOCAL_MACHINE, "SOFTWARE\\Java\\regutil", RegUtil.KEY_ALL_ACCESS)[RegUtil.NATIVE_HANDLE];
        RegUtil.RegSetValueEx(handle, "TestName", "TestValue");
        byte[] val = RegUtil.RegQueryValueEx(handle, "TestName");
        System.out.println(new String(val).toString().trim());
        RegUtil.RegDeleteKey(RegUtil.HKEY_LOCAL_MACHINE, "SOFTWARE\\SnipCode\\regutil");
        RegUtil.RegDeleteKey(RegUtil.HKEY_LOCAL_MACHINE, "SOFTWARE\\SnipCode");
    }

    public static void testReadEnum() {
        int handle = RegUtil.RegOpenKey(RegUtil.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", RegUtil.KEY_QUERY_VALUE)[RegUtil.NATIVE_HANDLE];
        int[] info = RegUtil.RegQueryInfoKey(handle);
        int count = info[RegUtil.VALUES_NUMBER];
        int maxlen = info[RegUtil.MAX_VALUE_NAME_LENGTH];
        for (int index = 0; index < count; index++) {
            byte[] name = RegUtil.RegEnumValue(handle, index, maxlen + 1);
            System.out.print(new String(name).trim() + " = ");
            byte[] values = RegUtil.RegQueryValueEx(handle, name);
            if (null != values) System.out.print(new String(values).trim());
            System.out.println();
        }
        RegUtil.RegCloseKey(handle);
    }
}
