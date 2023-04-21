package org.gplugins.quartz;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.geronimo.naming.reference.ConfigurationAwareReference;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.KernelRegistry;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * ReplaceMe
 *
 * @version $Rev: 46019 $ $Date: 2004-09-14 05:56:06 -0400 (Tue, 14 Sep 2004) $
 */
public class QuartzJobFactory extends SimpleJobFactory {

    private static final String PROPERTY_PREFIX = "org.gplugins.quartz.";

    private static final Log log = LogFactory.getLog(QuartzJobFactory.class);

    public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {
        Kernel kernel = KernelRegistry.getSingleKernel();
        Job job = super.newJob(bundle);
        Map map = bundle.getJobDetail().getJobDataMap();
        for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            if (key.startsWith(PROPERTY_PREFIX)) {
                processKey(kernel, job, bundle.getJobDetail().getName(), key.substring(PROPERTY_PREFIX.length()), map.get(key));
            }
        }
        return job;
    }

    private void processKey(Kernel kernel, Job job, String jobName, String name, Object value) throws SchedulerException {
        if (name.startsWith("ejb.")) {
            injectReference(value, kernel, name, job, jobName, 4, "EJB Reference");
        } else if (name.startsWith("resource.")) {
            injectReference(value, kernel, name, job, jobName, 9, "Resource Reference");
        } else if (name.startsWith("gbean.")) {
            injectReference(value, kernel, name, job, jobName, 6, "GBean Reference");
        } else {
            throw new SchedulerException("Unrecognized job parameter '" + name + "'");
        }
    }

    private void injectReference(Object value, Kernel kernel, String name, Job job, String jobName, int prefixLength, String refType) throws SchedulerException {
        ConfigurationAwareReference ref = (ConfigurationAwareReference) value;
        ref.setKernel(kernel);
        String property = name.substring(prefixLength);
        String setterName = "set" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
        Method all[] = job.getClass().getMethods();
        try {
            Object refValue = ref.getContent();
            for (int i = 0; i < all.length; i++) {
                Method method = all[i];
                if (method.getName().equals(setterName) && method.getParameterTypes().length == 1 && Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && method.getReturnType().equals(Void.TYPE)) {
                    if (method.getParameterTypes()[0].isAssignableFrom(refValue.getClass())) {
                        method.invoke(job, new Object[] { refValue });
                        return;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Unable to set " + refType + " on Quartz job", e);
            throw new SchedulerException("Unable to set " + refType + " '" + property + "' on job " + jobName, e);
        }
        throw new SchedulerException("No setter method " + setterName + " found for " + refType + " " + property + " on job " + jobName);
    }
}
