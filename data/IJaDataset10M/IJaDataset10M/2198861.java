package org.model.noticeSending;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MailSendingTask implements Job {

    private final Sender sender = new Sender();

    public MailSendingTask() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            sender.send();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
