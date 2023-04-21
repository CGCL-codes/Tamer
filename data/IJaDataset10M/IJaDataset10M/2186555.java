package com.nhncorp.usf.core.result;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import com.nhncorp.lucy.web.dispatcher.RedirectResultInfo;
import com.nhncorp.lucy.web.dispatcher.RedirectUtil;
import com.nhncorp.lucy.web.helper.ServletHelper;
import com.nhncorp.usf.core.UsfDispatcher;
import com.nhncorp.usf.core.config.AttributeInfo;
import com.nhncorp.usf.core.config.AttributeInfo.ATTRIBUTE_NAME;
import com.nhncorp.usf.core.config.runtime.ResultPageInfo;
import com.nhncorp.usf.core.util.StringUtil;
import com.nhncorp.usf.core.web.UsfRedirectUtil;

/**
 * 다른 경로로의 Redirect 수행을 위한 {@link AbstractResult}.
 *
 * @author Web Platform Development Team
 */
@SuppressWarnings("serial")
public class RedirectResult extends AbstractResult {

    private static final String PARAM_SEPARATOR = ",";

    private static final String PARAM_ALL = "all";

    /**
     * 이동할 페이지의 target.
     */
    private String target = "window.top";

    /**
     * 페이지 이동을 위한 method(POST 또는 GET).
     */
    private String method = "POST";

    /**
     * 이동할 페이지의 target 설정.
     *
     * @param target the target page path
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * 페이지 이동을 위한 method 설정.
     *
     * @param method the method name
     */
    public void setMethod(String method) {
        if (StringUtil.isNotEmpty(method)) {
            this.method = method.toUpperCase();
        }
    }

    private String params;

    /**
     * Gets the params.
     *
     * @return the params
     */
    public String getParams() {
        return params;
    }

    /**
     * Sets the params.
     *
     * @param params the new params
     */
    public void setParams(String params) {
        this.params = params;
    }

    private String excludes;

    private String includes;

    /**
     * Gets the excludes.
     *
     * @return the excludes
     */
    public String getExcludes() {
        return excludes;
    }

    /**
     * Sets the excludes.
     *
     * @param excludes the new excludes
     */
    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    /**
     * Gets the includes.
     *
     * @return the includes
     */
    public String getIncludes() {
        return includes;
    }

    /**
     * Sets the includes.
     *
     * @param includes the new includes
     */
    public void setIncludes(String includes) {
        this.includes = includes;
    }

    /**
     * 전달된 {@link DataMap} 정보를 {@link ResultPageInfo} 에 적용하여 결과 페이지 내보냄.
     *
     * @param resultPage    the ResultPageInfo
     * @param dataStructure the result data
     * @throws Exception the Exception
     */
    public void execute(ResultPageInfo resultPage, Map<String, Object> dataStructure) throws Exception {
        String src = resultPage.getSrc();
        src = StringUtil.getNewSrc(src, dataStructure);
        setIncludes();
        Map<String, Object> keepValues = excludes(includes(dataStructure));
        keepValues.remove(UsfDispatcher.getInstance().getActionParameter());
        keepValues.remove(AttributeInfo.getInstance().getAttribute(ATTRIBUTE_NAME.actionParameter));
        HttpServletResponse response = ServletHelper.getResponse();
        if ("POST".equals(method)) {
            RedirectResultInfo redirectInfo = RedirectUtil.getFormPage(target, src, keepValues);
            response.setContentType("text/html; charset=" + getPageEncoding());
            PrintWriter out = response.getWriter();
            out.println(redirectInfo.getText());
            out.flush();
            return;
        }
        RedirectResultInfo redirectInfo = UsfRedirectUtil.getNoFormPage(target, src, keepValues);
        switch(redirectInfo.getType()) {
            case SEND_REDIRECT:
                response.sendRedirect(redirectInfo.getText());
                break;
            case WRITE_STREAM:
                response.setContentType("text/html; charset=" + getPageEncoding());
                PrintWriter out = response.getWriter();
                out.println(redirectInfo.getText());
                out.flush();
                break;
        }
    }

    /**
     * 하위 호환성을 위해 params property를 처리한다.
     */
    private void setIncludes() {
        if (StringUtil.isNotEmpty(params)) {
            log.info("use 'includes' property instead of 'params' property");
            if (StringUtil.isEmpty(includes)) {
                includes = params;
            }
        }
    }

    /**
     * Includes.
     *
     * @param dataStructure the data structure
     * @return the map< string, object>
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> includes(Map<String, Object> dataStructure) {
        Map<String, Object> keepValues = new HashMap<String, Object>();
        if (includes == null) {
            includes = "";
        }
        if (includes.equals(PARAM_ALL)) {
            keepValues = dataStructure;
        } else if (includes.equals(UsfDispatcher.REQUEST_PARAM_NAME)) {
            keepValues = (Map<String, Object>) dataStructure.get(UsfDispatcher.REQUEST_PARAM_NAME);
        } else {
            String[] splitParameterNames = includes.split(PARAM_SEPARATOR);
            for (String parameterName : splitParameterNames) {
                String trimName = parameterName.trim();
                keepValues.put(trimName, dataStructure.get(trimName));
            }
        }
        return (Map<String, Object>) (keepValues == null ? Collections.emptyMap() : keepValues);
    }

    /**
     * Excludes.
     *
     * @param keepValues the keep values
     * @return the map< string, object>
     */
    private Map<String, Object> excludes(Map<String, Object> keepValues) {
        if (StringUtil.isEmpty(excludes)) {
            return keepValues;
        }
        if (keepValues != null && !keepValues.isEmpty()) {
            String[] names = excludes.split(",");
            for (String name : names) {
                keepValues.remove(name.trim());
            }
        }
        return keepValues;
    }
}
