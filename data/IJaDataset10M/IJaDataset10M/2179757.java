package com.trinea.sns.utilImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �ַ����࣬����ʵ��һЩ�ַ�ĳ��ò���
 * 
 * @author Trinea 2011-7-22 ����12:36:29
 */
public class StringUtils {

    public static final String NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String NUMBERS = "0123456789";

    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * �ж��ַ��Ƿ�Ϊ�ջ򳤶�Ϊ0
     * 
     * @param str
     * @return ���ַ�Ϊnull�򳤶�Ϊ0, ����true; ���򷵻�false.
     * 
     * <pre>
     *      isEmpty(null)   =   true;
     *      isEmpty("")     =   true;
     *      isEmpty("  ")   =   false;
     * </pre>
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * �ж��ַ��Ƿ�Ϊ�ջ򳤶�Ϊ0������ɿո����
     * 
     * @param str
     * @return ���ַ�Ϊnull�򳤶�Ϊ0����ɿո����, ����true; ���򷵻�false.
     * 
     * <pre>
     *      isBlank(null)   =   true;
     *      isBlank("")     =   true;
     *      isBlank("  ")   =   true;
     *      isBlank("a")    =   false;
     *      isBlank("a ")   =   false;
     *      isBlank(" a")   =   false;
     *      isBlank("a b")  =   false;
     * </pre>
     */
    public static boolean isBlank(String str) {
        return (isEmpty(str) || (str.trim().length() == 0));
    }

    /**
     * �Ƚ�����string�Ƿ����
     * 
     * @param actual string 1
     * @param expected string 2
     * @return
     * @see
     *      <ul>
     *      <li>compareString(null, null) = true</li>
     *      <li>compareString(null, "Aa") = false</li>
     *      <li>compareString("A", "Aa") = false</li>
     *      <li>compareString("Aa", "Aa") = true</li>
     *      </ul>
     */
    public static boolean compareString(String actual, String expected) {
        if (actual == null || expected == null) {
            return (actual == null && expected == null);
        }
        return actual.equals(expected);
    }

    /**
     * ���ַ�����ĸ��д�󷵻�
     * 
     * @param str ԭ�ַ�
     * @return ����ĸ��д����ַ�
     * 
     * <pre>
     *      capitalizeFirstLetter(null)     =   null;
     *      capitalizeFirstLetter("")       =   "";
     *      capitalizeFirstLetter("1ab")    =   "1ab"
     *      capitalizeFirstLetter("a")      =   "A"
     *      capitalizeFirstLetter("ab")     =   "Ab"
     *      capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str) || !Character.isLetter(str.charAt(0))) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * �������ͨ�ַ�����utf8���б���
     * 
     * @param str ԭ�ַ�
     * @return ������ַ������󷵻�null
     * 
     * <pre>
     *      utf8Encode(null)        =   null
     *      utf8Encode("")          =   "";
     *      utf8Encode("aa")        =   "aa";
     *      utf8Encode("��������")   = "������ַ�";
     * </pre>
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return str;
    }

    /**
     * ���ַ�ת��Ϊutf-8���ֽں��½�String
     * 
     * @param str Դ�ַ�
     * @return �½����String�����?��null
     */
    public static String utf8Byte(String str) {
        return utf8Byte(str, null);
    }

    /**
     * ���ַ�ת��Ϊutf-8���ֽں��½�String
     * 
     * @param str Դ�ַ�
     * @param defultReturn Ĭ�ϳ��?��
     * @return �½����String
     */
    public static String utf8Byte(String str, String defultReturn) {
        if (isEmpty(str)) {
            return defultReturn;
        }
        try {
            return new String(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return defultReturn;
        }
    }

    /**
     * null�ַ�ת��Ϊ����Ϊ0���ַ�
     * 
     * @param str ��ת���ַ�
     * @return
     * @see
     * <pre>
     *  nullStrToEmpty(null)    =   "";
     *  nullStrToEmpty("")      =   "";
     *  nullStrToEmpty("aa")    =   "";
     * </pre>
     */
    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * �Թ̶���ʽ�õ���ǰʱ����ַ�
     * 
     * @param format ʱ���ʽ��ͬʱ���format����"yyyy-MM-dd HH:mm:ss"
     * @return ʱ���ַ���formatΪ�ջ򳤶�Ϊ�ջ�ֻ��ո���Ĭ��Ϊ"yyyy-MM-dd HH:mm:ss"
     * 
     * <pre>
     *      currentDate(null)                   = "yyyy-MM-dd HH:mm:ss"
     *      currentDate("")                     = "yyyy-MM-dd HH:mm:ss"
     *      currentDate("   ")                  = "yyyy-MM-dd HH:mm:ss"
     *      currentDate("yyyy-MM-dd")           = "yyyy-MM-dd"
     *      currentDate("yyyy/MM/dd HH:mm:ss")  = "yyyy/MM/dd HH:mm:ss"
     * </pre>
     */
    public static String currentDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(isBlank(format) ? "yyyy-MM-dd HH:mm:ss" : format);
        return dateFormat.format(new Date());
    }

    /**
     * ��"yyyy-MM-dd HH:mm:ss"��ʽ�õ���ǰʱ��
     * 
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static String currentDate() {
        return currentDate("");
    }

    /**
     * �Թ̶���ʽ�õ���ǰʱ����ַ������
     * 
     * @param format ʱ���ʽ��ͬʱ���format����"yyyy-MM-dd HH:mm:ss SS"
     * @return ʱ���ַ���formatΪ�ջ򳤶�Ϊ�ջ�ֻ��ո���Ĭ��Ϊ"yyyy-MM-dd HH:mm:ss SS"
     * 
     * <pre>
     *      currentDate(null)                   = "yyyy-MM-dd HH:mm:ss SS"
     *      currentDate("")                     = "yyyy-MM-dd HH:mm:ss SS"
     *      currentDate("   ")                  = "yyyy-MM-dd HH:mm:ss SS"
     *      currentDate("yyyy-MM-dd")           = "yyyy-MM-dd"
     *      currentDate("yyyy/MM/dd HH:mm:ss SS")  = "yyyy/MM/dd HH:mm:ss SS"
     * </pre>
     */
    public static String currentDateInMills(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(isBlank(format) ? "yyyy-MM-dd HH-mm-ss SS" : format);
        return dateFormat.format(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * ��"yyyy-MM-dd HH:mm:ss SS"��ʽ�õ���ǰʱ��
     * 
     * @return "yyyy-MM-dd HH:mm:ss SS"
     */
    public static String currentDateInMills() {
        return currentDateInMills("");
    }

    /**
     * ����ʱ�䣬������ʾ״̬����ʱ��
     * 
     * @param time
     * @return
     */
    public static String processTime(Long time) {
        long oneDay = 24 * 60 * 60 * 1000;
        Date now = new Date();
        Date orginalTime = new Date(time);
        long nowDay = now.getTime() - (now.getHours() * 3600 + now.getMinutes() * 60 + now.getSeconds()) * 1000;
        long yesterday = nowDay - oneDay;
        String nowHourAndMinute = toDoubleDigit(orginalTime.getHours()) + ":" + toDoubleDigit(orginalTime.getMinutes());
        if (time >= now.getTime()) {
            return "�ո�";
        } else if ((now.getTime() - time) < (60 * 1000)) {
            return (now.getTime() - time) / 1000 + "��ǰ " + nowHourAndMinute + " ";
        } else if ((now.getTime() - time) < (60 * 60 * 1000)) {
            return (now.getTime() - time) / 1000 / 60 + "����ǰ " + nowHourAndMinute + " ";
        } else if ((now.getTime() - time) < (24 * 60 * 60 * 1000)) {
            return (now.getTime() - time) / 1000 / 60 / 60 + "Сʱǰ " + nowHourAndMinute + " ";
        } else if (time >= nowDay) {
            return "���� " + nowHourAndMinute;
        } else if (time >= yesterday) {
            return "���� " + nowHourAndMinute;
        } else {
            return toDoubleDigit(orginalTime.getMonth()) + "-" + toDoubleDigit(orginalTime.getDate()) + " " + nowHourAndMinute + ":" + toDoubleDigit(orginalTime.getSeconds());
        }
    }

    /**
     * ��һλ����ʮλ��0�����λ����
     * 
     * @param number
     * @return
     */
    public static String toDoubleDigit(int number) {
        if (number >= 0 && number < 10) {
            return "0" + ((Integer) number).toString();
        }
        return ((Integer) number).toString();
    }

    /**
     * �õ�href���ӵ�innerHtml
     * 
     * @param href href����
     * @return href��innerHtml
     *         <ul>
     *         <li>���ַ���""</li>
     *         <li>���ַ�Ϊ�գ��Ҳ������������ķ���ԭ����</li>
     *         <li>���ַ�Ϊ�գ��ҷ����������ķ������һ��innerHtml</li>
     *         </ul>
     * @see
     * <pre>
     *      getHrefInnerHtml(null)                                  = ""
     *      getHrefInnerHtml("")                                    = ""
     *      getHrefInnerHtml("mp3")                                 = "mp3";
     *      getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     *      getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     *      getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     *      getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     *      getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     *      getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     *      getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     *      getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     *      getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }
        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * �õ��̶����ȵ�����ַ��ַ������ֺ���ĸ������
     * 
     * @param length ����
     * @return
     * @see ��{@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS_AND_LETTERS, length);
    }

    /**
     * �õ��̶����ȵ�����ַ��ַ������ֻ�����
     * 
     * @param length ����
     * @return
     * @see ��{@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    /**
     * �õ��̶����ȵ�����ַ��ַ�����ĸ������
     * 
     * @param length ����
     * @return
     * @see ��{@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomLetters(int length) {
        return getRandom(LETTERS, length);
    }

    /**
     * �õ��̶����ȵ�����ַ��ַ��ɴ�д��ĸ������
     * 
     * @param length ����
     * @return
     * @see ��{@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomCapitalLetters(int length) {
        return getRandom(CAPITAL_LETTERS, length);
    }

    /**
     * �õ��̶����ȵ�����ַ��ַ���Сд��ĸ������
     * 
     * @param length ����
     * @return
     * @see ��{@link StringUtils#getRandom(String source, int length)}
     */
    public static String getRandomLowerCaseLetters(int length) {
        return getRandom(LOWER_CASE_LETTERS, length);
    }

    /**
     * �õ��̶����ȵ�����ַ��ַ���source���ַ������
     * 
     * @param source Դ�ַ�
     * @param length ����
     * @return
     *         <ul>
     *         <li>��sourceΪnull��Ϊ���ַ�����null</li>
     *         <li>�����{@link StringUtils#getRandom(char[] sourceChar, int length)}</li>
     *         </ul>
     */
    public static String getRandom(String source, int length) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        char[] sourceChar = source.toCharArray();
        return getRandom(sourceChar, length);
    }

    /**
     * �õ��̶����ȵ�����ַ��ַ���sourceChar���ַ������
     * 
     * @param sourceChar Դ�ַ�����
     * @param length ����
     * @return
     *         <ul>
     *         <li>��sourceCharΪnull�򳤶�Ϊ0������null</li>
     *         <li>��lengthС��0������null</li>
     *         </ul>
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }
        StringBuilder str = new StringBuilder("");
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[random.nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * html��ת���ַ�ת��������ַ�
     * 
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        if (StringUtils.isEmpty(source)) {
            return "";
        } else {
            return source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
        }
    }
}
