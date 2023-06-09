package java.util;

import gnu.classpath.Configuration;
import java.io.*;

/**
 *
 */
final class VMTimeZone {

    static {
        if (Configuration.INIT_LOAD_LIBRARY) {
            System.loadLibrary("javautil");
        }
    }

    /**
   * This method returns a time zone id string which is in the form
   * (standard zone name) or (standard zone name)(GMT offset) or
   * (standard zone name)(GMT offset)(daylight time zone name).  The
   * GMT offset can be in seconds, or where it is evenly divisible by
   * 3600, then it can be in hours.  The offset must be the time to
   * add to the local time to get GMT.  If a offset is given and the
   * time zone observes daylight saving then the (daylight time zone
   * name) must also be given (otherwise it is assumed the time zone
   * does not observe any daylight savings).
   * <p>
   * The result of this method is given to the method
   * TimeZone.getDefaultTimeZone(String) which tries to map the time
   * zone id to a known TimeZone.  See that method on how the returned
   * String is mapped to a real TimeZone object.
   * <p>
   * The reference implementation which is made for GNU/Posix like
   * systems calls <code>System.getenv("TZ")</code>,
   * <code>readTimeZoneFile("/etc/timezone")</code>,
   * <code>readtzFile("/etc/localtime")</code> and finally
   * <code>getSystemTimeZoneId()</code> till a supported TimeZone is
   * found through <code>TimeZone.getDefaultTimeZone(String)</code>.
   * If every method fails <code>null</code> is returned (which means
   * the TimeZone code will fall back on GMT as default time zone).
   * <p>
   * Note that this method is called inside a
   * <code>AccessController.doPrivileged()</code> block and runs with
   * the priviliges of the java.util system classes.  It will only be
   * called when the default time zone is not yet set, the system
   * property user.timezone isn't set and it is requested for the
   * first time.
   */
    static TimeZone getDefaultTimeZoneId() {
        TimeZone zone = null;
        String tzid = System.getenv("TZ");
        if (tzid != null && !tzid.equals("")) zone = TimeZone.getDefaultTimeZone(tzid);
        if (zone == null) {
            tzid = readTimeZoneFile("/etc/timezone");
            if (tzid != null && !tzid.equals("")) zone = TimeZone.getDefaultTimeZone(tzid);
        }
        if (zone == null) {
            tzid = readtzFile("/etc/localtime");
            if (tzid != null && !tzid.equals("")) zone = TimeZone.getDefaultTimeZone(tzid);
        }
        if (zone == null) {
            tzid = getSystemTimeZoneId();
            if (tzid != null && !tzid.equals("")) zone = TimeZone.getDefaultTimeZone(tzid);
        }
        return zone;
    }

    /**
   * Tries to read the time zone name from a file. Only the first
   * consecutive letters, digits, slashes, dashes and underscores are
   * read from the file. If the file cannot be read or an IOException
   * occurs null is returned.
   * <p>
   * The /etc/timezone file is not standard, but a lot of systems have
   * it. If it exist the first line always contains a string
   * describing the timezone of the host of domain. Some systems
   * contain a /etc/TIMEZONE file which is used to set the TZ
   * environment variable (which is checked before /etc/timezone is
   * read).
   */
    private static String readTimeZoneFile(String file) {
        File f = new File(file);
        if (!f.exists()) return null;
        InputStreamReader isr = null;
        try {
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            isr = new InputStreamReader(bis);
            StringBuffer sb = new StringBuffer();
            int i = isr.read();
            while (i != -1) {
                char c = (char) i;
                if (Character.isLetter(c) || Character.isDigit(c) || c == '/' || c == '-' || c == '_') {
                    sb.append(c);
                    i = isr.read();
                } else break;
            }
            return sb.toString();
        } catch (IOException ioe) {
            return null;
        } finally {
            try {
                if (isr != null) isr.close();
            } catch (IOException ioe) {
            }
        }
    }

    /**
   * Tries to read a file as a "standard" tzfile and return a time
   * zone id string as expected by <code>getDefaultTimeZone(String)</code>.
   * If the file doesn't exist, an IOException occurs or it isn't a tzfile
   * that can be parsed null is returned.
   * <p>
   * The tzfile structure (as also used by glibc) is described in the Olson
   * tz database archive as can be found at
   * <code>ftp://elsie.nci.nih.gov/pub/</code>.
   * <p>
   * At least the following platforms support the tzdata file format
   * and /etc/localtime (GNU/Linux, Darwin, Solaris and FreeBSD at
   * least). Some systems (like Darwin) don't start the file with the
   * required magic bytes 'TZif', this implementation can handle
   * that).
   */
    private static String readtzFile(String file) {
        File f = new File(file);
        if (!f.exists()) return null;
        DataInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            byte[] tzif = new byte[4];
            dis.readFully(tzif);
            if (tzif[0] == 'T' && tzif[1] == 'Z' && tzif[2] == 'i' && tzif[3] == 'f') skipFully(dis, 16 + 3 * 4); else skipFully(dis, 16 + 3 * 4 - 4);
            int timecnt = dis.readInt();
            int typecnt = dis.readInt();
            if (typecnt > 0) {
                int charcnt = dis.readInt();
                skipFully(dis, timecnt * (4 + 1));
                int abbrind = -1;
                int dst_abbrind = -1;
                int gmt_offset = 0;
                while (typecnt-- > 0) {
                    int offset = dis.readInt();
                    int dst = dis.readByte();
                    if (dst == 0) {
                        abbrind = dis.readByte();
                        gmt_offset = offset;
                    } else dst_abbrind = dis.readByte();
                }
                gmt_offset *= -1;
                if (gmt_offset % 3600 == 0) gmt_offset /= 3600;
                if (abbrind >= 0) {
                    byte[] names = new byte[charcnt];
                    dis.readFully(names);
                    int j = abbrind;
                    while (j < charcnt && names[j] != 0) j++;
                    String zonename = new String(names, abbrind, j - abbrind, "ASCII");
                    String dst_zonename;
                    if (dst_abbrind >= 0) {
                        j = dst_abbrind;
                        while (j < charcnt && names[j] != 0) j++;
                        dst_zonename = new String(names, dst_abbrind, j - dst_abbrind, "ASCII");
                    } else dst_zonename = "";
                    String offset_string;
                    if ("".equals(dst_zonename) && (gmt_offset == 0 || zonename.startsWith("GMT+") || zonename.startsWith("GMT-"))) offset_string = ""; else offset_string = Integer.toString(gmt_offset);
                    String id = zonename + offset_string + dst_zonename;
                    return id;
                }
            }
            return null;
        } catch (IOException ioe) {
            return null;
        } finally {
            try {
                if (dis != null) dis.close();
            } catch (IOException ioe) {
            }
        }
    }

    /**
   * Skips the requested number of bytes in the given InputStream.
   * Throws EOFException if not enough bytes could be skipped.
   * Negative numbers of bytes to skip are ignored.
   */
    private static void skipFully(InputStream is, long l) throws IOException {
        while (l > 0) {
            long k = is.skip(l);
            if (k <= 0) throw new EOFException();
            l -= k;
        }
    }

    /**
   * Tries to get the system time zone id through native code.
   */
    private static native String getSystemTimeZoneId();
}
