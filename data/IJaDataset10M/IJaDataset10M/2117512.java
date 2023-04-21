package com.jshop.action.interceptor.impl;

import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Controller;
import com.jshop.action.interceptor.AuthorityInterceptor;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@ParentPackage("jshop")
@Controller("myIsusercanregisterInterceptor")
public class MyIsusercanregisterInterceptor extends AbstractInterceptor {

    public static final String MSG = "对不起！暂时关闭注册";

    private AuthorityInterceptor authorityInterceptor;

    public AuthorityInterceptor getAuthorityInterceptor() {
        return authorityInterceptor;
    }

    public void setAuthorityInterceptor(AuthorityInterceptor authorityInterceptor) {
        this.authorityInterceptor = authorityInterceptor;
    }

    @Override
    public String intercept(ActionInvocation ai) throws Exception {
        ActionContext ctx = ai.getInvocationContext();
        boolean flag = this.getAuthorityInterceptor().IsusercanregisterIntercept();
        if (flag) {
            return ai.invoke();
        } else {
            ctx.put("gpmsg", MSG);
            return "isusercanregister";
        }
    }
}
