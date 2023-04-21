package com.idna.dm.aspects.logging.system;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import com.idna.dm.aspects.logging.system.MainServicePointcutsRepository.LogType;
import com.idna.dm.logging.system.ThreadLocalPerformanceMonitor;

@Aspect
@Component
public class DecisionLoaderServiceAspect extends MainServicePointcutsRepository {

    Log logger = LogFactory.getLog(this.getClass());

    @Around("decisionLoaderServicePointcut()")
    public Object aroundLoadDecision(ProceedingJoinPoint pjp) throws Throwable {
        String[] input = new String[] { "Decision Id" };
        String outputActionDescriptor = "Decision loaded";
        Map<LogType, String[]> descriptors = new HashMap<LogType, String[]>();
        descriptors.put(LogType.INPUT, input);
        descriptors.put(LogType.OUTPUT, new String[] { outputActionDescriptor });
        ThreadLocalPerformanceMonitor.startStopWatch(this.getClass().getSimpleName().replace("Aspect", ""));
        Object result = logByProxy(pjp, descriptors, LoggingStyle.BOTH_WHERE_OUTPUT_EXCLUDES_INPUT);
        ThreadLocalPerformanceMonitor.stopTheStopWatch();
        return result;
    }
}
