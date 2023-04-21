package org.ox.framework.security;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

/**
 * Clase responsable de la intercepci�n AOP de los m�todos desognados como
 * securizables
 * 
 * @author capgemini
 * 
 */
public class SecurityInterceptor implements MethodInterceptor {

    Logger logger = Logger.getLogger(SecurityInterceptor.class);

    /**
	 * M�todo que se invocar� antes de la invocaci�n real del m�todo Si el
	 * m�todo tiene definida una anotaci�n de AssertPermission cruzar� el
	 * permiso necesario con la lista de permisos de usuario. Si no se encuentra
	 * dicho permiso en la lista, lanzar� una excepci�n. En caso contrario,
	 * llamar� a la clase interceptada y seguir� el flujo normal de la
	 * operaci�n.
	 */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method m = invocation.getMethod();
        logger.debug("Iniciando llamada AOP: " + m.getName());
        AssertPermission a = (AssertPermission) m.getAnnotation(AssertPermission.class);
        logger.debug("Obtenemos la anotación asociada: " + a);
        logger.debug("Nombre del thread:" + Thread.currentThread().getName());
        UserInfo u = SecurityLocalThread.getUser();
        if (a != null && !u.hasPermMultiples(a.value())) {
            throw new SecurityException("No tiene permiso para realizar la operación.");
        }
        Object rval = invocation.proceed();
        logger.debug("Finalizando llamada AOP");
        return rval;
    }
}
