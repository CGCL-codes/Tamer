package com.googlecode.mycontainer.commons.servlet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getAttributes(HttpServletRequest req) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Enumeration<String> en = req.getAttributeNames();
        while (en.hasMoreElements()) {
            String key = en.nextElement();
            Object value = req.getAttribute((String) key);
            ret.put(key, value);
        }
        return ret;
    }

    public static String getUserPath(HttpServletRequest request) {
        String servetPath = request.getServletPath();
        return getUserPath(request, "^" + servetPath);
    }

    public static String getUserPath(HttpServletRequest request, String ignore) {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String starts = "";
        if (!contextPath.equals("/")) {
            starts += contextPath;
        }
        String ret = requestURI.substring(starts.length());
        ret = ret.replaceAll(ignore, "");
        return ret;
    }

    public static void checkMethods(HttpServletRequest request, HttpServletResponse resp, String msg, String... alloweds) {
        for (String allowed : alloweds) {
            if (allowed.equals(request.getMethod())) {
                return;
            }
        }
        sendUnsupportedMethod(resp, msg, alloweds);
    }

    public static void sendUnsupportedMethod(HttpServletResponse resp, String msg, String... alloweds) {
        for (String allowed : alloweds) {
            resp.addHeader("Allow", allowed);
        }
        sendError(resp, HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
    }

    public static void sendError(HttpServletResponse resp, int code, String msg) {
        try {
            if (msg == null) {
                resp.sendError(code);
            } else {
                resp.sendError(code, msg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String checkParameter(HttpServletRequest req, HttpServletResponse resp, String name, String... requires) {
        String value = req.getParameter(name);
        if (value == null) {
            value = "";
        }
        value = value.trim();
        for (String require : requires) {
            if (require.equals(value)) {
                return (require.length() == 0 ? requires[0] : require);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("parameter required '").append(name).append("'");
        if (requires.length > 0) {
            sb.append(" with one of these: ").append(Arrays.toString(requires));
        } else {
            sb.append(" with any value");
        }
        sb.append(", but was: '").append(value).append("'");
        throw new RuntimeException(sb.toString());
    }

    public static <T> List<T> getParameters(HttpServletRequest req, String name, Class<T> clazz) {
        try {
            String[] values = req.getParameterValues(name);
            if (values == null) {
                return new ArrayList<T>();
            }
            ArrayList<T> ret = new ArrayList<T>(values.length);
            for (String value : values) {
                Constructor<T> cons = clazz.getConstructor(new Class[] { String.class });
                T parsed = cons.newInstance(value);
                ret.add(parsed);
            }
            return ret;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> getHeaders(Map<String, List<String>> ret, HttpServletRequest request) {
        if (ret == null) {
            ret = new HashMap<String, List<String>>();
        }
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Enumeration<String> headers = request.getHeaders(name);
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                List<String> list = ret.get(name);
                if (list == null) {
                    list = new ArrayList<String>();
                    ret.put(name, list);
                }
                list.add(header);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> getParameters(Map<String, List<String>> ret, HttpServletRequest request) {
        if (ret == null) {
            ret = new HashMap<String, List<String>>();
        }
        Set<Entry<String, String[]>> entries = request.getParameterMap().entrySet();
        for (Entry<String, String[]> entry : entries) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            List<String> list = ret.get(key);
            if (list == null) {
                list = new ArrayList<String>();
                ret.put(key, list);
            }
            list.addAll(Arrays.asList(values));
        }
        return ret;
    }

    public static void setHeaders(HttpServletResponse response, Map<String, List<String>> headers) {
        Set<Entry<String, List<String>>> set = headers.entrySet();
        for (Entry<String, List<String>> entry : set) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            for (String value : values) {
                if (value != null) {
                    response.addHeader(key, value);
                }
            }
        }
    }

    public static void write(HttpServletResponse response, char[] array) {
        try {
            response.getWriter().write(array);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(HttpServletResponse response, byte[] content) {
        try {
            response.getOutputStream().write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
