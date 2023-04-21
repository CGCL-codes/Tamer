package com.sitescape.team.remoting.ws.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import com.sitescape.team.context.request.RequestContext;
import com.sitescape.team.context.request.RequestContextHolder;
import com.sitescape.team.domain.LoginInfo;
import com.sitescape.team.module.report.ReportModule;

public class LoginInfoInterceptor implements MethodInterceptor {

    private ReportModule reportModule;

    protected ReportModule getReportModule() {
        return reportModule;
    }

    public void setReportModule(ReportModule reportModule) {
        this.reportModule = reportModule;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        RequestContext rc = RequestContextHolder.getRequestContext();
        String authenticator = rc.getAuthenticator();
        if (LoginInfo.AUTHENTICATOR_WS.equals(authenticator) || LoginInfo.AUTHENTICATOR_REMOTING_T.equals(authenticator)) {
            getReportModule().addLoginInfo(new LoginInfo(authenticator, RequestContextHolder.getRequestContext().getUserId()));
        }
        return invocation.proceed();
    }
}
