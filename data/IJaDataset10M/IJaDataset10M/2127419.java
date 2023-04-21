package com.nhncorp.usf.core.interceptor.impl;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import com.nhncorp.lucy.web.helper.ServletHelper;
import com.nhncorp.lucy.web.interceptor.AbstractInterceptor;
import com.nhncorp.usf.core.result.template.directive.PagerInfo;

/**
 * {@link Action} 수행 전에 {@link PagerInfo}를 생성하고 수행 후에는 
 * {@link HttpServletRequest}에 담는 {@link Interceptor}. 
 * 
 * <ul>
 * 	<li>action 수행 전: pagerInfo 생성 후 page와 queryString 정보를 설정</li>
 * 	<li>action 수행 후: request에 pagerInfo 객체를 등록</li>
 * </ul>
 *
 * <p><pre>
 * &lt;interceptor-stack name="xxx"&gt;
 *	&lt;interceptor-ref name="pagerInfoInterceptor"/&gt;
 * &lt;/interceptor-stack&gt;
 * </pre></p>
 *
 * @author Web Platform Development Team
 */
public class PagerInfoInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1721769763596411887L;

    private static Log log = LogFactory.getLog(PagerInfoInterceptor.class);

    private String pagerType;

    public String getPagerType() {
        return pagerType;
    }

    public void setPagerType(String pagerType) {
        this.pagerType = pagerType;
    }

    /**
	 * {@link HttpServletRequest} 정보를 이용해서 {@link PagerInfo}를 생성하고,
	 * {@link PagerInfoAware}를 구현한 Action에 설정한다.
	 * 
	 * @param invocation {@link ActionInvocation}
	 * @return 실행 결과
	 * @throws Exception Exception
	 */
    public String intercept(ActionInvocation invocation) throws Exception {
        log.debug("Set page and queryString in PagerInfo.");
        HttpServletRequest request = ServletHelper.getRequest(invocation);
        int page = NumberUtils.toInt(request.getParameter("page"), 1);
        String queryString = StringUtils.defaultString(request.getQueryString());
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        PagerInfo pagerInfo = new PagerInfo(page, requestUri, queryString, requestMethod);
        request.setAttribute("pagerInfo", pagerInfo);
        return invocation.invoke();
    }
}
