package name.xwork.web;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import name.xwork.security.SecurityContext;

/**
 * WEB环境类
 * @author  zhaoxingyun
 */
public final class WebContext {

    private static final Log log = LogFactory.getLog(WebContext.class);

    /**
	 * @uml.property  name="webContext"
	 * @uml.associationEnd  
	 */
    private static final WebContext webContext = new WebContext();

    private WebContext() {
    }

    ;

    /**
	 * AJAX请求参数
	 */
    public static final String HTTP_REQUEST_AJAX = "http_request_ajax";

    /**
	 * 仅当HTTP_REQUEST_AJAX的值等于该值时，判断为一个AJAX请求。
	 */
    public static final String HTTP_REQUEST_AJAX_HANDLE_VALUE = "return_ajax";

    /**
	 * 当次请求记录数
	 */
    public static final String HTTP_REQUEST_ROW_NUM = "http_request_row_num";

    /**
	 * 当前请求页面下标
	 */
    public static final String HTTP_REQUEST_PAGE_INDEX = "http_request_page_index";

    /**
	 * 当前分段请求结果集名称
	 */
    public static final String HTTP_REQUEST_SECTION_NAME = "http_request_section_name";

    /**
	 * 当前请求默认返回结果
	 */
    public static final String HTTP_REQUEST_DEFAULT_RESULT = "http_request_default_result";

    /**
	 * 分段请求返回结果参数
	 */
    public static final String HTTP_REQUEST_SECTION_RESULT = "http_request_section_result";

    private static final ThreadLocal<String> requestURI = new ThreadLocal<String>();

    private static final ThreadLocal<Boolean> ajaxRequest = new ThreadLocal<Boolean>();

    private static final ThreadLocal<Boolean> sectionRequest = new ThreadLocal<Boolean>();

    private static final ThreadLocal<Integer> requestRowNum = new ThreadLocal<Integer>();

    private static final ThreadLocal<Integer> requestPageIndex = new ThreadLocal<Integer>();

    private static final ThreadLocal<String> requestSectionName = new ThreadLocal<String>();

    private static final ThreadLocal<Map<String, Object>> responseValues = new ThreadLocal<Map<String, Object>>();

    private static final ThreadLocal<HttpServletRequest> reqeust = new ThreadLocal<HttpServletRequest>();

    /**
	 * @uml.property  name="response"
	 */
    private static final ThreadLocal<HttpServletResponse> response = new ThreadLocal<HttpServletResponse>();

    /**
	 * @uml.property  name="domain"
	 */
    private static String domain;

    /**
	 * @uml.property  name="defaultRequestRowNum"
	 */
    private static Integer defaultRequestRowNum;

    public static Integer defalutRequestPageIndex;

    /**
	 * @uml.property  name="validater"
	 */
    private static Map<String, Validater> validaterMap;

    /**
	 * @uml.property  name="messageSource"
	 */
    private static MessageSource messageSource;

    static {
        if (log.isDebugEnabled()) {
            log.debug("开始初始化WebContext。");
        }
        try {
            InputStream inputStream = SecurityContext.class.getResourceAsStream("/WebContext.properties");
            Properties p = new Properties();
            p.load(inputStream);
            WebContext.domain = p.getProperty("domain");
            WebContext.defaultRequestRowNum = Integer.valueOf(p.getProperty("requestRowNum"));
            WebContext.defalutRequestPageIndex = Integer.valueOf(p.getProperty("requestPageIndex"));
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
                log.debug(e);
            }
        }
        if (WebContext.domain == null) {
            log.error("未在classpath:WebContext.properties中找到domain的配置！");
            throw new RuntimeException("未在classpath:WebContext.properties中找到domain的配置！");
        }
        if (WebContext.defaultRequestRowNum == null) {
            WebContext.defaultRequestRowNum = 20;
            if (log.isDebugEnabled()) {
                log.warn("未在classpath:WebContext.properties中找到requestRowNum的配置！\n默认值为：20。");
            }
        }
        if (WebContext.defalutRequestPageIndex == null) {
            WebContext.defalutRequestPageIndex = 0;
            if (log.isDebugEnabled()) {
                log.warn("未在classpath:WebContext.properties中找到requestPageIndex的配置！\n默认值为：0。");
            }
        }
    }

    /**
	 * 获得当前请求的URI
	 * 
	 * @return
	 */
    public static String getRequestURI() {
        return WebContext.requestURI.get();
    }

    /**
	 * 设置当前请求的URI
	 * 
	 * @param uri
	 */
    public static void setRequestURI(String uri) {
        WebContext.requestURI.set(uri);
    }

    /**
	 * 是否是ajax请求
	 * 
	 * @return
	 */
    public static Boolean isAjaxRequest() {
        return WebContext.ajaxRequest.get() != null && WebContext.ajaxRequest.get();
    }

    /**
	 * 设置是否是ajax请求
	 * 
	 * @param ajaxRequest
	 */
    public static void setAjaxRequest(Boolean ajaxRequest) {
        WebContext.ajaxRequest.set(ajaxRequest);
    }

    /**
	 * 是否是分段数据请求
	 * 
	 * @return
	 */
    public static Boolean isSectionRequest() {
        return WebContext.sectionRequest.get() != null && WebContext.sectionRequest.get();
    }

    /**
	 * 设置是否是分段数据请求
	 * 
	 * @param sectionRequest
	 */
    public static void setSectionRequest(Boolean sectionRequest) {
        WebContext.sectionRequest.set(sectionRequest);
    }

    /**
	 * 获得当前请求返回的内容
	 * 
	 * @return
	 */
    public static Map<String, Object> getResponseValues() {
        if (WebContext.responseValues.get() == null) {
            WebContext.responseValues.set(new HashMap<String, Object>());
        }
        return WebContext.responseValues.get();
    }

    /**
	 * 设置当前请求的返回内容
	 * 
	 * @param responseValues
	 */
    public static void addAllResponseValues(Map<String, Object> responseValues) {
        if (responseValues != null) {
            WebContext.getResponseValues().putAll(responseValues);
        }
    }

    /**
	 * 添加一个返回值
	 * 
	 * @param key
	 * @param value
	 */
    public static void addResponseValue(String key, Object value) {
        WebContext.getResponseValues().put(key, value);
    }

    /**
	 * 获得当前请求的HttpServletResponse
	 * @return
	 * @uml.property  name="response"
	 */
    public static HttpServletResponse getResponse() {
        return WebContext.response.get();
    }

    /**
	 * 设置当前请求的HttpServletResponse
	 * 
	 * @param response
	 */
    public static void setResponse(HttpServletResponse response) {
        WebContext.response.set(response);
    }

    /**
	 * 获得当前请求的HttpServletRequest
	 * 
	 * @return
	 */
    public static HttpServletRequest getRequest() {
        return WebContext.reqeust.get();
    }

    /**
	 * 设置当前请求的HttpServletRequest
	 * 
	 * @param reqeust
	 */
    public static void setReqeust(HttpServletRequest request) {
        WebContext.reqeust.set(request);
    }

    /**
	 * 获得当前应用域名
	 * @return
	 * @uml.property  name="domain"
	 */
    public static String getDomain() {
        return domain;
    }

    /**
	 * 获取当前请求页面下标
	 * 
	 * @return
	 */
    public static Integer getRequestPageIndex() {
        if (requestPageIndex.get() == null) {
            requestPageIndex.set(defalutRequestPageIndex);
        }
        return requestPageIndex.get();
    }

    /**
	 * 设置当前请求页面下标
	 * 
	 * @param requestPageIndex
	 */
    public static void setRequestPageIndex(Integer requestPageIndex) {
        WebContext.requestPageIndex.set(requestPageIndex);
    }

    /**
	 * 获得当前请求记录数
	 * 
	 * @return
	 */
    public static Integer getRequestRowNum() {
        if (requestRowNum.get() == null) {
            requestRowNum.set(defaultRequestRowNum);
        }
        return requestRowNum.get();
    }

    /**
	 * 设置当前请求记录数
	 * 
	 * @param requestPageIndex
	 */
    public static void setRequestRowNum(Integer requestRowNum) {
        WebContext.requestRowNum.set(requestRowNum);
    }

    /**
	 * 获得当前分段请求结果集名称
	 * 
	 * @return
	 */
    public static String getRequestSectionName() {
        return WebContext.requestSectionName.get();
    }

    /**
	 * 设置当前分段请求结果集名称
	 * 
	 * @param requestSectionName
	 */
    public static void setRequestSectionName(String requestSectionName) {
        WebContext.requestSectionName.set(requestSectionName);
    }

    /**
	 * 获取表单验证集合
	 * @return
	 * @uml.property  name="validater"
	 */
    public static Map<String, Validater> getValidaterMap() {
        return validaterMap;
    }

    /**
	 * 设置表单验证集合
	 * @param  validaterMap
	 * @uml.property  name="validater"
	 */
    public static void setValidaterMap(Map<String, Validater> validaterMap) {
        WebContext.validaterMap = validaterMap;
    }

    /**
	 * 设置messageSource
	 * @return
	 * @uml.property  name="messageSource"
	 */
    public static MessageSource getMessageSource() {
        return messageSource;
    }

    /**
	 * 获取messageSource
	 * @param  messageSource
	 * @uml.property  name="messageSource"
	 */
    public static void setMessageSource(MessageSource messageSource) {
        WebContext.messageSource = messageSource;
    }

    /**
	 * 清除线程变量
	 */
    public static void clean() {
        WebContext.ajaxRequest.remove();
        WebContext.reqeust.remove();
        WebContext.requestURI.remove();
        WebContext.response.remove();
        WebContext.responseValues.remove();
        WebContext.sectionRequest.remove();
        WebContext.requestPageIndex.remove();
        WebContext.requestRowNum.remove();
        WebContext.requestSectionName.remove();
    }

    /**
	 * 获得一个WebContext的实例
	 * 
	 * @return WebContext
	 */
    public static WebContext getSelf() {
        return webContext;
    }

    /**
	 * 获得当前时间毫秒数
	 * 
	 * @return
	 */
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
	 * 获得当前日期
	 * 
	 * @return
	 */
    public Date getCurrentDate() {
        return new Date();
    }

    /**
	 * 获得默认请求记录数
	 * @return
	 * @uml.property  name="defaultRequestRowNum"
	 */
    public int getDefaultRequestRowNum() {
        return defaultRequestRowNum;
    }

    /**
	 * 获得默认页面下标
	 * 
	 * @return
	 */
    public int getDefaultRequestPageIndex() {
        return defalutRequestPageIndex;
    }

    /**
	 * 获得AJAX请求参数名称
	 * 
	 * @return
	 */
    public String getRequestAjaxHandleParam() {
        return HTTP_REQUEST_AJAX;
    }

    /**
	 * 获得AJAX请求参数值
	 * 
	 * @return
	 */
    public String getRequestAjaxHandleValue() {
        return HTTP_REQUEST_AJAX_HANDLE_VALUE;
    }

    /**
	 * 获得当前请求记录数参数名称
	 * 
	 * @return
	 */
    public String getRequestRowNumParam() {
        return HTTP_REQUEST_ROW_NUM;
    }

    /**
	 * 获得当前分段请求结果集名称
	 * 
	 * @return
	 */
    public String getRequestSectionNameParam() {
        return HTTP_REQUEST_SECTION_NAME;
    }

    /**
	 * 获得当前请求记录页面下标参数名称
	 * 
	 * @return
	 */
    public String getRequestPageIndexParam() {
        return HTTP_REQUEST_PAGE_INDEX;
    }

    /**
	 * 获取当前请求默认返回结果参数名称
	 * 
	 * @return
	 */
    public String getRequestDefaultResult() {
        return HTTP_REQUEST_DEFAULT_RESULT;
    }

    /**
	 * 获取当前请求分段返回结果参数名称
	 * 
	 * @return
	 */
    public String getRequestSectionResult() {
        return HTTP_REQUEST_SECTION_RESULT;
    }

    /**
	 * 设置默认返回结果
	 * 
	 * @param webResult
	 */
    public static void setDefaultResponseRequest(WebResult webResult) {
        WebContext.getResponseValues().put(WebContext.HTTP_REQUEST_DEFAULT_RESULT, webResult);
    }

    /**
	 * 获得默认返回结果
	 * 
	 * @param webResult
	 * @return
	 */
    public static WebResult getDefaultResponseRequest() {
        return (WebResult) WebContext.getResponseValues().get(WebContext.HTTP_REQUEST_DEFAULT_RESULT);
    }

    /**
	 * 清除分段请求数据
	 */
    public static void cleanSectionRequest() {
        WebContext.sectionRequest.set(Boolean.FALSE);
    }
}
