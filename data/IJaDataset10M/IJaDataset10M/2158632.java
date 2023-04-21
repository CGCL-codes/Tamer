package net.sf.chineseutils.web;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * The <code>RequestWrapper</code> revert the request parameter values.
 * 
 * @author ½�ݹ�
 * @version $Id: RequestWrapper.java 50 2006-08-31 15:02:13Z fantasy4u $
 * 
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private ChineseConverter converter;

    /**
	 * @param request
	 */
    public RequestWrapper(HttpServletRequest request, ChineseConverter converter) {
        super(request);
        this.converter = converter;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        value = converter.revert(value);
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        for (String value : values) {
            value = converter.revert(value);
        }
        return values;
    }

    @Override
    public Map getParameterMap() {
        return super.getParameterMap();
    }

    @Override
    public String getQueryString() {
        return super.getQueryString();
    }
}
