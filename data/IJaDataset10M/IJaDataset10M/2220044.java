package org.beanopen.f.engine.action;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.beanopen.f.define.ActionDefine;
import org.beanopen.f.define.ParameterDefine;
import org.beanopen.f.exception.RequestAnalyticException;

/**
 * URL解析规则接口
 * 
 * @author Bean
 * @since 1.0.0
 * @version 1.0.0
 * 
 */
public interface ActionAnalytic {

    /**
	 * 解析URL{@link StringBuffer}返回一个新的实例
	 * 
	 * @param url
	 * @return {@link ActionDefine}
	 */
    public ActionDefine parseAction(StringBuffer url);

    /**
	 * 解析{@link javax.servlet.http.HttpServletRequest}返回一个新的实例
	 * 
	 * @param request
	 * @return {@link ActionDefine}
	 */
    public ActionDefine parseAction(HttpServletRequest request);

    /**
	 * 
	 * @param method
	 * @param request
	 * @return
	 * @throws RequestAnalyticException
	 */
    public ParameterDefine[] parseParameter(Method method, HttpServletRequest request) throws RequestAnalyticException;
}
