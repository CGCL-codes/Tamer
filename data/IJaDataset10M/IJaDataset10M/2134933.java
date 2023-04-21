package org.usca.workshift.gwt.workshiftapp.client.emailsender;

import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.core.client.GWT;
import org.usca.workshift.database.model.Member;
import org.usca.workshift.gwt.workshiftapp.client.emailsender.SendEmailAsync;

public interface SendEmail extends RemoteService {

    public Boolean sendEmail(String to, String subject, String text);

    public Boolean sendEmail(Member to, String subject, String text);

    /**
     * Utility/Convenience class.
     * Use SendEmail.App.getInstance() to access static instance of SendEmailAsync
     */
    public static class App {

        private static SendEmailAsync ourInstance = null;

        public static synchronized SendEmailAsync getInstance() {
            if (ourInstance == null) {
                ourInstance = (SendEmailAsync) GWT.create(SendEmail.class);
                ((ServiceDefTarget) ourInstance).setServiceEntryPoint(GWT.getModuleBaseURL() + "org.usca.workshift.gwt.workshiftapp.WorkshiftApp/EmailSender");
            }
            return ourInstance;
        }
    }
}
