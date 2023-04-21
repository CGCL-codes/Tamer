package org.apache.webbeans.test.component.exception;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.webbeans.Production;

@Production
public class MoreThanOneAroundInvokeComponent {

    @AroundInvoke
    public Object method1(InvocationContext ctx) throws Exception {
        return null;
    }

    @AroundInvoke
    public Object method2(InvocationContext ctx) throws Exception {
        return null;
    }
}
