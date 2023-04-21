package com.ma_la.myRunning;

import com.ma_la.myRunning.domain.PageTraceType;
import com.ma_la.myRunning.domain.Runner;
import com.ma_la.myRunning.domain.Country;
import com.ma_la.util.Constants;
import com.ma_la.myRunning.domain.EmailTypeRunner;
import com.ma_la.myRunning.manager.MyRunningManager;
import java.awt.Color;
import java.sql.ResultSet;
import java.util.*;
import java.text.*;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * UtilityBean fuer myRunning Projekt
 *
 * @author <a href="mailto:mail@myrunning.de">Martin Lang</a>
 */
public class RunningSystemBean implements Serializable {

    private static final long serialVersionUID = 8862825796929591028L;

    public RunningMasterBean runningMasterBean;

    private static org.apache.log4j.Logger log = Logger.getLogger(RunningSystemBean.class);

    public void setRunningMasterBean(RunningMasterBean mbIn) {
        this.runningMasterBean = mbIn;
    }

    public RunningMasterBean getRunningMasterBean() {
        return runningMasterBean;
    }

    public Date getActualDate() {
        Calendar cal = getCalendar(new Date());
        return cal.getTime();
    }

    public static Date getActualDateS() {
        Calendar cal = getCalendarS(new Date());
        return cal.getTime();
    }

    public Calendar getCalendar(Date date) {
        Calendar cal = getCalendar();
        cal.setTime(date);
        return cal;
    }

    public static Calendar getCalendarS() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("CET"));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }

    public static Calendar getCalendarStartOfDay(Date date) {
        Calendar cal = getCalendarS(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar getCalendarEndOfDay(Date date) {
        Calendar cal = getCalendarS(date);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal;
    }

    public static Calendar getCalendarS(Date date) {
        Calendar cal = getCalendarS();
        cal.setTime(date);
        return cal;
    }

    public Calendar getCalendar() {
        return getCalendar(runningMasterBean.getLocale());
    }

    public Calendar getCalendar(Locale locale) {
        Calendar cal = Calendar.getInstance(locale);
        cal.setTimeZone(TimeZone.getTimeZone("CET"));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }

    public static int getYearOfWeek(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        if (week == 1 && dayOfMonth > 20) return year + 1;
        if (week >= 52 && dayOfMonth < 10) return year - 1;
        return year;
    }

    /**
	 * Methode ueberprueft, ob im Request ein bestimmter Parameter drin war. Wenn ja, gibt sie diesen
	 * zurueck, ansonsten null
	 *
	 * @param strParamIn: dieser Parameter soll ueberprueft werden
	 * @return Parameterwert oder null, wenn Parameter nicht vorhanden
	 */
    public String getReqParam(String strParamIn) {
        try {
            if (runningMasterBean.getRequest() == null) {
                log.error("RunningSystemBean.getReqParam: Kein Request vorhanden!");
                return null;
            } else if (runningMasterBean.getRequest().getParameter(strParamIn) != null) {
                String r = runningMasterBean.getRequest().getParameter(strParamIn);
                r = r.replace('\'', '_');
                return r.trim();
            }
        } catch (NullPointerException e) {
            log.error("RunningSystemBean Methode getReqParam fuer Parameter >" + strParamIn + "<: NullPointerException!", e);
        }
        return null;
    }

    public static String getReqParam(String strParamIn, HttpServletRequest request) {
        try {
            if (request == null) {
                return null;
            } else if (request.getParameter(strParamIn) != null) {
                String r = request.getParameter(strParamIn);
                r = r.replace('\'', '_');
                return r.trim();
            }
        } catch (NullPointerException e) {
            log.error("RunningSystemBean Methode getReqParam fuer Parameter >" + strParamIn + "<: NullPointerException!", e);
        }
        return null;
    }

    /**
	 * holt alle Anfrage-Parameter aus dem Request und haengt diese per & in nen String
	 *
	 * @return String mit dem Query-String
	 */
    public String getAllRequestParameters() {
        return getAllRequestParameters(false);
    }

    /**
	 * holt alle Anfrage-Parameter aus dem Request und schreibt diese in nen String
	 *
	 * @param addByQuestionmark: true = erster Parameter per ? angehaengt; false = erster Parameter per & angehaengt
	 * @return String mit dem Query-String
	 */
    public String getAllRequestParameters(boolean addByQuestionmark) {
        if (runningMasterBean == null || runningMasterBean.getRequest() == null || runningMasterBean.getRequest().getParameterNames() == null) return "";
        return getAllRequestParameters(addByQuestionmark, runningMasterBean.getRequest());
    }

    /**
	 * holt alle Anfrage-Parameter aus dem Request und schreibt diese in nen String
	 *
	 * @param addByQuestionmark: true = erster Parameter per ? angehaengt; false = erster Parameter per & angehaengt
	 * @return String mit dem Query-String
	 */
    public static String getAllRequestParameters(boolean addByQuestionmark, HttpServletRequest request) {
        if (request == null) return "";
        String strTmp = "";
        String strTmpAttribute;
        Enumeration enumParams = request.getParameterNames();
        while (enumParams.hasMoreElements()) {
            strTmpAttribute = (String) enumParams.nextElement();
            if (strTmp.length() == 0 && addByQuestionmark) {
                strTmp = "?";
            } else {
                strTmp += "&";
            }
            strTmp += strTmpAttribute + "=" + request.getParameter(strTmpAttribute);
        }
        return strTmp;
    }

    public String getAllRequestParameters(boolean addByQuestionmark, String[] dropParams) {
        return getAllRequestParameters(addByQuestionmark, runningMasterBean.getRequest(), dropParams);
    }

    /**
	 * holt alle Anfrage-Parameter aus dem Request und schreibt diese in nen String
	 *
	 * @param addByQuestionmark: true = erster Parameter per ? angehaengt; false = erster Parameter per & angehaengt
	 * @param dropParams:        diese Parameter werden nicht mit zurueckgegeben
	 * @return String mit dem Query-String
	 */
    public static String getAllRequestParameters(boolean addByQuestionmark, HttpServletRequest request, String[] dropParams) {
        String strTmp = "";
        String strTmpAttribute;
        Enumeration enumParamNames = request.getParameterNames();
        boolean appendParam;
        while (enumParamNames.hasMoreElements()) {
            strTmpAttribute = (String) enumParamNames.nextElement();
            appendParam = true;
            for (String dropParam : dropParams) {
                if (strTmpAttribute.equals(dropParam)) {
                    appendParam = false;
                    break;
                }
            }
            if (appendParam) {
                if (strTmp.length() == 0 && addByQuestionmark) {
                    strTmp = "?";
                } else {
                    strTmp += "&";
                }
                strTmp += strTmpAttribute + "=" + request.getParameter(strTmpAttribute);
            }
        }
        return strTmp;
    }

    /**
	 * traegt in die DB die aktuelle Aktion des Nutzers ein
	 *
	 * @param fk_ptp: diese Seite wurde aufgerufen
	 * @return 0 = fehler 1 = ok
	 */
    public int insertPageTrace(int fk_ptp) {
        if (false) return 0;
        if (runningMasterBean == null) return 0;
        PageTraceType pageTraceType = MyRunningManager.getInstance().getPageTraceTypeById((long) fk_ptp);
        Runner runner = null;
        if (runningMasterBean.getAdminBean() != null) runner = runningMasterBean.getAdminBean().getRunner(); else if (runningMasterBean.getRunnerBean() != null) runner = runningMasterBean.getRunnerBean();
        String messages = "";
        for (String message : runningMasterBean.getInfoMessages()) {
            messages += message + "; ";
        }
        for (String message : runningMasterBean.getErrorMessages()) {
            messages += message + "; ";
        }
        MyRunningManager.getInstance().insertPageTrace(runningMasterBean.getRequest().getRemoteAddr(), pageTraceType, runningMasterBean.getRequest().getSession().getId(), runner, runningMasterBean.getRequest().getHeader("referer"), getAllRequestParameters(true), messages);
        return 1;
    }

    /**
	 * Formatiert ein uebergebenes Datums-Objekt in das Format yyyy-MM-dd HH:mm:ss
	 *
	 * @param dateAndTime Dieses Datum soll formatiert werden
	 * @return String
	 */
    public static String formatEnglishDBDateAndTime(Date dateAndTime) {
        DateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (dfmt.format(dateAndTime));
    }

    /**
	 * erzeugt ein Zufallspasswort der L�nge 8
	 *
	 * @return Passwort als String
	 */
    public String getRandomPassword8() {
        return getRandomPassword(8);
    }

    /**
	 * erzeugt ein neues Passwort der Laenge intLength
	 *
	 * @param intLength: Laenge des gewuenschten Passwortes
	 * @return neues Passwort als String
	 */
    public String getRandomPassword(int intLength) {
        String[] chars = { "a", "b", "c", "d", "e", "f", "g", "h", "R", "j", "k", "T", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "X", "_", "-", "A", "D", "H", "K", "L", "Y" };
        Random r = new Random(System.currentTimeMillis());
        String strPassword = "";
        while (strPassword.length() < intLength) {
            int j = r.nextInt(44);
            try {
                strPassword += chars[j];
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return strPassword;
    }

    public static String getSMTPHostMyRunning() {
        return "mail.myrunning.de";
    }

    public static String getPopHostMyRunning() {
        return "mail.myrunning.de";
    }

    public static String getSMTPUserMyRunning() {
        return Constants.EmailAddresses.MAIL_MYRUNNING;
    }

    public static String getSMTPUserNewsletterMyRunning() {
        return Constants.EmailAddresses.NEWSLETTER_MYRUNNING;
    }

    /**
	 * @return mail@myrunning.de
	 */
    public static String getSenderMyRunning() {
        return Constants.EmailAddresses.MAIL_MYRUNNING;
    }

    /**
	 * @return error@myrunning.de
	 */
    public static String getErrorSenderMyRunning() {
        return Constants.EmailAddresses.ERROR_MYRUNNING;
    }

    /**
	 * @return error@myrunning.de
	 */
    public static String getAnfrageSenderMyRunning() {
        return Constants.EmailAddresses.ANFRAGE_MYRUNNING;
    }

    /**
	 * @return newsletter@myrunning.de
	 */
    public static String getNewsletterSenderMyRunning() {
        return Constants.EmailAddresses.NEWSLETTER_MYRUNNING;
    }

    public static String getSMTPPasswordMyRunning() {
        return "mar94lan";
    }

    /**
	 * schliesst das uebergebene ResultSet, dessen Satatement und die Connection
	 *
	 * @param resultSet dieses RS schliessen
	 * @param caller	von wo wurde die Methode gerufen
	 */
    public static void closeResultSet(final ResultSet resultSet, String caller) {
        try {
            resultSet.getStatement().getConnection().close();
        } catch (Exception e) {
            log.error("Exception in RunningSystemBean: close Connection! caller: " + caller, e);
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                log.error("Exception in RunningSystemBean: close ResultSet! caller: " + caller, e);
            }
        }
    }

    /**
	 * prueft, ob der uebergebene Wert ein Double ist
	 *
	 * @param value diesen Wert pruefen
	 * @return true, wenn ja; false wenn nein
	 */
    public static boolean checkValueForDouble(String value) {
        try {
            new Double(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
	 * prueft, ob der uebergebene Wert ein Integer ist
	 *
	 * @param value diesen Wert pruefen
	 * @return true, wenn ja; false wenn nein
	 */
    public static boolean checkValueForInteger(String value) {
        try {
            new Integer(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
	 * prueft, ob der uebergebene Wert ein Long ist
	 *
	 * @param value diesen Wert pruefen
	 * @return true, wenn ja; false wenn nein
	 */
    public static boolean checkValueForLong(String value) {
        try {
            new Long(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
	 * holt das aktuelle Datum im dt. Format
	 *
	 * @return String
	 */
    public static String getGermanDate() {
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);
            return df.format(new Date());
        } catch (Exception e) {
            log.error("Exception in der RunningSystemBean, Methode getGermanDate", e);
            return "";
        }
    }

    /**
	 * holt das aktuelle Datum im dt. Format
	 *
	 * @return String
	 */
    public static String getGermanDateAndTime() {
        try {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN);
            return df.format(new Date());
        } catch (Exception e) {
            log.error("Exception in der RunningSystemBean, Methode getGermanDateAndTime", e);
            return "";
        }
    }

    /**
	 * formatiert ein uebergebenes Datum im Format dd.mm.yyyy ins engl.Format yyyy-mm-dd fuer mySQL
	 *
	 * @param datum: Datum, das umgewandelt werden soll
	 * @return String
	 */
    public static String formatToEnglishDate(String datum) {
        DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dfmt.parse(datum);
            return (sdf.format(date));
        } catch (ParseException e) {
            log.error("ParseException in der RunningSystemBean, Methode formatToEnglishDate", e);
        } catch (NullPointerException ne) {
            log.error("NullPointerException in der RunningSystemBean, Methode formatToEnglishDate", ne);
        }
        return "";
    }

    /**
	 * Formats the InputStream (Date and Time) into a dd.MM.yyyy HH:mm:ss formated String
	 *
	 * @param strDateAndTime dieses Datum soll umformattiert werden
	 * @return String
	 */
    public static String formatEnglishDBDateAndTime(String strDateAndTime) {
        if (strDateAndTime == null || strDateAndTime.length() == 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            Date date = sdf.parse(strDateAndTime);
            return (dfmt.format(date));
        } catch (ParseException e) {
            log.error("ParseException in der RunningSystemBean, Methode formatEnglishDBDateAndTime", e);
        } catch (NullPointerException ne) {
            log.error("NullPointerException in der RunningSystemBean, Methode formatEnglishDBDateAndTime", ne);
        }
        return "";
    }

    /**
	 * Formats the InputStream (Date and Time) into a dd.MM.yyyy HH:mm:ss formated String
	 *
	 * @param strDateAndTime dieses Datum soll umformattiert werden
	 * @return String
	 */
    public static String formatToEnglishDBDateAndTime(String strDateAndTime) {
        if (strDateAndTime == null || strDateAndTime.length() == 0) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        DateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(strDateAndTime);
            return dfmt.format(date);
        } catch (ParseException e) {
            log.error("ParseException in der RunningSystemBean, Methode formatToEnglishDBDateAndTime", e);
        } catch (NullPointerException ne) {
            log.error("NullPointerException in der RunningSystemBean, Methode formatToEnglishDBDateAndTime", ne);
        }
        return "";
    }

    /**
	 * Formats the InputStream (Date and Time) into a dd.MM.yyyy HH:mm:ss formated String
	 *
	 * @param strDateAndTime dieses Datum soll umformattiert werden
	 * @return String
	 */
    public static String formatEnglishDBTime(String strDateAndTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dfmt = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = sdf.parse(strDateAndTime);
            return dfmt.format(date);
        } catch (ParseException e) {
            log.error("ParseException in der RunningSystemBean, Methode formatEnglishDBTime", e);
        } catch (NullPointerException ne) {
            log.error("NullPointerException in der RunningSystemBean, Methode formatEnglishDBTime", ne);
        }
        return "";
    }

    /**
	 * Formatiert ein uebergebenes Datums-Objekt in das Format HH:mm:ss
	 *
	 * @param dateAndTime dieses Datum soll umformattiert werden
	 * @return String
	 */
    public static String formatEnglishDBTime(Date dateAndTime) {
        return formatDate(dateAndTime, "HH:mm:ss");
    }

    /**
	 * Formatiert ein uebergebenes Datums-Objekt in das Format yyyy-MM-dd
	 *
	 * @param date dieses Datum soll umformattiert werden
	 * @return String
	 */
    public static String formatToEnglishDBDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
	 * formatiert das uebergebene Datum im Format yyyy-mm-dd ins dt. Format dd.mm.yyyy
	 *
	 * @param strDatum Input-Datum
	 * @return String
	 */
    public static String formatToDateGerman(String strDatum) {
        if (strDatum != null && strDatum.length() == 10) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date date = sdf.parse(strDatum);
                return dfmt.format(date);
            } catch (ParseException e) {
                log.error("ParseException in der RunningSystemBean, Methode formatToDateGerman", e);
            } catch (NullPointerException ne) {
                log.error("NullPointerException in der RunningSystemBean, Methode formatToDateGerman", ne);
            }
        }
        return "";
    }

    /**
	 * formatiert das uebergebene Datum ins dt. Format dd.mm.yyyy
	 *
	 * @param datum dieses Datum soll umformattiert werden
	 * @return String
	 */
    public static String formatToDateGerman(Date datum) {
        return formatDate(datum, "dd.MM.yyyy");
    }

    /**
	 * formatiert das uebergebene Datum ins dt. Format dd.mm.yyyy HH:mm
	 *
	 * @param datum dieses Datum soll umformattiert werden
	 * @return String
	 */
    public static String formatToDateAndShortTimeGerman(Date datum) {
        return formatDate(datum, "dd.MM.yyyy HH:mm");
    }

    /**
	 * formatiert das uebergebene Datum ins dt. Format dd.mm.yyyy HH:mm:ss
	 *
	 * @param datum dieses Datum soll umformattiert werden
	 * @return String
	 */
    public static String formatToDateAndTimeGerman(Date datum) {
        return formatDate(datum, "dd.MM.yyyy HH:mm:ss");
    }

    /**
	 * formatiert das uebergebene Datum ins uebergebene Format
	 *
	 * @param date dieses Datum soll umformattiert werden
	 * @return String
	 */
    private static String formatDate(Date date, String pattern) {
        if (date != null) {
            DateFormat dfmt = new SimpleDateFormat(pattern);
            try {
                return dfmt.format(date);
            } catch (NullPointerException ne) {
                log.error("NullPointerException in der RunningSystemBean, Methode formatDate", ne);
            }
        }
        return "";
    }

    /**
	 * formatiert das uebergebene Datum ins dt. Format HH:mm:ss
	 *
	 * @param date dieses Datum soll umformattiert werden
	 * @return String
	 */
    public static String formatToTimeGerman(Date date) {
        return formatDate(date, "HH:mm");
    }

    /**
	 * macht aus dd.MM.yyyy ein Date Objekt
	 *
	 * @param germanDateIn Datum im Format dd.MM.yyyy
	 * @return Date
	 */
    public static Date getDateFromGerman(String germanDateIn) {
        if (germanDateIn == null || germanDateIn.length() == 0) {
            return null;
        }
        DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy");
        try {
            dfmt.setLenient(false);
            return dfmt.parse(germanDateIn);
        } catch (ParseException e) {
            log.error("ParseException in der SystemBean, Methode getDateFromGerman", e);
            return null;
        } catch (NullPointerException ne) {
            log.error("NullPointerException in der RunningSystemBean, Methode getDateFromGerman", ne);
            return null;
        }
    }

    /**
	 * macht aus dd.MM.yyyy HH:mm:ss ein Date Objekt
	 *
	 * @param germanDateAndTimeIn Datum im Format dd.MM.yyyy HH:mm:ss
	 * @return Date
	 */
    public static Date getDateAndTimeFromGerman(String germanDateAndTimeIn) {
        if (germanDateAndTimeIn == null || germanDateAndTimeIn.length() == 0) {
            return null;
        }
        DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            dfmt.setLenient(false);
            return dfmt.parse(germanDateAndTimeIn);
        } catch (ParseException e) {
            log.error("ParseException in der RunningSystemBean, Methode getDateAndTimeFromGerman", e);
            return null;
        } catch (NullPointerException ne) {
            log.error("NullPointerException in der RunningSystemBean, Methode getDateAndTimeFromGerman", ne);
            return null;
        }
    }

    /**
	 * macht aus yyyy-MM-dd ein Date-Objekt
	 *
	 * @param englischDateIn im Format yyyy-MM-dd
	 * @return Date
	 */
    public static Date getDateFromEnglish(String englischDateIn) {
        DateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dfmt.setLenient(false);
            return dfmt.parse(englischDateIn);
        } catch (ParseException e) {
            log.error("ParseException in der RunningSystemBean, Methode getDateFromEnglish", e);
            return null;
        }
    }

    /**
	 * prueft, ob ein Datum im Format dd.MM.yyyy uebergeben wurde
	 *
	 * @param strDatumIn dieses Datum soll ueberprueft werden
	 * @return boolean
	 */
    public static boolean checkForGermanDateFormat(String strDatumIn) {
        if (strDatumIn.length() != 10) {
            return false;
        }
        DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy");
        try {
            dfmt.setLenient(false);
            dfmt.parse(strDatumIn).toString();
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
	 * prueft, ob ein Datum im Format dd.MM.yyyy HH:mm:ss uebergeben wurde
	 *
	 * @param strDatumIn dieses Datum soll ueberprueft werden
	 * @return boolean
	 */
    public static boolean checkForGermanDateFormatAndTime(String strDatumIn) {
        if (strDatumIn.length() != 19) {
            return false;
        }
        DateFormat dfmt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            dfmt.setLenient(false);
            dfmt.parse(strDatumIn).toString();
            return true;
        } catch (ParseException e) {
            log.error("ParseException in der RunningSystemBean, Methode checkForGermanDateFormatAndTime", e);
            return false;
        }
    }

    /**
	 * prueft, ob ein Wert im Format h:mm:ss oder h:mm:ss,SS uebergeben wurde
	 *
	 * @param value dieser Wert soll ueberprueft werden
	 * @return boolean: false, wenn nicht im korrekten format; sonst true
	 */
    public static boolean checkForFinishTimeFormat(String value) {
        if (value.length() != 7) {
            if (value.length() != 10) return false;
        }
        DateFormat dfmt = new SimpleDateFormat("h:mm:ss");
        try {
            dfmt.setLenient(false);
            dfmt.parse(value).toString();
            return true;
        } catch (ParseException e) {
            log.error("ParseException (Format: h:mm:ss; Value: " + value + ") in der RunningSystemBean, Methode checkForFinishTimeFormat", e);
        }
        dfmt = new SimpleDateFormat("h:mm:ss,SS");
        try {
            dfmt.setLenient(false);
            dfmt.parse(value).toString();
            return true;
        } catch (ParseException e) {
            log.error("ParseException (Format: h:mm:ss,SS; Value: " + value + ") in der RunningSystemBean, Methode checkForFinishTimeFormat", e);
        }
        return false;
    }

    /**
	 * formattiert eine uebergebene Anzahl an Minuten in einen String im Format x h y min z s  oder -, wenn 0
	 *
	 * @param duration die umformatiert werden sollen
	 * @return String
	 */
    public static String formatMinToHourAndMinAndSecondStatic(Double duration) {
        if (duration == 0) {
            return "-";
        }
        int hour = getHourFromMin(duration.intValue());
        String hourAndMin = "";
        if (hour > 0) {
            hourAndMin += hour + ":";
        }
        int min = getMinFromMin(duration.intValue());
        if (min > 0) {
            hourAndMin += min + ":";
        }
        int seconds = getSecondsFromDuration(duration);
        if (seconds > 0) {
            hourAndMin += seconds + ":";
        }
        return hourAndMin;
    }

    /**
	 * formattiert eine uebergebene Anzahl an Minuten in einen String im Format x h y min z s  oder -, wenn 0
	 *
	 * @param duration die umformatiert werden sollen
	 * @return String
	 */
    public String formatMinToHourAndMinAndSecond(Double duration) {
        if (duration == 0) {
            return "-";
        }
        int hour = getHourFromMin(duration.intValue());
        String hourAndMin = "";
        if (hour > 0) {
            hourAndMin += hour + " " + getLocalizedText("hour.short") + " ";
        }
        int min = getMinFromMin(duration.intValue());
        if (min > 0) {
            hourAndMin += min + " " + getLocalizedText("min.short");
        }
        int seconds = getSecondsFromDuration(duration);
        if (seconds > 0) {
            hourAndMin += " " + seconds + " " + getLocalizedText("second_short");
        }
        return hourAndMin;
    }

    /**
	 * formattiert eine uebergebene Anzahl an Minuten in einen String im Format h:min:s oder -, wenn 0
	 *
	 * @param duration die umformatiert werden sollen
	 * @return String
	 */
    public String formatMinToFinisherTime(Double duration) {
        if (duration == 0) {
            return "-";
        }
        int hour = getHourFromMin(duration.intValue());
        String hourAndMin = "";
        if (hour > 0) {
            hourAndMin += hour;
        } else hourAndMin += "0";
        int min = getMinFromMin(duration.intValue());
        if (min >= 10) {
            hourAndMin += ":" + min;
        } else hourAndMin += ":0" + min;
        int seconds = getSecondsFromDuration(duration);
        if (seconds >= 10) {
            hourAndMin += ":" + seconds;
        } else hourAndMin += ":0" + seconds;
        return hourAndMin;
    }

    /**
	 * wie viele Stunden sind in den uebergebenen Minuten enthalten
	 *
	 * @param min Minuten
	 * @return int
	 */
    public static int getHourFromMin(int min) {
        return min / 60;
    }

    /**
	 * wie viele Tage sind in den uebergebenen Minuten enthalten
	 *
	 * @param min Minuten
	 * @return int
	 */
    public static int getDaysFromMin(int min) {
        return (min / (60 * 24));
    }

    /**
	 * wie viele Rest-Minuten sind in den uebergebenen Minuten enthalten
	 *
	 * @param min Minuten
	 * @return int
	 */
    public static int getMinFromMin(int min) {
        return min % 60;
    }

    /**
	 * wie viele Sekunden sind in der uebergebenen Dauer enthalten
	 *
	 * @param duration Dauer 38,5 fuer 38:30
	 * @return double
	 */
    public static int getSecondsFromDuration(double duration) {
        duration = round(duration, 2);
        if (String.valueOf(duration).indexOf(",") > 0) {
            String[] parts = String.valueOf(duration).split("\\,");
            String seconds = parts[1];
            if (seconds.length() == 1) seconds += "0";
            return ((Integer.parseInt(seconds) * 6) / 10);
        } else if (String.valueOf(duration).indexOf(".") > 0) {
            String[] parts = String.valueOf(duration).split("\\.");
            String seconds = parts[1];
            if (seconds.length() == 1) seconds += "0";
            return ((Integer.parseInt(seconds) * 6) / 10);
        }
        return 0;
    }

    public static Collection getCollection(Set set) {
        if (set == null || set.size() == 0) return Collections.EMPTY_LIST;
        return new ArrayList(set);
    }

    /**
	 * todo
	 *
	 * @param set
	 * @return
	 */
    public static Collection getIDCollection(Set set) {
        if (set == null) return Collections.EMPTY_LIST;
        Iterator iter = set.iterator();
        Collection coll = new ArrayList();
        while (iter.hasNext()) {
            coll.add(((IdNameInterface) iter.next()).getId());
        }
        return coll;
    }

    /**
	 * erzeugt aus einer Liste eine Hashtable mit Integern als Key
	 *
	 * @param list diese Liste in eine Hashtable wandeln
	 * @return Hashtable
	 */
    public static Hashtable getHashTable(List list) {
        Hashtable ht = new Hashtable();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ht.put(i, list.get(i));
            }
        }
        return ht;
    }

    /**
	 * erzeugt aus einem Set eine Hashtable mit Integern als Key
	 *
	 * @param set dieses Set in ne Hashtable wandeln
	 * @return Hashtable
	 */
    public static Hashtable getHashTable(Set set) {
        Hashtable ht = new Hashtable();
        List list = (List) getCollection(set);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ht.put(i, list.get(i));
            }
        }
        return ht;
    }

    /**
	 * liefert den poassenden Google Maps key zurueck; je nachdem ob lokal oder produktive Umgebung
	 *
	 * @return key fuer Google Maps
	 */
    public String getGoogleKey() {
        if (runningMasterBean.isDevelopment()) return Constants.GoogleMap_Keys.LOCAL;
        return Constants.GoogleMap_Keys.PRODUCTIVE_HOSTEUROPE;
    }

    /**
	 * holt den lokalisierten Text zum uebergebenen Key aus den property.files
	 *
	 * @param key key des Textes
	 * @return lokalisierter Text als String
	 */
    public String getLocalizedText(final String key) {
        Locale locale = runningMasterBean.getLocale();
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("scripttext", locale);
            return bundle.getString(key);
        } catch (java.util.MissingResourceException e) {
            return "[" + key + "]";
        }
    }

    /**
	 * holt den lokalisierten Text zum uebergebenen Key aus den property.files
	 *
	 * @param key key des Textes
	 * @return lokalisierter Text als String
	 */
    public static String getLocalizedText(Locale locale, final String key) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("scripttext", locale);
            return bundle.getString(key);
        } catch (java.util.MissingResourceException e) {
            return "[" + key + "]";
        }
    }

    /**
	 * holt den lokalisierten Text zum uebergebenen Key aus den property.files und ersetzt Platzhalter mit den Werten
	 * aus dem uebergebenem Array
	 *
	 * @param key	key des Textes
	 * @param params diese Werte an Stelle der Platzhalter im Text setzen
	 * @return lokalisierter Text als String
	 */
    public static String getLocalizedTextStatic(Locale locale, final String key, final Object[] params) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("scripttext", locale);
            String bundleValue = bundle.getString(key);
            return MessageFormat.format(bundleValue, params);
        } catch (java.util.MissingResourceException e) {
            return "[" + key + "]";
        }
    }

    /**
	 * holt den lokalisierten Text zum uebergebenen Key aus den property.files und ersetzt Platzhalter mit den Werten
	 * aus dem uebergebenem Array
	 *
	 * @param key	key des Textes
	 * @param params diese Werte an Stelle der Platzhalter im Text setzen
	 * @return lokalisierter Text als String
	 */
    public String getLocalizedText(final String key, final Object[] params) {
        Locale locale = runningMasterBean.getLocale();
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("scripttext", locale);
            String bundleValue = bundle.getString(key);
            return MessageFormat.format(bundleValue, params);
        } catch (java.util.MissingResourceException e) {
            return "[" + key + "]";
        }
    }

    /**
	 * holt den lokalisierten Text zum uebergebenen Key aus den property.files und ersetzt Platzhalter mit den Werten
	 * aus dem uebergebenem Array
	 *
	 * @param key	key des Textes
	 * @param params diese Werte an Stelle der Platzhalter im Text setzen
	 * @return lokalisierter Text als String
	 */
    public String getLocalizedText(Locale locale, final String key, final Object[] params) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("scripttext", locale);
            String bundleValue = bundle.getString(key);
            return MessageFormat.format(bundleValue, params);
        } catch (java.util.MissingResourceException e) {
            return "[" + key + "]";
        }
    }

    /**
	 * gibt blank anstelle von null zurueck oder den uebergebenen Wert
	 *
	 * @param value diesen Wert formattieren
	 * @return String
	 */
    public static String getStringValue(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    /**
	 * gibt blank anstelle von null zurueck oder den uebergebenen Wert
	 *
	 * @param value diesen Wert formattieren
	 * @return String
	 */
    public static String getStringValue(Double value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
	 * gibt blank anstelle von null zurueck oder den uebergebenen Wert
	 *
	 * @param value diesen Wert formattieren
	 * @return String
	 */
    public static String getStringValue(Integer value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
	 * Methode ueberprueft, ob im Request ein bestimmter Parameter drin war. Wenn ja, gibt sie diesen
	 * als <code>String</code> zurueck, ansonsten den uebergebenen Defaultwert
	 *
	 * @param strParamIn:   dieser Parameter soll ueberprueft werden
	 * @param strDefaultIn: falls der Parameter nicht im Request ist, dann den Defaultwert zurueckgeben
	 * @return String
	 */
    public String getReqParam(String strParamIn, String strDefaultIn) {
        String strTmp = getReqParam(strParamIn);
        if (strTmp != null) {
            return strTmp.trim();
        }
        return strDefaultIn;
    }

    /**
	 * Methode ueberprueft, ob im Request ein bestimmter Parameter drin war. Wenn ja, gibt sie diesen
	 * als <code>String</code> zurueck, ansonsten den uebergebenen Defaultwert
	 *
	 * @param strParamIn:   dieser Parameter soll ueberprueft werden
	 * @param intDefaultIn: falls der Parameter nicht im Request ist, dann den Defaultwert zurueckgeben
	 * @return String
	 */
    public String getReqParam(String strParamIn, Integer intDefaultIn) {
        String strTmp = getReqParam(strParamIn);
        if (strTmp != null) {
            return strTmp.trim();
        } else if (intDefaultIn != null) return intDefaultIn.toString();
        return "";
    }

    /**
	 * Methode ueberprueft, ob im Request ein bestimmter Parameter drin war. Wenn ja, gibt sie diesen
	 * als <code>String</code> zurueck, ansonsten den uebergebenen Defaultwert
	 *
	 * @param strParamIn:  dieser Parameter soll ueberprueft werden
	 * @param dblefaultIn: falls der Parameter nicht im Request ist, dann den Defaultwert zurueckgeben
	 * @return String
	 */
    public String getReqParam(String strParamIn, Double dblefaultIn) {
        String strTmp = getReqParam(strParamIn);
        if (strTmp != null) {
            return strTmp.trim();
        } else if (dblefaultIn != null) return dblefaultIn.toString();
        return "";
    }

    /**
	 * Methode ueberprueft, ob im Request ein bestimmter Parameter drin war. Wenn ja, gibt sie diesen
	 * als <code>int</code> Wert zurueck, ansonsten den uebergebenen Defaultwert
	 *
	 * @param strParamIn:   dieser Parameter soll ueberprueft werden
	 * @param intDefaultIn: falls der Parameter nicht im Request ist, dann den Defaultwert zurueckgeben
	 * @return int
	 * @throws NumberFormatException wenn strParamIn keine Zahl ist
	 */
    public int getReqParam(String strParamIn, int intDefaultIn) throws NumberFormatException {
        String strTmp = getReqParam(strParamIn);
        if (StringUtils.isNotEmpty(strTmp) && checkValueForInteger(strTmp)) {
            Integer value = Integer.parseInt(strTmp);
            if (value != null) intDefaultIn = value;
        }
        return intDefaultIn;
    }

    /**
	 * Methode ueberprueft, ob im Request ein bestimmter Parameter drin war. Wenn ja, gibt sie diesen
	 * als <code>double</code> Wert zurueck, ansonsten den uebergebenen Defaultwert
	 *
	 * @param strParamIn:   dieser Parameter soll ueberprueft werden
	 * @param dblDefaultIn: falls der Parameter nicht im Request ist, dann den Defaultwert zurueckgeben
	 * @return double
	 * @throws NumberFormatException wenn strParamIn keine Zahl ist
	 */
    public double getReqParam(String strParamIn, double dblDefaultIn) throws NumberFormatException {
        String strTmp = getReqParam(strParamIn);
        if (strTmp == null || strTmp.length() == 0) {
            return dblDefaultIn;
        }
        return Double.parseDouble(strTmp);
    }

    /**
	 * holt den aktuellen Tag des Monats aus dem aktuellen Datum
	 *
	 * @return int
	 */
    public static int getDay() {
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);
            df.format(new Date());
            return df.getCalendar().get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {
            log.error("Exception in der RunningSystemBean, Methode getDay", e);
            return 0;
        }
    }

    /**
	 * holt den Monat aus dem aktuellen Datum
	 *
	 * @return int
	 */
    public static int getMonth() {
        return getMonth(new Date());
    }

    /**
	 * holt den Monat aus dem uebergebenen Datum
	 *
	 * @param date Datum
	 * @return int
	 */
    public static int getMonth(Date date) {
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);
            df.format(date);
            return df.getCalendar().get(Calendar.MONTH) + 1;
        } catch (Exception e) {
            log.error("Exception in der RunningSystemBean, Methode getMonth", e);
            return 0;
        }
    }

    /**
	 * holt die Woche aus dem aktuellen Datum
	 *
	 * @return int
	 */
    public static int getWeek() {
        return getWeek(new Date());
    }

    /**
	 * holt die Woche aus dem uebergebenem Datum
	 *
	 * @param date Datum
	 * @return int
	 */
    public static int getWeek(Date date) {
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);
            df.format(date);
            return df.getCalendar().get(Calendar.WEEK_OF_YEAR);
        } catch (Exception e) {
            log.error("Exception in der RunningSystemBean, Methode getWeek", e);
            return 0;
        }
    }

    /**
	 * holt aktuelles Jahr aus dem aktuellen Datum
	 *
	 * @return int: aktuelles Jahr
	 */
    public static int getYear() {
        return getYear(new Date());
    }

    /**
	 * holt aktuelles Jahr aus dem aktuellen Datum
	 *
	 * @param date Datum
	 * @return int: aktuelles Jahr
	 */
    public static int getYear(Date date) {
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);
            df.format(date);
            return df.getCalendar().get(Calendar.YEAR);
        } catch (Exception e) {
            log.error("Exception in der RunningSystemBean, Methode getYear", e);
            return 0;
        }
    }

    /**
	 * holt aktuelles Jahr aus dem aktuellen Datum
	 *
	 * @param datum Datum, aus dem das Jahr extrahier werden soll
	 * @return int: aktuelles Jahr
	 */
    public static String getYear(String datum) {
        if (checkForGermanDateFormat(datum)) {
            try {
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                df.parse(datum);
                return ("" + df.getCalendar().get(Calendar.YEAR));
            } catch (Exception e) {
                log.error("Exception in der RunningSystemBean, Methode getYear", e);
                return "";
            }
        }
        return "";
    }

    /**
	 * Methode rundet auf 2 Nachkommastellen
	 *
	 * @param dblIn diesen Wert runden
	 * @return gerundeter Wert
	 */
    public static double round(double dblIn) {
        int digits = 2;
        return round(dblIn, digits);
    }

    /**
	 * Methode rundet auf x Nachkommastellen
	 *
	 * @param dblIn  diesen Wert runden
	 * @param digits auf so viele Nachkommastellen
	 * @return gerundeter Wert
	 */
    public static double round(double dblIn, int digits) {
        double digitFactor = Math.pow(10, digits);
        return Math.round(dblIn * digitFactor) / digitFactor;
    }

    /**
	 * Methode encodiert den uebergenen String in nen MD5 String
	 *
	 * @param strIn diesen Wert encoden
	 * @return encodierter String
	 */
    public static String getMD5EncodedString(String strIn) {
        try {
            java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(strIn.getBytes());
            byte[] digest = md5.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte aDigest : digest) {
                hexString.append(Integer.toHexString(0xFF & aDigest));
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException n) {
            return "";
        }
    }

    /**
	 * ueberprueft einen uebergebenen String, ob er eine email-Adresse sein koennte
	 *
	 * @param email diesen String pruefen
	 * @return true, wenn OK, sonst false
	 */
    public static boolean checkEMail(String email) {
        if (StringUtils.isNotEmpty(email) && -1 != email.indexOf("@") && !email.contains(" ")) {
            if (email.lastIndexOf(".") > email.lastIndexOf("@")) {
                if (email.substring(email.lastIndexOf(".") + 1, email.length()).length() <= 3 && email.substring(email.lastIndexOf(".") + 1, email.length()).length() >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * ueberprueft einen uebergebene PLZ, ob fuer das uebergebene Land gueltig
	 *
	 * @param zip	 diesen String pruefen
	 * @param country fuer dieses Land
	 * @return true, wenn OK, sonst false
	 */
    public static boolean checkZipForCountry(String zip, Country country) {
        if (country.getId() == Constants.Values.Country.GERMANY) return (zip.length() == 5);
        if (country.getId() == Constants.Values.Country.SWITZERLAND) return (zip.length() == 4);
        if (country.getId() == Constants.Values.Country.AUSTRIA) return (zip.length() == 4);
        if (country.getId() == Constants.Values.Country.FRANCE) return (zip.length() == 5 || zip.length() == 2);
        if (country.getId() == Constants.Values.Country.ITALY) return (zip.length() == 5);
        if (country.getId() == Constants.Values.Country.USA) return (zip.length() == 5);
        return true;
    }

    /**
	 * transformiert eine boolsche int Eingabe in die entsprechende ja/nein-Kombination
	 *
	 * @param intBoolIn: int 1 = ja, else = nein
	 * @return String ja oder nein
	 */
    public String getIntBooleanText(int intBoolIn) {
        if (intBoolIn == 1) {
            return getLocalizedText("yes");
        }
        return getLocalizedText("no");
    }

    /**
	 * transformiert eine boolsche Eingabe in die entsprechende ja/nein-Kombination
	 *
	 * @param boolIn: int true = ja, false = nein
	 * @return String ja oder nein
	 */
    public String getBooleanText(boolean boolIn) {
        if (boolIn) {
            return getLocalizedText("yes");
        }
        return getLocalizedText("no");
    }

    /**
	 * transformiert eine int Eingabe in die entsprechende true/false-Kombination
	 *
	 * @param intIn: 1 = true, 0 = false
	 * @return true oder false
	 */
    public static boolean getBooleanValue(int intIn) {
        return intIn == 1;
    }

    /**
	 * transformiert eine boolesche Eingabe in die entsprechende true/false-Kombination
	 *
	 * @param booleanIn: Boolean.TRUE = true; null = false, Boolean.FALSE = false
	 * @return true oder false
	 */
    public static boolean getBooleanValue(Boolean booleanIn) {
        return booleanIn != null && booleanIn;
    }

    /**
	 * transformiert eine boolsche Eingabe in die entsprechende 0/1-Kombination
	 *
	 * @param boolIn: true = 1, false = 0
	 * @return 1 oder 0
	 */
    public static int getIntValue(boolean boolIn) {
        if (boolIn) {
            return 1;
        }
        return 0;
    }

    /**
	 * prueft, ob ein Jahrgang einigermassen gueltig ist
	 *
	 * @param jahrgang zu ueberpruefenden Jahrgang
	 * @return wenn jahrgang in den letzten 100 Jahren liegt, dann true
	 */
    public boolean checkValidBirthDate(int jahrgang) {
        return jahrgang >= (getYear() - 100);
    }

    /**
	 * prueft, ob ein Jahrgang einigermassen gueltig ist
	 *
	 * @param jahrgang zu ueberpruefenden Jahrgang
	 * @param maxYears maximales Alter, das noch OK ist
	 * @return wenn jahrgang in den letzten datum+maxYears Jahren liegt, dann true
	 */
    public static boolean checkValidBirthDate(int jahrgang, int maxYears) {
        return jahrgang >= (getYear() - maxYears);
    }

    /**
	 * prueft, ob ein Jahrgang einigermassen gueltig ist
	 *
	 * @param jahrgang zu ueberpruefenden Jahrgang
	 * @param maxYears maximales Alter, das noch OK ist
	 * @param minYears mindestens so alt muss man sein
	 * @return wenn jahrgang zwischen jahrgang+minYears und datum+maxYears Jahren liegt, dann true
	 */
    public static boolean checkValidBirthDate(int jahrgang, int maxYears, int minYears) {
        return !((!checkValidBirthDate(jahrgang, maxYears)) || (jahrgang > (getYear() - minYears)));
    }

    /**
	 * splittet eine uebergebene Zeit im Format h:mm:ss in Sekunden auf
	 *
	 * @param zeit im format (h)h:mm:ss
	 * @return double
	 */
    public static double getMinutesFromResultString(String zeit) {
        if (zeit == null) return 0.0;
        if (zeit.indexOf(":") < 0) return 0.0;
        String xx[] = zeit.split(":");
        int hours = 0;
        int minutes = 0;
        if (xx.length > 0) hours = Integer.parseInt(xx[0]);
        if (xx.length > 1) {
            if (xx[1].indexOf(".") > 0) xx[1] = xx[1].substring(0, xx[1].indexOf(".") - 1);
            minutes = Integer.parseInt(xx[1]);
        }
        return (double) hours * 60 + minutes;
    }

    /**
	 * splittet eine uebergebene Zeit im Format h:mm:ss in Sekunden auf
	 *
	 * @param time im format (h)h:mm:ss
	 * @return double
	 */
    public static double getSecondsFromResultString(String time) {
        if (time == null) return 0.0;
        if (time.indexOf(":") < 0) return 0.0;
        String xx[] = time.split(":");
        int hours = Integer.parseInt(xx[0]);
        int minutes = Integer.parseInt(xx[1]);
        String yy = xx[2].split(",")[0];
        int seconds = Integer.parseInt(yy);
        return (double) ((hours * 60 + minutes) * 60) + seconds;
    }

    /**
	 * berechnet die Finisher-Zeit als Double Wert
	 *
	 * @param finishTime im Format h:mm:ss
	 * @return Zeit in Minuten als Double oder null, wenn inkorrektes Format
	 */
    public static Double getFinishTimeAsDouble(String finishTime) {
        if (finishTime == null) return null;
        double result;
        String parts[] = finishTime.split(":");
        if (parts.length != 3) return null;
        try {
            result = (Integer.parseInt(parts[0]) * 60);
            result += (Integer.parseInt(parts[1]));
            if (parts[2].contains(",")) {
                String seconds = parts[2].split(",")[0];
                result += round(Double.parseDouble(seconds) / 60);
            } else {
                result += round(Double.parseDouble(parts[2]) / 60);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return result;
    }

    /**
	 * ermittelt eine Google konform formattierte Adresse
	 *
	 * @param googleMapAddress
	 * @return leerer String oder Adresse
	 */
    public static String getGoogleAddress(GoogleMapAddress googleMapAddress) {
        String googleAddress = "";
        if (googleMapAddress == null) return googleAddress;
        if (getStringValue(googleMapAddress.getStreet()).length() > 0) {
            googleAddress = googleMapAddress.getStreet() + ",";
        }
        if (googleMapAddress.getZip() != null && !googleMapAddress.getZip().equals("0")) {
            googleAddress += getStringValue(googleMapAddress.getZip()) + ",";
        }
        if (getStringValue(googleMapAddress.getTown()).length() > 0) {
            googleAddress += googleMapAddress.getTown() + ",";
        }
        Country country = googleMapAddress.getCountry();
        if (country != null) {
            googleAddress += country.getName() + ",";
        }
        if (googleAddress.length() > 0) return googleAddress.substring(0, googleAddress.length() - 1);
        return "";
    }

    /**
	 * ist das Logo auch pyhsisch vorhanden?
	 *
	 * @param logoUrl
	 * @return
	 */
    public boolean checkLogoForExistance(String logoUrl) {
        if (logoUrl != null && logoUrl.length() > 0) {
            File fImage = new File(runningMasterBean.getServletContext().getRealPath("images") + File.separatorChar + "event" + File.separatorChar + logoUrl);
            if (fImage.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
	 * ist das Logo auch pyhsisch vorhanden?
	 *
	 * @param imagesDir
	 * @param logoUrl
	 * @return true, wenn ja, sonst false
	 */
    public static boolean checkLogoForExistance(String imagesDir, String logoUrl) {
        if (logoUrl != null && logoUrl.length() > 0) {
            File fImage = new File(imagesDir + File.separatorChar + "event" + File.separatorChar + logoUrl);
            if (fImage.exists()) {
                return true;
            }
        }
        return false;
    }

    public static String formatNumber(double number) {
        NumberFormat nf = new DecimalFormat("#0.0#");
        return nf.format(number);
    }

    private static String formatNumber(double number, Locale locale) {
        NumberFormat formatter = NumberFormat.getNumberInstance(locale);
        return formatter.format(number);
    }

    /**
	 * gibt die Anrede des Runners zurueck, wenn ein Geschlecht hinterlegt ist
	 * Herr oder Frau mit dem kompletten Namen aus Vorname blank Nachname
	 *
	 * @param runner fuer diesen Lauefer
	 * @return Anrede als String
	 */
    public String getSalutation(Runner runner) {
        return RunningSystemBean.getSalutation(runningMasterBean.getLocale(), runner);
    }

    public static String getSalutation(Locale locale, Runner runner) {
        String salutation = "";
        if (runner != null && runner.getGender() != null) {
            if (runner.getGender().getId().intValue() == 1) {
                salutation += getLocalizedText(locale, "salutation.male") + " ";
            } else if (runner.getGender().getId().intValue() == 2) {
                salutation += getLocalizedText(locale, "salutation.female") + " ";
            } else {
                salutation += getLocalizedText(locale, "dear_male_female") + " ";
            }
        }
        salutation += runner.getCompleteName();
        return salutation;
    }

    public String getSalutationFooter() {
        return getSalutationFooter(null);
    }

    /**
	 * Gruss-Formel
	 *
	 * @param name name des Unterzeichners
	 * @return Text html-formattierter Text
	 */
    public String getSalutationFooter(String name) {
        if (name == null) name = "myRunning.de-Team";
        return "<small>" + MessageFormat.format(Constants.MailTexts.SALUTATION, new String[] { name }) + "</small>";
    }

    /**
	 * Gruss-Formel
	 *
	 * @param name name des Unterzeichners
	 * @return Text html-formattierter Text
	 */
    public String getSalutationFooter(Locale locale, String name) {
        if (name == null) name = "myRunning.de";
        return "<small>" + MessageFormat.format(getLocalizedText(locale, "salutation_team_formatted"), new String[] { name }) + "</small>";
    }

    /**
	 * Grussformel und Kontakt-Footer
	 *
	 * @return Text
	 */
    public String getSalutationContactFooter() {
        return getSalutationContactFooter("");
    }

    /**
	 * Grussformel und Kontakt-Footer
	 *
	 * @param locale Land/Sprache
	 * @return Text
	 */
    public String getSalutationContactFooter(Locale locale) {
        return getSalutationContactFooter(locale, null);
    }

    /**
	 * Gruss-Formel und Kontakt-Footer
	 *
	 * @param name
	 * @return Text
	 */
    public String getSalutationContactFooter(String name) {
        if (name == null || name.length() == 0) name = "myRunning.de";
        return getSalutationFooter(name) + Constants.MailTexts.CONTACT;
    }

    /**
	 * Gruss-Formel und Kontakt-Footer
	 *
	 * @param locale Land/Sprache
	 * @param name   Unterzeichner
	 * @return Text
	 */
    public String getSalutationContactFooter(Locale locale, String name) {
        if (name == null) name = "myRunning.de";
        return getSalutationFooter(locale, name) + Constants.MailTexts.CONTACT;
    }

    /**
	 * Kennwort vergessen Text
	 *
	 * @param locale Land/Sprache
	 * @return Text
	 */
    public String getPasswordForgotten(Locale locale) {
        return getLocalizedText(locale, "password_reset_info");
    }

    /**
	 * liefert den "mail automatisch generiert" footer
	 *
	 * @param emailAddress an diese email-adresse wird die mail gesendet
	 * @return Text html-formattierter Text
	 */
    public String getAutomaticMailFooter(String emailAddress) {
        return "<small>" + MessageFormat.format(Constants.MailTexts.AUTOMATIC_MAIL, new Object[] { emailAddress }) + "</small>";
    }

    /**
	 * liefert den "mail automatisch generiert" footer
	 *
	 * @param emailAddress an diese email-adresse wird die mail gesendet
	 * @return Text html-formattierter Text
	 */
    public String getAutomaticMailFooter(Locale locale, String emailAddress) {
        return "<small>" + MessageFormat.format(getLocalizedText(locale, "email_automatic"), new Object[] { emailAddress }) + "</small>";
    }

    /**
	 * leifert den DISCLAIMER footer incl. AUTOMATIC_MAIL footer
	 *
	 * @param emailAddress an diese email-adresse wird die mail gesendet
	 * @return Text html-formattierter Text
	 */
    public String getDisclaimerMailFooter(String emailAddress) {
        return "<small>" + MessageFormat.format(Constants.MailTexts.DISCLAIMER, new Object[] { getAutomaticMailFooter(emailAddress) }) + "</small>";
    }

    /**
	 * Abbestellung der E-Mail Benachrichtigung mit Link
	 *
	 * @param emailTypeRunner diese Benachrichtigung loeschen
	 * @return Text html-formattierter Text
	 */
    public String getNewsletterDeActivationMailFooter(EmailTypeRunner emailTypeRunner) {
        return "<small>" + MessageFormat.format(Constants.MailTexts.NEWSLETTER_DEACTIVATION, new Object[] { "" + emailTypeRunner.getRunner().getFrontendRunnerID(), "" + emailTypeRunner.getId().intValue() }) + "</small>";
    }

    /**
	 * Abbestellung der E-Mail Benachrichtigung mit Link
	 *
	 * @param emailTypeRunner diese Benachrichtigung loeschen
	 * @return Text html-formattierter Text
	 */
    public String getNewsletterDeActivationMailFooter(Locale locale, EmailTypeRunner emailTypeRunner) {
        return "<small>" + getLocalizedText(locale, "email_unsubscribe") + ": " + MessageFormat.format("<a href=\"http://www.myrunning.de?p=180&runnerId={0}&etr={1}\">" + getLocalizedText(locale, "click.here") + "</a>", new Object[] { "" + emailTypeRunner.getRunner().getFrontendRunnerID(), "" + emailTypeRunner.getId().intValue() }) + "</small>";
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    /**
	 * Methode schreibt ins Logfile
	 *
	 * @param identifier Thema
	 * @param logLevel   Level, mit dem geloggt werden soll
	 * @param messgage   die zu loggende Nachricht
	 */
    public static void writeLog(LogBean logBean, String identifier, int logLevel, String messgage) {
        try {
            if (logBean == null) {
                log.error("LogBean is null in der RunningSystemBean, Methode writeLog!");
            } else if (logBean.getLogLevel() >= logLevel) {
                logBean.openLogFile();
                logBean.writeLog(identifier + ";" + messgage);
                logBean.closeLogFile();
            }
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException in der RunningSystemBean, Methode writeLog", e);
        } catch (IOException e) {
            log.error("IOException in der RunningSystemBean, Methode writeLog", e);
        }
    }

    public static Locale getLocale(Long languageId) {
        if (languageId == null || Constants.Language.GERMAN == languageId) {
            return Locale.GERMAN;
        } else if (Constants.Language.ENGLISH == languageId) {
            return Locale.ENGLISH;
        } else if (Constants.Language.FRENCH == languageId) {
            return Locale.FRENCH;
        } else {
            return Locale.GERMAN;
        }
    }

    public static Color getColorFromHTML(String theColor) {
        if (theColor.length() != 6) throw new IllegalArgumentException("Not a valid HTML color");
        return new Color(Integer.valueOf(theColor.substring(0, 2), 16).intValue(), Integer.valueOf(theColor.substring(2, 4), 16).intValue(), Integer.valueOf(theColor.substring(4, 6), 16).intValue());
    }

    public static String createCharsetEncodedString(String s) {
        if (s == null) return null;
        String out;
        try {
            byte[] bytes = s.getBytes("ISO-8859-1");
            FacesContext ctx = FacesContext.getCurrentInstance();
            out = createCharsetEncodedString(bytes, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            out = null;
        }
        return out;
    }

    public static String createCharsetEncodedString(byte[] bytes, String enc) throws UnsupportedEncodingException {
        String encoded = new String(bytes, enc);
        return encoded;
    }
}
