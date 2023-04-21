package au.edu.monash.merc.capture.mail;

import java.util.Map;

public interface MailService {

    public void sendMail(String emailFrom, String emailTo, String emailSubject, String emailBody, boolean isHtml);

    public void sendMail(String emailFrom, String emailTo, String emailSubject, Map<String, String> templateValues, String templateFile, boolean isHtml);
}
