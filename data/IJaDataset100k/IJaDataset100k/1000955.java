package common.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hyun
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Util {

    public static String checkSession(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String returnPage = "";
        if (request.getSession(false) == null) {
            returnPage = "../../lt/lc/login.l2";
        }
        return returnPage;
    }

    public static boolean checkAuth(HttpServletRequest request, HttpServletResponse response, String level) throws ServletException {
        boolean result = false;
        String login_level = (String) request.getSession(true).getAttribute("auth");
        System.out.println("level : " + login_level);
        String[] levels = level.split("\\|");
        for (int i = 0; i < levels.length; i++) {
            if (login_level.equals(levels[i])) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static String[] explode(String strSrcText, String strSplit) {
        String strSrc = (strSrcText == null) ? "" : strSrcText;
        Vector vTmp = new Vector();
        int inCnt = strSplit.length();
        int inLen = strSrc.length();
        while (inLen > 0) {
            int in_tmp = strSrc.indexOf(strSplit);
            if (in_tmp > 0) {
                vTmp.add(strSrc.substring(0, in_tmp));
                if (in_tmp + inCnt <= strSrc.length()) {
                    strSrc = strSrc.substring(in_tmp + inCnt, strSrc.length());
                } else break;
            } else if (in_tmp == 0) {
                vTmp.add("");
                if (in_tmp + inCnt <= strSrc.length()) {
                    strSrc = strSrc.substring(in_tmp + inCnt, strSrc.length());
                } else break;
            } else {
                vTmp.add(strSrc);
                break;
            }
            inLen = inLen - (in_tmp + inCnt) + 1;
        }
        int inSize = vTmp.size();
        if (inSize < 1) {
            vTmp.add(strSrcText);
            inSize = 1;
        }
        String[] arrResult = new String[inSize];
        for (int i = 0; i < inSize; i++) {
            arrResult[i] = (String) vTmp.get(i);
        }
        return arrResult;
    }

    public static String replace(String source, String str1, String str2) {
        int index, start = 0;
        StringBuffer value = new StringBuffer();
        while ((index = source.indexOf(str1, start)) != -1) {
            value.append(source.substring(start, index) + str2);
            start = index + str1.length();
        }
        if (start < source.length()) {
            value.append(source.substring(start, source.length()));
        }
        return value.toString();
    }

    public static String getStyleDt(String dt, int style) {
        if ((dt == null) || (dt.equals(""))) return "";
        if (dt.length() < 8) return dt;
        String MakeDate = null;
        switch(style) {
            case 1:
                MakeDate = dt.substring(0, 4) + "-" + dt.substring(4, 6) + "-" + dt.substring(6, 8);
                break;
            case 2:
                MakeDate = dt.substring(0, 4) + "/" + dt.substring(4, 6) + "/" + dt.substring(6, 8);
                break;
            case 3:
                MakeDate = dt.substring(0, 4) + "占쏙옙 " + dt.substring(4, 6) + "占쏙옙 " + dt.substring(6, 8) + "占쏙옙";
                break;
            case 4:
                MakeDate = dt.substring(0, 4) + "." + dt.substring(4, 6) + "." + dt.substring(6, 8);
                break;
        }
        return MakeDate;
    }

    public static void delDirectoryFile(String _path) {
        File file = new File(_path);
        String filelist[] = file.list();
        for (int i = 0; i < filelist.length; i++) {
            File newfile = new File(file, filelist[i]);
            if (newfile.isFile()) {
                newfile.delete();
            }
        }
    }

    /**
	 * 占쏙옙; 占쏙옙트占쏙옙8占쏙옙 占쏙옙환..
	 * @param str
	 * @return
	 */
    public static String nullToString(String str) {
        if ((str == null) || (str.trim().length() < 1)) str = "&nbsp;";
        return str;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.length() < 1) return true;
        return false;
    }

    public static String plus_zero(String str, int cou) {
        int len = cou - str.length();
        for (; len > 0; len--) {
            str = "0" + str;
        }
        return str;
    }

    public static String nullToZero(String str) {
        if (str == null || "".equals(str)) str = "0";
        return str;
    }

    public static String[] getNowWeekMinus(int j, String _sFormat, String _nowdate) {
        String[] sReturnWeeks = new String[j];
        Calendar[] returnCals = new Calendar[j];
        Calendar cale = Calendar.getInstance();
        cale.clear();
        int iAddType = 0;
        if (_sFormat.indexOf("W") >= 0 || _sFormat.indexOf("w") >= 0) {
            int iYear = Integer.parseInt(_nowdate.substring(0, 4));
            int iMonth = Integer.parseInt(_nowdate.substring(4, 6));
            int iDay = Integer.parseInt(_nowdate.substring(6, 8));
            cale.set(Calendar.YEAR, iYear);
            cale.set(Calendar.MONTH, (iMonth - 1));
            cale.set(Calendar.DATE, iDay);
            cale.add(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - cale.get(Calendar.DAY_OF_WEEK));
            cale.add(Calendar.WEEK_OF_YEAR, -(j - 1));
            iAddType = Calendar.WEEK_OF_YEAR;
        } else {
            int iYear = Integer.parseInt(_nowdate.substring(0, 4));
            int iMonth = Integer.parseInt(_nowdate.substring(4, 6));
            int iWeek = Integer.parseInt(_nowdate.substring(6, 8));
            cale.set(Calendar.YEAR, iYear);
            cale.set(Calendar.MONTH, (iMonth - 12));
            iAddType = Calendar.MONTH;
        }
        for (int i = 0; i < j; i++) {
            sReturnWeeks[i] = getCalToString(_sFormat, (Calendar) cale.clone());
            cale.add(iAddType, 1);
        }
        return sReturnWeeks;
    }

    public static String getCalToString(String sFormat, Calendar cal) {
        Calendar calendar;
        if (cal == null) return ""; else calendar = (Calendar) cal.clone();
        if (sFormat.indexOf("W") >= 0 || sFormat.indexOf("w") >= 0) {
            Calendar calTemp = (Calendar) cal.clone();
            calTemp.set(Calendar.DATE, 1);
            if (calTemp.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - calendar.get(Calendar.DAY_OF_WEEK));
                calendar.add(Calendar.WEEK_OF_MONTH, -1);
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(sFormat);
        java.util.Date date = calendar.getTime();
        String sReturn = dateFormat.format(date);
        return sReturn;
    }

    public static String getParam(HttpServletRequest request, String param) {
        if (request.getParameter(param) == null) {
            return "";
        } else {
            return request.getParameter(param);
        }
    }

    public static String getParam(HttpServletRequest request, String param, String strDefault) {
        if (request.getParameter(param) == null || (request.getParameter(param).equalsIgnoreCase(""))) {
            return strDefault;
        } else {
            return request.getParameter(param);
        }
    }

    public static Object getAttribute(HttpServletRequest request, String param) {
        if (request.getAttribute(param) == null) {
            return "";
        } else {
            return request.getAttribute(param);
        }
    }

    public static Object getAttribute(HttpServletRequest request, String param, String strDefault) {
        if (request.getAttribute(param) == null) {
            return strDefault;
        } else {
            return request.getAttribute(param);
        }
    }

    public static String getWQRootPath() {
        String sLicensePath = "";
        String sBasePath = System.getProperty("catalina.base");
        String sClassPath = new Util().getClass().getClassLoader().getResource("").getPath().trim();
        sClassPath = sClassPath.substring(sClassPath.indexOf("webapps") - 1, sClassPath.indexOf("WEB-INF") - 1);
        sClassPath = sBasePath + sClassPath;
        if (sClassPath.indexOf("\\") != -1) {
            sClassPath = com.innerbus.basis.util.Inner_StringUtil.replaceString(sClassPath, "\\", "/");
        }
        File fPath = new File(sClassPath);
        sLicensePath = fPath.getAbsolutePath();
        return sLicensePath;
    }

    public static ArrayList fileListDatecompared() {
        FileFilter noDirectories = new FileFilter() {

            public boolean accept(File f) {
                return !f.isDirectory();
            }
        };
        Comparator<File> descendingOnModificationDate = new Comparator<File>() {

            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                int returnValue;
                if (diff < 0L) {
                    returnValue = -1;
                } else if (diff > 0L) {
                    returnValue = +1;
                } else {
                    assert diff == 0L;
                    returnValue = 0;
                }
                return -returnValue;
            }
        };
        String csvPath = Util.getWQRootPath() + File.separator + "temp" + File.separator + "export";
        File directory = new File(csvPath);
        File[] filesInDirectory = directory.listFiles(noDirectories);
        Arrays.sort(filesInDirectory, descendingOnModificationDate);
        ArrayList a = new ArrayList();
        for (File file : filesInDirectory) {
            a.add(file);
        }
        ArrayList csvNames = new ArrayList();
        for (int j = 0; j < a.size(); j++) {
            String csvfilename = a.get(j).toString();
            String ext = csvfilename.substring(csvfilename.lastIndexOf(File.separator));
            String tp = "";
            StringTokenizer token11 = new StringTokenizer(ext, File.separator);
            while (token11.hasMoreTokens()) {
                for (int i = 0; i < token11.countTokens(); i++) {
                    tp = token11.nextToken();
                    csvNames.add(tp);
                }
            }
        }
        return csvNames;
    }

    public static String getSystemDecodedString(String str) {
        String enc = "";
        String decodedString = "";
        if (str == null || str.equals("")) {
            return "";
        }
        try {
            FileWriter fileWriter = new FileWriter("out");
            enc = fileWriter.getEncoding();
            decodedString = URLDecoder.decode(str, enc);
            return decodedString;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("common.util.Util.getSystemDecodedString() " + ex);
            return str;
        }
    }
}
