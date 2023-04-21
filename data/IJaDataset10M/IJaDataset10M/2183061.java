package com.sts.webmeet.server.filters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.io.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sts.webmeet.api.PluginInfo;
import com.sts.webmeet.common.IOUtil;
import com.sts.webmeet.pluginmanager.PluginManager;
import com.sts.webmeet.web.Constants;

public class PluginClientJarFilter implements Filter {

    public void init(FilterConfig config) {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws java.io.IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String requestFile = request.getRequestURI();
        boolean archiveWritten = false;
        if (PluginManager.getInstance().isPluginClientArchiveRequest(requestFile)) {
            OutputStream os = response.getOutputStream();
            PluginManager.getInstance().writeClientArchive(requestFile, os);
            os.flush();
            archiveWritten = true;
        }
        if (!archiveWritten) {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}
