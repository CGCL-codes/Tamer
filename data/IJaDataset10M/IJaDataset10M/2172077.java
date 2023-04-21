package net.sunji.spring.plus.logger;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.stereotype.Component;

/**
 * @author seyoung
 *
 */
@Component
@Aspect
public class LogThrows implements ThrowsAdvice {

    private Logger log = Logger.getLogger(this.getClass());

    @AfterThrowing(pointcut = "execution(* *..*.*(..))", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        if (log.isDebugEnabled()) {
            String name = joinPoint.getSignature().toLongString();
            log.debug("Exception occured: " + name + " thrown:" + e.getClass().getName());
        }
    }
}
