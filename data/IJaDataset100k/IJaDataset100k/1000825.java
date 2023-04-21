package common.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by IntelliJ IDEA.
 * User: xuweigui
 * Date: 2/5/12
 * Time: 10:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Aspect
public class GateKeeper {

    private Log log = LogFactory.getLog(this.getClass());

    @Before("execution(public * *.service.*.*(..))")
    public void enter(JoinPoint jp) {
        log.debug("Entering " + jp.toString());
    }

    @After("execution(public * *.service.*.*(..))")
    public void exit(JoinPoint jp) {
        log.debug("Exiting  " + jp.toString());
    }
}
