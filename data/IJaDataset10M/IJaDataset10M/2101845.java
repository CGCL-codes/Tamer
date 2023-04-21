package net.excel.report.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO :ģ���﷨���������࣬���������Զ����ģ���﷨���з��������װ��������ģ������й�
 * �ĺ����ģ�嶨����صĹؼ��ֺ��﷨�������ּ�����һ�𣬱��ڹ����ά����
 * @author juny
 * @since 1.0
 */
public final class AnalyseTempletTool {

    /**
     * �жϴ���������Ƿ���һ��ģ��Ԫ��
     * @param content ģ�嶨��Ԫ�����ݡ����ַ�������
     * @return true����ǰ����������һ��ģ��Ԫ�ض��������� <br>
     * false��ǰ�������ݲ���һ��ģ��Ԫ�ض���������
     */
    public static boolean isTempletElement(String content) {
        if (content.startsWith("$") && content.endsWith("}")) {
            return true;
        }
        return false;
    }

    /**
     * �жϴ����ַ��Ƿ���һ���ֶ����͵�ģ��Ԫ�ض��塣
     * @param templet ģ�嶨�����ݡ�
     * @return true �����ַ���һ���ֶ���ģ��Ԫ�ض��塣<br>
     * false �����ַ���һ���ֶ���ģ��Ԫ�ض��塣
     * @since 1.0
     */
    public static boolean isFieldTemplet(String templet) {
        if (templet.startsWith(KEY_ELEMENT_FIELD)) {
            return true;
        }
        return false;
    }

    /**
     * �жϴ����ַ��Ƿ���һ���������͵�ģ��Ԫ�ض��塣
     * @param templet ģ�嶨�����ݡ�
     * @return true �����ַ���һ��������ģ��Ԫ�ض��塣<br>
     * false �����ַ���һ��������ģ��Ԫ�ض��塣
     * @since 1.0
     */
    public static boolean isArgumentTemplet(String templet) {
        if (templet.startsWith(KEY_ELEMENT_ARGUMENT)) {
            return true;
        }
        return false;
    }

    /**
     * �жϴ����ַ��Ƿ���һ���������͵�ģ��Ԫ�ض��塣
     * @param templet ģ�嶨�����ݡ�
     * @return true �����ַ���һ��������ģ��Ԫ�ض��塣<br>
     * false �����ַ���һ��������ģ��Ԫ�ض��塣
     * @since 1.0
     */
    public static boolean isVariableTemplet(String templet) {
        if (templet.startsWith(KEY_ELEMENT_VARIABLE)) {
            return true;
        }
        return false;
    }

    /**
     * �жϴ����ַ��Ƿ���һ��Band���͵�ģ��Ԫ�ض��塣
     * @param templet ģ�嶨�����ݡ�
     * @return true �����ַ���һ��Band��ģ��Ԫ�ض��塣<br>
     * false �����ַ���һ��Band��ģ��Ԫ�ض��塣
     * @since 1.0
     */
    public static boolean isBandTemplet(String templet) {
        if (templet.startsWith(KEY_ELEMENT_BAND)) {
            return true;
        }
        return false;
    }

    /**
     * �жϴ����ַ��Ƿ���һ��Group���͵�ģ��Ԫ�ض��塣
     * @param templet ģ�嶨�����ݡ�
     * @return true �����ַ���һ��Group��ģ��Ԫ�ض��塣<br>
     * false �����ַ���һ��Group��ģ��Ԫ�ض��塣
     * @since 1.0
     */
    public static boolean isGroupTemplet(String templet) {
        if (templet.startsWith(KEY_ELEMENT_GROUP)) {
            return true;
        }
        return false;
    }

    /**
     * �жϴ����ַ��Ƿ���head���������������head���������ڴ�?ǰBand/Group Head
     * ���ֵĿ�ʼ���ú������Ȼ��鴫�������Ƿ���һ��Band��Group��ģ��������������ֱ�ӷ���
     * false��
     * @param templet ����ģ�嶨�����ݡ�
     * @return true ��ǰ����ģ����head��<br>
     * false ��ǰ����ģ�岻��head.
     * @since 1.0
     */
    public static boolean isHead(String templet) {
        if (isBandTemplet(templet) || isGroupTemplet(templet)) {
            if (getPorperty(templet, PROPERTY_PROPE).equals(BAND_HEAD)) {
                return true;
            }
        }
        return false;
    }

    /**
     * �жϴ����ַ��Ƿ���body���������������body���������ڴ�?ǰBand/Group body
     * ���ֵĿ�ʼ���ú������Ȼ��鴫�������Ƿ���һ��Band��Group��ģ��������������ֱ�ӷ���
     * false��
     * @param templet ����ģ�嶨�����ݡ�
     * @return true ��ǰ����ģ����body��<br>
     * false ��ǰ����ģ�岻��body.
     * @since 1.0
     */
    public static boolean isDetail(String templet) {
        if (isBandTemplet(templet) || isGroupTemplet(templet)) {
            if (getPorperty(templet, PROPERTY_PROPE).equals(BAND_BODY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * �жϴ����ַ��Ƿ���foot���������������foot���������ڴ�?ǰBand/Group foot
     * ���ֵĿ�ʼ���ú������Ȼ��鴫�������Ƿ���һ��Band��Group��ģ��������������ֱ�ӷ���
     * false��
     * @param templet ����ģ�嶨�����ݡ�
     * @return true ��ǰ����ģ����foot��<br>
     * false ��ǰ����ģ�岻��foot.
     * @since 1.0
     */
    public static boolean isBottom(String templet) {
        if (isBandTemplet(templet) || isGroupTemplet(templet)) {
            if (getPorperty(templet, PROPERTY_PROPE).equals(BAND_FOOT)) {
                return true;
            }
        }
        return false;
    }

    /**
     * �жϴ����ַ��Ƿ���end���������������end���������ڴ�?ǰBand/Group end
     * ���ֵĿ�ʼ���ú������Ȼ��鴫�������Ƿ���һ��Band��Group��ģ��������������ֱ�ӷ���
     * false��
     * @param templet ����ģ�嶨�����ݡ�
     * @return true ��ǰ����ģ����end��<br>
     * false ��ǰ����ģ�岻��end.
     * @since 1.0
     */
    public static boolean isEnd(String templet) {
        if (isBandTemplet(templet) || isGroupTemplet(templet)) {
            if (getPorperty(templet, PROPERTY_PROPE).equals(BAND_END)) {
                return true;
            }
        }
        return false;
    }

    /**
     * �Ӵ���ģ�嶨�����ݣ�templet����ȡ��ָ�����Ե�����ֵ��
     * @param templet ģ�嶨������
     * @param propertyName �������
     * @return ����ȡ�õ�����ֵ
     * @since 1.0
     */
    public static String getPorperty(String templet, byte propertyName) {
        String strRet = null;
        switch(propertyName) {
            case PROPERTY_NAME:
                strRet = getSubInfo(templet, REGEX_NAME);
                break;
            case PROPERTY_PROPE:
                strRet = getSubInfo(templet, REGEX_PROPERTY);
                break;
            case PROPERTY_DATASOURCE:
                strRet = getSubInfo(templet, REGEX_DATASOURCE);
                break;
            default:
                ;
        }
        if (null != strRet) {
            strRet = getSubInfo(strRet, REGEX_VALUE);
            if (!"".equals(strRet)) {
                strRet = strRet.substring(1, strRet.length() - 1).trim();
            }
        } else {
            strRet = "";
        }
        return strRet;
    }

    /**
     * ����������ʽ���������ַ��е�һ���Ӵ�������������searchRegex��ָ����
     * @param content ������Ҫ���ҵ��ַ�
     * @param searchRegex ���������������һ��������ʽ�������ַ���
     * @return �ҵ����ز��ҵ��Ľ��
     */
    public static String getSubInfo(String content, String searchRegex) {
        Matcher matcher = Pattern.compile(searchRegex).matcher(content);
        if (matcher.find()) {
            return content.substring(matcher.start(), matcher.end());
        }
        return "";
    }

    /**
     * ����������ʽ���������ַ��е�һ���Ӵ�������������searchRegex��ָ����
     * ����getSubInfo(String, String)��Ҫ������ڣ�����ֻ�ܷ���һ�����
     * @param content ������Ҫ���ҵ��ַ�
     * @param searchRegex ���������������һ��������ʽ�������ַ���
     * @return �ҵ��򷵻ز��ҵ��Ľ������һ����List����Ľ���б?
     */
    public static List getAllSubInfo(String content, String searchRegex) {
        Matcher matcher = Pattern.compile(searchRegex).matcher(content);
        ArrayList list = new ArrayList();
        while (matcher.find()) {
            list.add(content.substring(matcher.start(), matcher.end()));
        }
        return list;
    }

    /**
     * ȡ��ģ��������������ݡ�
     * @param elementTemplet ����ģ�嶨�����ݡ�
     * @return ����ģ�嶨���ʵ�����ݣ�ȥ��������
     */
    public static String getElementDeclare(String elementTemplet) {
        return elementTemplet.substring(3, elementTemplet.length() - 1);
    }

    /**
     * �ж�һ��ģ������Ƿ����˱�����ơ�ÿ��ģ����������Զ���һ������
     * �Ա�������ط�����ͨ���������øñ��ֵ��
     * @param content ����ģ�嶨�����ݡ�
     * @return true ��ǰģ����ڱ�����<br>
     * 
     */
    public static boolean haveVariableName(StringBuffer content) {
        return content.charAt(0) == KEY_FIELD_NAME_DEFINE_BEGIN;
    }

    /**
     * ȡ�ñ���ģ���ж���ı������
     * @param content ģ�嶨������
     * @return ���ģ�嶨���˱����򷵻ظñ���ɾ��ԭģ�嶨�������ж������֡�<br>
     * ���򷵻�null��
     */
    public static String getVariableName(StringBuffer content) {
        int pos = content.lastIndexOf(AnalyseTempletTool.KEY_FIELD_NAME_DEFINE);
        String name = null;
        if (-1 != pos) {
            name = content.substring(pos + 3, content.length()).trim();
        }
        content.deleteCharAt(0);
        pos = content.lastIndexOf("}");
        if (-1 != pos) {
            content.delete(pos, content.length());
        }
        return name;
    }

    private static final String KEY_ELEMENT_FIELD = "$F{";

    private static final String KEY_ELEMENT_ARGUMENT = "$P{";

    private static final String KEY_ELEMENT_VARIABLE = "$V{";

    private static final String KEY_ELEMENT_BAND = "$B{";

    private static final String KEY_ELEMENT_GROUP = "$G{";

    private static final String REGEX_NAME = "[nN]ame\\s*=.*?[;\\}]";

    private static final String REGEX_PROPERTY = "[pP]roperty\\s*=.*?[;\\}]";

    private static final String REGEX_DATASOURCE = "[dD]ata[sS]ource\\s*=.*?[;\\}]";

    private static final String REGEX_VALUE = "=.*[;\\}]";

    public static final String REGEX_FIELD_TEMPLET = "\\$[fFpPvVbBgG]\\{.*?\\}";

    public static final byte PROPERTY_NAME = 1;

    public static final byte PROPERTY_PROPE = 2;

    public static final byte PROPERTY_DATASOURCE = 3;

    public static final String DATASOURCE_FIELD_SEPARATOR_REGEX = "\\.";

    public static final String DATASOURCE_FIELD_SEPARATOR = ".";

    public static final String BAND_HEAD = "head";

    public static final String BAND_BODY = "body";

    public static final String BAND_FOOT = "foot";

    public static final String BAND_END = "end";

    public static final String REGEX_FIELD_BODY = "\\{.*\\}";

    public static final String KEY_FIELD_NAME_DEFINE = "as ";

    public static final char KEY_FIELD_NAME_DEFINE_BEGIN = '{';
}
