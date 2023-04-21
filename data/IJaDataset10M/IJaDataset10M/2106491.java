package com.sns2Life.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ��������б���Ĺ����࣬�ṩ�ǿ��жϣ�����ת���ȹ��ܡ�
 * 
 * @author ����
 * @version v 1.0.0
 */
public class CollectionUtil {

    /**
	 * ���ַ�����ת����BigDecimal���顣
	 * 
	 * @param strs
	 *            �ַ�����
	 * @return BigDecimal[]
	 */
    public static BigDecimal[] String2BigDecimals(String strs[]) {
        if (isEmpty(strs)) {
            return null;
        }
        BigDecimal bd[] = new BigDecimal[strs.length];
        for (int i = 0; i < strs.length; i++) {
            if (strs[i] != null) bd[i] = new BigDecimal(strs[i]);
        }
        return bd;
    }

    /**
	 * ȥ��List�������ظ���ֵ
	 * @param list
	 * @return ȥ�غ��list
	 */
    public static List removeDupValue(List list) {
        Set set = new HashSet();
        set.addAll(list);
        List retList = new ArrayList();
        retList.addAll(set);
        return retList;
    }

    /**
	 * ��Map������ȥ��ֵ
	 * @param Map��ݼ���
	 * @param ��Ҫȥ������ݵ�KeyList
	 */
    public static void removeKeyList(Map map, List keyList) {
        if (isEmpty(map) || isEmpty(keyList)) {
            return;
        }
        for (Iterator it = keyList.iterator(); it.hasNext(); ) {
            map.remove(it.next());
        }
    }

    /**
	 * ��һ�����List�ָ��С��List��
	 * 
	 * @param list
	 *            Ҫ�ָ��List
	 * @param size
	 *            ÿ��СList�Ĵ�С
	 * @return Ԫ��ΪСList��List
	 */
    public static List splitList(List list, int size) {
        List retList = new ArrayList();
        if (isEmpty(list)) {
            return retList;
        }
        int start = 0;
        int end = 0;
        int length = list.size();
        for (int i = 0; i <= length / size; i++) {
            start = i * size;
            end = (i + 1) * size;
            if (end > length) {
                end = length;
            }
            if (end > start) {
                List sublist = list.subList(start, end);
                retList.add(sublist);
            }
        }
        return retList;
    }

    /**
	 * ��String���͵�List��� ת��ΪString[]�����顣
	 * 
	 * @param List strings
	 * @return String[]
	 */
    public static String[] stringListToStringArr(List strings) {
        if (strings == null) {
            return new String[0];
        }
        String[] target = new String[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            target[i] = new String(strings.get(i).toString());
        }
        return target;
    }

    /**
	 * ��String���͵�List��� ת��ΪString��
	 * 
	 * @param List strings
	 * @return String
	 */
    public static String stringListToStr(List strings) {
        if (strings == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            if (i < strings.size() - 1) sb.append("");
        }
        return sb.toString();
    }

    /**
	 * ��String���� ת��ΪLong[]�����顣
	 * 
	 * @param longList
	 *            ��Long��ԭ�ص�List
	 * @return Long[]
	 */
    public static Long[] stringArrToLongArr(String[] strings) {
        if (strings == null) {
            return new Long[0];
        }
        Long[] target = new Long[strings.length];
        for (int i = 0; i < strings.length; i++) {
            target[i] = new Long(strings[i]);
        }
        return target;
    }

    /**
	 * ��Long����Listת��ΪLong[]�����顣
	 * 
	 * @param longList
	 *            ��Long��ԭ�ص�List
	 * @return Long[]
	 */
    public static Long[] longListToLongArr(List longList) {
        if (longList == null) {
            return new Long[0];
        }
        Long[] target = new Long[longList.size()];
        for (int i = 0; i < longList.size(); i++) {
            target[i] = (Long) longList.get(i);
        }
        return target;
    }

    /**
	 * ��һ��list of string���string,ʹ��<code>splitStr</code>�ֿ�
	 * 
	 * @param List
	 *            Ҫת����List��
	 * @param splitStr
	 *            �����ַ�
	 * @return String �Իس���\n�ֿ����String
	 */
    public static String listToString(List lst, String splitStr) {
        String str = "";
        if (isEmpty(lst)) {
            return str;
        }
        for (int i = 0; i < lst.size(); i++) {
            str = str.concat(lst.get(i).toString()).concat(splitStr);
        }
        return str;
    }

    /**
	 * ��Listת��ΪBigDecimal[]�����顣
	 * 
	 * @param bigDecimalList
	 *            ��BigDecimal��ԭ�ص�List
	 * @return BigDecimal[]
	 */
    public static BigDecimal[] stringListToBigDecimalArr(List bigDecimalList) {
        if (bigDecimalList == null) {
            return new BigDecimal[0];
        }
        BigDecimal[] target = new BigDecimal[bigDecimalList.size()];
        for (int i = 0; i < bigDecimalList.size(); i++) {
            target[i] = new BigDecimal(bigDecimalList.get(i).toString());
        }
        return target;
    }

    /**
	 * �ж�List�Ƿ�Ϊ�գ�����<code>null</code>��û��һ��Ԫ�ص��жϡ�
	 * 
	 * @param list
	 * @return
	 */
    public static boolean isEmpty(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
	 * �ж�String[]�Ƿ�Ϊ�գ�����<code>null</code>��û��һ��Ԫ�ص��жϡ�
	 * 
	 * @param strArray
	 * @return
	 */
    public static boolean isEmpty(String[] strArray) {
        if (strArray == null || strArray.length <= 0) {
            return true;
        }
        return false;
    }

    /**
	 * �ж�Long[]�Ƿ�Ϊ�գ�����<code>null</code>��û��һ��Ԫ�ص��жϡ�
	 * 
	 * @param longArr
	 * @return
	 */
    public static boolean isEmpty(Long[] longArr) {
        if (longArr == null || longArr.length <= 0) {
            return true;
        }
        return false;
    }

    /**
	 * �ж�Map�Ƿ�Ϊ�գ�����<code>null</code>��û��һ��Ԫ�ص��жϡ�
	 * 
	 * @param map
	 * @return
	 */
    public static boolean isEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
	 * �ж�Set�Ƿ�Ϊ�գ�����<code>null</code>��û��һ��Ԫ�ص��жϡ�
	 * 
	 * @param map
	 * @return
	 */
    public static boolean isEmpty(Set set) {
        if (set == null || set.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
	 * ������ת����List��
	 * 
	 * @param objs
	 * @return
	 */
    public static List arrayToList(Object[] objs) {
        List ret = new ArrayList();
        if (objs == null || objs.length <= 0) {
            return ret;
        }
        for (int i = 0; i < objs.length; i++) {
            ret.add(objs[i]);
        }
        return ret;
    }

    public static List stringArrayToLongList(String[] objs) {
        List ret = new ArrayList();
        if (objs == null || objs.length <= 0) {
            return ret;
        }
        for (int i = 0; i < objs.length; i++) {
            Long longobj = new Long(objs[i]);
            ret.add(longobj);
        }
        return ret;
    }

    public static List defaultIfNull(List list) {
        if (list == null) {
            return new ArrayList();
        }
        return list;
    }

    /**
	 * ��һ��<code>elmLst</code>��<code>orgLst</code>�г�ȥ
	 * 
	 * @param elmLst
	 *            the List Ҫ����ȥ�Ĳ���
	 * @param orgLst
	 *            the List ԭʼlist
	 * @return ʣ���list
	 */
    public static List removeList(List elmLst, List orgLst) {
        List rst = new ArrayList();
        if (orgLst != null && elmLst != null && elmLst.size() > 0) {
            for (int i = 0; i < orgLst.size(); i++) {
                if (!elmLst.contains(orgLst.get(i))) {
                    rst.add(orgLst.get(i));
                }
            }
            return rst;
        } else if (elmLst != null && elmLst.size() == 0) {
            return orgLst;
        }
        return rst;
    }

    /**
	 * ��Setת��ΪList
	 * 
	 * @param set
	 * @return
	 */
    public static List setToList(Set set) {
        List list = new ArrayList();
        list.addAll(set);
        return list;
    }

    /**
	 * ��Object[]�е����ת��ΪList
	 * @param Object[] objArray
	 * @return List
	 */
    public static List Array2List(Object[] objArray) {
        List vList = new ArrayList();
        for (int i = 0; i < objArray.length; i++) {
            Object obj = objArray[i];
            vList.add(obj);
        }
        return vList;
    }

    /**
	 * ��Object[]�е����ת��Ϊ��<code>,</code>�ֿ����ַ�
	 * @param objArray
	 * @return String
	 */
    public static String Array2String(Object[] objArray) {
        if ((objArray == null) || (objArray.length == 0)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < objArray.length; i++) {
            String value = (String) objArray[i];
            sb.append(value).append(',');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
	 * ��Object[]�е����ת��Ϊ��<code>,</code>�ֿ����ַ�
	 * @param objArray
	 * @return String
	 */
    public static String LongArray2String(Object[] objArray) {
        if ((objArray == null) || (objArray.length == 0)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < objArray.length; i++) {
            Long value = (Long) objArray[i];
            sb.append(value.toString()).append(',');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
	 * ��collection�е�ids���ת��Ϊ��<code>,</code>�ֿ����ַ�
	 * @param ids
	 * @return String 
	 */
    public static String List2String(Collection ids) {
        if ((ids == null) || ids.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Iterator it = ids.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (obj != null) {
                sb.append(obj).append(',');
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
	 * �����б?����Ĳ�����\n�ָ�
	 * 
	 * @param str
	 *            the input String
	 * @return list of String
	 */
    public static List stringToList(String str) {
        List vList = new ArrayList();
        if (str != null) {
            String[] strArr = str.split("\n");
            for (int i = 0; i < strArr.length; i++) {
                vList.add(StringUtilEx.trim(strArr[i]));
            }
        }
        return vList;
    }

    /**
	 * �����б?����Ĳ�����ָ��
	 * 
	 * @param str
	 *            the input String
	 * @return list of String
	 */
    public static List stringToList(String str, String token) {
        List vList = new ArrayList();
        if (str != null) {
            String[] strArr = str.split(token);
            for (int i = 0; i < strArr.length; i++) {
                vList.add(StringUtilEx.trim(strArr[i]));
            }
        }
        return vList;
    }

    /**
	 * �������ĸ�ʽΪ�� 10 �ʻ�,1.73323 ��Ʒ,1.43434 ����,1.32300 ����,1.32300 ֻ���԰׽�,0.1343252
	 * 
	 * @param text
	 *            the input String
	 * @return �б?����Ϊ�� �ʻ� ��Ʒ ���� ���� ֻ���԰׽�
	 */
    public static List stringToList(String text, String firstToken, String twoToken) {
        List vList = new ArrayList();
        if (text != null) {
            String[] firstStringArray = text.split(firstToken);
            for (int i = 0; i < firstStringArray.length; i++) {
                String[] twoStringArray = firstStringArray[i].split(twoToken);
                vList.add(StringUtilEx.trim(twoStringArray[0]));
            }
        }
        return vList;
    }
}
