package org.jwatch.domain.instance;

import org.jwatch.domain.quartz.Scheduler;
import java.util.List;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 28, 2011 5:01:26 PM
 */
public class QuartzInstanceUtil {

    /**
    * @param quartzInstance
    * @param instanceId               scheduler-instance-id
    * @return
    */
    public static Scheduler getSchedulerByInstanceId(QuartzInstance quartzInstance, String instanceId) {
        List list = quartzInstance.getSchedulerList();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Scheduler s = (Scheduler) list.get(i);
                if (s.getInstanceId().equals(instanceId)) {
                    return s;
                }
            }
        }
        return null;
    }
}
