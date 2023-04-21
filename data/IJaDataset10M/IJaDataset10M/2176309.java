package si.cit.eprojekti.emailer.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.log4j.Priority;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.i18n.Messages;
import com.jcorporate.expresso.core.misc.DateTime;
import com.jcorporate.expresso.services.dbobj.Setup;
import si.cit.eprojekti.emailer.MailerSchema;
import si.cit.eprojekti.emailer.controller.mailerstates.ErrorState;
import si.cit.eprojekti.emailer.dbobj.Message;
import si.cit.eprojekti.emailer.dbobj.Template;
import si.cit.eprojekti.emailer.util.HtmlTemplateMailer;
import si.cit.eprojekti.projectvianet.util.link.ITickable;

/**
 * @author Luka Pavlic (luka.pavlic@cit.si)
 *
 * PackageMailSenderJob description:
 *	Send mails every ... days 
 */
public class PackageMailSenderJob implements ITickable {

    /**
	 * @see si.cit.eprojekti.projectvianet.util.link.ITickable#tick()
	 */
    public void tick() {
        boolean runNow = false;
        try {
            String lastPackageSending = Setup.getValue("", "si.cit.eprojekti.emailer.MailerSchema", "LastPackageSending");
            int daysBetweenSending = Integer.parseInt(Setup.getValue("", "si.cit.eprojekti.emailer.MailerSchema", "DaysBetweenSending"));
            Calendar nowCal = getCalendarFromString(DateTime.getDateTimeForDB());
            int howOldIsLastSending = howDaysOldIsDate(nowCal, lastPackageSending);
            if (howOldIsLastSending >= daysBetweenSending) runNow = true;
            if (!runNow) return;
            Setup set = new Setup();
            set.setField(Setup.SETUP_CODE, "LastPackageSending");
            set.setField(Setup.SCHEMA_CLASS, "si.cit.eprojekti.emailer.MailerSchema");
            set.find();
            set.setField(Setup.SETUP_VALUE, DateTime.getDateTimeForDB());
            set.update();
            ArrayList receipents = new ArrayList();
            StringBuffer pre = new StringBuffer();
            StringBuffer post = new StringBuffer();
            StringBuffer mid = new StringBuffer();
            Template.getDefaultTemplate(pre, post, mid);
            String midContent = mid.toString();
            Message c = new Message();
            ArrayList allRecs = c.searchAndRetrieveList("Date");
            Iterator iter = allRecs.iterator();
            while (iter.hasNext()) {
                c = (Message) iter.next();
                if (c.isFieldNull("DateSent")) {
                    String toM = c.getField("Receipent");
                    ArrayList msgs = null;
                    for (int i = 0; i < receipents.size(); i++) if (((String) ((ArrayList) receipents.get(i)).get(0)).equalsIgnoreCase(toM)) msgs = (ArrayList) receipents.get(i);
                    if (msgs == null) {
                        msgs = new ArrayList();
                        msgs.add(toM);
                        receipents.add(msgs);
                    }
                    msgs.add(c);
                }
            }
            for (int i = 0; i < receipents.size(); i++) {
                StringBuffer currMess = new StringBuffer();
                ArrayList curr = (ArrayList) receipents.get(i);
                for (int j = 1; j < curr.size(); j++) {
                    Message msg = (Message) curr.get(j);
                    currMess.append(msg.getField("Subject"));
                    currMess.append("(" + msg.getField("Date") + ")<br/>");
                    currMess.append(msg.getField("Content"));
                    currMess.append(midContent);
                }
                HtmlTemplateMailer.sendMail("", (String) curr.get(0), Setup.getValue("", "si.cit.eprojekti.emailer.MailerSchema", "PackageMailTitle"), currMess.toString(), -1, true);
            }
            for (int i = 0; i < receipents.size(); i++) {
                ArrayList curr = (ArrayList) receipents.get(i);
                for (int j = 1; j < curr.size(); j++) {
                    Message msg = (Message) curr.get(j);
                    msg.setField("DateSent", DateTime.getDateTimeForDB());
                    msg.update();
                }
            }
            int daysToSave = Integer.parseInt(Setup.getValue("", "si.cit.eprojekti.emailer.MailerSchema", "DaysToSave"));
            if (daysToSave > 0) {
                getCalendarFromString(DateTime.getDateTimeForDB());
                Message m = new Message();
                ArrayList allM = m.searchAndRetrieveList();
                Iterator iterM = allRecs.iterator();
                while (iterM.hasNext()) {
                    m = (Message) iterM.next();
                    if (m.isFieldNull("DateSent")) continue;
                    if (howDaysOldIsDate(nowCal, m.getField("DateSent")) > daysToSave) m.delete();
                }
            }
        } catch (Exception exc) {
            if (MailerSchema.standardLog.isEnabledFor(Priority.WARN)) MailerSchema.standardLog.warn(" :: Exception in \"" + this.getClass().getName() + "\" : " + exc.toString());
            if (MailerSchema.debugLog.isDebugEnabled()) MailerSchema.debugLog.debug(" :: Exception in run \"" + this.getClass().getName() + "\" : " + exc.toString(), exc.fillInStackTrace());
        }
    }

    /**
	 * Get Calendar from string
	 * @param date String in format "yyyy-MM-dd HH:mm:ss "
	 * @return Calendar
	 */
    private GregorianCalendar getCalendarFromString(String date) {
        date = date.substring(0, date.indexOf(" "));
        StringTokenizer st = new StringTokenizer(date, "-");
        return new GregorianCalendar(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()) - 1, Integer.parseInt(st.nextToken()));
    }

    /**
	 * How Days old is given date
	 * @param now Calendar, holding Now date
	 * @param date2 String in format "yyyy-MM-dd HH:mm:ss "
	 * @return Number of days
	 */
    private int howDaysOldIsDate(Calendar now, String date2) {
        Calendar cal2 = getCalendarFromString(date2);
        long diffMillis = now.getTimeInMillis() - cal2.getTimeInMillis();
        return (int) (diffMillis / (24 * 60 * 60 * 1000));
    }
}
