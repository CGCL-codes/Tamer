package org.go.test;

import static org.go.TriggerBuilder.newTrigger;
import org.go.SchedulerFactory;
import org.go.Trigger;
import org.go.Work;
import org.go.WorkBuilder;
import org.go.busi.work.DoBusi1;
import org.go.core.AlwaysScheduleBuilder;
import org.go.core.StdSchedulerFactory;
import org.go.domain.TriggerType;

public class GoRmiTest {

    public static void main(String[] args) throws Exception {
        Trigger trigger = newTrigger(TriggerType.ALWAYS.getName()).withIdentity("1", "1").withDescription("triggerDescription").withSchedule(AlwaysScheduleBuilder.repeatSchedule()).build();
        Work worker = WorkBuilder.newWork(DoBusi1.class).build();
        worker.setWorkId("1");
        SchedulerFactory factory = new StdSchedulerFactory("client.xml");
        org.go.Scheduler scheduler = factory.getScheduler("DefaultQuartzScheduler");
        scheduler.scheduler(worker, trigger);
        System.out.println("%%%%%%%%%%% ");
    }
}
