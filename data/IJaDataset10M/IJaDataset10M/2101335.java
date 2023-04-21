package swisseph;

/**
* This class offers some static variables and methods for tracing the
* method calls and parameter contents of any called method in swisseph
* package. This is not suited multithreaded applications.
*/
public class Trace {

    static int level = 0;

    static CFmt cv = new CFmt();

    protected static void trace(int level, String method) {
        if (level > 0) {
            for (int z = level; z > 0; z--) {
                System.out.print(" ");
            }
        }
        System.out.println(method);
    }

    public static String fmtDbl(double val) {
        return cv.fmt("%23.16f", val) + "(" + dblToHex(val) + ")";
    }

    public static void printDblArr(String name, double[] arr) {
        int len = SMath.max(0, 16 - name.length());
        String pad = "                                              ".substring(0, len);
        if (arr == null) {
            System.out.print("    " + name + "[]: ");
            System.out.println("null");
        } else {
            for (int z = 0; z < arr.length; z++) {
                System.out.print("    " + name + "[" + z + "]: ");
                System.out.println(pad + "/" + fmtDbl(arr[z]));
            }
        }
    }

    public static void printDblArr(String name, double[] arr, int offs, int cnt) {
        int len = SMath.max(0, 16 - name.length());
        String pad = "                                              ".substring(0, len);
        if (arr == null) {
            System.out.print("    " + name + "[]: ");
            System.out.println("null");
        } else {
            for (int z = offs; z < arr.length && z < offs + cnt; z++) {
                System.out.print("    " + name + "[" + (z - offs) + "]: ");
                System.out.println(pad + "/" + fmtDbl(arr[z]));
            }
        }
    }

    private static String dblToHex(double val) {
        long dbl = Double.doubleToLongBits(val);
        return cv.fmt("%.02X", (dbl & 0xff00000000000000L) >>> 56) + " " + cv.fmt("%.02X", (dbl & 0x00ff000000000000L) >> 48) + " " + cv.fmt("%.02X", (dbl & 0x0000ff0000000000L) >> 40) + " " + cv.fmt("%.02X", (dbl & 0x000000ff00000000L) >> 32) + " " + cv.fmt("%.02X", (dbl & 0x00000000ff000000L) >> 24) + " " + cv.fmt("%.02X", (dbl & 0x0000000000ff0000L) >> 16) + " " + cv.fmt("%.02X", (dbl & 0x000000000000ff00L) >> 8) + " " + cv.fmt("%.02X", (dbl & 0x00000000000000ffL));
    }
}
