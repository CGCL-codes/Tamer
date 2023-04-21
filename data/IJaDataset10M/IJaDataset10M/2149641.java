package org.alesv.common.logging.interceptors;

import java.lang.reflect.Method;
import org.alesv.common.logging.annotations.LogDebug;
import org.alesv.common.logging.annotations.spring.Bean;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Bean(name = "logDebug")
public class LogDebugInterceptor {

    /**
	 * Obtiene el método interceptado, el que contiene la anotación de escritura
	 * de trazas.
	 * 
	 * @param jp
	 *            Punto de intercepción.
	 * @return Método interceptado.
	 */
    @SuppressWarnings({ "finally", "unchecked" })
    protected Method getMethod(JoinPoint jp) {
        Method invoked = null;
        try {
            MethodSignature met = (MethodSignature) jp.getSignature();
            invoked = jp.getSourceLocation().getWithinType().getMethod(met.getMethod().getName(), met.getMethod().getParameterTypes());
        } finally {
            return invoked;
        }
    }

    /**
	 * Crear el logger que se usará para escribir las trazas.
	 * 
	 * @param jp
	 *            Punto de intercepción.
	 * @return Logger que se usará para escribir las trazas.
	 */
    @SuppressWarnings({ "rawtypes" })
    protected Logger getLog(JoinPoint jp) {
        Logger logger = null;
        try {
            LogDebug logdebug = this.getMethod(jp).getAnnotation(LogDebug.class);
            Class clazz = logdebug.loggerClass();
            if (clazz == null) {
                clazz = LogDebugInterceptor.class;
            }
            logger = LoggerFactory.getLogger(clazz);
        } finally {
            if (logger == null) {
                logger = LoggerFactory.getLogger(LogDebugInterceptor.class);
            }
        }
        return logger;
    }

    /**
	 * Método que se ejecuta justo antes del comienzo de la ejecución del método
	 * anotado.
	 * 
	 * @param jp
	 *            Punto de intercepción.
	 */
    @Before("@annotation(org.alesv.common.logging.annotations.LogDebug)")
    public void beforeLog(JoinPoint jp) {
        Logger logger = this.getLog(jp);
        if (logger.isDebugEnabled()) {
            int n = 1;
            logger.debug("Comienzo de la ejecución del método {}", this.getMethod(jp));
            logger.debug("Parámetros de entrada:");
            for (Object o : jp.getArgs()) {
                if (o != null) {
                    logger.debug(" * Parámetro {}", n++);
                    logger.debug("   - Tipo : {}", o.getClass().getSimpleName());
                    logger.debug("   - Valor: {}", o.toString());
                }
            }
        }
    }

    /**
	 * Método que se ejecuta justo después del comienzo de la ejecución del
	 * método anotado.
	 * 
	 * @param jp
	 *            Punto de intercepción.
	 * @param retVal
	 *            Valor que devuelve el método anotado.
	 */
    @AfterReturning(pointcut = "@annotation(org.alesv.common.logging.annotations.LogDebug)", returning = "retVal")
    public void afterLog(JoinPoint jp, Object retVal) {
        Logger logger = this.getLog(jp);
        if (logger.isDebugEnabled()) {
            logger.debug("Fin del método. Valor devuelto: {}", retVal);
        }
    }
}
