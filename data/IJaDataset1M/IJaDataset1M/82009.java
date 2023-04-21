package com.company.common.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import com.company.common.Constant;

/**
 * 
 * @author ychen
 * 
 */
public class StringUtil {

    /**
	 * 是否不为空
	 * 
	 * @param target
	 *            要检验的目标字符串
	 * @return
	 */
    public static boolean isNotEmpty(String target) {
        return !isEmpty(target);
    }

    /**
	 * 是否为空
	 * 
	 * @param target
	 *            要检验的目标字符串
	 * @return
	 */
    public static boolean isEmpty(String target) {
        if (target == null || target.equals("")) {
            return true;
        }
        return false;
    }

    public static final String toUnicode(String str) {
        try {
            str = new String(str.getBytes("gb2312"), "iso-8859-1");
        } catch (Exception e) {
        }
        return str;
    }

    public static Integer getChar(String str, int index) {
        int temp = 0;
        if (isNotEmpty(str) && str.length() > index) {
            temp = Integer.parseInt(str.substring(index, index + 1));
        }
        return temp;
    }

    /**
	 * 
	 * @param target
	 * @param source
	 * @param index
	 * @param count
	 * @return
	 */
    public static String composePermission(String target, Integer source, int index) {
        StringBuffer str = new StringBuffer();
        if (isEmpty(target)) {
            target = Constant.permissionString;
        }
        if (target.length() > index) {
            for (int i = 0; i < Constant.permissionCount; i++) {
                if (target.length() > i) {
                    if (index == i) {
                        str.append(source);
                    } else {
                        str.append(getChar(target, i));
                    }
                } else {
                    str.append("0");
                }
            }
        }
        return str.toString();
    }

    public static String composeRight(String target, String source, int index) {
        StringBuffer str = new StringBuffer();
        if (isEmpty(target)) {
            target = Constant.permissionString;
        }
        if (target.length() > index) {
            for (int i = 0; i < Constant.permissionCount; i++) {
                if (target.length() > i) {
                    if (index == i) {
                        str.append(source);
                    } else {
                        str.append(getChar(target, i));
                    }
                } else {
                    str.append("0");
                }
            }
        }
        return str.toString();
    }

    public static Map<String, StringBuffer> composeRightMap(Map<String, StringBuffer> map, String[] right, int index) {
        if (map == null) {
            map = new HashMap<String, StringBuffer>();
        }
        if (right != null) {
            for (int i = 0; i < right.length; i++) {
                String temp[] = right[i].split(":");
                StringBuffer oldRight = new StringBuffer();
                if (map.get(temp[0]) != null) {
                    oldRight.append(map.get(temp[0]));
                    for (int j = oldRight.length(); j < index; j++) {
                        oldRight.append("0");
                    }
                } else {
                    for (int j = 0; j < index; j++) {
                        oldRight.append("0");
                    }
                }
                oldRight.append(temp[1]);
                map.put(temp[0], oldRight);
            }
        }
        return map;
    }

    public static Map<String, StringBuffer> composeComRightMap(Map<String, String[]> rightMap) {
        Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();
        String view[] = rightMap.get(Constant.RIGHT_VIEW);
        map = composeRightMap(map, view, 0);
        String add[] = rightMap.get(Constant.RIGHT_ADD);
        map = composeRightMap(map, add, 1);
        String edit[] = rightMap.get(Constant.RIGHT_EDIT);
        map = composeRightMap(map, edit, 2);
        String delete[] = rightMap.get(Constant.RIGHT_DELETE);
        map = composeRightMap(map, delete, 3);
        return map;
    }

    public static Double doubleFormat(Object d) {
        if (d != null) {
            DecimalFormat df = new DecimalFormat("######0.00");
            String result = df.format(d);
            return Double.parseDouble(result);
        }
        return (Double) d;
    }

    public static String stringFormat(Object d) {
        if (d != null) {
            DecimalFormat df = new DecimalFormat("######0.00");
            String result = df.format(d);
            return result;
        }
        return (String) d;
    }

    public static String getAutoNo(Integer value, Integer digit) {
        String no = Integer.toString(value);
        if (no.length() < digit) {
            int num = digit - no.length();
            for (int i = 0; i < num; i++) {
                no = "0" + no;
            }
        }
        return no;
    }

    public static void main(String args[]) {
        double a = 1.05;
        double b = 0.03;
        double c = 11.0;
        DecimalFormat df = new DecimalFormat("######0.00");
        String result = df.format(a * b * c);
        System.out.println(Double.parseDouble(result));
    }
}
