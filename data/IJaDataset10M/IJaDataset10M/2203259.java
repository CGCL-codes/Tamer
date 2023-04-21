package com.tysanclan.site.projectewok.tasks;

import java.util.Calendar;
import java.util.List;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.EmailChangeConfirmation;
import com.tysanclan.site.projectewok.entities.dao.EmailChangeConfirmationDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.EmailChangeConfirmationFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class EmailChangeConfirmationExpirationTask extends PeriodicTask {

    /**
     * 
     */
    public EmailChangeConfirmationExpirationTask() {
        super("E-Mail change confirmations cleanup", "Expiration", ExecutionMode.DAILY);
    }

    @SpringBean
    private UserService userService;

    @SpringBean
    private EmailChangeConfirmationDAO emailChangeConfirmationDAO;

    /**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
    @Override
    public void run() {
        Calendar cal = DateUtil.getCalendarInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        EmailChangeConfirmationFilter filter = new EmailChangeConfirmationFilter();
        filter.setDateBefore(cal.getTime());
        List<EmailChangeConfirmation> expiredConfirmations = emailChangeConfirmationDAO.findByFilter(filter);
        for (EmailChangeConfirmation confirmation : expiredConfirmations) {
            userService.expireConfirmation(confirmation);
        }
    }
}
