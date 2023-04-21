package com.blueskyminds.struts2.urlplugin.filter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Only accept requests matching the specified pattern
 */
public class PatternFilter implements RequestFilter {

    private static final String PATTERN_PARAM = "pattern";

    private Map<String, String> params;

    private Pattern pattern;

    public PatternFilter() {
        params = new HashMap<String, String>();
    }

    /**
     * Initialise a parameter of the filter
     */
    public void setParam(String name, String value) {
        params.put(name, value);
    }

    /**
     * Filter the input request.  Accept it if it matches the pattern
     *
     * @param servletRequest
     * @return true if it should be processed by the ActionMapper
     */
    public boolean accept(HttpServletRequest servletRequest) {
        preparePattern();
        if (pattern != null) {
            return pattern.matcher(servletRequest.getRequestURI()).find();
        } else {
            return true;
        }
    }

    private void preparePattern() {
        if (pattern == null) {
            String pattern = params.get(PATTERN_PARAM);
            if (pattern != null) {
                this.pattern = Pattern.compile(pattern);
            }
        }
    }
}
