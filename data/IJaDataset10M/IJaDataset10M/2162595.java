package moio.util;

import waba.sys.Vm;

public class System {

    public static FakeOutputStream out;

    public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
        Vm.copyArray(src, srcPos, dest, destPos, length);
    }

    public static String getProperty(String s1, String s2) {
        if (s1 == "line.separator") {
            return "\n";
        } else {
            return "";
        }
    }

    public static String getProperty(String s1) {
        return "";
    }
}
