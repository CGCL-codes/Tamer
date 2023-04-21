package self.micromagic.util;

import java.io.UnsupportedEncodingException;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;

/**
 * <code>StringTool</code>����ʵ��һЩ���õ��ַ���. <p>
 *
 * @author  micromagic
 * @version 1.0, 2002-10-18
 */
public class StringTool {

    public static final int MAX_INTERN_SIZE = 1024 * 8;

    /**
    * ���ַ����intern����. <p>
    * ���������ַ�Ϊnull, ��ֱ�ӷ���null�����������ַ�
    * �ȳ���������, �򲻽���intern���?
    *
    * @param str           Ҫintern������ַ�
    * @param newOnBeyond   �����������Ƿ�Ҫ���¹����ַ�
    * @return              ��������ַ�
    */
    public static String intern(String str, boolean newOnBeyond) {
        if (str == null) {
            return null;
        }
        return str.length() > MAX_INTERN_SIZE ? (newOnBeyond ? new String(str) : str) : str.intern();
    }

    /**
    * ���ַ����intern����. <p>
    * ���������ַ�Ϊnull, ��ֱ�ӷ���null�����������ַ�
    * �ȳ���������, �򲻽���intern���?
    *
    * @param str    Ҫintern������ַ�
    * @return       ��������ַ�
    */
    public static String intern(String str) {
        return intern(str, false);
    }

    /**
    * ���ָ���ķָ���<code>delimiter</code>�ָ�һ���ַ�
    * <code>str</code>. <p>
    * ��������ַ�<code>str</code>Ϊ<code>null</code>���մ�
    * <code>""</code>���򷵻�<code>null</code>��ֻ��ָ���
    * <code>delimiter</code>���򷵻س���Ϊ0���ַ����顣�������
    * �ַ�<code>str</code>���Էָ�������������֮�У��򷵻�һ��
    * �ַ����飬ÿ����Ԫ��һ�����ָ��������ַ�
    * <p>
    * ���磺
    * <blockquote><pre>
    * StringProcessor.separateString("һ,��,��", ",")
    *         ���� {"һ", "��", "��"}
    * StringProcessor.separateString("��ã���Һã��Һܺá�", "����")
    *         ���� {"���", "��Һ�", "�Һܺá�"}
    * StringProcessor.separateString(null, ", \t\r\n")
    *         ���� null
    * StringProcessor.separateString(", , , , ", ", \n")
    *         ���� {}
    * </pre></blockquote>
    *
    * @param str         Ҫ���зָ���ַ�
    * @param delimiter   �ָ����
    * @return  �ָ����ַ�����ɵ�����
    */
    public static String[] separateString(String str, String delimiter) {
        return separateString(str, delimiter, false);
    }

    /**
    * ���ָ���ķָ���<code>delimiter</code>�ָ�һ���ַ�
    * <code>str</code>. <p>
    *
    * @param str         Ҫ���зָ���ַ�
    * @param delimiter   �ָ����
    * @param trim        �Ƿ�Ҫtrim�ָ������ÿ���ַ�
    * @return  �ָ����ַ�����ɵ�����
    */
    public static String[] separateString(String str, String delimiter, boolean trim) {
        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiter);
        int count = st.countTokens();
        if (count == 0) {
            return new String[0];
        }
        String[] bolck = new String[count];
        for (int i = 0; i < count; i++) {
            bolck[i] = st.nextToken();
        }
        if (trim) {
            for (int i = 0; i < count; i++) {
                bolck[i] = bolck[i].trim();
            }
        }
        return bolck;
    }

    /**
    * ��<code>src</code>�ַ��е�����<code>oldStr</code>�滻Ϊ
    * <code>newStr</code>. <p>
    * ���<code>oldStr</code>û����ԭ�ַ��г���, �򷵻�ԭ�ַ�.
    *
    * @param src           ԭ�ַ�
    * @param oldStr        Ҫ�滻���Ӵ�
    * @return              �滻����ַ�
    */
    public static String replaceAll(String src, String oldStr, String newStr) {
        if (src == null) {
            return null;
        }
        if (oldStr == null || oldStr.length() == 0) {
            return src;
        }
        if (newStr == null) {
            newStr = "";
        }
        int index = src.indexOf(oldStr);
        if (index == -1) {
            return src;
        }
        StringBuffer result = new StringBuffer(src.length() + (newStr.length() - oldStr.length()) * 4 + 16);
        do {
            result.append(src.substring(0, index));
            result.append(newStr);
            src = src.substring(index + oldStr.length());
            index = src.indexOf(oldStr);
        } while (index != -1);
        result.append(src);
        return result.toString();
    }

    /**
    * ���ַ�����<code>arr</code>�����ӷ�<code>link</code>���ӳ�һ���ַ�. <p>
    *
    * @param arr    Ҫ���ӵ��ַ�����
    * @param link   ���ӷ�
    * @return       �����ӷ����Ӻ���ַ�
    */
    public static String linkStringArr(String[] arr, String link) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        if (arr.length == 1) {
            return arr[0];
        }
        link = link == null ? "" : link;
        StringBuffer buf = new StringBuffer(arr.length * (link.length() + 16));
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                buf.append(link);
            }
            buf.append(arr[i]);
        }
        return buf.toString();
    }

    /**
    * ���ַ���һ���ĸ�ʽת����Map. <p>
    *
    * @param str              Ҫת����Map���ַ�
    * @param itemDelimiter    MapԪ�صķָ����
    * @param kvDelimiter      key��value�ķָ���
    * @return          ת�����Map����
    */
    public static Map string2Map(String str, String itemDelimiter, char kvDelimiter) {
        return string2Map(str, itemDelimiter, kvDelimiter, true, true, null, null);
    }

    /**
    * ���ַ���һ���ĸ�ʽת����Map. <p>
    *
    * @param str              Ҫת����Map���ַ�
    * @param itemDelimiter    MapԪ�صķָ����
    * @param kvDelimiter      key��value�ķָ���
    * @param trimItem         �Ƿ�Ҫ��ÿ��Ԫ�ؽ���trim
    * @param needResolve      �Ƿ�Ҫ�����ı���"${...}"�Ķ�̬����
    * @param resolveRes       ���?̬�����ǰ󶨵���Դ
    * @param result           ��ת���Ľ������Map��
    * @return          ת�����Map����
    */
    public static Map string2Map(String str, String itemDelimiter, char kvDelimiter, boolean trimItem, boolean needResolve, Map resolveRes, Map result) {
        if (str == null) {
            return null;
        }
        if (result == null) {
            result = new HashMap();
        }
        if (needResolve) {
            str = Utility.resolveDynamicPropnames(str, resolveRes);
        }
        String[] arr = StringTool.separateString(str, itemDelimiter, trimItem);
        for (int i = 0; i < arr.length; i++) {
            int index = arr[i].indexOf(kvDelimiter);
            if (index != -1) {
                String k = arr[i].substring(0, index);
                String v = arr[i].substring(index + 1);
                result.put(trimItem ? k.trim() : k, trimItem ? v.trim() : v);
            } else if (arr[i].length() > 0) {
                result.put(trimItem ? arr[i].trim() : arr[i], "");
            }
        }
        return result;
    }

    public static void printStringHaxcode(PrintStream out, String str, boolean endline) {
        if (str == null) {
            return;
        }
        int count = str.length();
        for (int i = 0; i < count; i++) {
            out.print(Integer.toHexString(str.charAt(i)));
        }
        if (endline) {
            out.println();
        }
    }

    /**
    * ��ISO8859-1��ʽ���ַ�<code>str</code>ת��ΪUnicode�����ʽ
    * ���ַ�. <p>
    * ����Java���ַ���Unicode�����ʽ�����������ʽ���ַ���Ҫ��
    * ����и�ʽת��������ͻ��ڴ洢����ʾ��ʱ��������롣
    *
    * @param   str    Ҫ���б����ʽת�����ַ�
    * @return  ת����Unicode�����ʽ���ַ�
    */
    public static String decodeFrom8859_1(String str) {
        if (str == null) {
            return null;
        }
        try {
            String decodeStr;
            byte[] temp = str.getBytes("8859_1");
            decodeStr = new String(temp);
            return decodeStr;
        } catch (UnsupportedEncodingException uee) {
            throw new InternalError();
        }
    }

    /**
    * ��һ��ISO8859-1��ʽ���ַ�<code>str</code>ת��ΪUnicode�����ʽ
    * ���ַ�. <p>
    * ����Java���ַ���Unicode�����ʽ�����������ʽ���ַ���Ҫ��
    * ����и�ʽת��������ͻ��ڴ洢����ʾ��ʱ��������롣
    *
    * @param   astr    Ҫ���б����ʽת�����ַ�����
    * @return  ת����Unicode�����ʽ���ַ�����
    */
    public static String[] decodeFrom8859_1(String[] astr) {
        String[] decodeValues = new String[astr.length];
        for (int i = 0; i < decodeValues.length; i++) {
            decodeValues[i] = decodeFrom8859_1(astr[i]);
        }
        return decodeValues;
    }

    /**
    * ��Unicode��ʽ���ַ�<code>str</code>ת��ΪISO8859-1�����ʽ
    * ���ַ�. <p>
    * ����Java���ַ���Unicode�����ʽ���������豸��ҪISO8859-1
    * �����ʽ�Ļ������������и�ʽת��������ͻ�����ʾ���ĵ�ʱ��
    * �������롣
    *
    * @param   str    Ҫ���б����ʽת�����ַ�
    * @return  ת����ISO8859-1�����ʽ���ַ�
    */
    public static String encodeTo8859_1(String str) {
        if (str == null) {
            return null;
        }
        try {
            String encodeStr;
            byte[] temp = str.getBytes();
            encodeStr = new String(temp, "8859_1");
            return encodeStr;
        } catch (UnsupportedEncodingException uee) {
            throw new InternalError();
        }
    }

    /**
    * ��һ��Unicode��ʽ���ַ�<code>str</code>ת��ΪISO8859-1�����ʽ
    * ���ַ�. <p>
    * ����Java���ַ���Unicode�����ʽ���������豸��ҪISO8859-1
    * �����ʽ�Ļ������������и�ʽת��������ͻ�����ʾ���ĵ�ʱ��
    * �������롣
    *
    * @param   astr    Ҫ���б����ʽת�����ַ�����
    * @return  ת����ISO8859-1�����ʽ���ַ�����
    */
    public static String[] encodeTo8859_1(String[] astr) {
        String[] encodeValues = new String[astr.length];
        for (int i = 0; i < encodeValues.length; i++) {
            encodeValues[i] = encodeTo8859_1(astr[i]);
        }
        return encodeValues;
    }
}
