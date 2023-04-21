package de.kout.wlFxp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Utility class
 *
 * @author Alexander Kout 30. März 2002
 */
public class Utilities {

    /** verbose output */
    public static boolean debug = false;

    /** the FileWriter for the debug mode */
    public static FileWriter logFile = null;

    private static final String[] monthsWS = { " Jan ", " Feb ", " Mar ", " Apr ", " May ", " Jun ", " Jul ", " Aug ", " Sep ", " Oct ", " Nov ", " Dec " };

    private static final String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    /**
	 * transforms a long into a String like 4KiB
	 *
	 * @param size Description of Parameter
	 *
	 * @return Description of the Returned Value
	 */
    public static String humanReadable(double size) {
        DecimalFormat df = new DecimalFormat("###0.00");
        double f1;
        double f2;
        double f3;
        if ((f1 = size / 1024.0) > 1.0) {
            if ((f2 = f1 / 1024.0) > 1.0) {
                if ((f3 = f2 / 1024) > 1.0) {
                    return df.format(f3) + "GiB";
                }
                return df.format(f2) + "MiB";
            }
            return df.format(f1) + "KiB";
        }
        return df.format(size) + "B";
    }

    /**
	 * Description of the Method
	 *
	 * @param size Description of Parameter
	 *
	 * @return Description of the Returned Value
	 */
    public static String humanReadable(long size) {
        return humanReadable(size * 1.0);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param time DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public static String humanReadableTime(double time) {
        DecimalFormat df = new DecimalFormat("###0");
        double min;
        double hour;
        double day;
        double sec;
        sec = time * 1.0;
        StringBuffer buf = new StringBuffer(50);
        if ((min = sec / 60.0) >= 1.0) {
            if ((hour = min / 60.0) >= 1.0) {
                if ((day = hour / 24.0) >= 1.0) {
                    buf.append(df.format(day)).append("d ").append(df.format(hour - (Math.floor(day) * 24.0))).append("h ").append(df.format(min - (Math.floor(hour) * 60.0))).append("min ").append(df.format(sec - (Math.floor(min) * 60.0))).append("s");
                    return buf.toString();
                }
                buf.append(df.format(hour)).append("h ").append(df.format(min - (Math.floor(hour) * 60.0))).append("min ").append(df.format(sec - (Math.floor(min) * 60.0))).append("s");
                return buf.toString();
            }
            buf.append(df.format(min)).append("min ").append(df.format(sec - (Math.floor(min) * 60.0))).append("s");
            return buf.toString();
        }
        buf.append(df.format(sec)).append("s");
        return buf.toString();
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param time DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public static String humanReadableTime2(double time) {
        DecimalFormat df = new DecimalFormat("00");
        double min;
        double hour;
        double day;
        double sec;
        sec = time * 1.0;
        StringBuffer buf = new StringBuffer(50);
        if ((min = sec / 60.0) >= 1.0) {
            if ((hour = min / 60.0) >= 1.0) {
                if ((day = hour / 24.0) >= 1.0) {
                    buf.append(df.format(day)).append("d:").append(df.format(hour - (Math.floor(day) * 24.0))).append(":").append(df.format(min - (Math.floor(hour) * 60.0))).append(":").append(df.format(sec - (Math.floor(min) * 60.0)));
                    return buf.toString();
                }
                buf.append(df.format(hour)).append(":").append(df.format(min - (Math.floor(hour) * 60.0))).append(":").append(df.format(sec - (Math.floor(min) * 60.0)));
                return buf.toString();
            }
            buf.append(df.format(min)).append(":").append(df.format(sec - (Math.floor(min) * 60.0)));
            return buf.toString();
        }
        buf.append(new DecimalFormat("#0").format(sec));
        return buf.toString();
    }

    /**
	 * Heapsort "with bottom-up linear search" algorithm
	 *
	 * @param list must be a list of Strings
	 */
    public static void sortList(String[] list) {
        int n = list.length;
        for (int i = n / 2; i > 0; i--) {
            reheap(list, i, n);
        }
        for (int m = n; m > 0; m--) {
            String t = list[0];
            list[0] = list[m - 1];
            list[m - 1] = t;
            reheap(list, 1, m - 1);
        }
    }

    /**
	 * reheap method for heapsort
	 *
	 * @param array String array to be sorted
	 * @param root position of root
	 * @param end position of end
	 */
    private static void reheap(String[] array, int root, int end) {
        int[] stack = new int[new Double(Math.log(array.length) / Math.log(2)).intValue() + 10];
        int s = 0;
        int pos = root;
        stack[s++] = pos;
        while ((2 * pos) <= end) {
            if (((2 * pos) + 1) > end) {
                stack[s++] = 2 * pos;
                break;
            }
            if (array[(2 * pos) - 1].compareToIgnoreCase(array[2 * pos]) > 0) {
                stack[s++] = 2 * pos;
            } else {
                stack[s++] = (2 * pos) + 1;
            }
            pos = stack[s - 1];
        }
        pos = root;
        for (int i = s - 1; i >= 0; i--) {
            if (array[stack[i] - 1].compareToIgnoreCase(array[root - 1]) > -1) {
                pos = stack[i];
                s = i + 1;
                break;
            }
        }
        String temp = array[root - 1];
        for (int i = 1; i < s; i++) {
            array[(stack[i] / 2) - 1] = array[stack[i] - 1];
        }
        array[pos - 1] = temp;
    }

    /**
	 * Heapsort "with linear bottom-up search" algorithm
	 *
	 * @param list FtpFile array which is going to be sorted
	 * @param sortBy Description of the Parameter
	 * @param prio DOCUMENT ME!
	 * @param prioList DOCUMENT ME!
	 */
    public static void sortFiles(Vector<MyFile> list, String sortBy, boolean prio, Vector prioList) {
        int n = list.size();
        for (int i = n / 2; i > 0; i--) {
            reheap(list, i, n, sortBy, prio, prioList);
        }
        for (int m = n; m > 0; m--) {
            MyFile t = (MyFile) list.elementAt(0);
            list.setElementAt(list.elementAt(m - 1), 0);
            list.setElementAt(t, m - 1);
            reheap(list, 1, m - 1, sortBy, prio, prioList);
        }
    }

    /**
	 * reheap method for heapsort
	 *
	 * @param array array to be sorted
	 * @param root position of root
	 * @param end position of end
	 * @param sortBy Description of the Parameter
	 * @param prio DOCUMENT ME!
	 * @param prioList DOCUMENT ME!
	 */
    private static void reheap(Vector<MyFile> array, int root, int end, String sortBy, boolean prio, Vector prioList) {
        int[] stack = new int[new Double(Math.log(array.size()) / Math.log(2)).intValue() + 10];
        int s = 0;
        int pos = root;
        stack[s++] = pos;
        while ((2 * pos) <= end) {
            if (((2 * pos) + 1) > end) {
                stack[s++] = 2 * pos;
                break;
            }
            if (compareFiles(array.elementAt((2 * pos) - 1), array.elementAt(2 * pos), sortBy, prio, prioList) > 0) {
                stack[s++] = 2 * pos;
            } else {
                stack[s++] = (2 * pos) + 1;
            }
            pos = stack[s - 1];
        }
        pos = root;
        for (int i = s - 1; i >= 0; i--) {
            if (compareFiles(array.elementAt(stack[i] - 1), array.elementAt(root - 1), sortBy, prio, prioList) > -1) {
                pos = stack[i];
                s = i + 1;
                break;
            }
        }
        MyFile temp = array.elementAt(root - 1);
        for (int i = 1; i < s; i++) {
            array.setElementAt(array.elementAt(stack[i] - 1), (stack[i] / 2) - 1);
        }
        array.setElementAt(temp, pos - 1);
    }

    /**
	 * compare method for sorting
	 *
	 * @param o1 first file
	 * @param o2 second file
	 * @param sortBy Description of the Parameter
	 * @param prio DOCUMENT ME!
	 * @param prioList DOCUMENT ME!
	 *
	 * @return result of compareToIgnoreCase
	 */
    private static int compareFiles(Object o1, Object o2, String sortBy, boolean prio, Vector prioList) {
        int ret;
        MyFile f1 = (MyFile) o1;
        MyFile f2 = (MyFile) o2;
        if (prio) {
            int m1 = matches(prioList, f1.getName());
            int m2 = matches(prioList, f2.getName());
            if ((m1 != -1) || (m2 != -1)) {
                if (m1 > m2) {
                    return -1;
                } else if (m2 > m1) {
                    return 1;
                }
            }
        }
        if (f1.isDirectory() && !f2.isDirectory()) {
            return -1;
        }
        if (!f1.isDirectory() && f2.isDirectory()) {
            return 1;
        }
        if (sortBy.equals("Name")) {
            return f1.getName().compareToIgnoreCase(f2.getName());
        } else if (sortBy.equals("IName")) {
            return -f1.getName().compareToIgnoreCase(f2.getName());
        } else if (sortBy.equals("Size")) {
            ret = (int) (f1.getSize() - f2.getSize());
        } else if (sortBy.equals("ISize")) {
            ret = (int) (f2.getSize() - f1.getSize());
        } else if (sortBy.equals("Date")) {
            ret = compDate(f1.getDate(), f2.getDate());
        } else if (sortBy.equals("IDate")) {
            ret = compDate(f2.getDate(), f1.getDate());
        } else {
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
        if (ret == 0) {
            ret = f1.getName().compareToIgnoreCase(f2.getName());
        }
        return ret;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param v DOCUMENT ME!
	 * @param s DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    private static int matches(Vector v, String s) {
        for (int i = 0; i < v.size(); i++) {
            try {
                if (Pattern.matches(((String) v.elementAt(i)).toLowerCase(), s.toLowerCase())) {
                    return (v.size() - i);
                }
            } catch (Exception e) {
            }
        }
        return -1;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param d1 DOCUMENT ME!
	 * @param d2 DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    private static int compDate(String d1, String d2) {
        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.setTime(new Date());
        GregorianCalendar cal2 = new GregorianCalendar();
        cal2.setTime(new Date());
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        if (d1.indexOf("/") == 2) {
            cal1.set(Calendar.MONTH, Integer.parseInt(d1.substring(0, 2)) - 1);
            cal1.set(Calendar.DATE, Integer.parseInt(d1.substring(3, 5)));
            if (cal1.compareTo(cal) > 0) {
                cal1.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
            }
        } else {
            cal1.set(Calendar.YEAR, Integer.parseInt(d1.substring(0, 4)));
            cal1.set(Calendar.MONTH, Integer.parseInt(d1.substring(5, 7)) - 1);
            cal1.set(Calendar.DATE, Integer.parseInt(d1.substring(8, 10)));
        }
        if (d2.indexOf("/") == 2) {
            cal2.set(Calendar.MONTH, Integer.parseInt(d2.substring(0, 2)) - 1);
            cal2.set(Calendar.DATE, Integer.parseInt(d2.substring(3, 5)));
            if (cal2.compareTo(cal) > 0) {
                cal2.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
            }
        } else {
            cal2.set(Calendar.YEAR, Integer.parseInt(d2.substring(0, 4)));
            cal2.set(Calendar.MONTH, Integer.parseInt(d2.substring(5, 7)) - 1);
            cal2.set(Calendar.DATE, Integer.parseInt(d2.substring(8, 10)));
        }
        if (cal1.before(cal2)) {
            return -1;
        }
        return 1;
    }

    /**
	 * print method which writes to a file if debug is true
	 *
	 * @param s String to be printed
	 */
    public static void print(String s) {
        if (debug) {
            String settings = System.getProperty("user.home", ".") + File.separator + ".wlFxp";
            System.out.print(s);
            try {
                if (logFile == null) {
                    if (!new File(settings).isDirectory()) {
                        new File(settings).mkdir();
                    }
                    logFile = new FileWriter(settings + File.separator + "log.txt", true);
                }
                logFile.write(s);
                logFile.flush();
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
    }

    /**
	 * saves the stack trace of an exception
	 *
	 * @param e Description of the Parameter
	 */
    public static void saveStackTrace(Exception e) {
        String settings = System.getProperty("user.home", ".") + File.separator + ".wlFxp" + File.separator + "logs";
        try {
            if (!new File(settings).isDirectory()) {
                new File(settings).mkdirs();
            }
            FileWriter exceptionFile = new FileWriter(settings + File.separator + "exception: " + (System.currentTimeMillis() / 1000));
            StackTraceElement[] t = e.getStackTrace();
            exceptionFile.write(e.toString() + "\n");
            for (int i = 0; i < t.length; i++) {
                exceptionFile.write(t[i].toString() + "\n");
            }
            exceptionFile.flush();
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }

    /**
	 * parses the output of a list command into an array of FtpFiles
	 *
	 * @param output output of a LIST
	 * @param ftpDir directory of the LIST
	 *
	 * @return array of FtpFiles
	 */
    public static Vector<MyFile> parseList(String output, String ftpDir) {
        String[] completeList = split(output, "\r\n");
        Vector<MyFile> files = new Vector<MyFile>(completeList.length, 100);
        MyFile tmp;
        int k = 0;
        int index;
        for (int i = 0; i < completeList.length; i++) {
            index = -1;
            int j = 0;
            int tindex = 0;
            while (j < monthsWS.length) {
                tindex = completeList[i].indexOf(monthsWS[j]);
                if ((tindex != -1) && ((index == -1) || (tindex < index))) {
                    index = tindex;
                }
                j++;
            }
            if (index == -1) {
                continue;
            }
            tmp = new MyFile("");
            files.addElement(tmp);
            if (completeList[i].indexOf(" -> ") != -1) {
                completeList[i] = completeList[i].substring(0, completeList[i].indexOf(" -> "));
            }
            tmp.setName(completeList[i].substring(completeList[i].substring(index + 10, completeList[i].length()).indexOf(" ") + 11 + index, completeList[i].length()));
            if (tmp.getName().equals(".") || tmp.getName().equals("..")) {
                files.removeElementAt(files.size() - 1);
                continue;
            }
            tmp.setSize(Long.parseLong(completeList[i].substring(completeList[i].substring(0, index).lastIndexOf(" ") + 1, index)));
            tmp.setMode(completeList[i].substring(0, 10));
            tmp.setFtpMode(true);
            tmp.setDate(parseDate(completeList[i].substring(index, index + 13)));
            if (ftpDir.equals("/")) {
                tmp.setAbsolutePath(ftpDir + tmp.getName());
            } else {
                tmp.setAbsolutePath(ftpDir + "/" + tmp.getName());
            }
            k++;
        }
        return files;
    }

    /**
	 * parses the dates of the LIST output into good looking Strings
	 *
	 * @param input Description of the Parameter
	 *
	 * @return Description of the Return Value
	 */
    private static String parseDate(String input) {
        String[] tdate = split(input, " ");
        String[] date = new String[3];
        int k = 0;
        for (int i = 0; i < tdate.length; i++) {
            if (tdate[i].equals("")) {
                continue;
            }
            date[k++] = tdate[i];
        }
        StringBuffer ret = new StringBuffer(30);
        for (int j = 0; j < 12; j++) {
            if (date[0].equals(months[j])) {
                if (j < 9) {
                    ret.append("0").append(j + 1);
                } else {
                    ret.append(j + 1);
                }
                break;
            }
        }
        int t = Integer.parseInt(date[1]);
        if (t < 10) {
            ret.append("/0").append(t);
        } else {
            ret.append("/").append(t);
        }
        if (date[2].indexOf(":") != -1) {
            ret.append(" ").append(date[2]);
        } else {
            String tmp = ret.toString();
            ret.delete(0, ret.length());
            ret.append(date[2]).append("/").append(tmp);
        }
        return ret.toString();
    }

    /**
	 * parses the dates of local files into good looking Strings
	 *
	 * @param date Description of the Parameter
	 *
	 * @return Description of the Return Value
	 */
    public static String parseDate(long date) {
        GregorianCalendar cal = new GregorianCalendar();
        int curYear;
        cal.setTime(new Date());
        curYear = cal.get(Calendar.YEAR);
        cal.setTime(new Date(date));
        StringBuffer ret = new StringBuffer(30);
        StringBuffer month = new StringBuffer(2);
        month.append(cal.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month.insert(0, "0");
        }
        StringBuffer day = new StringBuffer(2);
        day.append(cal.get(Calendar.DATE));
        if (day.length() == 1) {
            day.insert(0, "0");
        }
        StringBuffer hour = new StringBuffer(2);
        hour.append(cal.get(Calendar.HOUR_OF_DAY));
        if (hour.length() == 1) {
            hour.insert(0, "0");
        }
        StringBuffer minute = new StringBuffer(2);
        minute.append(cal.get(Calendar.MINUTE));
        if (minute.length() == 1) {
            minute.insert(0, "0");
        }
        if (curYear > cal.get(Calendar.YEAR)) {
            ret.append(cal.get(Calendar.YEAR)).append("/").append(month).append("/").append(day);
        } else {
            ret.append(month).append("/").append(day).append(" ").append(hour).append(":").append(minute);
        }
        return ret.toString();
    }

    /**
	 * rewrite of the split method from java.lang.String because it
	 * makes problems with gcj
	 *
	 * @param s Description of the Parameter
	 * @param key Description of the Parameter
	 *
	 * @return Description of the Return Value
	 */
    public static String[] split(String s, String key) {
        Vector<String> v = new Vector<String>(100, 50);
        while ((s.length() > 0) && (s.indexOf(key) != -1)) {
            v.addElement(s.substring(0, s.indexOf(key)));
            s = s.substring(s.indexOf(key) + key.length(), s.length());
        }
        v.addElement(s);
        while ((v.size() > 0) && v.elementAt(v.size() - 1).equals("")) {
            v.removeElementAt(v.size() - 1);
        }
        String[] t = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            t[i] = v.elementAt(i);
        }
        return t;
    }

    /**
	 * no more errors with parsing ints with this method
	 *
	 * @param s Description of the Parameter
	 *
	 * @return Description of the Return Value
	 */
    public static int parseInt(String s) {
        StringBuffer b = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++) {
            if (Pattern.matches("[-0-9]", s.substring(i, i + 1))) {
                b.append(s.substring(i, i + 1));
            }
        }
        return Integer.parseInt(b.toString());
    }
}
