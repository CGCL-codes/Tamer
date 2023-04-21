package com.narirelays.ems.tasks;

import java.util.Date;
import java.util.List;
import org.apache.commons.beanutils.LazyDynaBean;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.narirelays.ems.persistence.orm.TaskInfo;
import com.narirelays.ems.resources.CacheProvider;
import com.narirelays.ems.resources.WebVariable;

public class JobHelper {

    private static final Logger log = LoggerFactory.getLogger(JobHelper.class);

    public static boolean registerJob(JobKey jobKey, String message) {
        if (!CacheProvider.cacheValid) {
            log.error("No global cache available, please contact admin");
            return false;
        } else {
            LazyDynaBean bean = new LazyDynaBean();
            bean.set("host_ip", WebVariable.HOST_IP);
            bean.set("host_name", WebVariable.HOST_NAME);
            bean.set("message", message);
            bean.set("message_time", WebVariable.sdf.format(new Date()));
            CacheProvider.setObject(jobKey.getName(), bean);
            return true;
        }
    }

    public static boolean unregisterJob(JobKey jobKey) {
        if (!CacheProvider.cacheValid) {
            log.error("No global cache available, please contact admin");
            return false;
        } else {
            CacheProvider.removeObject(jobKey.getName());
            return true;
        }
    }
}
