package org.allcolor.ywt.adapter.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.allcolor.ywt.filter.CFilterChain;

/**
 * 
 DOCUMENT ME!
 * 
 * @author Quentin Anciaux
 * @version 0.1.0
 */
public class CRequestDispatcher implements RequestDispatcher {

    /**
	 * 
	 DOCUMENT ME!
	 * 
	 * @author Quentin Anciaux
	 * @version 0.1.0
	 */
    public class CHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private Map<String, String[]> map = null;

        private final String parameters;

        /** DOCUMENT ME! */
        private Pattern pathInfo = null;

        /**
		 * Creates a new CHttpServletRequestWrapper object.
		 * 
		 * @param arg0
		 *            DOCUMENT ME!
		 */
        @SuppressWarnings("unchecked")
        public CHttpServletRequestWrapper(final HttpServletRequest arg0, final String parameters) {
            super(arg0);
            this.parameters = parameters;
            if ((parameters != null) && !"".equals(parameters.trim())) {
                this.map = new HashMap<String, String[]>();
                final String[] params = parameters.split("&");
                for (final String param : params) {
                    try {
                        final String[] pair = param.split("=");
                        final String key = URLDecoder.decode(pair[0], "utf-8");
                        final String value = pair.length > 1 ? URLDecoder.decode(pair[1], "utf-8") : "";
                        if (this.map.get(key) == null) {
                            this.map.put(key, new String[] { value });
                        } else {
                            final String[] vals = this.map.get(key);
                            final List<String> list = new ArrayList<String>(Arrays.asList(vals));
                            list.add(value);
                            this.map.put(key, list.toArray(new String[list.size()]));
                        }
                    } catch (final Exception ignore) {
                    }
                }
                final Map<String, String[]> toCopy = super.getParameterMap();
                final Map<String, String[]> toReturn = new HashMap<String, String[]>();
                for (final Map.Entry<String, String[]> entry : toCopy.entrySet()) {
                    toReturn.put(entry.getKey(), entry.getValue());
                }
                for (final Map.Entry<String, String[]> entry : this.map.entrySet()) {
                    toReturn.put(entry.getKey(), entry.getValue());
                }
                this.map = Collections.unmodifiableMap(toReturn);
            }
        }

        public CHttpServletRequestWrapper(HttpServletRequest request, Map<String, String[]> map) {
            super(request);
            this.parameters = null;
            this.map = Collections.unmodifiableMap(map);
        }

        @Override
        public String getParameter(final String name) {
            if (this.map != null) {
                final String[] array = this.map.get(name);
                if ((array != null) && (array.length > 0)) {
                    return array[0];
                }
                return null;
            }
            return super.getParameter(name);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Map getParameterMap() {
            if (this.map != null) {
                return this.map;
            }
            return super.getParameterMap();
        }

        @Override
        @SuppressWarnings("unchecked")
        public Enumeration getParameterNames() {
            if (this.map != null) {
                return new Enumeration<String>() {

                    final Iterator<String> it = CHttpServletRequestWrapper.this.map.keySet().iterator();

                    @Override
                    public boolean hasMoreElements() {
                        return this.it.hasNext();
                    }

                    @Override
                    public String nextElement() {
                        return this.it.next();
                    }
                };
            }
            return super.getParameterNames();
        }

        @Override
        public String[] getParameterValues(final String name) {
            if (this.map != null) {
                return this.map.get(name);
            }
            return super.getParameterValues(name);
        }

        /**
		 * DOCUMENT ME!
		 * 
		 * @return DOCUMENT ME!
		 */
        @Override
        public String getPathInfo() {
            if (this.pathInfo != null) {
                final Matcher m = this.pathInfo.matcher(CRequestDispatcher.this.url);
                if (m.matches()) {
                    if (m.groupCount() > 0) {
                        final StringBuffer buffer = new StringBuffer();
                        for (int i = 1; i <= m.groupCount(); i++) {
                            if (i == 1) {
                                buffer.append(CRequestDispatcher.this.url.substring(0, m.start(i)));
                            } else {
                                buffer.append(CRequestDispatcher.this.url.substring(m.end(i - 1), m.start(i)));
                            }
                            if (i == m.groupCount()) {
                                buffer.append(CRequestDispatcher.this.url.substring(m.end(i)));
                            }
                        }
                        return buffer.toString();
                    }
                }
            }
            return null;
        }

        /**
		 * DOCUMENT ME!
		 * 
		 * @return DOCUMENT ME!
		 */
        @Override
        public String getPathTranslated() {
            final String pinfo = this.getPathInfo();
            if (pinfo != null) {
                return this.getContextPath() + pinfo;
            }
            return null;
        }

        @Override
        public String getQueryString() {
            if ((this.parameters != null) && !"".equals(this.parameters)) {
                return this.parameters;
            }
            return super.getQueryString();
        }

        /**
		 * DOCUMENT ME!
		 * 
		 * @return DOCUMENT ME!
		 */
        @Override
        public String getRequestURI() {
            return this.getContextPath() + CRequestDispatcher.this.url;
        }

        /**
		 * DOCUMENT ME!
		 * 
		 * @return DOCUMENT ME!
		 */
        @Override
        public StringBuffer getRequestURL() {
            return new StringBuffer(this.getRequestURI());
        }

        /**
		 * DOCUMENT ME!
		 * 
		 * @return DOCUMENT ME!
		 */
        @Override
        public String getServletPath() {
            if (this.pathInfo != null) {
                final Matcher m = this.pathInfo.matcher(CRequestDispatcher.this.url);
                if (m.matches()) {
                    if (m.groupCount() > 0) {
                        final StringBuffer buffer = new StringBuffer();
                        for (int i = 1; i <= m.groupCount(); i++) {
                            buffer.append(m.group(i));
                        }
                        return buffer.toString();
                    }
                }
            }
            return null;
        }

        /**
		 * DOCUMENT ME!
		 * 
		 * @param pathInfo
		 *            DOCUMENT ME!
		 */
        public void setPathInfo(final Pattern pathInfo) {
            this.pathInfo = pathInfo;
        }
    }

    private final String parameters;

    /** DOCUMENT ME! */
    private final String url;

    /**
	 * Creates a new CRequestDispatcher object.
	 * 
	 * @param url
	 *            DOCUMENT ME!
	 */
    public CRequestDispatcher(final String url) {
        if (url.indexOf("?") != -1) {
            this.url = url.substring(0, url.indexOf("?"));
            this.parameters = url.substring(url.indexOf("?") + 1);
        } else {
            this.url = url;
            this.parameters = null;
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param f
	 *            DOCUMENT ME!
	 * @param origResponse
	 *            DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
    private void flushCache(final File f, final HttpServletResponse origResponse) throws IOException {
        if (f == null) {
            return;
        }
        final byte buffer[] = new byte[2048];
        int inb = -1;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(f);
            final OutputStream out = origResponse.getOutputStream();
            while ((inb = fin.read(buffer)) != -1) {
                out.write(buffer, 0, inb);
            }
        } finally {
            try {
                fin.close();
            } catch (final Exception ignore) {
                ;
            }
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param arg0
	 *            DOCUMENT ME!
	 * @param arg1
	 *            DOCUMENT ME!
	 * 
	 * @throws ServletException
	 *             DOCUMENT ME!
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
    public void forward(final ServletRequest arg0, final ServletResponse arg1) throws ServletException, IOException {
        final CHttpServletRequestWrapper request = new CHttpServletRequestWrapper((HttpServletRequest) arg0, this.parameters);
        final HttpServletResponse response = (HttpServletResponse) arg1;
        final CFilterChain chain = CFilterChain.getFilterChain(request);
        request.setAttribute("org.allcolor.ywt.adapter.web.currentRequest", request);
        request.setAttribute("org.allcolor.ywt.adapter.web.currentResponse", response);
        try {
            chain.doFilter(request, response);
        } finally {
            request.setAttribute("org.allcolor.ywt.adapter.web.currentRequest", arg0);
            request.setAttribute("org.allcolor.ywt.adapter.web.currentResponse", arg1);
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param arg0
	 *            DOCUMENT ME!
	 * @param arg1
	 *            DOCUMENT ME!
	 * 
	 * @throws ServletException
	 *             DOCUMENT ME!
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
    public void include(final ServletRequest arg0, final ServletResponse arg1) throws ServletException, IOException {
        final CHttpServletRequestWrapper request = new CHttpServletRequestWrapper((HttpServletRequest) arg0, this.parameters);
        final HttpServletResponse origResponse = (HttpServletResponse) arg1;
        final CCacheHttpResponse response = new CCacheHttpResponse(new CFakeHttpResponse());
        try {
            final CFilterChain chain = CFilterChain.getFilterChain(request);
            request.setAttribute("org.allcolor.ywt.adapter.web.currentRequest", request);
            request.setAttribute("org.allcolor.ywt.adapter.web.currentResponse", response);
            try {
                chain.doFilter(request, response);
            } finally {
                request.setAttribute("org.allcolor.ywt.adapter.web.currentRequest", request);
                request.setAttribute("org.allcolor.ywt.adapter.web.currentResponse", arg1);
            }
            response.getOutputStream().flush();
            this.flushCache(response.getBuffer(), origResponse);
        } finally {
            response.deleteCache();
        }
    }
}
