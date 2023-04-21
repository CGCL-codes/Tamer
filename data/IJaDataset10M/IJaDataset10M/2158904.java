package com.cateshop.def;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.ResponseUtils;

/**
 * ������ʾ����.
 */
public abstract class HTMLSupport {

    /**
     * <code>html</code>����<code>name</code>.
     */
    protected static final String NAME = "name";

    /**
     * <code>html</code>����<code>id</code>.
     */
    protected static final String ID = "id";

    /**
     * <code>html</code>����<code>class</code>.
     */
    protected static final String CLASS = "class";

    /**
     * <code>html</code>����<code>name</code>.
     */
    protected static final String STYLE = "style";

    /**
     * <code>html</code>����<code>value</code>.
     */
    protected static final String VALUE = "value";

    /**
     * <code>html</code>����<code>type</code>.
     */
    protected static final String TYPE = "type";

    /**
     * �س�����.
     */
    protected static final String CRLF = "\r\n";

    /**
     * �������. �������ֵ��Ϊ<code>null</code>�����, ����������.
     * 
     * @param out
     *            ���.
     * @param attributeName
     *            ������.
     * @param attributeValue
     *            ����ֵ.
     * @throws IOException
     */
    protected static void appendAttribute(Appendable out, String attributeName, String attributeValue) throws IOException {
        if (attributeValue != null) {
            out.append(" " + attributeName + "=\"");
            out.append(attributeValue);
            out.append("\"");
        }
    }

    /**
     * �򿪱�ǩͷ.
     * 
     * @param out
     * @param tagName
     *            ��ǩ��.
     * @throws IOException
     */
    protected static void begin(Appendable out, String tagName) throws IOException {
        out.append("<" + tagName);
    }

    /**
     * �رձ�ǩͷ.
     * 
     * @param out
     * @throws IOException
     */
    protected static void close(Appendable out) throws IOException {
        out.append(">");
    }

    /**
     * �����ǩβ.
     * 
     * @param out
     * @param tagName
     *            ��ǩ��.
     * @throws IOException
     */
    protected static void end(Appendable out, String tagName) throws IOException {
        out.append("</" + tagName + ">");
    }

    /**
     * �����ַ��е�<code>html</code>�ؼ��ֺ�<code>null</code>ֵ.
     * 
     * @param value
     *            Ҫ���˵�ֵ.
     * @param filterNull
     *            �Ƿ�<code>null</code>ֵ����Ϊ���ַ�.
     * @return String
     */
    protected static String filter(String value, boolean filterNull) {
        return (value == null) ? (filterNull ? "" : null) : ResponseUtils.filter(value);
    }

    /**
     * ���������ֽ���Ϊѡ��. ѡ��֮��ʹ�ûس�(<code>\r\n</code>)�ָ�, ѡ��ı�ǩ��ֵ֮��ʹ�õȺ�(<code>=</code>)�ָ�.
     * 
     * @param text
     *            ��������.
     * @return ѡ���б�.
     */
    protected static List<LabelValueBean> parseOptions(String text) {
        ArrayList<LabelValueBean> options = new ArrayList<LabelValueBean>();
        if (text != null && !"".equals(text)) {
            String[] optionExpressions = text.split("\\r\\n");
            for (String optionExpression : optionExpressions) {
                String[] strings = optionExpression.split("=");
                if (strings.length == 1) {
                    options.add(new LabelValueBean(strings[0], strings[0]));
                } else if (strings.length == 2) {
                    options.add(new LabelValueBean(strings[0], strings[1]));
                }
            }
        }
        return options;
    }

    /**
     * <code>html</code>����<code>id</code>.
     */
    protected String id;

    /**
     * <code>html</code>����<code>class</code>.
     */
    protected String clazz;

    /**
     * <code>html</code>����<code>style</code>.
     */
    protected String style;

    /**
     * ���ô�������.
     * 
     * @param name
     *            ������.
     * @param value
     *            ����ֵ.
     */
    public final void configure(String name, String value) {
        if (ID.equals(name)) {
            id = value;
        } else if (CLASS.equals(name)) {
            clazz = value;
        } else if (STYLE.equals(name)) {
            style = value;
        } else {
            configure0(name, value);
        }
    }

    /**
     * ���<code>html</code>����<code>id/class/style</code>.
     * 
     * @param out
     * @throws IOException
     */
    protected void appendCommonAttributes(Appendable out) throws IOException {
        appendAttribute(out, ID, id);
        appendAttribute(out, CLASS, clazz);
        appendAttribute(out, STYLE, style);
    }

    /**
     * ��������.
     * 
     * @param name
     *            ������.
     * @param value
     *            ����ֵ.
     */
    @SuppressWarnings("unused")
    protected void configure0(String name, String value) {
    }
}
