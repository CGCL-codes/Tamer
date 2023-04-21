package com.yeep.struts2Study.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

@SuppressWarnings("serial")
public class GeneralInterceptorExample implements Interceptor {

    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

    /**
	 * Destroy Interceptor
	 */
    public void destroy() {
        System.out.println("[GeneralInterceptorExample]destroy...");
    }

    /**
	 * Initialize Interceptor
	 */
    public void init() {
        System.out.println("[GeneralInterceptorExample]init doing...");
        System.out.println("[GeneralInterceptorExample]Parameter is:" + getParameter());
    }

    @Override
    public String intercept(ActionInvocation arg0) throws Exception {
        System.out.println("[GeneralInterceptorExample]start invoking...");
        String result = arg0.invoke();
        System.out.println("[GeneralInterceptorExample]end invoking...");
        return result;
    }
}
