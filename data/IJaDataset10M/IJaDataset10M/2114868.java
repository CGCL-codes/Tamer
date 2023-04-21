package com.tien.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.lang.time.DateFormatUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.tien.utils.ConstantsTool;

@Component
public class CacheRefreshQuartz {

    @Autowired
    @Qualifier("ehCache")
    private Cache ehCache;

    @SuppressWarnings("unchecked")
    protected void executeInternal() throws JobExecutionException {
        System.out.println("Begin QuartzJob. Start Time:" + DateFormatUtils.format(new Date(), ConstantsTool.DEFAULT_DATE_FORMAT));
        List<String> list = ehCache.getKeys();
        for (Object s : list) {
            System.out.println("\tBegin refresh the cache.--->" + s);
            Element element = ehCache.get(s);
            Map<String, Object> map = (Map<String, Object>) element.getValue();
            Object result = null;
            ProceedingJoinPoint joinPoint = (ProceedingJoinPoint) map.get("invocation");
            try {
                result = joinPoint.proceed();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (result != null) {
                map.remove("result");
                map.put("result", (Serializable) result);
            }
            System.out.println("\t\tEnding refresh the cache.--->");
        }
        System.out.println("Ending QuartzJob. End Time:" + DateFormatUtils.format(new Date(), ConstantsTool.DEFAULT_DATE_FORMAT));
    }
}
