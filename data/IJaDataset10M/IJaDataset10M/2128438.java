package com.technoetic.xplanner.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;
import com.technoetic.xplanner.util.LogUtil;

public class ActivityLogFilter implements Filter {

    public static final Logger LOG = LogUtil.getLogger();

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ActivityLogFilterHelper helper = new ActivityLogFilterHelper();
        helper.doHelperSetUp(request);
        LOG.info(helper.getStartLogRecord());
        filterChain.doFilter(request, response);
        LOG.info(helper.getEndLogRecord());
    }

    public void destroy() {
    }
}
