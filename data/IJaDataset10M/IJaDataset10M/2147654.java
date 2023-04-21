package org.campware.cream.modules.scheduledjobs;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.turbine.modules.ScheduledJob;
import org.apache.turbine.services.schedule.JobEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.turbine.util.mail.HtmlEmail;
import org.apache.turbine.Turbine;
import org.apache.torque.util.Criteria;
import org.campware.cream.om.Notification;
import org.campware.cream.om.NotificationPeer;
import org.campware.cream.om.OnlineSubscription;
import org.campware.cream.om.OnlineSubscriptionPeer;
import org.campware.cream.om.PrintSubscription;
import org.campware.cream.om.PrintSubscriptionPeer;

/**
 * Online Subscription Job.
 *
 * Close old subscriptions and send notifications
 * @author <a href="mailto:pandzic@volny.cz">Nenad Pandzic</a>
 */
public class OnlineSubscriptionJob extends ScheduledJob {

    /** Logging */
    private static Log log = LogFactory.getLog(OnlineSubscriptionJob.class);

    private int taskcount = 0;

    /**
	 * Constructor
	 */
    public OnlineSubscriptionJob() {
    }

    /**
	 * Run the Jobentry from the scheduler queue.
	 * From ScheduledJob.
	 *
	 * @param job The job to run.
	 */
    public void run(JobEntry job) throws Exception {
        doExpiredOnlineSubs();
        doExpiredPrintSubs();
        doOnlineSubsToExpire();
    }

    private void doOnlineSubsToExpire() throws Exception {
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.DATE, 7);
        Date notiDate = new Date(rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DATE));
        Criteria criteria = new Criteria();
        criteria.add(NotificationPeer.NOTIFICATION_ID, new Integer(1002), Criteria.EQUAL);
        Notification myNotif = (Notification) NotificationPeer.doSelect(criteria).get(0);
        VelocityContext context = new VelocityContext();
        ShedVelocityTool velTool = new ShedVelocityTool(context);
        Criteria osubcrit = new Criteria();
        osubcrit.add(OnlineSubscriptionPeer.END_DATE, notiDate, Criteria.EQUAL);
        osubcrit.add(OnlineSubscriptionPeer.STATUS, new Integer(30), Criteria.EQUAL);
        List substoclose = OnlineSubscriptionPeer.doSelect(osubcrit);
        Iterator i = substoclose.iterator();
        while (i.hasNext()) {
            OnlineSubscription subclose = (OnlineSubscription) i.next();
            String sEmailAddress = subclose.getCustomerRelatedByCustomerId().getEmail();
            if (sEmailAddress.length() > 1) {
                HtmlEmail ve = new HtmlEmail();
                ve.setCharset("UTF-8");
                ve.addTo(sEmailAddress, "");
                ve.setFrom(Turbine.getConfiguration().getString("mail.smtp.from"), Turbine.getConfiguration().getString("mail.smtp.from.name"));
                ve.setSubject(myNotif.getSubject());
                context.put("name", subclose.getCustomerRelatedByCustomerId().getCustomerName1());
                context.put("display", subclose.getCustomerRelatedByCustomerId().getCustomerDisplay());
                context.put("dear", subclose.getCustomerRelatedByCustomerId().getDear());
                context.put("email", subclose.getCustomerRelatedByCustomerId().getEmail());
                context.put("custom1", subclose.getCustomerRelatedByCustomerId().getCustom1());
                context.put("custom2", subclose.getCustomerRelatedByCustomerId().getCustom2());
                context.put("custom3", subclose.getCustomerRelatedByCustomerId().getCustom3());
                context.put("custom4", subclose.getCustomerRelatedByCustomerId().getCustom4());
                context.put("custom5", subclose.getCustomerRelatedByCustomerId().getCustom5());
                context.put("custom6", subclose.getCustomerRelatedByCustomerId().getCustom6());
                context.put("login", subclose.getCustomerRelatedByCustomerId().getLoginName());
                context.put("password", subclose.getCustomerRelatedByCustomerId().getPasswordValue());
                context.put("productdisplay", subclose.getProduct().getProductDisplay());
                context.put("productdescription", subclose.getProduct().getProductDescription());
                context.put("startdate", formatDate(subclose.getStartDate()));
                context.put("enddate", formatDate(subclose.getEndDate()));
                ve.setTextMsg(velTool.evaluate(myNotif.getBody()));
                ve.send();
            }
        }
    }

    private void doExpiredOnlineSubs() throws Exception {
        Criteria criteria = new Criteria();
        criteria.add(NotificationPeer.NOTIFICATION_ID, new Integer(1003), Criteria.EQUAL);
        Notification myNotif = (Notification) NotificationPeer.doSelect(criteria).get(0);
        VelocityContext context = new VelocityContext();
        ShedVelocityTool velTool = new ShedVelocityTool(context);
        Criteria osubcrit = new Criteria();
        osubcrit.add(OnlineSubscriptionPeer.END_DATE, new Date(), Criteria.LESS_EQUAL);
        osubcrit.add(OnlineSubscriptionPeer.STATUS, new Integer(30), Criteria.EQUAL);
        List substoclose = OnlineSubscriptionPeer.doSelect(osubcrit);
        Iterator i = substoclose.iterator();
        while (i.hasNext()) {
            OnlineSubscription subclose = (OnlineSubscription) i.next();
            subclose.setStatus(50);
            subclose.setModifiedBy("system");
            subclose.setModified(new Date());
            subclose.setModified(true);
            subclose.setNew(false);
            subclose.save();
            String sEmailAddress = subclose.getCustomerRelatedByCustomerId().getEmail();
            if (sEmailAddress.length() > 1) {
                HtmlEmail ve = new HtmlEmail();
                ve.setCharset("UTF-8");
                ve.addTo(sEmailAddress, "");
                ve.setFrom(Turbine.getConfiguration().getString("mail.smtp.from"), Turbine.getConfiguration().getString("mail.smtp.from.name"));
                ve.setSubject(myNotif.getSubject());
                context.put("name", subclose.getCustomerRelatedByCustomerId().getCustomerName1());
                context.put("display", subclose.getCustomerRelatedByCustomerId().getCustomerDisplay());
                context.put("dear", subclose.getCustomerRelatedByCustomerId().getDear());
                context.put("email", subclose.getCustomerRelatedByCustomerId().getEmail());
                context.put("custom1", subclose.getCustomerRelatedByCustomerId().getCustom1());
                context.put("custom2", subclose.getCustomerRelatedByCustomerId().getCustom2());
                context.put("custom3", subclose.getCustomerRelatedByCustomerId().getCustom3());
                context.put("custom4", subclose.getCustomerRelatedByCustomerId().getCustom4());
                context.put("custom5", subclose.getCustomerRelatedByCustomerId().getCustom5());
                context.put("custom6", subclose.getCustomerRelatedByCustomerId().getCustom6());
                context.put("productdisplay", subclose.getProduct().getProductDisplay());
                context.put("productdescription", subclose.getProduct().getProductDescription());
                context.put("startdate", formatDate(subclose.getStartDate()));
                context.put("enddate", formatDate(subclose.getEndDate()));
                ve.setTextMsg(velTool.evaluate(myNotif.getBody()));
                ve.send();
            }
        }
    }

    private void doExpiredPrintSubs() throws Exception {
        Criteria osubcrit = new Criteria();
        osubcrit.add(PrintSubscriptionPeer.END_DATE, new Date(), Criteria.LESS_EQUAL);
        osubcrit.add(PrintSubscriptionPeer.STATUS, new Integer(30), Criteria.EQUAL);
        List substoclose = PrintSubscriptionPeer.doSelect(osubcrit);
        Iterator i = substoclose.iterator();
        while (i.hasNext()) {
            PrintSubscription subclose = (PrintSubscription) i.next();
            subclose.setStatus(50);
            subclose.setModifiedBy("system");
            subclose.setModified(new Date());
            subclose.setModified(true);
            subclose.setNew(false);
            subclose.save();
        }
    }

    private String formatDate(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(d);
    }
}
