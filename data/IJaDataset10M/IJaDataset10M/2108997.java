package sk.sigp.tetras.job;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import org.quartz.Scheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.util.StringUtils;

public class CronChange {

    private CronTriggerBean _triggerNewFax;

    private Scheduler _scheduler;

    private String _expression = "0 0/1 * ? * *";

    public void newJob() {
        System.out.println("NEW TICK: " + new Date());
    }

    public void oldJob() {
        System.out.println("OLD TICK: " + new Date());
    }

    public void reconfigureCron(CronTriggerBean aTrigger, String aExpression) {
        if (!StringUtils.hasLength(aExpression)) return;
        Date dt = new Date();
        try {
            System.out.println(dt + "# Reconfiguration of trigger '" + aTrigger.getJobName() + "' new: '" + aExpression.trim() + "' old: '" + aTrigger.getCronExpression().trim() + "'");
            if (!aExpression.trim().equals(aTrigger.getCronExpression())) {
                aTrigger.setCronExpression(aExpression);
                aTrigger.setMisfireInstruction(CronTriggerBean.MISFIRE_INSTRUCTION_DO_NOTHING);
                aTrigger.updateAfterMisfire(null);
                _scheduler.rescheduleJob(aTrigger.getName(), aTrigger.getGroup(), aTrigger);
                System.out.println(dt + "# Cron reconfiguration successfull, new cron expresion registered on trigger '" + aTrigger.getName() + "' is " + aExpression);
            }
        } catch (Exception e) {
            System.out.println(dt + "# Cron reconfiguration failed, possible cause of wrong cron expression");
        }
    }

    public void setExpression(String aExpression) {
        _expression = aExpression;
    }

    public void reset() {
        System.out.println("reset called");
        reconfigureCron(_triggerNewFax, _expression);
    }

    public void setTriggerNewFax(CronTriggerBean aTriggerNewFax) {
        _triggerNewFax = aTriggerNewFax;
    }

    public void setSheduler(Scheduler shedulerFactory) {
        _scheduler = shedulerFactory;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "beanRefFactory.xml" });
        CronChange cc = (CronChange) ctx.getBean("test");
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            String exp = buf.readLine();
            cc.setExpression(exp);
        }
    }
}
