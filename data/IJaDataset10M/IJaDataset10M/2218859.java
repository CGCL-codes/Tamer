package net.taylor.jsf;

import java.util.List;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Cache the editor bean calls that hit the database and return select item
 * lists.
 * 
 * @author jgilbert
 */
public class ListCacheInterceptor {

    private static final Log logger = LogFactory.getLog(ListCacheInterceptor.class);

    @AroundInvoke
    public Object invoke(InvocationContext ctx) throws Exception {
        if (ctx.getMethod().getReturnType().equals(List.class)) {
            String name = ctx.getTarget().getClass().getName() + "." + ctx.getMethod().getName();
            ListCache cache = new ListCache() {

                @Override
                protected Object proceed(Object ctx) {
                    try {
                        return ((InvocationContext) ctx).proceed();
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                }
            };
            return cache.invoke(ctx, name);
        }
        return ctx.proceed();
    }
}
