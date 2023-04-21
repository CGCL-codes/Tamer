package net.asfun.jvalog.util;

import javax.servlet.http.HttpServletRequest;
import net.asfun.jvalog.common.InteractException;

public class RequestUtil {

    public static String getRequiredParam(HttpServletRequest req, String name) {
        String param = req.getParameter(name);
        if (param == null || "".equals(param.trim())) {
            throw new InteractException(name + " is required.");
        }
        return param.trim();
    }

    public static String getStringParam(HttpServletRequest req, String name, String defau) {
        String param = req.getParameter(name);
        if (param == null || "".equals(param.trim())) {
            param = defau;
        }
        return param.trim();
    }

    public static int getIntParam(HttpServletRequest req, String name) {
        String num = getStringParam(req, name, "0");
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            throw new InteractException(name + " is not an int value.");
        }
    }

    public static String getStringParam(HttpServletRequest req, String name) {
        return getStringParam(req, name, "");
    }

    public static Long getLongParam(HttpServletRequest req, String name) {
        String num = getRequiredParam(req, name);
        return Long.decode(num);
    }

    public static Long[] getLongParams(HttpServletRequest req, String name) {
        String[] params = req.getParameterValues(name);
        if (params == null) {
            return new Long[0];
        }
        Long[] ls = new Long[params.length];
        int j = 0;
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null || "".equals(params[i].trim()) || !params[i].matches("\\d+")) {
                continue;
            }
            ls[j] = Long.decode(params[i]);
            j++;
        }
        Long[] res = new Long[j];
        System.arraycopy(ls, 0, res, 0, j);
        return res;
    }

    public static boolean isAjaxRequest(HttpServletRequest req) {
        String xrw = req.getHeader("x-requested-with");
        return xrw != null;
    }
}
