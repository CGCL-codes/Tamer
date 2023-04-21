package com.util;

import javax.servlet.http.HttpServletRequest;

public class ParamUtil {

    public ParamUtil() {
    }

    /**
     * @param request
     * @param s         �������
     * @return             ����ֵ
     */
    public static String getString(HttpServletRequest request, String s) {
        String s1 = request.getParameter(s);
        if (s1 != null && !s1.equals("")) return s1; else return null;
    }

    /**
     *
     * @param request
     * @param s         �������
     * @param s1         if s=null ʱ ����ֵs1
     * @return             ����ֵ
     */
    public static String getString(HttpServletRequest request, String s, String s1) {
        String s2 = getString(request, s);
        if (s2 == null) s2 = s1;
        return s2;
    }

    /**
     * @param request
     * @param s      ����ֵ
     * @return          ����ֵ����������
     */
    public static int getInt(HttpServletRequest request, String s) {
        try {
            return Integer.parseInt(getString(request, s));
        } catch (NumberFormatException numberformatexception) {
            System.out.println("com.suncn.common.ParamUtil ERROR getInt(HttpServletRequest request, String s)");
            numberformatexception.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @param request
     * @param s       ����ֵ
     * @param i         if s=null ʱ ����ֵ
     * @return           ����ֵ����������
     */
    public static int getInt(HttpServletRequest request, String s, int i) {
        try {
            String s1 = getString(request, s);
            if (s1 == null) return i; else return Integer.parseInt(s1);
        } catch (NumberFormatException numberformatexception) {
            System.out.println("com.suncn.common.ParamUtil ERROR getInt(HttpServletRequest request, String s, int i)");
            numberformatexception.printStackTrace();
        }
        return 0;
    }

    /**
     *
     * @param request
     * @param s       ����ֵ
     * @param i         if s=null ʱ ����ֵ
     * @return           ����ֵ����������
     */
    public static double getDouble(HttpServletRequest request, String s, double i) {
        try {
            String s1 = getString(request, s);
            if (s1 == null) return i; else return Double.parseDouble(s1);
        } catch (NumberFormatException numberformatexception) {
            System.out.println("com.suncn.common.ParamUtil ERROR getInt(HttpServletRequest request, String s, double i)");
            numberformatexception.printStackTrace();
        }
        return 0.0;
    }

    public static float getFloat(HttpServletRequest request, String s, float i) {
        try {
            String s1 = getString(request, s);
            if (s1 == null) return i; else return Float.parseFloat(s1);
        } catch (NumberFormatException numberformatexception) {
            System.out.println("com.suncn.common.ParamUtil ERROR getInt(HttpServletRequest request, String s, double i)");
            numberformatexception.printStackTrace();
        }
        return 0;
    }

    public static long getLong(HttpServletRequest request, String s, long i) {
        try {
            String s1 = getString(request, s);
            if (s1 == null) return i; else return Long.parseLong(s1);
        } catch (NumberFormatException numberformatexception) {
            System.out.println("com.suncn.common.ParamUtil ERROR getLong(HttpServletRequest request, String s, long  i)");
            numberformatexception.printStackTrace();
        }
        return 0;
    }

    public static boolean isValid(HttpServletRequest request) {
        return false;
    }

    public static String[] getStrArray(HttpServletRequest request, String s) {
        String[] arry = request.getParameterValues(s);
        if (arry == null) return null; else return arry;
    }

    public static int[] getIntArray(HttpServletRequest request, String s) {
        String[] s1 = getStrArray(request, s);
        int[] j = null;
        if (s1 != null) {
            for (int i = 0; i < s1.length; i++) {
                if (i == 0) j = new int[s1.length];
                j[i] = Integer.parseInt(s1[i]);
            }
        }
        return j;
    }

    public static long[] getLongArray(HttpServletRequest request, String s) {
        String[] s1 = getStrArray(request, s);
        long[] j = null;
        if (s1 != null) {
            for (int i = 0; i < s1.length; i++) {
                if (i == 0) j = new long[s1.length];
                j[i] = Long.parseLong(s1[i]);
            }
        }
        return j;
    }

    public static LongList getLongList(HttpServletRequest request, String s) {
        long[] array = getLongArray(request, s);
        LongList list = new LongList();
        for (int k = 0; k < array.length; k++) {
            list.add(array[k]);
        }
        return list;
    }

    public static String getAction(HttpServletRequest request) {
        return getString(request, com.siteeval.common.Constants.ACTION_TYPE, "");
    }
}
