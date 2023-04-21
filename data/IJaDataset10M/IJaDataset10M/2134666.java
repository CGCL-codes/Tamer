package net.sf.smailstandalone.aspects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 
 * @since 19.02.2011
 * @author S�bastien CHATEL
 */
@Aspect
public class LoggingAspect {

    private static final Log log = LogFactory.getLog(LoggingAspect.class);

    @AfterReturning(value = "readLine()", returning = "line")
    public void logReadLine(String line) {
        if (line != null && log.isDebugEnabled()) {
            log.debug("RECV ".concat(line.replace('\t', '*')));
        }
    }

    @Before("println() && args(value)")
    public void logPrintln(String value) {
        if (log.isDebugEnabled()) {
            log.debug("SEND ".concat(value.replace('\t', '*')));
        }
    }

    @Around("syso() && args(value)")
    public void logSysOutCall(String value) {
        log.info(value);
    }

    @Pointcut("call(* java.io.BufferedReader.readLine()) && within(MainChat)")
    public void readLine() {
    }

    @Pointcut("execution(public void MainChat.char(..))")
    public void println() {
    }

    @Pointcut("call(* java.io.PrintStream.println(..)) && within(MainChat)")
    public void syso() {
    }
}
