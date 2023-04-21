package org.riverock.common.tools;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * $Id: ServletTools.java,v 1.8 2006/01/23 17:05:21 serg_main Exp $
 */
public final class ServletTools {

    private static final Logger log = Logger.getLogger(ServletTools.class);

    public static void cleanSession(final HttpSession session) throws Exception {
        if (session == null) return;
        int countLoop = 3;
        for (int i = 0; i < countLoop; i++) {
            try {
                for (Enumeration e = session.getAttributeNames(); e.hasMoreElements(); e = session.getAttributeNames()) {
                    String name = (java.lang.String) e.nextElement();
                    if (log.isDebugEnabled()) log.debug("Attribute: " + name);
                    session.removeAttribute(name);
                }
            } catch (java.util.ConcurrentModificationException e) {
                if (i == countLoop - 1) throw e;
            }
        }
    }

    public static String getHiddenItem(final String name, final String value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\">\n");
    }

    public static String getHiddenItem(final String name, final int value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\">\n");
    }

    public static String getHiddenItem(final String name, final Integer value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + (value != null ? value.longValue() : 0) + "\">\n");
    }

    public static String getHiddenItem(final String name, final long value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\">\n");
    }

    public static String getHiddenItem(final String name, final Long value) {
        return ("<input type=\"hidden\" name=\"" + name + "\" value=\"" + (value != null ? value : 0) + "\">\n");
    }

    public static void immediateRemoveAttribute(final HttpSession session, final String attr) {
        Object obj = session.getAttribute(attr);
        try {
            if (log.isDebugEnabled()) log.debug("#12.12.001 search method 'clearObject'");
            Class cl = obj.getClass();
            Method m = cl.getMethod("clearObject", (Class[]) null);
            if (log.isDebugEnabled()) log.debug("#12.12.002 invoke method 'clearObject'");
            if (m != null) m.invoke(obj, (Object[]) null);
            if (log.isDebugEnabled()) log.debug("#12.12.003 complete invoke method 'clearObject'");
        } catch (Exception e) {
            if (log.isInfoEnabled()) log.info("#12.12.003  method 'clearObject' not found. Error " + e.toString());
        }
        session.removeAttribute(attr);
        obj = null;
    }

    /**
     * ���������� ��������� �������� ����������. ���� ���������� �� ����������������, ���������� ������ ������
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * String def  - ������ �� ���������<br>
     * </blockquote>
     */
    public static String getString(final HttpServletRequest request, final String f, final String def, final String fromCharset, final String toCharset) {
        String s_ = def;
        if (request.getParameter(f) != null) {
            try {
                s_ = StringTools.convertString(request.getParameter(f), fromCharset, toCharset);
            } catch (Exception e) {
            }
        }
        return s_;
    }

    /**
     * ���������� int �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * </blockquote>
     */
    public static Integer getInt(final HttpServletRequest request, final String f) {
        return getInt(request, f, null);
    }

    /**
     * ���������� int �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * int def - �������� �� ��������<br>
     * </blockquote>
     */
    public static Integer getInt(final HttpServletRequest request, final String f, final Integer def) {
        Integer i_ = def;
        if (request.getParameter(f) != null) {
            try {
                String s_ = request.getParameter(f);
                i_ = new Integer(s_);
            } catch (Exception exc) {
                log.warn("Exception in getInt(), def value will be return", exc);
            }
        }
        return i_;
    }

    /**
     * ���������� long �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * </blockquote>
     */
    public static Long getLong(final HttpServletRequest request, final String f) {
        return getLong(request, f, null);
    }

    /**
     * ���������� long �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * long def - �������� �� ��������
     * </blockquote>
     */
    public static Long getLong(final HttpServletRequest request, final String f, final Long def) {
        Long i_ = def;
        if (request.getParameter(f) != null) {
            try {
                String s_ = request.getParameter(f);
                i_ = new Long(s_);
            } catch (Exception exc) {
                log.warn("Exception in getLong(), def value will be return", exc);
            }
        }
        return i_;
    }

    /**
     * ���������� float �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * </blockquote>
     */
    public static Float getFloat(final HttpServletRequest request, final String f) {
        return getFloat(request, f, null);
    }

    /**
     * ���������� float �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * float def - �������� �� ���������
     * </blockquote>
     */
    public static Float getFloat(final HttpServletRequest request, final String f, final Float def) {
        Float i_ = def;
        if (request.getParameter(f) != null) {
            try {
                String s_ = request.getParameter(f);
                s_ = s_.replace(',', '.');
                i_ = new Float(s_);
            } catch (Exception exc) {
                log.warn("Exception in getFloat(), def value will be return", exc);
            }
        }
        return i_;
    }

    public static Double getDouble(final HttpServletRequest request, final String f) {
        return getDouble(request, f, null);
    }

    /**
     * ���������� double �������� ����������. ���� ���������� �� ����������������, ���������� 0
     * ���������:
     * <blockquote>
     * HttpServletRequest request	- ������ ��� request �� ��������� JSP<br>
     * String f - ��� ���������� ��� ��������� ��������<br>
     * double def - �������� �� ���������
     * </blockquote>
     */
    public static Double getDouble(final HttpServletRequest request, final String f, final Double def) {
        Double i_ = def;
        if (request.getParameter(f) != null) {
            try {
                String s_ = request.getParameter(f);
                s_ = s_.replace(',', '.');
                i_ = new Double(s_);
            } catch (Exception exc) {
                log.warn("Exception in getDouble(), def value will be return", exc);
            }
        }
        return i_;
    }

    public static Map<String, Object> getParameterMap(final String parameter) {
        if (parameter == null) return null;
        Map<String, Object> map = new HashMap<String, Object>();
        String s = parameter;
        if (parameter.indexOf('?') != -1) s = parameter.substring(parameter.indexOf('?') + 1); else s = parameter;
        StringTokenizer st = new StringTokenizer(s, "&", false);
        while (st.hasMoreTokens()) {
            String param = st.nextToken();
            int idx = param.indexOf('=');
            if (idx == -1) MainTools.putKey(map, param, ""); else MainTools.putKey(map, param.substring(0, idx), param.substring(idx + 1));
        }
        return map;
    }
}
