package org.yehongyu.websale.common.util;

import java.math.BigDecimal;

/**
 * ����˵����ͨ��ת����
 * @author yehongyu.org
 * @version 1.0 2007-12-3 ����01:28:02
 */
public class Convert {

    /**
	 * �������ܡ��Ƿ�ΪNull����ַ�
	 * @param s �����ַ�
	 * @return true or false
	 */
    public static boolean isEmpty(String s) {
        return (s == null || s.trim().equals("")) ? true : false;
    }

    /**
     * �������ܡ�����BigDecimal����,���򷵻�new BigDecimal(0).
     * @param s �����ַ�
     * @return ����BigDecimal.
     */
    public static BigDecimal getBigDecimal(String strN) {
        return getBigDecimal(strN, 0);
    }

    /**
     * �������ܡ�����BigDecimal����,���򷵻�Ĭ��ֵ.
     * @param s �����ַ�
     * @param defval Ĭ��ֵ
     * @return ����BigDecimal.
     */
    public static BigDecimal getBigDecimal(String s, int defval) {
        if (s == null) return new BigDecimal(defval);
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return new BigDecimal(defval);
        }
    }

    /**
     * �������ܡ���ȡ�߼�ֵ��"1"��"TRUE"Ϊtrue,"2"��"FALSE"Ϊfalse.Ĭ��Ϊfalse.
     * @param b �����ַ�
     * @return �߼�ֵ
     */
    public static boolean getBoolean(String b) {
        return getBoolean(b, false);
    }

    /**
     * �������ܡ���ȡ�߼�ֵ��"1"��"TRUE"Ϊtrue,"2"��"FALSE"Ϊfalse.
     * @param b �����ַ�
     * @param defval Ĭ��ֵ
     * @return �߼�ֵ
     */
    public static boolean getBoolean(String b, boolean defval) {
        if (b == null) return defval;
        String upperB = b.toUpperCase();
        if ("1".equals(b)) return (true);
        if ("0".equals(b)) return (false);
        if ("TRUE".equals(upperB)) return (true);
        if ("FALSE".equals(upperB)) return (false);
        return defval;
    }

    /**
     * �������ܡ���ȡbyte��ֵ�����򷵻�0��
     * @param strN �����ַ�
     * @return ������ֵ
     */
    public static byte getByte(String strN) {
        return getByte(strN, (byte) 0);
    }

    /**
     * �������ܡ���ȡbyte��ֵ�����򷵻�Ĭ����ֵ��
     * @param strN �����ַ�
     * @param defVal Ĭ����ֵ
     * @return ������ֵ
     */
    public static byte getByte(String strN, byte defval) {
        if (isEmpty(strN)) return defval;
        return strN.getBytes()[0];
    }

    /**
     * �������ܡ���ȡshort��ֵ�����򷵻�0��
     * @param strN �����ַ�
     * @return ������ֵ
     */
    public static short getShort(String strN) {
        return getShort(strN, (short) 0);
    }

    /**
     * �������ܡ���ȡshort��ֵ�����򷵻�Ĭ����ֵ��
     * @param strN �����ַ�
     * @param defVal Ĭ����ֵ
     * @return ������ֵ
     */
    public static short getShort(String strN, short defval) {
        if (isEmpty(strN)) return defval;
        try {
            return Short.parseShort(strN);
        } catch (NumberFormatException e) {
            return defval;
        }
    }

    /**
     * �������ܡ���ȡint��ֵ�����򷵻�0��
     * @param strN �����ַ�
     * @return ������ֵ
     */
    public static int getInt(String strN) {
        return getInt(strN, 0);
    }

    /**
     * �������ܡ���ȡint��ֵ�����򷵻�Ĭ����ֵ��
     * @param strN �����ַ�
     * @param defVal Ĭ����ֵ
     * @return ������ֵ
     */
    public static int getInt(String strN, int defVal) {
        if (isEmpty(strN)) return defVal;
        try {
            return Integer.parseInt(strN);
        } catch (NumberFormatException e) {
            return defVal;
        }
    }

    /**
     * �������ܡ���ȡlong��ֵ�����򷵻�0��
     * @param strN �����ַ�
     * @return ������ֵ
     */
    public static float getFloat(String strN) {
        return getFloat(strN, 0f);
    }

    /**
     * �������ܡ���ȡfloat��ֵ�����򷵻�Ĭ����ֵ��
     * @param strN �����ַ�
     * @param defVal Ĭ����ֵ
     * @return ������ֵ
     */
    public static float getFloat(String strN, float defVal) {
        if (isEmpty(strN)) return defVal;
        try {
            return Float.parseFloat(strN);
        } catch (NumberFormatException e) {
            return defVal;
        }
    }

    /**
     * �������ܡ���ȡlong��ֵ�����򷵻�0��
     * @param strN �����ַ�
     * @return ������ֵ
     */
    public static long getLong(String strN) {
        return getLong(strN, 0L);
    }

    /**
     * �������ܡ���ȡlong��ֵ�����򷵻�Ĭ����ֵ��
     * @param strN �����ַ�
     * @param defVal Ĭ����ֵ
     * @return ������ֵ
     */
    public static long getLong(String strN, long defVal) {
        if (isEmpty(strN)) return defVal;
        try {
            return Long.parseLong(strN);
        } catch (NumberFormatException e) {
            return defVal;
        }
    }

    /**
     * �������ܡ���ȡdouble��ֵ�����򷵻�0��
     * @param strN �����ַ�
     * @return ������ֵ
     */
    public static double getDouble(String strN) {
        return getDouble(strN, 0d);
    }

    /**
     * �������ܡ���ȡdouble��ֵ�����򷵻�Ĭ����ֵ��
     * @param strN �����ַ�
     * @param defVal Ĭ����ֵ
     * @return ������ֵ
     */
    public static double getDouble(String strN, double defVal) {
        if (isEmpty(strN)) return defVal;
        try {
            return Double.parseDouble(strN);
        } catch (NumberFormatException e) {
            return defVal;
        }
    }

    /**
     * �������ܡ���ȡ������ַ��ʾ��Null�򷵻ؿմ�
     * @param o �������
     * @return String ����ַ�
     */
    public static String getString(Object o) {
        return o != null ? o.toString().trim() : "";
    }

    /**
     * �������ܡ���ȡ�����ַ�����ֵ�������˿�ֵ�Ϳմ���
     * @param s �����ַ����
     * @return String �������ַ�
     */
    public static String getString(String s) {
        return getString(s, "");
    }

    /**
     * �������ܡ���ȡ�����ַ�����ֵ�����Ϊ���򷵻�Ĭ�϶���
     * @param s �����ַ����
     * @param defval Ĭ�Ϸ����ַ����
     * @return String ������ַ�
     */
    public static String getString(String s, String defval) {
        return isEmpty(s) ? defval : s.trim();
    }

    /**
	 * �������ܡ�main����
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println(Convert.getLong(null));
    }
}
