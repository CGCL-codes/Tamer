package info.jmonit.aop;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

/**
 * 
 * @author <a href="mailto:ndeloof@sourceforge.net">Nicolas De Loof</a>
 * 
 */
public class MonitoredAdvisor extends AbstractPointcutAdvisor {

    /** pointcut for monitoring */
    private MonitoredPointcut pointcut = new MonitoredPointcut();

    /**
     * {@inheritDoc}
     * @see org.springframework.aop.PointcutAdvisor#getPointcut()
     */
    public Pointcut getPointcut() {
        return pointcut;
    }

    /**
     * {@inheritDoc}
     * @see org.springframework.aop.Advisor#getAdvice()
     */
    public Advice getAdvice() {
        return null;
    }

    /**
     * Pointcut matcher for Monitored annotated beans.
     * 
     * @author <a href="mailto:ndeloof@sourceforge.net">Nicolas De Loof</a>
     */
    public static class MonitoredPointcut extends StaticMethodMatcherPointcut implements Serializable {

        /**
         * {@inheritDoc}
         * @see org.springframework.aop.MethodMatcher#matches(java.lang.reflect.Method, java.lang.Class)
         */
        public boolean matches(Method method, Class targetClass) {
            return false;
        }
    }
}
