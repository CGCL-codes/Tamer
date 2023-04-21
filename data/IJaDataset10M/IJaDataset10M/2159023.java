package net.sourceforge.javautil.cdi.impl.standard;

import java.lang.reflect.Method;
import net.sourceforge.javautil.interceptor.IInterceptorLink;
import net.sourceforge.javautil.interceptor.IInterceptorManager;
import net.sourceforge.javautil.interceptor.InterceptorBase;
import net.sourceforge.javautil.interceptor.InterceptorCompiler;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class BeanInterceptor extends InterceptorBase {

    public BeanInterceptor(InterceptorCompiler compiler, IInterceptorLink chain) {
        super(compiler, chain);
    }

    @Override
    public Object invoke(Object target, Method method, Object... arguments) throws Exception {
        if (method.getName().endsWith("$$")) {
            method = target.getClass().getMethod(method.getName().replace("$$", ""), method.getParameterTypes());
        }
        return super.invoke(target, method, arguments);
    }
}
