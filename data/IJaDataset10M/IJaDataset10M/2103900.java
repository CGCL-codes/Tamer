package org.mgkFramework.scheduler;

import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.DateTime;
import org.mgkFramework.messaging.IGatewayProxy;
import org.mgkFramework.messaging.MsgEnvHlpr;

public class TestSchedulerService extends TestCase {

    private Mockery context = new Mockery();

    private final IGatewayProxy gateway = this.context.mock(IGatewayProxy.class);

    public void testSimpleTriggerJob() throws InterruptedException {
        final MsgEnvHlpr response = MsgEnvHlpr.newInstance();
        response.setName("testSimpleTriggerJob");
        response.setSuccess(true);
        this.context.checking(new Expectations() {

            {
                exactly(2).of(gateway).sendMsgWaitForResponse(with(aNonNull(MsgEnvHlpr.class)));
                will(returnValue(response));
            }
        });
        SchedulerService serv = new SchedulerService();
        serv.setPropertiesResourceString("classpath:/org/mgkFramework/scheduler/quartz.properties");
        serv.setGateway(this.gateway);
        serv.initializeScheduler();
        MsgEnvHlpr inMsg = MsgEnvHlpr.newInstance();
        inMsg.setName("testSimpleTriggerJob");
        serv.addMsgJob("testSimpleTriggerJob", "TestSchedulerService", inMsg.getXmlString());
        DateTime start = new DateTime();
        DateTime end = start.plusMinutes(1);
        serv.addSimpleTrigger("testSimpleTriggerJob", "TestSchedulerService", "testSimpleTriggerJob", "TestSchedulerService", start.toDate(), end.toDate(), 1, 100);
        Thread.sleep(400);
        serv.shutdownScheduler();
        this.context.assertIsSatisfied();
    }

    public void testSpring() {
        ContextHelper.getContext();
    }
}
