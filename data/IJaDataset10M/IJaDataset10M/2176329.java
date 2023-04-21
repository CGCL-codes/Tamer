package org.vardb.web.filters;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CExpiresHeaderFilter implements Filter {

    protected static final String PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

    protected FilterConfig config;

    protected Integer seconds = 7776000;

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        setCacheExpireDate(response, this.seconds);
        chain.doFilter(req, resp);
    }

    public static void setCacheExpireDate(HttpServletResponse response, int seconds) {
        if (response != null) {
            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.SECOND, seconds);
            response.setHeader("Cache-Control", "PUBLIC, max-age=" + seconds + ", must-revalidate");
            response.setHeader("Expires", getExpiresDateFormat().format(cal.getTime()));
        }
    }

    protected static DateFormat getExpiresDateFormat() {
        DateFormat httpDateFormat = new SimpleDateFormat(PATTERN, Locale.US);
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return httpDateFormat;
    }

    public void init(FilterConfig config) {
        this.config = config;
        this.seconds = Integer.valueOf(this.config.getInitParameter("seconds"));
    }

    public void destroy() {
        this.config = null;
    }
}
