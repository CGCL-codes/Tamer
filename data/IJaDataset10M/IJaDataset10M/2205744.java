package org.nexopenframework.scheduling.quartz.model;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import org.nexopenframework.scheduling.SchedulerException;
import org.nexopenframework.scheduling.model.SchedulerBean;
import org.nexopenframework.scheduling.model.SimpleTrigger;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Extension of {@link PeriodicTrigger} for dealing with quartz features such as validate
 * cron expression</p>
 * 
 * @see org.springframework.scheduling.support.PeriodicTrigger
 * @see org.springframework.scheduling.Trigger
 * @author Francesc Xavier Magdaleno
 * @version $Revision 3328$,$Date: 2010-08-31 14:18:44 +0100 $ 
 * @since 2.0.0.GA
 */
public class QuartzPeriodicTrigger extends SchedulerBean implements Trigger {

    /**serialization stuff*/
    private static final long serialVersionUID = 1L;

    /**Delegate component for using with TaskScheduler implementations different from Quartz*/
    private final PeriodicTrigger delegate;

    public QuartzPeriodicTrigger(final PeriodicTrigger trigger) {
        this.delegate = trigger;
    }

    public Date nextExecutionTime(final TriggerContext triggerContext) {
        return this.delegate.nextExecutionTime(triggerContext);
    }

    /**
	 * 
	 * @return
	 */
    public SimpleTrigger toSimpleTrigger() {
        try {
            final TriggerContext triggerContext = new SimpleTriggerContext();
            final Date startTime = this.delegate.nextExecutionTime(triggerContext);
            final long delay = getField("initialDelay", this.delegate.getClass()).getLong(this.delegate);
            final long fixedRate = getField("period", this.delegate.getClass()).getLong(this.delegate);
            return new SimpleTrigger(delay, fixedRate, startTime, this.delegate);
        } catch (final IllegalArgumentException e) {
            throw new SchedulerException(e);
        } catch (final IllegalAccessException e) {
            throw new SchedulerException(e);
        }
    }

    /**
	 * <p>Get a field from the superclass</p>
	 * 
	 * @return
	 * @throws NoSuchFieldException
	 */
    final Field getField(final String fieldName, final Class<?> clazz) {
        return AccessController.doPrivileged(new PrivilegedAction<Field>() {

            public Field run() {
                try {
                    final Field f = clazz.getDeclaredField(fieldName);
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    return f;
                } catch (final NoSuchFieldException e) {
                    throw new SchedulerException("No field in class " + clazz, e);
                }
            }
        });
    }
}
