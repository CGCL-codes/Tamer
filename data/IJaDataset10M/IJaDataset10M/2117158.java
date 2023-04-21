package org.delafer.benchmark.constants;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.StringTokenizer;
import org.delafer.benchmark.helpers.Helpers;

public class Constants {

    public static final Date RELEASE_DATE = Helpers.asDate("10.05.2011");

    public static final String SF_WEB_SITE = "http://azureus.sourceforge.net/";

    public static final String BENCHMARK_WEB_SITE = "http://azureus.aelitis.com/";

    public static final String DEFAULT_ENCODING = "UTF8";

    public static final String BYTE_ENCODING = "ISO-8859-1";

    public static Charset BYTE_CHARSET;

    public static Charset DEFAULT_CHARSET;

    static {
        try {
            BYTE_CHARSET = Charset.forName(Constants.BYTE_ENCODING);
            DEFAULT_CHARSET = Charset.forName(Constants.DEFAULT_ENCODING);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static final String INFINITY_STRING = "∞";

    public static final int INFINITY_AS_INT = 31536000;

    public static final String BENCHMARK_NAME = "jsBenchmark";

    public static final String BENCHMARK_VERSION = "1.0.0.0";

    public static final byte[] VERSION_ID = ("JSBN001").getBytes();

    public static final String OSName = System.getProperty("os.name");

    public static final boolean isOSX = OSName.toLowerCase().startsWith("mac os");

    public static final boolean isLinux = OSName.equalsIgnoreCase("Linux");

    public static final boolean isSolaris = OSName.equalsIgnoreCase("SunOS");

    public static final boolean isFreeBSD = OSName.equalsIgnoreCase("FreeBSD");

    public static final boolean isWindowsXP = OSName.equalsIgnoreCase("Windows XP");

    public static final boolean isWindows95 = OSName.equalsIgnoreCase("Windows 95");

    public static final boolean isWindows98 = OSName.equalsIgnoreCase("Windows 98");

    public static final boolean isWindows2000 = OSName.equalsIgnoreCase("Windows 2000");

    public static final boolean isWindowsME = OSName.equalsIgnoreCase("Windows ME");

    public static final boolean isWindows9598ME = isWindows95 || isWindows98 || isWindowsME;

    public static final boolean isWindows = OSName.toLowerCase().contains("windows");

    public static final boolean isUnix = !isWindows && !isOSX;

    public static final boolean isWindowsVista;

    public static final boolean isWindowsVistaSP2OrHigher;

    public static final boolean isWindowsVistaOrHigher;

    public static final boolean isWindows7OrHigher;

    static {
        if (isWindows) {
            Float ver = null;
            try {
                ver = new Float(System.getProperty("os.version"));
            } catch (Throwable e) {
            }
            boolean vista_sp2_or_higher = false;
            if (ver == null) {
                isWindowsVista = false;
                isWindowsVistaOrHigher = false;
                isWindows7OrHigher = false;
            } else {
                float f_ver = ver.floatValue();
                isWindowsVista = f_ver == 6;
                isWindowsVistaOrHigher = f_ver >= 6;
                isWindows7OrHigher = f_ver >= 6.1f;
                if (isWindowsVista) {
                    LineNumberReader lnr = null;
                    try {
                        Process p = Runtime.getRuntime().exec(new String[] { "reg", "query", "HKLM\\Software\\Microsoft\\Windows NT\\CurrentVersion", "/v", "CSDVersion" });
                        lnr = new LineNumberReader(new InputStreamReader(p.getInputStream()));
                        while (true) {
                            String line = lnr.readLine();
                            if (line == null) {
                                break;
                            }
                            if (line.matches(".*CSDVersion.*")) {
                                vista_sp2_or_higher = line.matches(".*Service Pack [2-9]");
                                break;
                            }
                        }
                    } catch (Throwable e) {
                    } finally {
                        if (lnr != null) {
                            try {
                                lnr.close();
                            } catch (Throwable e) {
                            }
                        }
                    }
                }
            }
            isWindowsVistaSP2OrHigher = vista_sp2_or_higher;
        } else {
            isWindowsVista = false;
            isWindowsVistaSP2OrHigher = false;
            isWindowsVistaOrHigher = false;
            isWindows7OrHigher = false;
        }
    }

    public static final boolean isOSX_10_5_OrHigher;

    public static final boolean isOSX_10_6_OrHigher;

    static {
        if (isOSX) {
            int first_digit = 0;
            int second_digit = 0;
            try {
                String os_version = System.getProperty("os.version");
                String[] bits = os_version.split("\\.");
                first_digit = Integer.parseInt(bits[0]);
                if (bits.length > 1) {
                    second_digit = Integer.parseInt(bits[1]);
                }
            } catch (Throwable e) {
            }
            isOSX_10_5_OrHigher = first_digit > 10 || (first_digit == 10 && second_digit >= 5);
            isOSX_10_6_OrHigher = first_digit > 10 || (first_digit == 10 && second_digit >= 6);
        } else {
            isOSX_10_5_OrHigher = false;
            isOSX_10_6_OrHigher = false;
        }
    }

    public static final String JAVA_VERSION = System.getProperty("java.version");

    public static final String FILE_WILDCARD = isWindows ? "*.*" : "*";

    /**
  	 * Gets the current version, or if a CVS version, the one on which it is based 
  	 * @return
  	 */
    public static String getBaseVersion() {
        return (getBaseVersion(BENCHMARK_VERSION));
    }

    public static String getBaseVersion(String version) {
        int p1 = version.indexOf("_");
        if (p1 == -1) {
            return (version);
        }
        return (version.substring(0, p1));
    }

    /**
  	 * is this a formal build or CVS/incremental 
  	 * @return
  	 */
    public static boolean isCVSVersion() {
        return (isCVSVersion(BENCHMARK_VERSION));
    }

    public static boolean isCVSVersion(String version) {
        return (version.indexOf("_") != -1);
    }

    /**
  	 * For CVS builds this returns the incremental build number. For people running their own
  	 * builds this returns -1 
  	 * @return
  	 */
    public static int getIncrementalBuild() {
        return (getIncrementalBuild(BENCHMARK_VERSION));
    }

    public static int getIncrementalBuild(String version) {
        if (!isCVSVersion(version)) {
            return (0);
        }
        int p1 = version.indexOf("_B");
        if (p1 == -1) {
            return (-1);
        }
        try {
            return (Integer.parseInt(version.substring(p1 + 2)));
        } catch (Throwable e) {
            System.out.println("can't parse version");
            return (-1);
        }
    }

    /**
		 * compare two version strings of form n.n.n.n (e.g. 1.2.3.4)
		 * @param version_1	
		 * @param version_2
		 * @return -ve -> version_1 lower, 0 = same, +ve -> version_1 higher
		 */
    public static int compareVersions(String version_1, String version_2) {
        try {
            if (version_1.startsWith(".")) {
                version_1 = "0" + version_1;
            }
            if (version_2.startsWith(".")) {
                version_2 = "0" + version_2;
            }
            StringTokenizer tok1 = new StringTokenizer(version_1, ".");
            StringTokenizer tok2 = new StringTokenizer(version_2, ".");
            while (true) {
                if (tok1.hasMoreTokens() && tok2.hasMoreTokens()) {
                    int i1 = Integer.parseInt(tok1.nextToken());
                    int i2 = Integer.parseInt(tok2.nextToken());
                    if (i1 != i2) {
                        return (i1 - i2);
                    }
                } else if (tok1.hasMoreTokens()) {
                    int i1 = Integer.parseInt(tok1.nextToken());
                    if (i1 != 0) {
                        return (1);
                    }
                } else if (tok2.hasMoreTokens()) {
                    int i2 = Integer.parseInt(tok2.nextToken());
                    if (i2 != 0) {
                        return (-1);
                    }
                } else {
                    return (0);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return (0);
        }
    }
}
